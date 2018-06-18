package randomizer_bruteforce;

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
        NONE,
        DEFAULT,
        SIXTY,
        FULLY_RANDOM,
        INTENDED,
        HARDMODE,
        SIXTY_HARDMODE;

        public static RandomizerMode fromInt(int i) {
            return RandomizerMode.values()[i];
        }
    }

    public enum ScavengerMode {
        OFF,
        SHORT,
        FULL;

        public static ScavengerMode fromInt(int i) {
            return ScavengerMode.values()[i];
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
