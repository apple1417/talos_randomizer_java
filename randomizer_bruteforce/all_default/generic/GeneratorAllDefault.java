package randomizer_bruteforce.all_default.generic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import randomizer_bruteforce.Enums.Arranger;
import randomizer_bruteforce.Generator;
import randomizer_bruteforce.MarkerGroup;
import randomizer_bruteforce.Rand;
import randomizer_bruteforce.TalosProgress;

public class GeneratorAllDefault implements Generator {
    public String getInfo() {
        return "All Default Settings, v11.0.2";
    }

    /*
      Define all our constants
      A few variables get destroyed during generation so they have backup versions
    */
    private static HashMap<String, Integer> TETRO_INDEXES = new HashMap<String, Integer>();
    private static HashMap<Arranger, String[]> BACKUP_LOCKED = new HashMap<Arranger, String[]>();
    private HashMap<Arranger, String[]> locked = new HashMap<Arranger, String[]>(BACKUP_LOCKED);
    private static String[] PORTAL_ORDER = new String[] {
        "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "ADevIsland",
        "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
        "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "CMessenger"
    };

    /*
      I'm not checking worlds here because random portals is off, so the three
       groups that don't normally have a world assigned go first, as they'd
       normally be first on the list to be checked
      I can also combine marker groups that would normally unlock at the same time,
       as long as it doesn't change the overall order or the markers
    */
    private MarkerGroup[] BACKUP_MARKERS = {
        new MarkerGroup(() -> true, new ArrayList<String>(Arrays.asList(
            "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
            "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.F1) && (!locked.containsKey(Arranger.CONNECTOR) || !locked.containsKey(Arranger.F3)), new ArrayList<String>(Arrays.asList(
            "F0-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.F1) && !locked.containsKey(Arranger.F3), new ArrayList<String>(Arrays.asList(
            "F3-Star"
        ))),
        new MarkerGroup(() -> true, new ArrayList<String>(Arrays.asList(
            "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star",
            "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
            "A3-Stashed for Later", "A3-Clock Star",
            "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
            "A4-DCtS"
        ))),
        new MarkerGroup(() -> true, new ArrayList<String>(Arrays.asList(
            "A5-Two Boxes", "A5-Two Boxes Star", "A5-Over the Fence", "A5-YKYMCTS",
            "A5-OLB", "A5-FC", "A5-FC Star",
            "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic",
            "A6-Star",
            "A7-Two Buzzers", "A7-Pinhole", "A7-LFI", "A7-WiaL",
            "A7-Trapped Inside", "A7-Star"
        ))),
        new MarkerGroup(() -> !(locked.containsKey(Arranger.A_STAR) || locked.containsKey(Arranger.B_STAR) || locked.containsKey(Arranger.C_STAR)), new ArrayList<String>(Arrays.asList(
            "A*-DDM", "A*-Nervewrecker", "A*-JfW"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.B_GATE), new ArrayList<String>(Arrays.asList(
            "B1-SaaS", "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence",
            "B1-RoD", "B1-Star",
            "B2-Higher Ground", "B2-Tomb", "B2-MotM", "B2-Moonshot",
            "B2-Star",
            "B3-Sunshot", "B3-Blown Away", "B3-Eagle's Nest", "B3-Woosh",
            "B3-Star",
            "B4-TRA", "B4-ABUH", "B4-Double-Plate", "B4-Self Help",
            "B4-RPS", "B4-WAtC", "B4-TRA Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.B_GATE) && !locked.containsKey(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
            "B4-Sphinx Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.B_GATE), new ArrayList<String>(Arrays.asList(
            "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-SES",
            "B5-Chambers"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.B_GATE) && ((!locked.containsKey(Arranger.CONNECTOR) && !locked.containsKey(Arranger.FAN)) || !locked.containsKey(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
            "B5-Obelisk Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.B_GATE), new ArrayList<String>(Arrays.asList(
            "B6-Egyptian Arcade", "B6-JDaW", "B6-Crisscross",
            "B7-WLJ", "B7-AFaF", "B7-BSbS Star", "B7-BSbS",
            "B7-BLoM"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.B_GATE) && !locked.containsKey(Arranger.FAN), new ArrayList<String>(Arrays.asList(
            "B7-Star"
        ))),
        new MarkerGroup(() -> !(locked.containsKey(Arranger.A_STAR) || locked.containsKey(Arranger.B_STAR) || locked.containsKey(Arranger.C_STAR)), new ArrayList<String>(Arrays.asList(
            "B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE), new ArrayList<String>(Arrays.asList(
            "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE) && !locked.containsKey(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
            "C1-MIA"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE), new ArrayList<String>(Arrays.asList(
            "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
            "C2-Star",
            "C3-Three Connectors", "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop",
            "C3-Star",
            "C4-Stables", "C4-Armory", "C4-Oubliette Star", "C4-Oubliette",
            "C4-Throne Room Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE) && !locked.containsKey(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
            "C4-Throne Room"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE), new ArrayList<String>(Arrays.asList(
            "C5-Time Crawls", "C5-Dumbwaiter", "C5-Time Flies", "C5-UCaJ",
            "C5-Time Flies Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE) && !locked.containsKey(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
            "C5-UCAJ Star", "C5-Dumbwaiter Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE), new ArrayList<String>(Arrays.asList(
            "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star",
            "C7-Prison Break", "C7-Carrier Pigeons", "C7-Crisscross", "C7-DMS",
            "C7-Star"
        ))),
        new MarkerGroup(() -> !(locked.containsKey(Arranger.A_STAR) || locked.containsKey(Arranger.B_STAR) || locked.containsKey(Arranger.C_STAR)), new ArrayList<String>(Arrays.asList(
            "C*-Nexus"
        ))),
        new MarkerGroup(() -> !(locked.containsKey(Arranger.A_STAR) || locked.containsKey(Arranger.B_STAR) || locked.containsKey(Arranger.C_STAR)) && (!locked.containsKey(Arranger.CONNECTOR) || !locked.containsKey(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
            "C*-Cobweb", "C*-Unreachable Garden"
        ))),
        new MarkerGroup(() -> !locked.containsKey(Arranger.C_GATE), new ArrayList<String>(Arrays.asList(
            "CM-Star"
        )))
    };

    public GeneratorAllDefault() {
        BACKUP_LOCKED.put(Arranger.A1_GATE, new String[] {});
        // No random portals means we alwasy start in A, so A gate would be fixed
        BACKUP_LOCKED.put(Arranger.A_GATE, new String[] {"DI1", "DJ3", "DL1", "DZ2"});
        BACKUP_LOCKED.put(Arranger.B_GATE, new String[] {});
        BACKUP_LOCKED.put(Arranger.C_GATE, new String[] {});
        BACKUP_LOCKED.put(Arranger.A_STAR, new String[] {"**1", "**2", "**3", "**4", "**5", "**6", "**7", "**8", "**9", "**10"});
        BACKUP_LOCKED.put(Arranger.B_STAR, new String[] {"**11", "**12", "**13", "**14", "**15", "**16", "**17", "**18", "**19", "**20"});
        BACKUP_LOCKED.put(Arranger.C_STAR, new String[] {"**21", "**22", "**23", "**24", "**25", "**26", "**27", "**28", "**29", "**30"});
        BACKUP_LOCKED.put(Arranger.CONNECTOR, new String[] {"ML1", "MT1", "MT2"});
        BACKUP_LOCKED.put(Arranger.CUBE, new String[] {"ML2", "MT3", "MT4", "MZ1"});
        BACKUP_LOCKED.put(Arranger.FAN, new String[] {"ML3", "MS1", "MT5", "MT6", "MZ2"});
        BACKUP_LOCKED.put(Arranger.RECORDER, new String[] {"MJ1", "MS2", "MT7", "MT8", "MZ3"});
        BACKUP_LOCKED.put(Arranger.PLATFORM, new String[] {"MI1", "ML4", "MO1", "MT9", "MT10", "MZ4"});
        BACKUP_LOCKED.put(Arranger.F1, new String[] {"NL1", "NL2", "NZ1", "NZ2"});
        BACKUP_LOCKED.put(Arranger.F2, new String[] {"NL3", "NL4", "NL5", "NL6", "NO1", "NT1", "NT2", "NT3", "NT4"});
        BACKUP_LOCKED.put(Arranger.F3, new String[] {"NI1", "NI2", "NI3", "NI4", "NJ1", "NJ2", "NL7", "NL8", "NS1", "NZ3"});
        BACKUP_LOCKED.put(Arranger.F4, new String[] {"NJ3", "NL9", "NO2", "NO3", "NS2", "NS3", "NT5", "NT6", "NT7", "NT8", "NZ4", "NZ5"});
        BACKUP_LOCKED.put(Arranger.F5, new String[] {"NI5", "NI6", "NJ4", "NL10", "NO4", "NO5", "NO6", "NO7", "NS4", "NT9", "NT10", "NT11", "NT12", "NZ6"});
        BACKUP_LOCKED.put(Arranger.F6, new String[] {"EL1", "EL2", "EL3", "EL4", "EO1", "ES1", "ES2", "ES3", "ES4"});
        locked = new HashMap<Arranger, String[]>(BACKUP_LOCKED);
        for (int i = 1; i < TalosProgress.TETROS.length - 1; i++) {
            TETRO_INDEXES.put(TalosProgress.TETROS[i], i);
        }
    }

    public TalosProgress generate(long seed) {
        TalosProgress progress = new TalosProgress();
        progress.setVar("Randomizer_Seed", (int)seed);
        Rand r = new Rand(seed);

        // Clone what would normally be destroyed
        locked = new HashMap<Arranger, String[]>(BACKUP_LOCKED);
        MarkerGroup[] markers = new MarkerGroup[BACKUP_MARKERS.length];
        for (int i = 0; i < BACKUP_MARKERS.length; i++) {
            markers[i] = BACKUP_MARKERS[i].clone();
        }

        // These help with the checksum, can't just do dummy calls
        progress.setVar("PaintItemSeed", r.next(0, 8909478));
        progress.setVar("Code_Floor4", r.next(1, 999));
        progress.setVar("Code_Floor5", r.next(1, 999));
        progress.setVar("Code_Floor6", r.next(4, 9)*100 + r.next(4, 9)*10 + r.next(4, 9));

        // Portals will always be in this order, just need it for the checksum
        for (int i = 0; i < PORTAL_ORDER.length; i++) {
            progress.setVar(PORTAL_ORDER[i], i + 1);
        }

        boolean checkGates = true;
        int arrangerStage = 0;
        int availableMarkers = 0;
        Arranger currentArranger;
        ArrayList<Arranger> accessableArrangers = new ArrayList<Arranger>();
        ArrayList<MarkerGroup> openMarkers = new ArrayList<MarkerGroup>();
        // I still need to use the open/closed markers system but I can hardcode the indexes
        ArrayList<Integer> closedMarkers = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));

        while (arrangerStage != -1 || accessableArrangers.size() > 0) {
            // Find new markers
            for (int i = 0; i < closedMarkers.size(); i++) {
                int index = closedMarkers.get(i);
                if (markers[index].isOpen()) {
                    openMarkers.add(markers[index]);
                    closedMarkers.remove(i);
                    availableMarkers += markers[index].getSize();
                    // Indexes shift down when we remove something
                    i--;
                }
            }

            // Work out arranger unlocking
            switch (arrangerStage) {
                case 0: {
                    accessableArrangers.add(Arranger.A_GATE);
                    arrangerStage++;
                    break;
                }
                case 1: {
                    accessableArrangers.addAll(Arrays.asList(Arranger.B_GATE, Arranger.C_GATE));
                    arrangerStage++;
                    break;
                }
                case 2: {
                    if (accessableArrangers.size() == 0) {
                        accessableArrangers.addAll(Arrays.asList(Arranger.CONNECTOR, Arranger.CUBE, Arranger.FAN, Arranger.RECORDER, Arranger.PLATFORM));
                        arrangerStage++;
                    }
                    break;
                }
                case 3: {
                    if (accessableArrangers.size() == 0) {
                        accessableArrangers.addAll(Arrays.asList(Arranger.A_STAR, Arranger.B_STAR, Arranger.C_STAR));
                        arrangerStage++;
                    }
                    break;
                }
                case 4: {
                    if (accessableArrangers.size() == 0) {
                        accessableArrangers.addAll(Arrays.asList(Arranger.F1, Arranger.F2, Arranger.F3));
                        arrangerStage++;
                    }
                    break;
                }
                case 5: {
                    if (accessableArrangers.size() == 0) {
                        accessableArrangers.addAll(Arrays.asList(Arranger.F4, Arranger.F5, Arranger.F6, Arranger.A1_GATE));
                        arrangerStage = -1;
                    }
                    break;
                }
            }

            currentArranger = accessableArrangers.remove(r.next(0, accessableArrangers.size() - 1));
            String[] sigils = locked.remove(currentArranger);

            /*
              Wrong hub softlock prevention
              This is slightly simpler because we know you're going to start in A
            */
            if (checkGates && (currentArranger == Arranger.B_GATE || currentArranger == Arranger.C_GATE)) {
                if (r.next(0, 99) < 25) {
                    sigils = new String[] {
                        "DI2", "DL2", "DT1", "DT2", "DZ3",
                        "DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"
                    };
                    accessableArrangers.removeAll(Arrays.asList(Arranger.B_GATE, Arranger.C_GATE));
                    closedMarkers.addAll(Arrays.asList(7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
                    locked.put(Arranger.A1_GATE, new String[] {"DJ1", "DJ2", "DZ1"});
                    locked.remove(Arranger.A_GATE);
                    locked.remove(Arranger.B_GATE);
                    locked.remove(Arranger.C_GATE);
                } else {
                    String[] uniqueSigils;
                    if (currentArranger == Arranger.B_GATE) {
                        uniqueSigils = new String[] {"DJ1", "DJ4", "DJ5"};
                        locked.put(Arranger.A1_GATE, new String[] {"DJ2", "DZ1"});
                        locked.put(Arranger.C_GATE, new String[] {"DL3", "DT3", "DT4", "DZ4"});
                        sigils = new String[] {"DI2", "DL2", "DT1", "DT2", "DZ3"};
                        closedMarkers.addAll(Arrays.asList(7, 8, 9, 10, 11, 12, 13));
                    } else {
                        uniqueSigils = new String[] {"DI2"};
                        locked.put(Arranger.A1_GATE, new String[] {"DJ1", "DJ2", "DZ1"});
                        locked.put(Arranger.B_GATE, new String[] {"DL2", "DT1", "DT2", "DZ3"});
                        sigils = new String[] {"DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"};
                        closedMarkers.addAll(Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
                    }

                    // Find new spots
                    ArrayList<MarkerGroup> tempOpenMarkers = new ArrayList<MarkerGroup>();
                    int tempAvailableMarkers = 0;
                    for (int index : closedMarkers) {
                        if (markers[index].isOpen()) {
                            tempOpenMarkers.add(markers[index]);
                            tempAvailableMarkers += markers[index].getSize();
                        }
                    }

                    // Place unique sigils in new spots
                    for (String sigil : uniqueSigils) {
                        int index = r.next(0, tempAvailableMarkers - 1);
                        for (MarkerGroup group : tempOpenMarkers) {
                            if (index >= group.getSize()) {
                                index -= group.getSize();
                            } else {
                                String randMarker = group.getMarkers().remove(index);
                                progress.setVar(randMarker, TETRO_INDEXES.get(sigil));
                                tempAvailableMarkers -= 1;
                                break;
                            }
                        }
                    }
                }
                checkGates = false;
            } else if (!checkGates) {
                if (currentArranger == Arranger.B_GATE) {
                    closedMarkers.addAll(Arrays.asList(7, 8, 9, 10, 11, 12, 13));
                } else if (currentArranger == Arranger.C_GATE) {
                    closedMarkers.addAll(Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
                }
            }

            // Place sigils
            for (String sigil : sigils) {
                int index = r.next(0, availableMarkers - 1);
                for (MarkerGroup group : openMarkers) {
                    if (index >= group.getSize()) {
                        index -= group.getSize();
                    } else {
                        String randMarker = group.getMarkers().remove(index);
                        progress.setVar(randMarker, TETRO_INDEXES.get(sigil));
                        availableMarkers -= 1;
                        if (group.getSize() < 1) {
                            openMarkers.remove(group);
                        }
                        break;
                    }
                }
            }
        }
        return progress;
    }
}
