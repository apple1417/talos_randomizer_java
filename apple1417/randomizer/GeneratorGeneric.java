package apple1417.randomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import apple1417.randomizer.Enums.*;
import apple1417.randomizer.Generator;
import apple1417.randomizer.MarkerGroup;
import apple1417.randomizer.Rand;
import apple1417.randomizer.TalosProgress;

/*
  A full simulator of the standard lua generator
  This is of course going to be slower than a specialized one, but it can do everything
   without needing you to do any work beforehand
*/

public class GeneratorGeneric implements Generator {
    public String getInfo() {
        return String.format("Generic, v12.2.2\nOptions: %s, %s Portals, Mobius %s, %s",
                             mode.toString(),
                             portals ? "Random" : "Standard",
                             loop != 0 ? "On" : "Off",
                             scavenger == ScavengerMode.OFF ? "Scavenger Off"
                                                            : (scavenger.toString() + " Scavenger")
                             );
    }

    /*
      Define all our constants
      A few variables get destroyed during generation so they have backup versions
    */
    private static HashMap<String, Integer> TETRO_INDEXES = new HashMap<String, Integer>();
    private static HashMap<Arranger, ArrayList<String>> BACKUP_ARRANGER_SIGILS = new HashMap<Arranger, ArrayList<String>>();
    private HashMap<Arranger, ArrayList<String>> arrangerSigils = new HashMap<Arranger, ArrayList<String>>(BACKUP_ARRANGER_SIGILS);
    private TalosProgress BACKUP_PROGRESS;
    private MarkerGroup[] BACKUP_MARKERS = new MarkerGroup[] {};

    private static String[] MARKERS_SIMPLE = new String[] {
      "A3-ABTU Star", "A2-Star", "A3-Clock Star", "A4-Star", "A1-Star",
      "A5-FC Star", "A5-Two Boxes Star", "A6-Star", "A7-Star", "B1-Star",
      "B2-Star", "B3-Star", "B4-TRA Star", "B5-Obelisk Star", "B7-Star",
      "B7-BSbS Star", "C1-Star", "C2-Star", "C3-Star", "C4-Oubliette Star",
      "C4-Throne Room Star", "C5-Dumbwaiter Star", "C5-Time Flies Star", "B4-Sphinx Star", "F3-Star",
      "CM-Star", "C5-UCAJ Star", "F0-Star", "C6-Star", "C7-Star",
      "A1-PaSL", "A5-Two Boxes", "A1-Beaten Path", "A1-Outnumbered", "A1-OtToU",
      "B4-Self Help", "B5-SES", "A2-Guards", "A5-YKYMCTS", "A7-LFI",
      "A5-Over the Fence", "A5-OLB", "A7-Trapped Inside", "B4-Double-Plate", "A1-ASooR",
      "A2-Hall of Windows", "A3-Stashed for Later", "A6-Mobile Mindfield", "A*-JfW", "B*-Merry Go Round",
      "B*-Peekaboo", "C*-Unreachable Garden", "C*-Nexus", "A*-Nervewrecker", "B*-Cat's Cradle",
      "A*-DDM", "C*-Cobweb", "B7-BSbS", "B3-Blown Away", "A1-Trio",
      "A2-Suicide Mission", "B1-WtaD", "B6-Crisscross", "B7-BLoM", "B1-Third Wheel",
      "B2-Tomb", "A1-Peephole", "A3-ABTU", "A3-Swallowed the Key", "A4-Branch it Out",
      "A4-Above All That", "B1-Over the Fence", "B1-RoD", "B2-MotM", "B3-Sunshot",
      "B6-JDaW", "A4-Push it Further", "A4-DCtS", "B1-SaaS", "B2-Moonshot",
      "B5-Plates", "B6-Egyptian Arcade", "B7-AFaF", "C1-Conservatory", "C4-Armory",
      "C5-Time Flies", "B7-WLJ", "C1-MIA", "C3-Jammer Quarantine", "C6-Circumlocution",
      "A3-AEP", "A6-Deception", "A6-Door too Far", "A7-Two Buzzers", "B2-Higher Ground",
      "B3-Eagle's Nest", "B4-ABUH", "B4-WAtC", "B5-Two Jammers", "C7-DMS",
      "A7-WiaL", "C2-ADaaF", "C3-Three Connectors", "C5-Time Crawls", "C5-Dumbwaiter",
      "C6-Seven Doors", "C7-Carrier Pigeons", "B5-Iron Curtain", "C2-Rapunzel", "C4-Oubliette",
      "C6-Two Way Street", "A7-Pinhole", "B3-Woosh", "B4-TRA", "B4-RPS",
      "C1-Labyrinth", "C2-Cemetery", "C3-BSLS", "C4-Stables", "C4-Throne Room",
      "C5-UCaJ", "C7-Prison Break", "C7-Crisscross", "A5-FC", "A6-Bichromatic",
      "B5-Chambers", "C1-Blowback", "C2-Short Wall", "C3-Weathertop"
    };

    private TalosProgress progress;
    private RandomizerMode mode;
    private ScavengerMode scavenger;
    private boolean portals;
    private int loop;
    private HashSet<World> openWorlds;

    // These two helper functions are used in the marker lists to define when groups unlock
    private boolean unlocked(Arranger arrangerName) {
        return !arrangerSigils.containsKey(arrangerName);
    }

    private boolean isWorldOpen(World worldName) {
        if (worldName == World.A1) {
            return true;
        }
        if (mode == RandomizerMode.INTENDED && !unlocked(Arranger.A1_GATE)) {
            return false;
        }
        if (scavenger == ScavengerMode.FULL || loop != 0) {
            return true;
        }

        boolean starOverride = true;
        int worldNum = progress.getVar(worldName.toString());
        if (worldNum == 8 | worldNum == 17 | worldNum == 25) {
            starOverride = unlocked(Arranger.A_STAR) && unlocked(Arranger.B_STAR) && unlocked(Arranger.C_STAR);
        }
        return starOverride && openWorlds.contains(worldName);
    }

    public GeneratorGeneric(TalosProgress progress) {
        BACKUP_PROGRESS = progress.clone();

        // Check what options have been set
        mode = RandomizerMode.fromTalosProgress(progress);
        scavenger = ScavengerMode.fromTalosProgress(progress);
        portals = progress.getVar("Randomizer_Portals") != -1;
        loop = progress.getVar("Randomizer_Loop");

        if (scavenger == ScavengerMode.OFF && loop == 0) {
            BACKUP_ARRANGER_SIGILS.put(Arranger.A1_GATE, new ArrayList<String>());
            BACKUP_ARRANGER_SIGILS.put(Arranger.A_GATE, new ArrayList<String>());
            BACKUP_ARRANGER_SIGILS.put(Arranger.B_GATE, new ArrayList<String>());
            BACKUP_ARRANGER_SIGILS.put(Arranger.C_GATE, new ArrayList<String>());
        } else {
            // Scavenger and mobius both don't care about greens so we can restore these
            BACKUP_ARRANGER_SIGILS.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DJ1", "DJ2", "DZ1")));
            BACKUP_ARRANGER_SIGILS.put(Arranger.A_GATE, new ArrayList<String>(Arrays.asList("DI1", "DJ3", "DL1", "DZ2")));
            BACKUP_ARRANGER_SIGILS.put(Arranger.B_GATE, new ArrayList<String>(Arrays.asList("DI2", "DL2", "DT1", "DT2", "DZ3")));
            BACKUP_ARRANGER_SIGILS.put(Arranger.C_GATE, new ArrayList<String>(Arrays.asList("DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4")));
        }
        BACKUP_ARRANGER_SIGILS.put(Arranger.A_STAR, new ArrayList<String>(Arrays.asList("**1", "**2", "**3", "**4", "**5", "**6", "**7", "**8", "**9", "**10")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.B_STAR, new ArrayList<String>(Arrays.asList("**11", "**12", "**13", "**14", "**15", "**16", "**17", "**18", "**19", "**20")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.C_STAR, new ArrayList<String>(Arrays.asList("**21", "**22", "**23", "**24", "**25", "**26", "**27", "**28", "**29", "**30")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.CONNECTOR, new ArrayList<String>(Arrays.asList("ML1", "MT1", "MT2")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.CUBE, new ArrayList<String>(Arrays.asList("ML2", "MT3", "MT4", "MZ1")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.FAN, new ArrayList<String>(Arrays.asList("ML3", "MS1", "MT5", "MT6", "MZ2")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.RECORDER, new ArrayList<String>(Arrays.asList("MJ1", "MS2", "MT7", "MT8", "MZ3")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.PLATFORM, new ArrayList<String>(Arrays.asList("MI1", "ML4", "MO1", "MT9", "MT10", "MZ4")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.F1, new ArrayList<String>(Arrays.asList("NL1", "NL2", "NZ1", "NZ2")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.F2, new ArrayList<String>(Arrays.asList("NL3", "NL4", "NL5", "NL6", "NO1", "NT1", "NT2", "NT3", "NT4")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.F3, new ArrayList<String>(Arrays.asList("NI1", "NI2", "NI3", "NI4", "NJ1", "NJ2", "NL7", "NL8", "NS1", "NZ3")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.F4, new ArrayList<String>(Arrays.asList("NJ3", "NL9", "NO2", "NO3", "NS2", "NS3", "NT5", "NT6", "NT7", "NT8", "NZ4", "NZ5")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.F5, new ArrayList<String>(Arrays.asList("NI5", "NI6", "NJ4", "NL10", "NO4", "NO5", "NO6", "NO7", "NS4", "NT9", "NT10", "NT11", "NT12", "NZ6")));
        BACKUP_ARRANGER_SIGILS.put(Arranger.F6, new ArrayList<String>(Arrays.asList("EL1", "EL2", "EL3", "EL4", "EO1", "ES1", "ES2", "ES3", "ES4")));
        arrangerSigils = new HashMap<Arranger, ArrayList<String>>(BACKUP_ARRANGER_SIGILS);
        for (int i = 1; i < TalosProgress.TETROS.length - 1; i++) {
            TETRO_INDEXES.put(TalosProgress.TETROS[i], i);
        }

        /*
          All the marker groups
          These are the exact same as in the normal lua script, using regex you can convert
           them from one format almost straight into the other with minimal effort
        */
        switch(mode) {
            case DEFAULT: {
                BACKUP_MARKERS = new MarkerGroup[] {
                    new MarkerGroup(() -> isWorldOpen(World.A1), new ArrayList<String>(Arrays.asList(
                        "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
                        "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
                    ))),
                    new MarkerGroup(() -> isWorldOpen(World.A2), new ArrayList<String>(Arrays.asList(
                        "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star"
                    )), World.A2),
                    new MarkerGroup(() -> isWorldOpen(World.A3), new ArrayList<String>(Arrays.asList(
                        "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
                        "A3-Stashed for Later", "A3-Clock Star"
                    )), World.A3),
                    new MarkerGroup(() -> isWorldOpen(World.A4), new ArrayList<String>(Arrays.asList(
                        "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "A4-DCtS"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A5), new ArrayList<String>(Arrays.asList(
                        "A5-Two Boxes", "A5-Two Boxes Star", "A5-Over the Fence", "A5-YKYMCTS",
                        "A5-OLB", "A5-FC", "A5-FC Star"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A6), new ArrayList<String>(Arrays.asList(
                        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic",
                        "A6-Star"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A7), new ArrayList<String>(Arrays.asList(
                        "A7-Two Buzzers", "A7-Pinhole", "A7-LFI", "A7-WiaL",
                        "A7-Trapped Inside", "A7-Star"
                    )), World.A7),
                    new MarkerGroup(() -> isWorldOpen(World.A8), new ArrayList<String>(Arrays.asList(
                        "A*-DDM", "A*-Nervewrecker", "A*-JfW"
                    )), World.A8),
                    new MarkerGroup(() -> isWorldOpen(World.B1), new ArrayList<String>(Arrays.asList(
                        "B1-SaaS", "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence",
                        "B1-RoD", "B1-Star"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B2), new ArrayList<String>(Arrays.asList(
                        "B2-Higher Ground", "B2-Tomb", "B2-MotM", "B2-Moonshot",
                        "B2-Star"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B3), new ArrayList<String>(Arrays.asList(
                        "B3-Sunshot", "B3-Blown Away", "B3-Eagle's Nest", "B3-Woosh",
                        "B3-Star"
                    )), World.B3),
                    new MarkerGroup(() -> isWorldOpen(World.B4), new ArrayList<String>(Arrays.asList(
                        "B4-TRA", "B4-ABUH", "B4-Double-Plate", "B4-Self Help",
                        "B4-RPS", "B4-WAtC", "B4-TRA Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B4-Sphinx Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B5), new ArrayList<String>(Arrays.asList(
                        "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-SES",
                        "B5-Chambers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && ((unlocked(Arranger.CONNECTOR) && unlocked(Arranger.FAN)) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "B5-Obelisk Star"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B6), new ArrayList<String>(Arrays.asList(
                        "B6-Egyptian Arcade", "B6-JDaW", "B6-Crisscross"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B7), new ArrayList<String>(Arrays.asList(
                        "B7-WLJ", "B7-AFaF", "B7-BSbS Star", "B7-BSbS",
                        "B7-BLoM"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B7-Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B8), new ArrayList<String>(Arrays.asList(
                        "B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.C1), new ArrayList<String>(Arrays.asList(
                        "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-Star"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C1) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C1-MIA"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C2), new ArrayList<String>(Arrays.asList(
                        "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
                        "C2-Star"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C3), new ArrayList<String>(Arrays.asList(
                        "C3-Three Connectors", "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop",
                        "C3-Star"
                    )), World.C3),
                    new MarkerGroup(() -> isWorldOpen(World.C4), new ArrayList<String>(Arrays.asList(
                        "C4-Stables", "C4-Armory", "C4-Oubliette Star", "C4-Oubliette",
                        "C4-Throne Room Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C4-Throne Room"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C5), new ArrayList<String>(Arrays.asList(
                        "C5-Time Crawls", "C5-Dumbwaiter", "C5-Time Flies", "C5-UCaJ",
                        "C5-Time Flies Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C5-UCAJ Star", "C5-Dumbwaiter Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C6), new ArrayList<String>(Arrays.asList(
                        "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C7), new ArrayList<String>(Arrays.asList(
                        "C7-Prison Break", "C7-Carrier Pigeons", "C7-Crisscross", "C7-DMS",
                        "C7-Star"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C8), new ArrayList<String>(Arrays.asList(
                        "C*-Nexus"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.C8) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "C*-Cobweb", "C*-Unreachable Garden"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.CMESSENGER), new ArrayList<String>(Arrays.asList(
                        "CM-Star"
                    )), World.CMESSENGER),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.F3)), new ArrayList<String>(Arrays.asList(
                        "F0-Star"
                    ))),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && unlocked(Arranger.F3), new ArrayList<String>(Arrays.asList(
                        "F3-Star"
                    )))
                };
                break;
            }
            case SIXTY: {
                BACKUP_MARKERS = new MarkerGroup[] {
                    new MarkerGroup(() -> isWorldOpen(World.A1), new ArrayList<String>(Arrays.asList(
                        "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
                        "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
                    ))),
                    new MarkerGroup(() -> isWorldOpen(World.A2), new ArrayList<String>(Arrays.asList(
                        "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star"
                    )), World.A2),
                    new MarkerGroup(() -> isWorldOpen(World.A3), new ArrayList<String>(Arrays.asList(
                        "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
                        "A3-Stashed for Later", "A3-Clock Star"
                    )), World.A3),
                    new MarkerGroup(() -> isWorldOpen(World.A4), new ArrayList<String>(Arrays.asList(
                        "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "A4-DCtS"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A5), new ArrayList<String>(Arrays.asList(
                        "A5-Two Boxes", "A5-Two Boxes Star", "A5-Over the Fence", "A5-YKYMCTS",
                        "A5-FC", "A5-FC Star"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A5) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "A5-OLB"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A6), new ArrayList<String>(Arrays.asList(
                        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic",
                        "A6-Star"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A7), new ArrayList<String>(Arrays.asList(
                        "A7-Two Buzzers", "A7-Pinhole", "A7-LFI", "A7-WiaL",
                        "A7-Trapped Inside", "A7-Star"
                    )), World.A7),
                    new MarkerGroup(() -> isWorldOpen(World.A8), new ArrayList<String>(Arrays.asList(
                        "A*-DDM", "A*-Nervewrecker", "A*-JfW"
                    )), World.A8),
                    new MarkerGroup(() -> isWorldOpen(World.B1), new ArrayList<String>(Arrays.asList(
                        "B1-RoD"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B1) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B1-Over the Fence"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B1) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "B1-WtaD", "B1-SaaS", "B1-Third Wheel", "B1-Star"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B2), new ArrayList<String>(Arrays.asList(
                        "B2-Higher Ground", "B2-Tomb", "B2-Moonshot", "B2-Star"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B2) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B2-MotM"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B3) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B3-Sunshot", "B3-Blown Away", "B3-Star", "B3-Eagle's Nest",
                        "B3-Woosh"
                    )), World.B3),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && (unlocked(Arranger.CUBE) || unlocked(Arranger.RECORDER)), new ArrayList<String>(Arrays.asList(
                        "B4-Self Help"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B4-TRA", "B4-WAtC"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && ((unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE)) || unlocked(Arranger.RECORDER)), new ArrayList<String>(Arrays.asList(
                        "B4-Double-Plate", "B4-RPS"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B4-ABUH", "B4-TRA Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR) && (unlocked(Arranger.CUBE) || unlocked(Arranger.FAN)), new ArrayList<String>(Arrays.asList(
                        "B4-Sphinx Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B5), new ArrayList<String>(Arrays.asList(
                        "B5-Iron Curtain", "B5-Chambers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B5-Plates", "B5-SES", "B5-Obelisk Star"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && (unlocked(Arranger.CUBE) || unlocked(Arranger.RECORDER)), new ArrayList<String>(Arrays.asList(
                        "B5-Two Jammers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B6), new ArrayList<String>(Arrays.asList(
                        "B6-Egyptian Arcade", "B6-Crisscross"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B6) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B6-JDaW"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B7), new ArrayList<String>(Arrays.asList(
                        "B7-WLJ", "B7-AFaF"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "B7-BSbS", "B7-BSbS Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B7-BLoM"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B7-Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B8), new ArrayList<String>(Arrays.asList(
                        "B*-Merry Go Round", "B*-Peekaboo"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.B8) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B*-Cat's Cradle"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.C1), new ArrayList<String>(Arrays.asList(
                        "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-Star"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C1) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C1-MIA"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C2), new ArrayList<String>(Arrays.asList(
                        "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
                        "C2-Star"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C3), new ArrayList<String>(Arrays.asList(
                        "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop", "C3-Star"
                    )), World.C3),
                    new MarkerGroup(() -> isWorldOpen(World.C3) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C3-Three Connectors"
                    )), World.C3),
                    new MarkerGroup(() -> isWorldOpen(World.C4), new ArrayList<String>(Arrays.asList(
                        "C4-Stables"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CONNECTOR) && (unlocked(Arranger.CUBE) || unlocked(Arranger.RECORDER) || unlocked(Arranger.PLATFORM)), new ArrayList<String>(Arrays.asList(
                        "C4-Oubliette"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "C4-Armory"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C4-Throne Room"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && (unlocked(Arranger.CUBE) || unlocked(Arranger.FAN)), new ArrayList<String>(Arrays.asList(
                        "C4-Throne Room Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && ((unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE)) || (unlocked(Arranger.RECORDER) && unlocked(Arranger.PLATFORM))), new ArrayList<String>(Arrays.asList(
                        "C4-Oubliette Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C5), new ArrayList<String>(Arrays.asList(
                        "C5-Time Crawls", "C5-Time Flies", "C5-Time Flies Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C5-Dumbwaiter", "C5-UCaJ", "C5-UCAJ Star", "C5-Dumbwaiter Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C6), new ArrayList<String>(Arrays.asList(
                        "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C7), new ArrayList<String>(Arrays.asList(
                        "C7-Carrier Pigeons"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C7) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C7-Prison Break", "C7-Crisscross", "C7-DMS", "C7-Star"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C8), new ArrayList<String>(Arrays.asList(
                        "C*-Nexus"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.C8) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C*-Unreachable Garden", "C*-Cobweb"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.CMESSENGER), new ArrayList<String>(Arrays.asList(
                        "CM-Star"
                    )), World.CMESSENGER),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.F3)), new ArrayList<String>(Arrays.asList(
                        "F0-Star"
                    ))),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && unlocked(Arranger.F2) && unlocked(Arranger.F3) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "F3-Star"
                    )))
                };
                break;
            }
            case INTENDED: {
                BACKUP_MARKERS = new MarkerGroup[] {
                    new MarkerGroup(() -> isWorldOpen(World.A1), new ArrayList<String>(Arrays.asList(
                        "A1-Peephole", "A1-Outnumbered", "A1-ASooR", "A1-OtToU",
                        "A1-Beaten Path", "A1-Star"
                    ))),
                    new MarkerGroup(() -> isWorldOpen(World.A1) && unlocked(Arranger.A1_GATE), new ArrayList<String>(Arrays.asList(
                        "A1-PaSL", "A1-Trio"
                    ))),
                    new MarkerGroup(() -> isWorldOpen(World.A2), new ArrayList<String>(Arrays.asList(
                        "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star"
                    )), World.A2),
                    new MarkerGroup(() -> isWorldOpen(World.A3), new ArrayList<String>(Arrays.asList(
                        "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
                        "A3-Stashed for Later", "A3-Clock Star"
                    )), World.A3),
                    new MarkerGroup(() -> isWorldOpen(World.A4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star",
                        "A4-DCtS"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A5) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "A5-Two Boxes Star"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A5) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "A5-Two Boxes", "A5-FC", "A5-FC Star"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "A5-OLB", "A5-Over the Fence", "A5-YKYMCTS"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A6), new ArrayList<String>(Arrays.asList(
                        "A6-Mobile Mindfield"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A6) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "A6-Deception", "A6-Bichromatic"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A6) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "A6-Star"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A6) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "A6-Door too Far"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A7) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "A7-LFI", "A7-Pinhole", "A7-WiaL", "A7-Trapped Inside",
                        "A7-Star"
                    )), World.A7),
                    new MarkerGroup(() -> isWorldOpen(World.A7) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "A7-Two Buzzers"
                    )), World.A7),
                    new MarkerGroup(() -> isWorldOpen(World.A8), new ArrayList<String>(Arrays.asList(
                        "A*-Nervewrecker", "A*-JfW"
                    )), World.A8),
                    new MarkerGroup(() -> isWorldOpen(World.A8) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "A*-DDM"
                    )), World.A8),
                    new MarkerGroup(() -> isWorldOpen(World.B1) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B1-WtaD", "B1-SaaS", "B1-Third Wheel", "B1-Star"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B1) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B1-Over the Fence", "B1-RoD"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B2), new ArrayList<String>(Arrays.asList(
                        "B2-Higher Ground"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B2) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B2-Tomb", "B2-Star"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B2) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B2-MotM"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B2) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B2-Moonshot"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B3) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B3-Sunshot"
                    )), World.B3),
                    new MarkerGroup(() -> isWorldOpen(World.B3) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B3-Blown Away", "B3-Star", "B3-Eagle's Nest", "B3-Woosh"
                    )), World.B3),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "B4-Self Help", "B4-Double-Plate"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B4-TRA", "B4-TRA Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "B4-WAtC"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CUBE) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "B4-RPS"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B4-ABUH", "B4-Sphinx Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "B5-Two Jammers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B5-SES", "B5-Obelisk Star", "B5-Iron Curtain", "B5-Chambers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "B5-Plates"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B6), new ArrayList<String>(Arrays.asList(
                        "B6-Egyptian Arcade"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B6) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B6-JDaW", "B6-Crisscross"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B7), new ArrayList<String>(Arrays.asList(
                        "B7-WLJ"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B7-BSbS"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B7-BLoM"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B7-Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "B7-AFaF", "B7-BSbS Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B8) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B*-Merry Go Round"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.B8) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B*-Cat's Cradle"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.B8) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "B*-Peekaboo"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.C1), new ArrayList<String>(Arrays.asList(
                        "C1-Labyrinth"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C1) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "C1-Conservatory", "C1-Star"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C1) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "C1-Blowback", "C1-MIA"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C2) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C2-Cemetery"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C2) && unlocked(Arranger.RECORDER) && unlocked(Arranger.PLATFORM), new ArrayList<String>(Arrays.asList(
                        "C2-Short Wall"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C2) && unlocked(Arranger.CUBE) && unlocked(Arranger.RECORDER) && unlocked(Arranger.PLATFORM), new ArrayList<String>(Arrays.asList(
                        "C2-Rapunzel", "C2-Star"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C2) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.RECORDER) && unlocked(Arranger.PLATFORM), new ArrayList<String>(Arrays.asList(
                        "C2-ADaaF"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C3) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop", "C3-Three Connectors",
                        "C3-Star"
                    )), World.C3),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "C4-Stables"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C4-Armory"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "C4-Throne Room", "C4-Throne Room Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.RECORDER) && unlocked(Arranger.PLATFORM), new ArrayList<String>(Arrays.asList(
                        "C4-Oubliette", "C4-Oubliette Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C5-UCAJ Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C5-UCaJ"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "C5-Dumbwaiter"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "C5-Time Crawls"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C5) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "C5-Time Flies", "C5-Time Flies Star", "C5-Dumbwaiter Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C6), new ArrayList<String>(Arrays.asList(
                        "C6-Two Way Street"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C6) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C6-Circumlocution"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C6) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "C6-Seven Doors", "C6-Star"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C7) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.RECORDER), new ArrayList<String>(Arrays.asList(
                        "C7-Crisscross"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C7) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.FAN), new ArrayList<String>(Arrays.asList(
                        "C7-Prison Break", "C7-Carrier Pigeons", "C7-DMS", "C7-Star"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C8) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C*-Nexus", "C*-Cobweb"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.C8) && unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE) && unlocked(Arranger.RECORDER) && unlocked(Arranger.PLATFORM), new ArrayList<String>(Arrays.asList(
                        "C*-Unreachable Garden"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.CMESSENGER), new ArrayList<String>(Arrays.asList(
                        "CM-Star"
                    )), World.CMESSENGER),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "F0-Star"
                    )))
                };
                break;
            }
            case HARDMODE: {
                BACKUP_MARKERS = new MarkerGroup[] {
                    new MarkerGroup(() -> isWorldOpen(World.A1), new ArrayList<String>(Arrays.asList(
                        "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
                        "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
                    ))),
                    new MarkerGroup(() -> isWorldOpen(World.A2), new ArrayList<String>(Arrays.asList(
                        "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star"
                    )), World.A2),
                    new MarkerGroup(() -> isWorldOpen(World.A3), new ArrayList<String>(Arrays.asList(
                        "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
                        "A3-Stashed for Later", "A3-Clock Star"
                    )), World.A3),
                    new MarkerGroup(() -> isWorldOpen(World.A4), new ArrayList<String>(Arrays.asList(
                        "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star",
                        "A4-DCtS"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A5), new ArrayList<String>(Arrays.asList(
                        "A5-Two Boxes", "A5-Two Boxes Star", "A5-Over the Fence", "A5-YKYMCTS",
                        "A5-OLB", "A5-FC", "A5-FC Star"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A6), new ArrayList<String>(Arrays.asList(
                        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic",
                        "A6-Star"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A7), new ArrayList<String>(Arrays.asList(
                        "A7-Two Buzzers", "A7-Pinhole", "A7-LFI", "A7-WiaL",
                        "A7-Trapped Inside", "A7-Star"
                    )), World.A7),
                    new MarkerGroup(() -> isWorldOpen(World.A8), new ArrayList<String>(Arrays.asList(
                        "A*-DDM", "A*-Nervewrecker", "A*-JfW"
                    )), World.A8),
                    new MarkerGroup(() -> isWorldOpen(World.B1), new ArrayList<String>(Arrays.asList(
                        "B1-SaaS", "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence",
                        "B1-RoD", "B1-Star"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B2), new ArrayList<String>(Arrays.asList(
                        "B2-Higher Ground", "B2-Tomb", "B2-MotM", "B2-Moonshot",
                        "B2-Star"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B3), new ArrayList<String>(Arrays.asList(
                        "B3-Sunshot", "B3-Blown Away", "B3-Eagle's Nest", "B3-Woosh",
                        "B3-Star"
                    )), World.B3),
                    new MarkerGroup(() -> isWorldOpen(World.B4), new ArrayList<String>(Arrays.asList(
                        "B4-TRA", "B4-ABUH", "B4-Double-Plate", "B4-Self Help",
                        "B4-RPS", "B4-WAtC", "B4-TRA Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "B4-Sphinx Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B5), new ArrayList<String>(Arrays.asList(
                        "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-SES",
                        "B5-Chambers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && ((unlocked(Arranger.CONNECTOR) && unlocked(Arranger.FAN)) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "B5-Obelisk Star"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B6), new ArrayList<String>(Arrays.asList(
                        "B6-Egyptian Arcade", "B6-JDaW", "B6-Crisscross"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B7), new ArrayList<String>(Arrays.asList(
                        "B7-WLJ", "B7-AFaF", "B7-BSbS Star", "B7-BSbS",
                        "B7-BLoM"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && (unlocked(Arranger.CUBE) || unlocked(Arranger.FAN)), new ArrayList<String>(Arrays.asList(
                        "B7-Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B8), new ArrayList<String>(Arrays.asList(
                        "B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.C1), new ArrayList<String>(Arrays.asList(
                        "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-MIA",
                        "C1-Star"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C2), new ArrayList<String>(Arrays.asList(
                        "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
                        "C2-Star"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C3), new ArrayList<String>(Arrays.asList(
                        "C3-Three Connectors", "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop",
                        "C3-Star"
                    )), World.C3),
                    new MarkerGroup(() -> isWorldOpen(World.C4), new ArrayList<String>(Arrays.asList(
                        "C4-Stables", "C4-Armory", "C4-Oubliette Star", "C4-Oubliette",
                        "C4-Throne Room", "C4-Throne Room Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C5), new ArrayList<String>(Arrays.asList(
                        "C5-Time Crawls", "C5-Dumbwaiter", "C5-Time Flies", "C5-UCaJ",
                        "C5-Time Flies Star", "C5-UCAJ Star", "C5-Dumbwaiter Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C6), new ArrayList<String>(Arrays.asList(
                        "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C7), new ArrayList<String>(Arrays.asList(
                        "C7-Prison Break", "C7-Carrier Pigeons", "C7-Crisscross", "C7-DMS",
                        "C7-Star"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C8), new ArrayList<String>(Arrays.asList(
                        "C*-Nexus", "C*-Cobweb", "C*-Unreachable Garden"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.CMESSENGER), new ArrayList<String>(Arrays.asList(
                        "CM-Star"
                    )), World.CMESSENGER),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.F3)), new ArrayList<String>(Arrays.asList(
                        "F0-Star"
                    ))),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && unlocked(Arranger.F3), new ArrayList<String>(Arrays.asList(
                        "F3-Star"
                    )))
                };
                break;
            }
            case SIXTY_HARDMODE: {
                BACKUP_MARKERS = new MarkerGroup[] {
                    new MarkerGroup(() -> isWorldOpen(World.A1), new ArrayList<String>(Arrays.asList(
                        "A1-Peephole", "A1-PaSL", "A1-Outnumbered", "A1-ASooR",
                        "A1-OtToU", "A1-Trio", "A1-Beaten Path", "A1-Star"
                    ))),
                    new MarkerGroup(() -> isWorldOpen(World.A2), new ArrayList<String>(Arrays.asList(
                        "A2-Hall of Windows", "A2-Guards", "A2-Suicide Mission", "A2-Star"
                    )), World.A2),
                    new MarkerGroup(() -> isWorldOpen(World.A3), new ArrayList<String>(Arrays.asList(
                        "A3-ABTU Star", "A3-ABTU", "A3-AEP", "A3-Swallowed the Key",
                        "A3-Stashed for Later", "A3-Clock Star"
                    )), World.A3),
                    new MarkerGroup(() -> isWorldOpen(World.A4), new ArrayList<String>(Arrays.asList(
                        "A4-Push it Further", "A4-Branch it Out", "A4-Above All That", "A4-Star",
                        "A4-DCtS"
                    )), World.A4),
                    new MarkerGroup(() -> isWorldOpen(World.A5), new ArrayList<String>(Arrays.asList(
                        "A5-Two Boxes", "A5-Two Boxes Star", "A5-Over the Fence", "A5-YKYMCTS",
                        "A5-OLB", "A5-FC", "A5-FC Star"
                    )), World.A5),
                    new MarkerGroup(() -> isWorldOpen(World.A6), new ArrayList<String>(Arrays.asList(
                        "A6-Mobile Mindfield", "A6-Deception", "A6-Door too Far", "A6-Bichromatic",
                        "A6-Star"
                    )), World.A6),
                    new MarkerGroup(() -> isWorldOpen(World.A7), new ArrayList<String>(Arrays.asList(
                        "A7-Two Buzzers", "A7-Pinhole", "A7-LFI", "A7-WiaL",
                        "A7-Trapped Inside", "A7-Star"
                    )), World.A7),
                    new MarkerGroup(() -> isWorldOpen(World.A8), new ArrayList<String>(Arrays.asList(
                        "A*-DDM", "A*-Nervewrecker", "A*-JfW"
                    )), World.A8),
                    new MarkerGroup(() -> isWorldOpen(World.B1), new ArrayList<String>(Arrays.asList(
                        "B1-SaaS", "B1-WtaD", "B1-Third Wheel", "B1-Over the Fence",
                        "B1-RoD", "B1-Star"
                    )), World.B1),
                    new MarkerGroup(() -> isWorldOpen(World.B2), new ArrayList<String>(Arrays.asList(
                        "B2-Higher Ground", "B2-Tomb", "B2-MotM", "B2-Moonshot",
                        "B2-Star"
                    )), World.B2),
                    new MarkerGroup(() -> isWorldOpen(World.B3) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "B3-Sunshot", "B3-Blown Away", "B3-Eagle's Nest", "B3-Woosh",
                        "B3-Star"
                    )), World.B3),
                    new MarkerGroup(() -> isWorldOpen(World.B4), new ArrayList<String>(Arrays.asList(
                        "B4-Self Help", "B4-WAtC"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR), new ArrayList<String>(Arrays.asList(
                        "B4-TRA", "B4-TRA Star", "B4-Sphinx Star"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && unlocked(Arranger.CONNECTOR) && (unlocked(Arranger.CUBE) || unlocked(Arranger.FAN)), new ArrayList<String>(Arrays.asList(
                        "B4-ABUH"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B4) && ((unlocked(Arranger.CONNECTOR) && unlocked(Arranger.CUBE)) || unlocked(Arranger.RECORDER)), new ArrayList<String>(Arrays.asList(
                        "B4-Double-Plate", "B4-RPS"
                    )), World.B4),
                    new MarkerGroup(() -> isWorldOpen(World.B5), new ArrayList<String>(Arrays.asList(
                        "B5-Plates", "B5-Two Jammers", "B5-Iron Curtain", "B5-SES",
                        "B5-Chambers"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B5) && ((unlocked(Arranger.CONNECTOR) && unlocked(Arranger.FAN)) || unlocked(Arranger.CUBE)), new ArrayList<String>(Arrays.asList(
                        "B5-Obelisk Star"
                    )), World.B5),
                    new MarkerGroup(() -> isWorldOpen(World.B6), new ArrayList<String>(Arrays.asList(
                        "B6-Egyptian Arcade", "B6-JDaW", "B6-Crisscross"
                    )), World.B6),
                    new MarkerGroup(() -> isWorldOpen(World.B7), new ArrayList<String>(Arrays.asList(
                        "B7-WLJ", "B7-AFaF", "B7-BSbS Star", "B7-BSbS",
                        "B7-BLoM"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B7) && (unlocked(Arranger.CUBE) || unlocked(Arranger.FAN)), new ArrayList<String>(Arrays.asList(
                        "B7-Star"
                    )), World.B7),
                    new MarkerGroup(() -> isWorldOpen(World.B8), new ArrayList<String>(Arrays.asList(
                        "B*-Merry Go Round", "B*-Cat's Cradle", "B*-Peekaboo"
                    )), World.B8),
                    new MarkerGroup(() -> isWorldOpen(World.C1), new ArrayList<String>(Arrays.asList(
                        "C1-Labyrinth", "C1-Conservatory", "C1-Blowback", "C1-MIA",
                        "C1-Star"
                    )), World.C1),
                    new MarkerGroup(() -> isWorldOpen(World.C2), new ArrayList<String>(Arrays.asList(
                        "C2-Cemetery", "C2-ADaaF", "C2-Rapunzel", "C2-Short Wall",
                        "C2-Star"
                    )), World.C2),
                    new MarkerGroup(() -> isWorldOpen(World.C3), new ArrayList<String>(Arrays.asList(
                        "C3-Three Connectors", "C3-BSLS", "C3-Jammer Quarantine", "C3-Weathertop",
                        "C3-Star"
                    )), World.C3),
                    new MarkerGroup(() -> isWorldOpen(World.C4), new ArrayList<String>(Arrays.asList(
                        "C4-Stables", "C4-Armory", "C4-Throne Room", "C4-Throne Room Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C4-Oubliette Star"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C4) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.CUBE) || unlocked(Arranger.RECORDER) || unlocked(Arranger.PLATFORM)), new ArrayList<String>(Arrays.asList(
                        "C4-Oubliette"
                    )), World.C4),
                    new MarkerGroup(() -> isWorldOpen(World.C5), new ArrayList<String>(Arrays.asList(
                        "C5-Time Crawls", "C5-Dumbwaiter", "C5-Time Flies", "C5-UCaJ",
                        "C5-Time Flies Star", "C5-UCAJ Star", "C5-Dumbwaiter Star"
                    )), World.C5),
                    new MarkerGroup(() -> isWorldOpen(World.C6), new ArrayList<String>(Arrays.asList(
                        "C6-Two Way Street", "C6-Circumlocution", "C6-Seven Doors", "C6-Star"
                    )), World.C6),
                    new MarkerGroup(() -> isWorldOpen(World.C7), new ArrayList<String>(Arrays.asList(
                        "C7-Prison Break", "C7-Carrier Pigeons", "C7-Crisscross", "C7-DMS",
                        "C7-Star"
                    )), World.C7),
                    new MarkerGroup(() -> isWorldOpen(World.C8), new ArrayList<String>(Arrays.asList(
                        "C*-Nexus", "C*-Unreachable Garden"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.C8) && unlocked(Arranger.CUBE), new ArrayList<String>(Arrays.asList(
                        "C*-Cobweb"
                    )), World.C8),
                    new MarkerGroup(() -> isWorldOpen(World.CMESSENGER), new ArrayList<String>(Arrays.asList(
                        "CM-Star"
                    )), World.CMESSENGER),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && (unlocked(Arranger.CONNECTOR) || unlocked(Arranger.F3)), new ArrayList<String>(Arrays.asList(
                        "F0-Star"
                    ))),
                    new MarkerGroup(() -> unlocked(Arranger.F1) && unlocked(Arranger.F3), new ArrayList<String>(Arrays.asList(
                        "F3-Star"
                    )))
                };
                break;
            }
            default: {}
        }
    }

    public GeneratorGeneric(HashMap<String, Integer> options) {
        this(new TalosProgress(options));
    }

    public GeneratorGeneric() {
        this(new TalosProgress());
    }

    public TalosProgress generate(long seed) {
        progress = BACKUP_PROGRESS.clone();
        progress.setVar("Randomizer_Seed", (int)seed);
        Rand r = new Rand(seed);

        // Clone the variables we will be destroying
        arrangerSigils = new HashMap<Arranger, ArrayList<String>>(BACKUP_ARRANGER_SIGILS);
        MarkerGroup[] markers = new MarkerGroup[BACKUP_MARKERS.length];
        for (int i = 0; i < BACKUP_MARKERS.length; i++) {
            markers[i] = BACKUP_MARKERS[i].clone();
        }

        // Important for checksum and to advance rng
        progress.setVar("PaintItemSeed", r.next(0, 8909478));
        progress.setVar("Code_Floor4", r.next(1, 999));
        progress.setVar("Code_Floor5", r.next(1, 999));
        progress.setVar("Code_Floor6", r.next(4, 9)*100 + r.next(4, 9)*10 + r.next(4, 9));

        // Decide what scavenger ending we're doing
        ScavengerEnding scavengerGoal = ScavengerEnding.NONE;
        if (scavenger != ScavengerMode.OFF) {
            int endingIndex;
            if (scavenger != ScavengerMode.FULL) {
                endingIndex = r.next(0, 2);
            } else {
                endingIndex = r.next(0, 3);
            }
            if (mode == RandomizerMode.INTENDED) {
                endingIndex = 3;
            }
            scavengerGoal = ScavengerEnding.fromInt(endingIndex);
            progress.setVar("Randomizer_ScavengerMode", endingIndex + 1);
        }

        // Randomize the portals
        World[] portalOrder = World.values();
        Hub startHub = Hub.A;
        if (loop != 0) {
            if (portals) {
                for (int index = 1; index < portalOrder.length; index++) {
                    int otherIndex = r.next(0, index - 1);
                    World tmp = portalOrder[index];
                    portalOrder[index] = portalOrder[otherIndex];
                    portalOrder[otherIndex] = tmp;
                }
            } else {
                Collections.rotate(Arrays.asList(portalOrder), 1);
            }
        } else if (portals) {
            for (int index = portalOrder.length - 1; index > 0; index--) {
                int otherIndex = r.next(1, index);
                World tmp = portalOrder[index];
                portalOrder[index] = portalOrder[otherIndex];
                portalOrder[otherIndex] = tmp;
            }

            int index = r.next(0, 21);
            // Intended and short scavenger have to go into A
            if (mode == RandomizerMode.INTENDED || scavenger == ScavengerMode.SHORT) {
                index = r.next(0, 7);
            }
            if (index == 7) {
                index = 8;
            } else if (8 <= index && index < 15) {
                index++;
                startHub = Hub.B;
            } else if (15 <= index) {
                index += 2;
                startHub = Hub.C;
            }
            World tmp = portalOrder[index];
            portalOrder[index] = portalOrder[0];
            portalOrder[0] = tmp;
        }
        for (int i = 0; i < portalOrder.length; i++) {
            progress.setVar(portalOrder[i].toString(), i + 1);
        }

        // Work out which indexes in the marker list will be accessable in which hub
        if (mode != RandomizerMode.NONE && mode != RandomizerMode.FULLY_RANDOM) {
            ArrayList<Integer> closedMarkerIndexes = new ArrayList<Integer>();
            ArrayList<Integer> aIndexes = new ArrayList<Integer>();
            ArrayList<Integer> bIndexes = new ArrayList<Integer>();
            ArrayList<Integer> cIndexes = new ArrayList<Integer>();
            for (int i = 0; i < markers.length; i++) {
                int worldPos = Arrays.asList(portalOrder).indexOf(markers[i].getWorld());
                if (worldPos == -1) {
                    closedMarkerIndexes.add(i);
                    continue;
                }
                switch (Hub.fromWorldInt(worldPos)) {
                    case A: {
                        aIndexes.add(i);
                        break;
                    }
                    case B: {
                        bIndexes.add(i);
                        break;
                    }
                    case C: {
                        cIndexes.add(i);
                        break;
                    }
                }
            }

            // Based on starting hub restore one arranger and setup some variables
            ArrayList<Integer> indexesToAdd = new ArrayList<Integer>();
            ArrayList<Arranger> lastHubs = new ArrayList<Arranger>();
            switch (startHub) {
                case A: {
                    arrangerSigils.put(Arranger.A_GATE, new ArrayList<String>(Arrays.asList("DI1", "DJ3", "DL1", "DZ2")));
                    indexesToAdd = aIndexes;
                    aIndexes = new ArrayList<Integer>();
                    lastHubs = new ArrayList<Arranger>(Arrays.asList(Arranger.B_GATE, Arranger.C_GATE));
                    break;
                }
                case B: {
                    arrangerSigils.put(Arranger.B_GATE, new ArrayList<String>(Arrays.asList("DI2", "DL2", "DT1", "DT2", "DZ3")));
                    indexesToAdd = bIndexes;
                    bIndexes = new ArrayList<Integer>();
                    lastHubs = new ArrayList<Arranger>(Arrays.asList(Arranger.A_GATE, Arranger.C_GATE));
                    break;
                }
                case C: {
                    arrangerSigils.put(Arranger.C_GATE, new ArrayList<String>(Arrays.asList("DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4")));
                    indexesToAdd = cIndexes;
                    cIndexes = new ArrayList<Integer>();
                    lastHubs = new ArrayList<Arranger>(Arrays.asList(Arranger.A_GATE, Arranger.B_GATE));
                    break;
                }
            }
            // Add worlds to openWorlds
            openWorlds = new HashSet<World>();
            for (int world : indexesToAdd) {
                closedMarkerIndexes.add(world);
                openWorlds.add(markers[world].getWorld());
            }

            boolean checkGates = true;
            // Small extra setup for mobius and scavenger, which don't care about greens
            if (loop != 0 || scavenger == ScavengerMode.FULL) {
                checkGates = false;
                indexesToAdd = new ArrayList<Integer>();
                indexesToAdd.addAll(aIndexes);
                indexesToAdd.addAll(bIndexes);
                indexesToAdd.addAll(cIndexes);
                aIndexes = new ArrayList<Integer>();
                bIndexes = new ArrayList<Integer>();
                cIndexes = new ArrayList<Integer>();
                for (int world : indexesToAdd) {
                    if (!closedMarkerIndexes.contains(world)) {
                        closedMarkerIndexes.add(world);
                    }
                    openWorlds.add(markers[world].getWorld());
                }
            }

            // In intended F3 star won't unlock in time so we manually set it to what it could be
            if (mode == RandomizerMode.INTENDED && scavenger == ScavengerMode.OFF) {
                String sigil;
                String marker = "F3-Star";
                int sigilIndex = r.next(0, 38);
                if (sigilIndex <= 8) {
                    sigil = arrangerSigils.get(Arranger.F6).remove(sigilIndex);
                } else {
                    sigil = arrangerSigils.get(Arranger.A_STAR).remove(0);
                }
                progress.setVar(marker, TETRO_INDEXES.get(sigil));
                arrangerSigils.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DJ1", "DJ2", "DZ1")));
            }

            // The main loop
            int arrangerStage = 0;
            int availableMarkers = 0;
            ArrayList<MarkerGroup> openMarkerGroups = new ArrayList<MarkerGroup>();
            ArrayList<Arranger> accessableArrangers = new ArrayList<Arranger>();
            Arranger pickedArranger = Arranger.NONE;
            while (arrangerStage != -1 || accessableArrangers.size() > 0) {
                // Find new markers
                for (int i = 0; i < closedMarkerIndexes.size(); i++) {
                    int index = closedMarkerIndexes.get(i);
                    if (markers[index].isOpen()) {
                        openMarkerGroups.add(markers[index]);
                        closedMarkerIndexes.remove(i);
                        availableMarkers += markers[index].getSize();
                        // Indexes shift down when we remove something
                        i--;
                    }
                }

                // Work out arranger unlocking
                if (arrangerStage != -1) {
                    /*
                      Scavenger Hunt
                      0. A1 Gate (If in Intended Mode)
                      1. ALl the arrangers for the relevant ending
                    */
                    if (scavenger != ScavengerMode.OFF) {
                        if (mode == RandomizerMode.INTENDED && !unlocked(Arranger.A1_GATE)) {
                            accessableArrangers.add(Arranger.A1_GATE);
                        } else {
                            arrangerStage = -1;
                            accessableArrangers.addAll(scavengerGoal.getAllowedArrangers());
                        }
                    /*
                      Intended
                      1. A1 Gate
                      2. Conenctor + Cube
                      3. A Gate
                      4. Other Hubs
                      5. Other Items
                      6. Star worlds
                      7. F1
                      8. Other Tower Floors
                    */
                    } else if (mode == RandomizerMode.INTENDED) {
                        switch (arrangerStage) {
                            case 0: {
                                accessableArrangers.add(Arranger.A1_GATE);
                                arrangerStage++;
                                break;
                            }
                            case 1: {
                                accessableArrangers.addAll(Arrays.asList(Arranger.CONNECTOR, Arranger.CUBE));
                                arrangerStage++;
                                break;
                            }
                            case 2: {
                                if (accessableArrangers.size() == 0) {
                                    accessableArrangers.add(Arranger.A_GATE);
                                    arrangerStage++;
                                }
                                break;
                            }
                            case 3: {
                                accessableArrangers.addAll(Arrays.asList(Arranger.B_GATE, Arranger.C_GATE));
                                arrangerStage++;
                                break;
                            }
                            case 4: {
                                if (accessableArrangers.size() == 0) {
                                    accessableArrangers.addAll(Arrays.asList(Arranger.FAN, Arranger.RECORDER, Arranger.PLATFORM));
                                    arrangerStage++;
                                }
                                break;
                            }
                            case 5: {
                                if (accessableArrangers.size() == 0) {
                                    accessableArrangers.addAll(Arrays.asList(Arranger.A_STAR, Arranger.B_STAR, Arranger.C_STAR));
                                    arrangerStage++;
                                }
                                break;
                            }
                            case 6: {
                                if (accessableArrangers.size() == 0) {
                                    accessableArrangers.add(Arranger.F1);
                                    arrangerStage++;
                                }
                                break;
                            }
                            case 7: {
                                accessableArrangers.addAll(Arrays.asList(Arranger.F2, Arranger.F3, Arranger.F4, Arranger.F5, Arranger.F6));
                                arrangerStage = -1;
                            }
                        }
                    /*
                      Default
                      1. Starting hub
                      2. Other Hubs
                      3. Items
                      4. Star Worlds
                      5. F1 F2 F3
                      6. Everything else
                    */
                    } else {
                        switch (arrangerStage) {
                            case 0: {
                                switch (startHub) {
                                    case A: {
                                        accessableArrangers.add(Arranger.A_GATE);
                                        break;
                                    }
                                    case B: {
                                        accessableArrangers.add(Arranger.B_GATE);
                                        break;
                                    }
                                    case C: {
                                        accessableArrangers.add(Arranger.C_GATE);
                                        break;
                                    }
                                }
                                arrangerStage++;
                                break;
                            }
                            case 1: {
                                if (pickedArranger == Arranger.A_GATE && startHub == Hub.A) {
                                    accessableArrangers.addAll(Arrays.asList(Arranger.B_GATE, Arranger.C_GATE));
                                } else if (pickedArranger == Arranger.B_GATE && startHub == Hub.B) {
                                    accessableArrangers.addAll(Arrays.asList(Arranger.A_GATE, Arranger.C_GATE));
                                } else if (pickedArranger == Arranger.C_GATE && startHub == Hub.C) {
                                    accessableArrangers.addAll(Arrays.asList(Arranger.A_GATE, Arranger.B_GATE));
                                }
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
                    }
                }

                pickedArranger = accessableArrangers.remove(r.next(0, accessableArrangers.size() - 1));
                ArrayList<String> sigils = arrangerSigils.remove(pickedArranger);

                // Wrong hub softlock prevention
                if (checkGates) {
                    if (lastHubs.contains(pickedArranger)) {
                        // Both hubs
                        if (r.next(0, 99) < 25) {
                            switch (startHub) {
                                case A: {
                                    sigils = new ArrayList<String>(Arrays.asList(
                                        "DI2", "DL2", "DT1", "DT2", "DZ3",
                                        "DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"
                                    ));
                                    break;
                                }
                                case B: {
                                    sigils = new ArrayList<String>(Arrays.asList(
                                        "DI1", "DJ3", "DL1", "DZ2",
                                        "DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"
                                    ));
                                    break;
                                }
                                case C: {
                                    sigils = new ArrayList<String>(Arrays.asList(
                                        "DI1", "DJ3", "DL1", "DZ2",
                                        "DI2", "DL2", "DT1", "DT2", "DZ3"
                                    ));
                                    break;
                                }
                            }

                            if (mode != RandomizerMode.INTENDED) {
                                arrangerSigils.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DJ1", "DJ2", "DZ1")));
                            }

                            accessableArrangers.removeAll(Arrays.asList(Arranger.A_GATE, Arranger.B_GATE, Arranger.C_GATE));
                            arrangerSigils.remove(Arranger.A_GATE);
                            arrangerSigils.remove(Arranger.B_GATE);
                            arrangerSigils.remove(Arranger.C_GATE);

                            indexesToAdd = new ArrayList<Integer>();
                            indexesToAdd.addAll(aIndexes);
                            indexesToAdd.addAll(bIndexes);
                            indexesToAdd.addAll(cIndexes);
                            aIndexes = new ArrayList<Integer>();
                            bIndexes = new ArrayList<Integer>();
                            cIndexes = new ArrayList<Integer>();
                            for (int world : indexesToAdd) {
                                if (!closedMarkerIndexes.contains(world)) {
                                    closedMarkerIndexes.add(world);
                                }
                                openWorlds.add(markers[world].getWorld());
                            }
                        // One hub
                        } else {
                            ArrayList<String> uniqueSigils = new ArrayList<String>();
                            switch (pickedArranger) {
                                case A_GATE: {
                                    if (startHub == Hub.B) {
                                        uniqueSigils.add("DT3");
                                        arrangerSigils.put(Arranger.C_GATE, new ArrayList<String>(Arrays.asList("DJ4", "DJ5", "DL3", "DT4", "DZ4")));
                                    } else {
                                        uniqueSigils.add("DT1");
                                        arrangerSigils.put(Arranger.B_GATE, new ArrayList<String>(Arrays.asList("DI2", "DL2", "DT2", "DZ3")));
                                    }
                                    arrangerSigils.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DJ1", "DJ2", "DZ1")));
                                    sigils = new ArrayList<String>(Arrays.asList("DI1", "DJ3", "DL1", "DZ2"));
                                    indexesToAdd = aIndexes;
                                    aIndexes = new ArrayList<Integer>();
                                    break;
                                }
                                case B_GATE: {
                                    if (startHub == Hub.A) {
                                        uniqueSigils.addAll(Arrays.asList("DJ1", "DJ4", "DJ5"));
                                        arrangerSigils.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DJ2", "DZ1")));
                                        arrangerSigils.put(Arranger.C_GATE, new ArrayList<String>(Arrays.asList("DL3", "DT3", "DT4", "DZ4")));
                                    } else {
                                        uniqueSigils.addAll(Arrays.asList("DJ1", "DJ2", "DJ3"));
                                        arrangerSigils.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DZ1")));
                                        arrangerSigils.put(Arranger.A_GATE, new ArrayList<String>(Arrays.asList("DI1", "DL1", "DZ2")));
                                    }
                                    sigils = new ArrayList<String>(Arrays.asList("DI2", "DL2", "DT1", "DT2", "DZ3"));
                                    indexesToAdd = bIndexes;
                                    bIndexes = new ArrayList<Integer>();
                                    break;
                                }
                                case C_GATE: {
                                    if (startHub == Hub.A) {
                                        uniqueSigils.add("DI2");
                                        arrangerSigils.put(Arranger.B_GATE, new ArrayList<String>(Arrays.asList("DL2", "DT1", "DT2", "DZ3")));
                                    } else {
                                        uniqueSigils.add("DI1");
                                        arrangerSigils.put(Arranger.A_GATE, new ArrayList<String>(Arrays.asList("DJ3", "DL1", "DZ2")));
                                    }
                                    arrangerSigils.put(Arranger.A1_GATE, new ArrayList<String>(Arrays.asList("DJ1", "DJ2", "DZ1")));
                                    sigils = new ArrayList<String>(Arrays.asList("DJ4", "DJ5", "DL3", "DT3", "DT4", "DZ4"));
                                    indexesToAdd = cIndexes;
                                    cIndexes = new ArrayList<Integer>();
                                    break;
                                }
                                default: {}
                            }
                            for (int world : indexesToAdd) {
                                if (!closedMarkerIndexes.contains(world)) {
                                    closedMarkerIndexes.add(world);
                                }
                                openWorlds.add(markers[world].getWorld());
                            }

                            if (mode == RandomizerMode.INTENDED) {
                                arrangerSigils.remove(Arranger.A1_GATE);
                                uniqueSigils.remove("DJ1");
                            }

                            // Find new spots
                            ArrayList<MarkerGroup> tempOpenMarkers = new ArrayList<MarkerGroup>();
                            int tempAvailableMarkers = 0;
                            for (int index : closedMarkerIndexes) {
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
                    }
                // Already done softlock prevention, need to unlock last world
                } else if (!checkGates) {
                    switch (pickedArranger) {
                        case A_GATE: {
                            for (int world : aIndexes) {
                                closedMarkerIndexes.add(world);
                                openWorlds.add(markers[world].getWorld());
                            }
                            aIndexes = new ArrayList<Integer>();
                            break;
                        }
                        case B_GATE: {
                            for (int world : bIndexes) {
                                closedMarkerIndexes.add(world);
                                openWorlds.add(markers[world].getWorld());
                            }
                            bIndexes = new ArrayList<Integer>();
                            break;
                        }
                        case C_GATE: {
                            for (int world : cIndexes) {
                                closedMarkerIndexes.add(world);
                                openWorlds.add(markers[world].getWorld());
                            }
                            cIndexes = new ArrayList<Integer>();
                            break;
                        }
                        default: {}
                    }
                }

                // Place the sigils
                for (String sigil : sigils) {
                    int index = r.next(0, availableMarkers - 1);
                    for (MarkerGroup group : openMarkerGroups) {
                        if (index >= group.getSize()) {
                            index -= group.getSize();
                        } else {
                            String randMarker = group.getMarkers().remove(index);
                            progress.setVar(randMarker, TETRO_INDEXES.get(sigil));
                            availableMarkers -= 1;
                            if (group.getSize() < 1) {
                                openMarkerGroups.remove(group);
                            }
                            break;
                        }
                    }
                }
            }

            /*
                Scavenger will break early without placing all the sigils so we need assign
                 the rest here
                The order only matters for checksum
            */
            if (scavenger != ScavengerMode.OFF) {
                ArrayList<String> allMarkers = new ArrayList<String>();
                for (MarkerGroup group : markers) {
                    allMarkers.addAll(group.getMarkers());
                }
                if (mode == RandomizerMode.INTENDED) {
                    allMarkers.add("F3-Star");
                }
                for (Arranger key: Arranger.values()) {
                    if (arrangerSigils.containsKey(key)) {
                        for (String sigil : arrangerSigils.get(key)) {
                            String marker = allMarkers.remove(0);
                            progress.setVar(marker, TETRO_INDEXES.get(sigil));
                        }
                    }
                }
            }
        // Fully random uses it's own generator, simple knuth randomization
        } else if (mode == RandomizerMode.FULLY_RANDOM) {
            for (int i = 0; i < MARKERS_SIMPLE.length; i++) {
                String otherMarker = MARKERS_SIMPLE[r.next(0, i)];
                progress.setVar(MARKERS_SIMPLE[i], progress.getVar(otherMarker));
                progress.setVar(otherMarker, i + 1);
            }
        /*
          No randomization uses it's own 'generator', the MARKERS_SIMPLE list is ordered so
           that this works
        */
        } else if (mode == RandomizerMode.NONE) {
            for (int i = 0; i < MARKERS_SIMPLE.length; i++) {
                progress.setVar(MARKERS_SIMPLE[i], i + 1);
            }
        }

        /*
          Mobius mode also generates these, which have no effect on checksum or the rng
           state (because by now everything's been placed), but I figured maybe someone
           wants to look at these, might as well leave them
        */
        if (loop != 0) {
            int F0Pos = r.next(1, 50);
            int F3Pos = r.next(1, 49);
            if (F3Pos >= F0Pos) {
                F3Pos++;
            }
            progress.setVar("Randomizer_LoopF0", F0Pos);
            progress.setVar("Randomizer_LoopF3", F3Pos);

            if ((loop & MobiusOptions.RANDOM_ARRANGERS.getMask()) != 0) {
                int randArrangers = r.next(0, 0x7FFF);
                if (progress.getVar("Randomizer_UnlockItems") == 1) {
                    randArrangers &= 0x7e0f;
                }
                progress.setVar("Randomizer_LoopArrangers", randArrangers);
            }
        }

        progress.setVar("Randomizer_ExtraSeed", r.next(1, 0x7FFFFFFF));
        progress.setVar("Randomizer_Generated", 1);

        return progress;
    }
}
