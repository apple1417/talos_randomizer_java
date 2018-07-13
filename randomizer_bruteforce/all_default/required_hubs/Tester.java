package randomizer_bruteforce.all_default.required_hubs;

import randomizer_bruteforce.all_default.generic.Generator;
import randomizer_bruteforce.generic.RunnableThread;
import randomizer_bruteforce.TalosProgress;

class Tester extends RunnableThread {
    // Just hardcode these three because it's easier than combining them
    private static String[] A_MARKERS = {
        "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
        "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
        "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
        "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
        "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole"
    };
    private static String[] A_B_MARKERS = {
        "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
        "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
        "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
        "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
        "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole",
        "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence", "B1-RoD", "B1-SaaS", "B1-Star",
        "B2-Tomb", "B2-Star", "B2-MotM", "B2-Moonshot", "B2-Higher Ground",
        "B3-Blown Away", "B3-Star", "B3-Sunshot", "B3-Eagle's Nest", "B3-Woosh",
        "B4-Self Help", "B4-Double-Plate", "B4-TRA", "B4-TRA Star", "B4-RPS", "B4-ABUH", "B4-WAtC", "B4-Sphinx Star",
        "B5-SES", "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-Chambers", "B5-Obelisk Star",
        "B6-Crisscross", "B6-JDaW", "B6-Egyptian Arcade",
        "B7-AFaF", "B7-WLJ", "B7-BSbS", "B7-BSbS Star", "B7-BLoM", "B7-Star"
    };
    private static String[] A_C_MARKERS = {
        "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
        "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
        "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
        "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
        "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole",
        "C1-Conservatory", "C1-MIA", "C1-Labyrinth", "C1-Blowback", "C1-Star",
        "C2-ADaaF", "C2-Star", "C2-Rapunzel", "C2-Cemetery", "C2-Short Wall",
        "C3-Three Connectors", "C3-Jammer Quarantine", "C3-BSLS", "C3-Weathertop", "C3-Star",
        "C4-Armory", "C4-Oubliette", "C4-Oubliette Star", "C4-Stables", "C4-Throne Room", "C4-Throne Room Star",
        "C5-Time Flies", "C5-Time Flies Star", "C5-Time Crawls", "C5-Dumbwaiter", "C5-Dumbwaiter Star", "C5-UCaJ", "C5-UCAJ Star",
        "C6-Seven Doors", "C6-Star", "C6-Circumlocution", "C6-Two Way Street",
        "C7-Carrier Pigeons", "C7-DMS", "C7-Star", "C7-Prison Break", "C7-Crisscross",
        "CM-Star"
    };

    Tester(String name) {
        super(name);
    }

    private Generator gen = new Generator();
    private int one_hub = 0;
    private int two_hub = 0;
    private int three_hub = 0;
    public void run() {
        for(long i = min; i <= max; i++) {
            TalosProgress progress;
            try {
                progress = gen.generate(i);
            } catch (Exception e) {
                System.out.println(String.format("Seed %d fails to generate", i));
                continue;
            }

            // Evaluate the seed

            // One hub seeds
            if (isCompleteable(progress, A_MARKERS)) {
                one_hub++;
                continue;
            }

            int DI_count = 0;
            int DJ_count = 0;
            for (String marker : A_MARKERS) {
                String sigil = TalosProgress.TETROS[progress.getVar(marker) - 1];
                if (sigil.startsWith("DI")) {
                    DI_count++;
                } else if (sigil.startsWith("DJ")) {
                    DI_count++;
                }
            }

            // Both hubs
            if (DI_count >= 2 && DJ_count >= 3) {
                if (isCompleteable(progress, A_B_MARKERS)
                    || isCompleteable(progress, A_C_MARKERS)) {
                    two_hub++;
                } else {
                    three_hub++;
                }

            // B first
            } else if (DI_count >= 2) {
                if (isCompleteable(progress, A_B_MARKERS)) {
                    two_hub++;
                } else {
                    three_hub++;
                }

            // C first
            } else if (DJ_count >= 3) {
                if (isCompleteable(progress, A_C_MARKERS)) {
                    two_hub++;
                } else {
                    three_hub++;
                }

            }
        }
    }

    // Only good way to do this is get the count of each type of shape
    private boolean isCompleteable(TalosProgress progress, String[] markersToCheck) {
        int E_count = 0;
        int ML_count = 0;
        int MT_count = 0;
        int MZ_count = 0;
        int NI_count = 0;
        int NJ_count = 0;
        int NL_count = 0;
        int NO_count = 0;
        int NS_count = 0;
        int NT_count = 0;
        int NZ_count = 0;

        for (String marker : markersToCheck) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker) - 1];
            if (sigil.charAt(0) == 'E') {
               E_count++;
           } else if (sigil.charAt(0) == 'M') {
               switch(sigil.charAt(1)) {
                   case 'L': ML_count++; break;
                   case 'T': MT_count++; break;
                   case 'Z': MZ_count++; break;
               }
           } else if (sigil.charAt(0) == 'N') {
                switch(sigil.charAt(1)) {
                    case 'I': NI_count++; break;
                    case 'J': NJ_count++; break;
                    case 'L': NL_count++; break;
                    case 'O': NO_count++; break;
                    case 'S': NS_count++; break;
                    case 'T': NT_count++; break;
                    case 'Z': NZ_count++; break;
                }
            }
        }
                // F6
        return (E_count >= 9 && NL_count >= 2 && NZ_count >= 2)
                // F2
                || (ML_count >= 1 && MT_count >= 2 && MZ_count >= 1
                    && NL_count >= 6 && NO_count >= 1 && NT_count >= 4 && NZ_count >= 2)
                 // F3
                || (NI_count >= 4 && NJ_count >= 2 && NL_count >= 4 && NS_count >= 1
                    && NZ_count >= 3);
    }

    public int[] getData() {
        waitFinished();
        return new int[] {one_hub, two_hub, three_hub};
    }


    private static int THREAD_NUM = 8;
    private static long PER_LOOP = 100000;
    private static long PER_THREAD = (PER_LOOP / THREAD_NUM);
    private static long current_seed = 0;
    private static long max_seed = 0x7FFFFFFF;
    private static int data[] = new int[3];

    public static void main(String[] args) {
        // So Ctrl-C gives some output
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(String.format("Stopped while working on batch starting at %d", current_seed));
                System.out.println(String.format("%d %d %d", data[0], data[1], data[2]));
                System.out.println(String.format("%.2f%% %.2f%% %.2f%%", (float)data[0]*100/current_seed, (float)data[1]*100/current_seed, (float)data[2]*100/current_seed));
            }
        });

        while (current_seed + PER_LOOP < max_seed) {
            // Need to create new threads because you can't restart them
            Tester[] threads = new Tester[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new Tester(Integer.toString(i));
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            // Get data out
            for (Tester thread : threads) {
                int[] output = thread.getData();
                data[0] += output[0];
                data[1] += output[1];
                data[2] += output[2];
            }
            // Occasionally print info
            if (current_seed % 10000000 == 0) {
                System.out.println(current_seed);
                System.out.println(String.format("%d %d %d", data[0], data[1], data[2]));
                System.out.println(String.format("%.2f%% %.2f%% %.2f%%", (float)data[0]*100/current_seed, (float)data[1]*100/current_seed, (float)data[2]*100/current_seed));
            }
        }
        // At this point we probably can't evenly split stuff so one thread can do the rest
        Tester thread = new Tester("0");
        thread.start(current_seed, 0x7FFFFFFF);
        int[] output = thread.getData();
        data[0] += output[0];
        data[1] += output[1];
        data[2] += output[2];
        System.out.println("Finished\n=============================================");
        // The program ending also triggers the output printing
    }
}
