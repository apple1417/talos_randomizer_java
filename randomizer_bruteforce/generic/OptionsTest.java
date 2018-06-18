package randomizer_bruteforce.generic;

import java.util.HashMap;
import java.util.Random;
import randomizer_bruteforce.Enums.RandomizerMode;
import randomizer_bruteforce.Enums.ScavengerMode;
import randomizer_bruteforce.TalosProgress;

class OptionsTest {
    private static Random rand = new Random();
    public static void main(String[] args) {
        HashMap<String, Integer> options = new HashMap<String, Integer>();
        for (int mode = 0; mode < 7; mode++) {
            options.put("Randomizer_Mode", mode);
            for (int scavenger = 0; scavenger < 3; scavenger++) {
                options.put("Randomizer_Scavenger", scavenger);
                for (int portals = 0; portals < 2; portals++) {
                    options.put("Randomizer_Portals", portals);
                    for (int loop = 0; loop < 2; loop ++) {
                        options.put("Randomizer_Loop", loop);
                        Generator g = new Generator(options);
                        int seed = rand.nextInt(1000000);
                        System.out.println(String.format("Mode: %s, Scavenger: %s, Portals: %s, Mobius: %s, Seed: %d", RandomizerMode.fromInt(mode), ScavengerMode.fromInt(scavenger), (portals == 1) ? "On" : "Off", (loop == 1) ? "All Sigils" : "Off", seed));
                        try {
                            System.out.println(g.generate(seed).getChecksum());
                        } catch (Exception e) {
                            System.out.println("Fails to generate");
                        }
                    }
                }
            }
        }
    }
}
