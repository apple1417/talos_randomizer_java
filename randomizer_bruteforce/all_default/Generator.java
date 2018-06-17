package randomizer_bruteforce.all_default;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import randomizer_bruteforce.*;

class Generator extends Thread implements Runnable {
    private Thread t;
    private String threadName;
    private long min;
    private long max;

    Generator(String name) {
        threadName = name;
    }

    public void run() {
        for(long i = min; i <= max; i++) {
            check(i);
        }
    }

    public void start(long min, long max) {
        setup();
        this.min = min;
        this.max = max;
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        } else {
            System.out.println("Already running");
        }
    }

    public void waitFinished() {
        try {
            t.join();
        } catch (InterruptedException e) {
            t = null;
        }
    }

    private static HashMap<String, Integer> TETRO_INDEXES = new HashMap<String, Integer>();
    private static HashMap<String, String[]> BACKUP_LOCKED = new HashMap<String, String[]>();
    private HashMap<String, String[]> locked = new HashMap<String, String[]>(BACKUP_LOCKED);
    private static String[] PORTAL_ORDER = new String[] {
        "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "ADevIsland",
        "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
        "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "CMessenger"
    };
    private static String[] A_MARKERS = {
        "A1-PaSL", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU", "A1-ASooR", "A1-Trio", "A1-Peephole", "A1-Star",
        "A2-Guards", "A2-Hall of Windows", "A2-Suicide Mission", "A2-Star",
        "A3-Stashed for Later", "A3-ABTU", "A3-ABTU Star", "A3-Swallowed the Key", "A3-AEP", "A3-Clock Star",
        "A4-Branch it Out", "A4-Above All That", "A4-Push it Further", "A4-Star", "A4-DCtS",
        "A5-Two Boxes", "A5-Two Boxes Star", "A5-YKYMCTS", "A5-Over the Fence", "A5-OLB", "A5-FC", "A5-FC Star",
        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic", "A6-Star",
        "A7-LFI", "A7-Trapped Inside", "A7-Two Buzzers", "A7-Star", "A7-WiaL", "A7-Pinhole"
    };

    private MarkerGroup[] BACKUP_MARKERS = {
        new MarkerGroup(() -> true, new ArrayList<String>(Arrays.asList(
            "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
            "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("F1") && (!locked.containsKey("Connector") || !locked.containsKey("F3")), new ArrayList<String>(Arrays.asList(
            "F0-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("F3"), new ArrayList<String>(Arrays.asList(
            "F3-Star"
        ))),
        new MarkerGroup(() -> true, new ArrayList<String>(Arrays.asList(
            "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star",
            "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
            "A3-Stashed for Later", "A3-Clock Star",
            "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("Connector"), new ArrayList<String>(Arrays.asList(
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
        new MarkerGroup(() -> !(locked.containsKey("A Star") || locked.containsKey("B Star") || locked.containsKey("C Star")), new ArrayList<String>(Arrays.asList(
            "A*-DDM", "A*-Nervewrecker", "A*-JfW"
        ))),
        new MarkerGroup(() -> !locked.containsKey("B Gate"), new ArrayList<String>(Arrays.asList(
            "B1-SaaS", "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence",
            "B1-RoD", "B1-Star",
            "B2-Higher Ground", "B2-Tomb", "B2-MotM", "B2-Moonshot",
            "B2-Star",
            "B3-Sunshot", "B3-Blown Away", "B3-Eagle's Nest", "B3-Woosh",
            "B3-Star",
            "B4-TRA", "B4-ABUH", "B4-Double-Plate", "B4-Self Help",
            "B4-RPS", "B4-WAtC", "B4-TRA Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("B Gate") && !locked.containsKey("Connector"), new ArrayList<String>(Arrays.asList(
            "B4-Sphinx Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("B Gate"), new ArrayList<String>(Arrays.asList(
            "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-SES",
            "B5-Chambers"
        ))),
        new MarkerGroup(() -> !locked.containsKey("B Gate") && ((!locked.containsKey("Connector") && !locked.containsKey("Fan")) || !locked.containsKey("Cube")), new ArrayList<String>(Arrays.asList(
            "B5-Obelisk Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("B Gate"), new ArrayList<String>(Arrays.asList(
            "B6-Egyptian Arcade", "B6-JDaW", "B6-Crisscross",
            "B7-WLJ", "B7-AFaF", "B7-BSbS Star", "B7-BSbS",
            "B7-BLoM"
        ))),
        new MarkerGroup(() -> !locked.containsKey("B Gate") && !locked.containsKey("Fan"), new ArrayList<String>(Arrays.asList(
            "B7-Star"
        ))),
        new MarkerGroup(() -> !(locked.containsKey("A Star") || locked.containsKey("B Star") || locked.containsKey("C Star")), new ArrayList<String>(Arrays.asList(
            "B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate"), new ArrayList<String>(Arrays.asList(
            "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate") && !locked.containsKey("Cube"), new ArrayList<String>(Arrays.asList(
            "C1-MIA"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate"), new ArrayList<String>(Arrays.asList(
            "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
            "C2-Star",
            "C3-Three Connectors", "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop",
            "C3-Star",
            "C4-Stables", "C4-Armory", "C4-Oubliette Star", "C4-Oubliette",
            "C4-Throne Room Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate") && !locked.containsKey("Cube"), new ArrayList<String>(Arrays.asList(
            "C4-Throne Room"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate"), new ArrayList<String>(Arrays.asList(
            "C5-Time Crawls", "C5-Dumbwaiter", "C5-Time Flies", "C5-UCaJ",
            "C5-Time Flies Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate") && !locked.containsKey("Cube"), new ArrayList<String>(Arrays.asList(
            "C5-UCAJ Star", "C5-Dumbwaiter Star"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate"), new ArrayList<String>(Arrays.asList(
            "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star",
            "C7-Prison Break", "C7-Carrier Pigeons", "C7-Crisscross", "C7-DMS",
            "C7-Star"
        ))),
        new MarkerGroup(() -> !(locked.containsKey("A Star") || locked.containsKey("B Star") || locked.containsKey("C Star")), new ArrayList<String>(Arrays.asList(
            "C*-Nexus"
        ))),
        new MarkerGroup(() -> !(locked.containsKey("A Star") || locked.containsKey("B Star") || locked.containsKey("C Star")) && (!locked.containsKey("Connector") || !locked.containsKey("Cube")), new ArrayList<String>(Arrays.asList(
            "C*-Cobweb", "C*-Unreachable Garden"
        ))),
        new MarkerGroup(() -> !locked.containsKey("C Gate"), new ArrayList<String>(Arrays.asList(
            "CM-Star"
        )))
    };

    public void setup() {
        BACKUP_LOCKED.put("A1 Gate", new String[] {});
        BACKUP_LOCKED.put("A Gate", new String[] {"DI1", "DJ3", "DL1", "DZ2"});
        BACKUP_LOCKED.put("B Gate", new String[] {});
        BACKUP_LOCKED.put("C Gate", new String[] {});
        BACKUP_LOCKED.put("A Star", new String[] {"**1", "**2", "**3", "**4", "**5", "**6", "**7", "**8", "**9", "**10"});
        BACKUP_LOCKED.put("B Star", new String[] {"**11", "**12", "**13", "**14", "**15", "**16", "**17", "**18", "**19", "**20"});
        BACKUP_LOCKED.put("C Star", new String[] {"**21", "**22", "**23", "**24", "**25", "**26", "**27", "**28", "**29", "**30"});
        BACKUP_LOCKED.put("Connector", new String[] {"ML1", "MT1", "MT2"});
        BACKUP_LOCKED.put("Cube", new String[] {"ML2", "MT3", "MT4", "MZ1"});
        BACKUP_LOCKED.put("Fan", new String[] {"ML3", "MS1", "MT5", "MT6", "MZ2"});
        BACKUP_LOCKED.put("Recorder", new String[] {"MJ1", "MS2", "MT7", "MT8", "MZ3"});
        BACKUP_LOCKED.put("Platform", new String[] {"MI1", "ML4", "MO1", "MT9", "MT10", "MZ4"});
        BACKUP_LOCKED.put("F1", new String[] {"NL1", "NL2", "NZ1", "NZ2"});
        BACKUP_LOCKED.put("F2", new String[] {"NL3", "NL4", "NL5", "NL6", "NO1", "NT1", "NT2", "NT3", "NT4"});
        BACKUP_LOCKED.put("F3", new String[] {"NI1", "NI2", "NI3", "NI4", "NJ1", "NJ2", "NL7", "NL8", "NS1", "NZ3"});
        BACKUP_LOCKED.put("F4", new String[] {"NJ3", "NL9", "NO2", "NO3", "NS2", "NS3", "NT5", "NT6", "NT7", "NT8", "NZ4", "NZ5"});
        BACKUP_LOCKED.put("F5", new String[] {"NI5", "NI6", "NJ4", "NL10", "NO4", "NO5", "NO6", "NO7", "NS4", "NT9", "NT10", "NT11", "NT12", "NZ6"});
        BACKUP_LOCKED.put("F6", new String[] {"EL1", "EL2", "EL3", "EL4", "EO1", "ES1", "ES2", "ES3", "ES4"});
        locked = new HashMap<String, String[]>(BACKUP_LOCKED);
        for (int i = 0; i < TalosProgress.TETROS.length; i++) {
            TETRO_INDEXES.put(TalosProgress.TETROS[i], i + 1);
        }
    }

    private TalosProgress empty;
    public TalosProgress generate(long seed) {
        TalosProgress progress = new TalosProgress();
        progress.setVar("Randomizer_Seed", (int)seed);
        Rand r = new Rand(seed);

        locked = new HashMap<String, String[]>(BACKUP_LOCKED);
        MarkerGroup[] markers = new MarkerGroup[BACKUP_MARKERS.length];
        for (int i = 0; i < BACKUP_MARKERS.length; i++) {
            markers[i] = BACKUP_MARKERS[i].clone();
        }

        progress.setVar("PaintItemSeed", r.next(0, 8909478));
        progress.setVar("Code_Floor4", r.next(1, 999));
        progress.setVar("Code_Floor5", r.next(1, 999));
        progress.setVar("Code_Floor6", r.next(4, 9)*100 + r.next(4, 9)*10 + r.next(4, 9));

        for (int i = 0; i < PORTAL_ORDER.length; i++) {
            progress.setVar(PORTAL_ORDER[i], i + 1);
        }

        boolean checkGates = true;
        int arrangerStage = 0;
        int availableMarkers = 0;
        String arranger = new String();
        ArrayList<String> accessableArrangers = new ArrayList<String>();
        ArrayList<MarkerGroup> openMarkers = new ArrayList<MarkerGroup>();
        ArrayList<Integer> closedMarkers = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6));

        while (!(arrangerStage > 4) || accessableArrangers.size() > 0) {
            for (int i = 0; i < closedMarkers.size(); i++) {
                int index = closedMarkers.get(i);
                if (markers[index].isOpen()) {
                    openMarkers.add(markers[index]);
                    closedMarkers.remove(i);
                    availableMarkers += markers[index].getSize();
                    i--;
                }
            }


            switch (arrangerStage) {
                case 0: {
                    accessableArrangers.add("A Gate");
                    arrangerStage++;
                    break;
                }
                case 1: {
                    accessableArrangers.addAll(Arrays.asList("B Gate", "C Gate"));
                    arrangerStage++;
                    break;
                }
                case 2: {
                    if (!locked.containsKey("B Gate") && !locked.containsKey("C Gate")) {
                        accessableArrangers.addAll(Arrays.asList("A Star", "B Star", "C Star"));
                        arrangerStage++;
                    }
                    break;
                }
                case 3: {
                    if (!(locked.containsKey("A Star") || locked.containsKey("B Star") || locked.containsKey("C Star"))) {
                        accessableArrangers.addAll(Arrays.asList("Connector", "Cube", "Fan", "Recorder", "Platform", "F1", "F3"));
                        arrangerStage++;
                    }
                    break;
                }
                case 4: {
                    if (closedMarkers.size() == 0) {
                        accessableArrangers.addAll(Arrays.asList("F4", "F5", "F6", "A1 Gate", "F2"));
                        arrangerStage++;
                    }
                    break;
                }
            }

            arranger = accessableArrangers.remove(r.next(0, accessableArrangers.size() - 1));
            String[] sigils = locked.remove(arranger);
            if (checkGates && (arranger == "B Gate" || arranger == "C Gate")) {
                if (r.next(0, 99) < 25) {
                    sigils = new String[] {
                        "DI2", "DL2", "DT1", "DT2", "DZ3",
                        "DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"
                    };
                    accessableArrangers.removeAll(Arrays.asList("B Gate", "C Gate"));
                    closedMarkers.addAll(Arrays.asList(7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
                    locked.put("A1 Gate", new String[] {"DJ1", "DJ2", "DZ1"});
                    locked.remove("A Gate");
                    locked.remove("B Gate");
                    locked.remove("C Gate");
                } else {
                    String[] uniqueSigils;
                    if (arranger == "B Gate") {
                        uniqueSigils = new String[] {"DJ1", "DJ4", "DJ5"};
                        locked.put("A1 Gate", new String[] {"DJ2", "DZ1"});
                        locked.put("C Gate", new String[] {"DL3", "DT3", "DT4", "DZ4"});
                        sigils = new String[] {"DI2", "DL2", "DT1", "DT2", "DZ3"};
                        closedMarkers.addAll(Arrays.asList(7, 8, 9, 10, 11, 12, 13));
                    } else {
                        uniqueSigils = new String[] {"DI2"};
                        locked.put("A1 Gate", new String[] {"DJ1", "DJ2", "DZ1"});
                        locked.put("B Gate", new String[] {"DL2", "DT1", "DT2", "DZ3"});
                        sigils = new String[] {"DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"};
                        closedMarkers.addAll(Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
                    }

                    ArrayList<MarkerGroup> tempOpenMarkers = new ArrayList<MarkerGroup>();
                    int tempAvailableMarkers = 0;
                    for (int index : closedMarkers) {
                        if (markers[index].isOpen()) {
                            tempOpenMarkers.add(markers[index]);
                            tempAvailableMarkers += markers[index].getSize();
                        }
                    }

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
                if (arranger == "B Gate") {
                    closedMarkers.addAll(Arrays.asList(7, 8, 9, 10, 11, 12, 13));
                } else if (arranger == "C Gate") {
                    closedMarkers.addAll(Arrays.asList(14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
                }
            }

            for (String sigil : sigils) {
                int index = r.next(0, availableMarkers - 1);
                for (MarkerGroup group : openMarkers) {
                    if (index >= group.getSize()) {
                        index -= group.getSize();
                    } else {
                        String randMarker = group.getMarkers().remove(index);
                        if (sigil.charAt(0) == 'E' && (randMarker.charAt(0) != 'A' || randMarker.charAt(1) == '*')) {
                            return empty;
                        }
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

    public boolean check(long seed) {
        TalosProgress progress = generate(seed);
        if (progress == null) {
            return false;
        }
        int lCount = 0;
        int zCount = 0;
        for (String marker : A_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker) - 1];
            if (sigil.startsWith("NL")) {
                lCount++;
            } else if (sigil.startsWith("NZ")) {
                zCount++;
            }
        }
        if (lCount >= 2 && zCount >= 2) {
            String output = String.format("%d, %s, %d", seed, progress.getChecksum(), progress.getVar("Code_Floor6"));
            System.out.println(output);
            try {
                Files.write(Paths.get("output.txt"), (output + "\n").getBytes(), StandardOpenOption.APPEND);
            } catch (Exception e) {}
            return true;
        }
        return false;
    }
}
