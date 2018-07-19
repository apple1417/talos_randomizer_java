package randomizer_bruteforce.all_default.required_hubs;

import randomizer_bruteforce.all_default.generic.GeneratorAllDefault;
import randomizer_bruteforce.SeedScheduler;
import randomizer_bruteforce.TalosProgress;

class Run {
    public static void main(String[] args) {
        int min = 0;
        int max = 0x7FFFFFFF;
        try {
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException|NumberFormatException e){}

        SeedScheduler s = new SeedScheduler(() -> new GeneratorAllDefault(),
                                            (TalosProgress p) -> evaluate(p),
                                            () -> printData());
        s.start(min, max);
    }


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

    /*
      These store the total amount of seeds as follows:
      0: Total      4: F6
      1: F2         5: F2+F6
      2: F3         6: F3+F6
      3: F2+F3      7. F2+F3+F6
    */
    private static long[] total = new long[8];
    private static int[] oneHub = new int[8];
    private static int[] twoHub = new int[8];
    private static int[] threeHub = new int[8];
    private static void evaluate(TalosProgress progress) {
        // One hub seeds
        int endingA = endingType(progress, A_MARKERS);
        if (endingA != 0) {
            oneHub[0]++;
            oneHub[endingA]++;
            total[0]++;
            total[endingA]++;
            return;
        }

        int DI_count = 0;
        int DJ_count = 0;
        for (String marker : A_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
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
                total[0]++;
                total[endingAB]++;
            } else if (endingAC != 0) {
                twoHub[0]++;
                twoHub[endingAC]++;
                total[0]++;
                total[endingAC]++;
            } else {
                threeHub[0]++;
                threeHub[7]++;
                total[0]++;
                total[7]++;
            }

        // B first
        } else if (DI_count >= 2) {
            if (endingAB != 0) {
                twoHub[0]++;
                twoHub[endingAB]++;
                total[0]++;
                total[endingAB]++;
            } else {
                threeHub[0]++;
                threeHub[7]++;
                total[0]++;
                total[7]++;
            }

        // C first
        } else if (DJ_count >= 3) {
            if (endingAC != 0) {
                twoHub[0]++;
                twoHub[endingAC]++;
                total[0]++;
                total[endingAC]++;
            } else {
                threeHub[0]++;
                threeHub[7]++;
                total[0]++;
                total[7]++;
            }

        } else {
            System.err.println(String.format("Unable to determine first hub of seed %d", progress.getVar("Randomizer_Seed")));
        }
    }

    // Work out what endings are accessabale
    private static int endingType(TalosProgress progress, String[] markersToCheck) {
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
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
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

    // Print all our data in a nice ascii table
    private static String ROW_SEPERATOR = "+------------+------------+------------+------------+------------+------------+------------+------------+------------+";
    private static void printData() {
        System.out.println(ROW_SEPERATOR);
        System.out.println("|            | F2         | F3         | F6         | F2+F3      | F2+F6      | F3+F6      | F2+F3+F6   | Total      |");
        System.out.println(ROW_SEPERATOR);
        // Changed the order of these so it looks nicer
        System.out.println(String.format("| One Hub    | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         oneHub[1], oneHub[2], oneHub[4], oneHub[3], oneHub[5], oneHub[6], oneHub[7], oneHub[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Two Hubs   | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         twoHub[1], twoHub[2], twoHub[4], twoHub[3], twoHub[5], twoHub[6], twoHub[7], twoHub[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Three Hubs | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         threeHub[1], threeHub[2], threeHub[4], threeHub[3], threeHub[5], threeHub[6], threeHub[7], threeHub[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Total      | %10d | %10d | %10d | %10d | %10d | %10d | %10d | %10d |",
                                         total[1], total[2], total[4], total[3], total[5], total[6], total[7], total[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(ROW_SEPERATOR);
        /*
          Because the other threads keep working while this prints, the percentages
           will add up to more than 100 while it's running.
          As it gets closer to the end this becomes less significant though, and
           when it's done it's fine
        */
        System.out.println(String.format("| One Hub    | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)oneHub[1]*100/total[0], (float)oneHub[2]*100/total[0], (float)oneHub[4]*100/total[0], (float)oneHub[3]*100/total[0],
                                         (float)oneHub[5]*100/total[0], (float)oneHub[6]*100/total[0], (float)oneHub[7]*100/total[0], (float)oneHub[0]*100/total[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Two Hubs   | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)twoHub[1]*100/total[0], (float)twoHub[2]*100/total[0], (float)twoHub[4]*100/total[0], (float)twoHub[3]*100/total[0],
                                         (float)twoHub[5]*100/total[0], (float)twoHub[6]*100/total[0], (float)twoHub[7]*100/total[0], (float)twoHub[0]*100/total[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Three Hubs | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)threeHub[1]*100/total[0], (float)threeHub[2]*100/total[0], (float)threeHub[4]*100/total[0], (float)threeHub[3]*100/total[0],
                                         (float)threeHub[5]*100/total[0], (float)threeHub[6]*100/total[0], (float)threeHub[7]*100/total[0], (float)threeHub[0]*100/total[0]));
        System.out.println(ROW_SEPERATOR);
        System.out.println(String.format("| Total      | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% | %9.2f%% |",
                                         (float)total[1]*100/total[0], (float)total[2]*100/total[0], (float)total[4]*100/total[0], (float)total[3]*100/total[0],
                                         (float)total[5]*100/total[0], (float)total[6]*100/total[0], (float)total[7]*100/total[0], (float)total[0]*100/total[0]));
        System.out.println(ROW_SEPERATOR);
    }
}
