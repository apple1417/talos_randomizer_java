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
            return values()[i];
        }
    }

    public enum Hub {
        A, B, C;

        public static Hub fromInt(int i) {
            return values()[i];
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

    public enum MobiusOptions {
        ALL_SIGILS("All Sigils",             0b000001),
        ALL_SHAPE("All of a Shape",          0b000010),
        ALL_COLOUR("All of a Colour",        0b000100),
        ETERNALIZE("Eternalize Ending",      0b001000),
        TWO_TOWER("Two Tower Floors",        0b010000),
        RANDOM_ARRANGERS("Random Arrangers", 0b100000);

        private String label;
        private int mask;

        private MobiusOptions(String label, int mask) {
            this.label = label;
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }

        public static MobiusOptions fromInt(int i) {
            return values()[i];
        }

        public static ArrayList<MobiusOptions> fromTalosProgress(TalosProgress progress) {
            int unlocks = progress.getVar("Randomizer_Loop");
            if (unlocks == 0) {return null;}
            ArrayList<MobiusOptions> arrangers = new ArrayList<MobiusOptions>();
            for (MobiusOptions mo : values()) {
                if ((unlocks & mo.getMask()) != 0) {
                    arrangers.add(mo);
                }
            }
            return arrangers;
        }

        public String toString() {
            return label;
        }
    }

    public enum MobiusRandomArrangers {
        A1_GATE("A1 Gate", 0x0001),
        A_GATE("A Gate", 0x0002),
        B_GATE("B Gate", 0x0004),
        C_GATE("C Gate", 0x0008),
        CONNECTOR("Connector", 0x0010),
        CUBE("Cube", 0x0020),
        FAN("Fan", 0x0040),
        RECORDER("Recorder", 0x0080),
        PLATFORM("Platform", 0x0100),
        F1("Floor 1", 0x0200),
        F2("Floor 2", 0x0400),
        F3("Floor 3", 0x0800),
        F4("Floor 4", 0x1000),
        F5("Floor 5", 0x2000),
        F6("Floor 6", 0x4000);

        private String label;
        private int mask;

        private MobiusRandomArrangers(String label, int mask) {
            this.label = label;
            this.mask = mask;
        }

        public int getMask() {
            return mask;
        }

        public static MobiusRandomArrangers fromInt(int i) {
            return values()[i];
        }

        public static ArrayList<MobiusRandomArrangers> fromTalosProgress(TalosProgress progress) {
            int unlocks = progress.getVar("Randomizer_LoopArrangers");
            if (unlocks == 0) {return null;}
            ArrayList<MobiusRandomArrangers> arrangers = new ArrayList<MobiusRandomArrangers>();
            for (MobiusRandomArrangers mra : values()) {
                if ((unlocks & mra.getMask()) != 0) {
                    arrangers.add(mra);
                }
            }
            return arrangers;
        }

        public String toString() {
            return label;
        }
    }

    public enum MoodySigils {
        OFF("Off"),
        COLOUR("Colour"),
        SHAPE("Shape"),
        BOTH("Colour + Shape");

        private String label;

        private MoodySigils(String label) {
            this.label = label;
        }

        public static MoodySigils fromInt(int i) {
            return values()[i];
        }

        public static MoodySigils fromTalosProgress(TalosProgress progress) {
            return values()[progress.getVar("Randomizer_Moody")];
        }

        public String toString() {
            return label;
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
            return values()[i];
        }

        public static RandomizerMode fromTalosProgress(TalosProgress progress) {
            return values()[progress.getVar("Randomizer_Mode")];
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
            return values()[i];
        }

        public static ScavengerEnding fromTalosProgress(TalosProgress progress) {
            return values()[progress.getVar("Randomizer_ScavengerMode")];
        }

        public ArrayList<Arranger> getAllowedArrangers() {
            switch(this) {
                case CONNECTOR_CLIP: return new ArrayList<Arranger>(Arrays.asList(Arranger.CONNECTOR, Arranger.F1));
                case F2_CLIP: return new ArrayList<Arranger>(Arrays.asList(Arranger.CUBE, Arranger.F1, Arranger.F2));
                case F3_CLIP: return new ArrayList<Arranger>(Arrays.asList(Arranger.F1, Arranger.F3));
                case F6: return new ArrayList<Arranger>(Arrays.asList(Arranger.F1, Arranger.F6));
                default: return null;
            }
        }

        public ArrayList<String> getAllowedSigils() {
            switch (this) {
                case CONNECTOR_CLIP: return new ArrayList<String>(Arrays.asList(
                    "ML1", "MT1", "MT2",
                    "NL1", "NL2", "NZ1", "NZ2"
                ));
                case F2_CLIP: return new ArrayList<String>(Arrays.asList(
                    "ML2", "MT3", "MT4", "MZ1",
                    "NL1", "NL2", "NZ1", "NZ2",
                    "NL3", "NL4", "NL5", "NL6", "NO1", "NT1", "NT2", "NT3", "NT4"
                ));
                case F3_CLIP: return new ArrayList<String>(Arrays.asList(
                    "NL1", "NL2", "NZ1", "NZ2",
                    "NI1", "NI2", "NI3", "NI4", "NJ1", "NJ2", "NL7", "NL8", "NS1", "NZ3"
                ));
                case F6: return new ArrayList<String>(Arrays.asList(
                    "NL1", "NL2", "NZ1", "NZ2",
                    "EL1", "EL2", "EL3", "EL4", "EO1", "ES1", "ES2", "ES3", "ES4"
                ));
                default: return null;
            }
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
            return values()[i];
        }

        public static ScavengerMode fromTalosProgress(TalosProgress progress) {
            return values()[progress.getVar("Randomizer_Scavenger")];
        }

        public String toString() {
            return label;
        }
    }

    public enum World {
        A1, A2, A3, A4, A5, A6, A7, A8, ADEVISLAND,
        B1, B2, B3, B4, B5, B6, B7, B8,
        C1, C2, C3, C4, C5, C6, C7, C8, CMESSENGER;

        public static World fromInt(int i) {
            return World.values()[i];
        }

        public String toString() {
            if (this == ADEVISLAND) {
                return "ADevIsland";
            } else if (this == CMESSENGER) {
                return "CMessenger";
            } else {
                return super.toString();
            }
        }
    }
}
