package randomizer_bruteforce;

import randomizer_bruteforce.GeneratorInterface;
import randomizer_bruteforce.TalosProgress;
import java.util.ArrayList;

public abstract class SeedScheduler extends RunnableThread {
    public SeedScheduler(String name) {
        super(name);
    }

    protected GeneratorInterface gen;
    public void run() {
        for(long seed = min; seed <= max; seed++) {
            TalosProgress progress;
            try {
                progress = gen.generate(seed);
            } catch (Exception e) {
                System.out.println(String.format("Seed %d fails to generate", seed));
                continue;
            }
            evaluate(progress);
        }
    }

    public static int THREAD_NUM = 8;
    public static int PER_BATCH = 100000;
    protected static long current_seed = 0;
    protected static long max_seed = 0x7FFFFFFF;

    public static void main(String[] args) {
        // So Ctrl-C gives some output
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(String.format("Stopped while working on batch starting at %d", current_seed));
                showData();
            }
        });

        try {
            current_seed = Math.max(0, Long.parseLong(args[0]));
            max_seed = Math.max(current_seed, Math.min(0x7FFFFFFF, Long.parseLong(args[1])));
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        System.out.println("Using generator: " + getGenInfo());

        int PER_THREAD = (PER_BATCH / THREAD_NUM);
        int batch_num = 0;
        while (current_seed + PER_BATCH < max_seed) {
            // Need to create new threads because you can't restart them
            SeedScheduler[] threads = new SeedScheduler[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = createThread(Integer.toString(i));
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            // Get data out
            for (SeedScheduler thread : threads) {
                thread.waitFinished();
                processData(thread.getData());
            }
            // Occasionally print info
            if (batch_num == 10) {
                showData();
                batch_num = -1;
            }
            batch_num++;
        }
        // At this point we probably can't evenly split stuff so one thread can do the rest
        SeedScheduler thread = createThread("0");
        thread.start(current_seed, max_seed);
        thread.waitFinished();
        processData(thread.getData());
        System.out.println("Finished\n=============================================");
        // The program ending also triggers the output printing
    }

    /*
      You don't *have* to overwrite these, it's just a good idea
      I use untyped ArrayLists because I don't know what type of output I might want
    */
    private static ArrayList empty;
    public ArrayList getData() {return empty;}
    public static void processData(ArrayList data) {}
    public static void showData() {}
    public static String getGenInfo() {return "Unknown";}

    // You do have to make this one though, would be pointless without
    public abstract void evaluate(TalosProgress progress);
    // I can't make this abstract but it also needs to be overwritten
    public static SeedScheduler createThread(String name) {return null;}
}
