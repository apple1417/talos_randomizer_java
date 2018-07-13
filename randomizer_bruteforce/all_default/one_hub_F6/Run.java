package randomizer_bruteforce.all_default.one_hub_F6;

import randomizer_bruteforce.RunnableThread;
import randomizer_bruteforce.TalosProgress;

class Run extends RunnableThread {
    Run(String name) {
        super(name);
    }

    Generator gen = new Generator();
    public void run() {
        for(long seed = min; seed <= max; seed ++) {
            TalosProgress progress;
            try {
                /*
                  Seed evaluation is done in the generator, because it uses
                   a custom one that might not actually return anything, so
                   it's probably better to keep it all in the same place
                */
                gen.check(seed);
            } catch (Exception e) {
                System.out.println(String.format("Seed %d fails to generate", seed));
                continue;
            }
        }
    }

    /*
      I really wish there was a way to have this bit predefined somewhere but I need to make
       sure it uses the right class for the threads which just gets awkward
    */
    private static int THREAD_NUM = 8;
    private static int PER_LOOP = 100000;
    private static int PER_THREAD = (PER_LOOP / THREAD_NUM);
    private static int current_seed = 0;
    private static int max_seed = 0x7FFFFFFF;

    public static void main(String[] args) {
        // So Ctrl-C gives some output
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(String.format("Stopped while working on batch starting at %d", current_seed));
            }
        });

        try {
            current_seed = Math.max(0, Integer.parseInt(args[0]));
            max_seed = Math.max(current_seed, Math.min(0x7FFFFFFF, Integer.parseInt(args[1])));
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        System.out.println(String.format("Using generator: %s, %s", Generator.GEN_TYPE, Generator.GEN_VERSION));

        while (current_seed + PER_LOOP < max_seed) {
            // Need to create new threads because you can't restart them
            Run[] threads = new Run[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new Run(Integer.toString(i));
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            // All evaluation is done on the thread so just wait for them to finish
            for (Run thread : threads) {
                thread.waitFinished();
            }
        }
        // At this point we probably can't evenly split stuff so one thread can do the rest
        Run thread = new Run("0");
        thread.start(current_seed, max_seed);
        System.out.println("Finished\n=============================================");
    }
}
