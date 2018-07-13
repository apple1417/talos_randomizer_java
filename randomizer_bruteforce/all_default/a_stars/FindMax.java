package randomizer_bruteforce.all_default.a_stars;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import randomizer_bruteforce.RunnableThread;
import randomizer_bruteforce.TalosProgress;
import randomizer_bruteforce.all_default.generic.Generator;

class FindMax extends RunnableThread {
    private static String[] A_MARKERS = {
        "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
        "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
        "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
        "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
        "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole"
    };
    // How many stars are notable enough to cause the seed to be print
    private static int NOTABLE_MIN = 21;

    FindMax(String name) {
        super(name);
    }

    private Generator gen = new Generator();
    public void run() {
        for(long seed = min; seed <= max; seed ++) {
            TalosProgress progress;
            try {
                progress = gen.generate(seed);
            } catch (Exception e) {
                System.out.println(String.format("Seed %d fails to generate", seed));
                continue;
            }

            // Seed evaluation
            int starCount = 0;
            for (String marker : A_MARKERS) {
                if (progress.getVar(marker) <= 30) {
                    starCount++;
                }
            }

            if (starCount >= NOTABLE_MIN) {
                String output = String.format("%d (%d)", seed, starCount);
                System.out.println(output);
                try {
                    Files.write(Paths.get("randomizer_bruteforce/all_default/a_stars/output.txt"), (output + "\n").getBytes(), StandardOpenOption.APPEND);
                } catch (IOException e) {}
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
    private static long current_seed = 0;
    private static long max_seed = 0x7FFFFFFF;

    public static void main(String[] args) {
        // So Ctrl-C gives some output
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(String.format("Stopped while working on batch starting at %d", current_seed));
            }
        });

        try {
            current_seed = Math.max(0, Long.parseLong(args[0]));
            max_seed = Math.max(current_seed, Math.min(0x7FFFFFFF, Long.parseLong(args[1])));
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        System.out.println(String.format("Using generator: %s, %s", Generator.GEN_TYPE, Generator.GEN_VERSION));

        while (current_seed + PER_LOOP < max_seed) {
            // Need to create new threads because you can't restart them
            FindMax[] threads = new FindMax[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new FindMax(Integer.toString(i));
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            // All evaluation is done on the thread so just wait for them to finish
            for (FindMax thread : threads) {
                thread.waitFinished();
            }
        }
        // At this point we probably can't evenly split stuff so one thread can do the rest
        FindMax thread = new FindMax("0");
        thread.start(current_seed, max_seed);
        System.out.println("Finished\n=============================================");
    }
}
