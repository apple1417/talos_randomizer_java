package randomizer_bruteforce.all_default.one_hub_F6;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

        SeedScheduler s = new SeedScheduler(() -> new GeneratorF6(),
                                            (TalosProgress p) -> evaluate(p));
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
    private static void evaluate(TalosProgress progress) {
        // This generator can return null if it's an invalid seed
        if (progress == null) {
            return;
        }
        // Need to make sure we can get to F1 within A
        int lCount = 0;
        int zCount = 0;
        for (String marker : A_MARKERS) {
            String sigil = TalosProgress.TETROS[progress.getVar(marker)];
            if (sigil.startsWith("NL")) {
                lCount++;
            } else if (sigil.startsWith("NZ")) {
                zCount++;
            }
        }
        if (lCount >= 2 && zCount >= 2) {
            String output = String.format("%d, %s, %d", progress.getVar("Randomizer_Seed"), progress.getChecksum(), progress.getVar("Code_Floor6"));
            System.out.println(output);
            try {
                Files.write(Paths.get("randomizer_bruteforce/all_default/one_hub_F6/output.txt"), (output + "\n").getBytes(), StandardOpenOption.APPEND);
            } catch (IOException e) {}
        }
    }
}
