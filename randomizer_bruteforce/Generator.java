package randomizer_bruteforce;

public interface Generator {
    String getInfo();
    TalosProgress generate(long seed);
}
