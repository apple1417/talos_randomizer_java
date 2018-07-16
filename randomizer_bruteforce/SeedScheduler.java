package randomizer_bruteforce;

import randomizer_bruteforce.generic.Generator;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SeedScheduler {
    /*
      I need to create a bunch of thread objects, if I tried with the main class
       then it wouldn't get anything I overwrote
    */
    private class SeedChecker extends RunnableThread {
        Generator gen;
        Consumer<TalosProgress> evaluate;
        SeedChecker(String name, Generator gen, Consumer<TalosProgress> evaluate) {
            super(name);
            this.evaluate = evaluate;
            this.gen = gen;
        }

        public void run() {
            for(long seed = min; seed <= max; seed++) {
                TalosProgress progress;
                // Generation still sometimes fails so we have to be safe
                try {
                    progress = gen.generate(seed);
                } catch (Exception e) {
                    System.err.println(String.format("Seed %d fails to generate", seed));
                    continue;
                }
                evaluate.accept(progress);
            }
        }
    }

    /*
      Accessing the same generator on multiple threads breaks things so I need a
       function to make them
      The 'evaluate' function is run on every generated seed, to be used to evaluate them
      The 'print' function is run every 10 million seeds and when the program is quit
      It is intended to be used to print stats accross multiple seeds, stuff you
       can't do from in eval
    */
    private Supplier<Generator> createGen;
    private Consumer<TalosProgress> evaluate;
    private Runnable print;
    public SeedScheduler(Supplier<Generator> createGen, Consumer<TalosProgress> evaluate, Runnable print) {
        this.createGen = createGen;
        this.evaluate = evaluate;
        this.print = print;
    }
    public SeedScheduler(Supplier<Generator> createGen, Consumer<TalosProgress> evaluate) {
        this(createGen, evaluate, () -> {});
    }

    // Might as well have this, incase someone wants to change them
    private static int THREAD_NUM = 8;
    private static int BATCH_SIZE = 100000;
    public void setThreadingStats(int threadNum, int batchSize) {
        THREAD_NUM = threadNum;
        BATCH_SIZE = batchSize;
    }

    private static int PRINT_GAP = 10000000;
    private static long start_seed = 0;
    private static long current_seed = 0;
    private static long max_seed = 0x7FFFFFFF;
    public void start(long min, long max) {
        // So Ctrl-C gives some output
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(String.format("Stopped while working on batch starting at %d", current_seed));
                print.run();
            }
        });

        start_seed = Math.max(0, min);
        current_seed = start_seed;
        max_seed = Math.max(start_seed, Math.min(0x7FFFFFFF, max));

        System.out.println(String.format("Using Generator: %s", createGen.get().getGenInfo()));

        while (current_seed + BATCH_SIZE < max_seed) {
            int PER_THREAD = (BATCH_SIZE / THREAD_NUM);
            // Need to create new threads because you can't restart them
            SeedChecker[] threads = new SeedChecker[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new SeedChecker(Integer.toString(i), createGen.get(), evaluate);
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            // All evaluation is done on the thread so just wait for them to finish
            for (SeedChecker thread : threads) {
                thread.waitFinished();
            }

            if ((current_seed - start_seed) % PRINT_GAP == 0) {
                print.run();
            }
        }
        // At this point we probably can't evenly split stuff so one thread can do the rest
        SeedChecker thread = new SeedChecker("0", createGen.get(), evaluate);
        thread.start(current_seed, max_seed);
        thread.waitFinished();
        System.out.println("Finished\n=============================================");
    }
    public void start() {
        start(0, 0x7FFFFFFF);
    }
}
