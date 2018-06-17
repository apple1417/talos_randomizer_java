package randomizer_bruteforce;

public class Rand {
    private long seed;

    public Rand(long seed) {
        this.seed = seed;
    }

    public int next(int min, int max) {
        seed = (214013 * seed + 2531011) % 0x80000000;
        if (min == max) {return min;}
        return (int)(seed % (max - (min - 1))) + min;
    }
}
