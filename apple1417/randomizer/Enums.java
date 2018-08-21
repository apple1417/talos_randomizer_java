package apple1417.randomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Enums {
    public enum Arranger {
        A1_GATE, A_GATE, B_GATE, C_GATE,
        A_STAR, B_STAR, C_STAR,
        CONNECTOR, CUBE, FAN, RECORDER, PLATFORM,
        F1, F2, F3, F4, F5, F6,
        NONE;

        public static Arranger fromInt(int i) {
            return Arranger.values()[i];
        }
    }

    public enum Hub {
        A, B, C;

        public static Hub fromInt(int i) {
            return Hub.values()[i];
        }

        public static Hub fromWorldInt(int i) {
            if (26 <= i || i < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (i < 9) {
                return Hub.A;
            } else if (9 <= i && i < 17) {
                return Hub.B;
            } else {
                return Hub.C;
            }
        }
    }

    public enum RandomizerMode {
        NONE("No Randomization"),
        DEFAULT("Default"),
        SIXTY("60fps Friendly"),
        FULLY_RANDOM("Fully Random"),
        INTENDED("Intended"),
        HARDMODE("Hardmode"),
        SIXTY_HARDMODE("60fps Hardmode");
        private String label;

        private RandomizerMode(String label) {
            this.label = label;
        }

        public static RandomizerMode fromInt(int i) {
            return RandomizerMode.values()[i];
        }

        public String toString() {
            return label;
        }
    }

    public enum ScavengerMode {
        OFF("Off"),
        SHORT("Short"),
        FULL("Full");
        private String label;

        private ScavengerMode(String label) {
            this.label = label;
        }

        public static ScavengerMode fromInt(int i) {
            return ScavengerMode.values()[i];
        }

        public String toString() {
            return label;
        }
    }

    public enum ScavengerEnding {
        CONNECTOR_CLIP,
        F2_CLIP,
        F3_CLIP,
        F6,
        NONE;

        public static ScavengerEnding fromInt(int i) {
            return ScavengerEnding.values()[i];
        }

        private static HashMap<ScavengerEnding, ArrayList<Arranger>> makeArrangerHashMap() {
            HashMap<ScavengerEnding, ArrayList<Arranger>> out = new HashMap<ScavengerEnding, ArrayList<Arranger>>();
            out.put(CONNECTOR_CLIP, new ArrayList<Arranger>(Arrays.asList(Arranger.CONNECTOR, Arranger.F1)));
            out.put(F2_CLIP,  new ArrayList<Arranger>(Arrays.asList(Arranger.CUBE, Arranger.F1, Arranger.F2)));
            out.put(F3_CLIP, new ArrayList<Arranger>(Arrays.asList(Arranger.F1, Arranger.F3)));
            out.put(F6, new ArrayList<Arranger>(Arrays.asList(Arranger.F1, Arranger.F6)));
            out.put(NONE, null);
            return out;
        }
        private static HashMap<ScavengerEnding, ArrayList<Arranger>> allowedArrangers = makeArrangerHashMap();
        public ArrayList<Arranger> getAllowedArrangers() {
            return allowedArrangers.get(this);
        }

        private static HashMap<ScavengerEnding, ArrayList<String>> makeSigilHashMap() {
            HashMap<ScavengerEnding, ArrayList<String>> out = new HashMap<ScavengerEnding, ArrayList<String>>();
            out.put(CONNECTOR_CLIP, new ArrayList<String>(Arrays.asList(
                "ML1", "MT1", "MT2",
                "NL1", "NL2", "NZ1", "NZ2"
            )));
            out.put(F2_CLIP,  new ArrayList<String>(Arrays.asList(
                "ML2", "MT3", "MT4", "MZ1",
                "NL1", "NL2", "NZ1", "NZ2",
                "NL3", "NL4", "NL5", "NL6", "NO1", "NT1", "NT2", "NT3", "NT4"
            )));
            out.put(F3_CLIP, new ArrayList<String>(Arrays.asList(
                "NL1", "NL2", "NZ1", "NZ2",
                "NI1", "NI2", "NI3", "NI4", "NJ1", "NJ2", "NL7", "NL8", "NS1", "NZ3"
            )));
            out.put(F6, new ArrayList<String>(Arrays.asList(
                "NL1", "NL2", "NZ1", "NZ2",
                "EL1", "EL2", "EL3", "EL4", "EO1", "ES1", "ES2", "ES3", "ES4"
            )));
            out.put(NONE, null);
            return out;
        }
        private static HashMap<ScavengerEnding, ArrayList<String>> allowedSigils = makeSigilHashMap();
        public ArrayList<String> getAllowedSigils() {
            return allowedSigils.get(this);
        }
    }

    public enum World {
        A1 ("A1", Hub.A), A2 ("A2", Hub.A), A3 ("A3", Hub.A), A4 ("A4", Hub.A),
        A5 ("A5", Hub.A), A6 ("A6", Hub.A), A7 ("A7", Hub.A), A8 ("A8", Hub.A),
        ADEVISLAND ("ADevIsland", Hub.A),
        B1 ("B1", Hub.B), B2 ("B2", Hub.B), B3 ("B3", Hub.B), B4 ("B4", Hub.B),
        B5 ("B5", Hub.B), B6 ("B6", Hub.B), B7 ("B7", Hub.B), B8 ("B8", Hub.B),
        C1 ("C1", Hub.C), C2 ("C2", Hub.C), C3 ("C3", Hub.C), C4 ("C4", Hub.C),
        C5 ("C5", Hub.C), C6 ("C6", Hub.C), C7 ("C7", Hub.C), C8 ("C8", Hub.C),
        CMESSENGER ("CMessenger", Hub.C);
        private String label;
        private Hub worldHub;

        private World(String label, Hub worldHub) {
            this.label = label;
            this.worldHub = worldHub;
        }

        public Hub getHub() {
            return worldHub;
        }

        public static World fromInt(int i) {
            return World.values()[i];
        }

        public String toString() {
            return label;
        }
    }
}
