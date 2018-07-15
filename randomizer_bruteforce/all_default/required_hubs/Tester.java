package randomizer_bruteforce.all_default.required_hubs;

import randomizer_bruteforce.all_default.generic.GeneratorAllDefault;
import randomizer_bruteforce.RunnableThread;
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

    private GeneratorAllDefault gen = new GeneratorAllDefault();
    /*
      These the amount of seeds as follows:
      0: Total      4: F6
      1: F2         5: F2+F6
      2: F3         6: F3+F6
      3: F2+F3      7. F2+F3+F6
    */
    private int[] oneHub = new int[8];
    private int[] twoHub = new int[8];
    private int[] threeHub = new int[8];
    public void run() {
        for(long seed = min; seed <= max; seed++) {
            TalosProgress progress;
            try {
                progress = gen.generate(seed);
            } catch (Exception e) {
                System.out.println(String.format("Seed %d fails to generate", seed));
                continue;
            }

            // Evaluate the seed

            // One hub seeds
            int endingA = endingType(progress, A_MARKERS);
            if (endingA != 0) {
                oneHub[0]++;
                oneHub[endingA]++;
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

            int endingAB = endingType(progress, A_B_MARKERS);
            int endingAC = endingType(progress, A_C_MARKERS);

            // Both hubs
            if (DI_count >= 2 && DJ_count >= 3) {
                if (endingAB != 0) {
                    twoHub[0]++;
                    twoHub[endingAB]++;
                } else if (endingAC != 0) {
                    twoHub[0]++;
                    twoHub[endingAC]++;
                } else {
                    threeHub[0]++;
                    threeHub[7]++;
                }

            // B first
            } else if (DI_count >= 2) {
                if (endingAB != 0) {
                    twoHub[0]++;
                    twoHub[endingAB]++;
                } else {
                    threeHub[0]++;
                    threeHub[7]++;
                }

            // C first
            } else if (DJ_count >= 3) {
                if (endingAC != 0) {
                    twoHub[0]++;
                    twoHub[endingAC]++;
                } else {
                    threeHub[0]++;
                    threeHub[7]++;
                }

            } else {
                System.err.println(String.format("Unable to determine first hub of seed %d", seed));
            }
        }
    }

    // Only good way to do this is get the count of each type of shape
    private int endingType(TalosProgress progress, String[] markersToCheck) {
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

        // We might get more than one possible ending at once
        int output = 0;
        // F2
        if (ML_count >= 1 && MT_count >= 2 && MZ_count >= 1
            && NL_count >= 6 && NO_count >= 1 && NT_count >= 4 && NZ_count >= 2) {
            output += 1;
        }
        // F3
        if (NI_count >= 4 && NJ_count >= 2 && NL_count >= 4 && NS_count >= 1 && NZ_count >= 3) {
            output += 2;
        }
        // F6
        if (E_count >= 9 && NL_count >= 2 && NZ_count >= 2) {
            output += 4;
        }
        return output;
    }

    /*
      This is a bad way to store it I know but I need a single method returning
       it all if I want to eventually just have a generic functon to start all
       the threads
    */
    private int[][] getData() {
        return new int[][] {oneHub, twoHub, threeHub};
    }

    private static int[] overallTotal = new int[8];
    private static int[] oneHubTotal = new int[8];
    private static int[] twoHubTotal = new int[8];
    private static int[] threeHubTotal = new int[8];
    private static void processData(int[][] data) {
        int[] oneHub = data[0];
        int[] twoHub = data[1];
        int[] threeHub = data[2];
        for (int i = 0; i < 8; i++) {
            overallTotal[i] += oneHub[i] + twoHub[i] + threeHub[i];
            oneHubTotal[i] += oneHub[i];
            twoHubTotal[i] += twoHub[i];
            threeHubTotal[i] += threeHub[i];
        }
    }

    private static String[] rowNames = new String[] {
        "Total", "F2", "F3", "F2+F3", "F6", "F2+F6", "F3+F6", "F2+F3+F6"
    };
    private static String ROW_SEPERATOR = "+------------+------------+------------+------------+------------+------------+------------+------------+------------+";
    private static void printData() {
        System.out.println(ROW_SEPERATOR);
        System.out.println("|            | F2         | F3         | F6         | F2+F3      | F2+F6      | F3+F6      | F2+F3+F6   | Total      |");
        System.out.println(ROW_SEPERATOR);
        // Changed the order of these so it looks nicer
        System.out.println(String.format("| One Hub    | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         oneHubTotal[1], oneHubTotal[2], oneHubTotal[4], oneHubTotal[3],
                                         oneHubTotal[5], oneHubTotal[6], oneHubTotal[7], oneHubTotal[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Two Hubs   | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         twoHubTotal[1], twoHubTotal[2], twoHubTotal[4], twoHubTotal[3],
                                         twoHubTotal[5], twoHubTotal[6], twoHubTotal[7], twoHubTotal[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Three Hubs | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         threeHubTotal[1], threeHubTotal[2], threeHubTotal[4], threeHubTotal[3],
                                         threeHubTotal[5], threeHubTotal[6], threeHubTotal[7], threeHubTotal[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Total      | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         overallTotal[1], overallTotal[2], overallTotal[4], overallTotal[3],
                                         overallTotal[5], overallTotal[6], overallTotal[7], overallTotal[0]));
        System.out.println(ROW_SEPERATOR);
        int total = overallTotal[0];
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| One Hub    | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)oneHubTotal[1]*100/total, (float)oneHubTotal[2]*100/total, (float)oneHubTotal[4]*100/total,
                                         (float)oneHubTotal[3]*100/total, (float)oneHubTotal[5]*100/total, (float)oneHubTotal[6]*100/total,
                                         (float)oneHubTotal[7]*100/total, (float)oneHubTotal[0]*100/total));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Two Hubs   | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)twoHubTotal[1]*100/total, (float)twoHubTotal[2]*100/total, (float)twoHubTotal[4]*100/total,
                                         (float)twoHubTotal[3]*100/total, (float)twoHubTotal[5]*100/total, (float)twoHubTotal[6]*100/total,
                                         (float)twoHubTotal[7]*100/total, (float)twoHubTotal[0]*100/total));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Three Hubs | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)threeHubTotal[1]*100/total, (float)threeHubTotal[2]*100/total, (float)threeHubTotal[4]*100/total,
                                         (float)threeHubTotal[3]*100/total, (float)threeHubTotal[5]*100/total, (float)threeHubTotal[6]*100/total,
                                         (float)threeHubTotal[7]*100/total, (float)threeHubTotal[0]*100/total));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Total      | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)overallTotal[1]*100/total, (float)overallTotal[2]*100/total, (float)overallTotal[4]*100/total,
                                         (float)overallTotal[3]*100/total, (float)overallTotal[5]*100/total, (float)overallTotal[6]*100/total,
                                         (float)overallTotal[7]*100/total, (float)overallTotal[0]*100/total));
        System.out.println(ROW_SEPERATOR);
    }

    /*
      I really wish there was a way to have this bit predefined somewhere but I need to make
       sure it uses the right class for the threads which just gets awkward
    */
    private static int THREAD_NUM = 8;
    private static int PER_LOOP = 100000;
    private static int PER_THREAD = (PER_LOOP / THREAD_NUM);
    private static long currentSeed = 0;
    private static long max_seed = 0x7FFFFFFF;

    public static void main(String[] args) {
        // So Ctrl-C gives some output
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                printData();
            }
        });

        while (currentSeed + PER_LOOP < max_seed) {
            // Need to create new threads because you can't restart them
            Tester[] threads = new Tester[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new Tester(Integer.toString(i));
                threads[i].start(currentSeed, currentSeed + PER_THREAD - 1);
                currentSeed += PER_THREAD;
            }
            // Get data out
            for (Tester thread : threads) {
                thread.waitFinished();
                processData(thread.getData());
            }
            // Occasionally print info
            if (currentSeed % 10000000 == 0) {
                printData();
            }
        }
        // At this point we probably can't evenly split stuff so one thread can do the rest
        Tester thread = new Tester("0");
        thread.start(currentSeed, 0x7FFFFFFF);
        currentSeed = 0x7FFFFFFF;
        thread.waitFinished();
        processData(thread.getData());
        System.out.println("Finished\n=============================================");
        // The program ending also triggers the output printing
    }
}
