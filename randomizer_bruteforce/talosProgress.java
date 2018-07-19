package randomizer_bruteforce;

import java.util.HashMap;
import java.util.Iterator;

public class TalosProgress {
    private HashMap<String, Integer> data;

    public TalosProgress(HashMap<String, Integer> options) {
        data = new HashMap<String, Integer>();
        data.put("Randomizer_Mode", 1);
        data.put("Randomizer_ShowAll", 1);
        data.put("Randomizer_Scavenger", 0);
        data.put("Randomizer_Loop", 0);
        data.putAll(options);
    }

    public TalosProgress() {
        this(new HashMap<String, Integer>());
    }

    public void setVar(String key, int val) {
        data.put(key, val);
    }

    public int getVar(String key) {
        return data.containsKey(key) ? data.get(key) : -1;
    }

    public void printMemory() {
        Iterator<String> it = data.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            System.out.println(key + ": " + data.get(key));
        }
    }

    public TalosProgress clone() {
        return new TalosProgress(data);
    }

    public static final String[] TETROS = {"Incorrect Index",
        "**1",  "**2",  "**3",  "**4",  "**5",  "**6",   "**7",  "**8",  "**9",  "**10",
        "**11", "**12", "**13", "**14", "**15", "**16",  "**17", "**18", "**19", "**20",
        "**21", "**22", "**23", "**24", "**25", "**26",  "**27", "**28", "**29", "**30",
        "DI1",  "DI2",  "DJ1",  "DJ2",  "DJ3",  "DJ4",   "DJ5",  "DL1",  "DL2",  "DL3",
        "DT1",  "DT2",  "DT3",  "DT4",  "DZ1",  "DZ2",   "DZ3",  "DZ4",  "EL1",  "EL2",
        "EL3",  "EL4",  "EO1",  "ES1",  "ES2",  "ES3",   "ES4",  "MI1",  "MJ1",  "ML1",
        "ML2",  "ML3",  "ML4",  "MO1",  "MS1",  "MS2",   "MT1",  "MT2",  "MT3",  "MT4",
        "MT5",  "MT6",  "MT7",  "MT8",  "MT9",  "MT10",  "MZ1",  "MZ2",  "MZ3",  "MZ4",
        "NI1",  "NI2",  "NI3",  "NI4",  "NI5",  "NI6",   "NJ1",  "NJ2",  "NJ3",  "NJ4",
        "NL1",  "NL2",  "NL3",  "NL4",  "NL5",  "NL6",   "NL7",  "NL8",  "NL9",  "NL10",
        "NO1",  "NO2",  "NO3",  "NO4",  "NO5",  "NO6",   "NO7",  "NS1",  "NS2",  "NS3",
        "NS4",  "NT1",  "NT2",  "NT3",  "NT4",  "NT5",   "NT6",  "NT7",  "NT8",  "NT9",
        "NT10", "NT11", "NT12", "NZ1",  "NZ2",  "NZ3",   "NZ4",  "NZ5",  "NZ6",
        "Incorrect Index"
    };

    private static String[] CHECKSUM_MARKERS = {
        "A1-Peephole", "A2-Hall of Windows", "A1-PaSL", "A1-Outnumbered",
        "A1-ASooR", "A2-Guards", "A1-OtToU", "A1-Trio",
        "A2-Suicide Mission", "A3-ABTU Star", "A3-ABTU", "A3-AEP",
        "A3-Swallowed the Key", "A1-Beaten Path", "A3-Stashed for Later", "A6-Mobile Mindfield",
        "C6-Two Way Street", "B6-Egyptian Arcade", "C1-Labyrinth", "A*-DDM",
        "A*-Nervewrecker", "B7-WLJ", "A*-JfW", "B2-Higher Ground",
        "A5-Two Boxes", "A5-Two Boxes Star", "A4-Push it Further", "A4-Branch it Out",
        "A4-DCtS", "A5-Over the Fence", "A4-Above All That", "B1-WtaD",
        "A5-YKYMCTS", "B1-SaaS", "B6-JDaW", "A5-OLB",
        "B6-Crisscross", "A7-Two Buzzers", "A6-Deception", "A7-Pinhole",
        "A7-LFI", "B2-Tomb", "B*-Merry Go Round", "B4-TRA",
        "C4-Stables", "C2-Cemetery", "A6-Door too Far", "C1-Conservatory",
        "A7-WiaL", "B2-MotM", "B3-Sunshot", "B2-Moonshot",
        "B7-AFaF", "B7-BSbS Star", "C3-Three Connectors", "C6-Circumlocution",
        "C7-Prison Break", "C3-BSLS", "C7-Carrier Pigeons", "C5-Time Crawls",
        "C7-Crisscross", "C6-Seven Doors", "C*-Unreachable Garden", "B5-Plates",
        "B5-Two Jammers", "A5-FC", "A5-FC Star", "B3-Blown Away",
        "B7-BSbS", "B1-Third Wheel", "A6-Bichromatic", "A7-Trapped Inside",
        "B*-Cat's Cradle", "C7-DMS", "C5-Dumbwaiter", "B4-ABUH",
        "B5-Iron Curtain", "C1-Blowback", "C4-Armory", "B5-SES",
        "B5-Chambers", "B1-Over the Fence", "C3-Jammer Quarantine", "C3-Weathertop",
        "B1-RoD", "C*-Nexus", "B4-Double-Plate", "B4-Self Help",
        "C5-Time Flies", "B3-Eagle's Nest", "B7-BLoM", "C2-ADaaF",
        "C5-UCaJ", "C4-Oubliette Star", "C4-Oubliette", "B4-RPS",
        "C4-Throne Room", "C4-Throne Room Star", "B3-Woosh", "B*-Peekaboo",
        "B4-WAtC", "C1-MIA", "C2-Rapunzel", "C2-Short Wall",
        "C*-Cobweb", "A1-Star", "A2-Star", "A3-Clock Star",
        "A4-Star", "A6-Star", "A7-Star", "B1-Star",
        "B2-Star", "B3-Star", "B4-TRA Star", "B4-Sphinx Star",
        "B5-Obelisk Star", "B7-Star", "C1-Star", "C2-Star",
        "C3-Star", "C5-UCAJ Star", "C5-Dumbwaiter Star", "C5-Time Flies Star",
        "C6-Star", "C7-Star", "CM-Star", "F3-Star",
        "F0-Star",
        "PaintItemSeed", "Code_Floor4", "Code_Floor5", "Code_Floor6",
        "A1", "A2", "A3", "A4", "A5", "A6", "A7", "A8", "ADevIsland",
        "B1", "B2", "B3", "B4", "B5", "B6", "B7", "B8",
        "C1", "C2", "C3", "C4", "C5", "C6", "C7", "C8", "CMessenger",
        "Randomizer_Seed", "Randomizer_Mode", "Randomizer_Scavenger", "Randomizer_Loop"
    };

    public String getChecksum(boolean showWarning) {
        int sum1 = 0;
        int sum2 = 0;
        for (int index = 0; index < CHECKSUM_MARKERS.length; index++) {
            String key = CHECKSUM_MARKERS[index];
            int value = getVar(key);
            if (showWarning && value == -1) {
                System.err.println(String.format("'%s' does not have a value assigned to it", key));
            }
            sum1 = (sum1 + value*(index + 1)) & 0xFFFF;
            sum2 = (sum2 + sum1) & 0xFFFF;
        }
        return String.format("%04X%04X", sum1, sum2);
    }

    public String getChecksum() {
        return getChecksum(true);
    }
}
