package apple1417.randomizer;

public interface Generator {
    String getInfo();
    TalosProgress generate(long seed);
}
