package randomizer_bruteforce.all_default.required_hubs;

class Run {
    private static int THREAD_NUM = 8;
    private static long PER_LOOP = 100000;
    private static long PER_THREAD = (PER_LOOP / THREAD_NUM);
    private static long current_seed = 0;
    private static long max_seed = 0x7FFFFFFF;
    private static int data[] = new int[3];

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println(String.format("Stopped while working on batch starting at %d", current_seed));
                System.out.println(String.format("%d %d %d", data[0], data[1], data[2]));
                System.out.println(String.format("%.2f%% %.2f%% %.2f%%", (float)data[0]*100/current_seed, (float)data[1]*100/current_seed, (float)data[2]*100/current_seed));
            }
        });

        while (current_seed + PER_LOOP < max_seed) {
            Tester[] threads = new Tester[THREAD_NUM];
            for (int i = 0; i < THREAD_NUM; i++) {
                threads[i] = new Tester(Integer.toString(i));
                threads[i].start(current_seed, current_seed + PER_THREAD - 1);
                current_seed += PER_THREAD;
            }
            for (Tester thread : threads) {
                int[] output = thread.waitFinishedAndGetData();
                data[0] += output[0];
                data[1] += output[1];
                data[2] += output[2];
            }
            if (current_seed % 10000000 == 0) {
                System.out.println(current_seed);
                System.out.println(String.format("%d %d %d", data[0], data[1], data[2]));
                System.out.println(String.format("%.2f%% %.2f%% %.2f%%", (float)data[0]*100/current_seed, (float)data[1]*100/current_seed, (float)data[2]*100/current_seed));
            }
        }
        Tester thread = new Tester("0");
        thread.start(current_seed, 0x7FFFFFFF);
        int[] output = thread.waitFinishedAndGetData();
        data[0] += output[0];
        data[1] += output[1];
        data[2] += output[2];
        System.out.println("Total:");
        System.out.println(String.format("%d %d %d", data[0], data[1], data[2]));
        System.out.println(String.format("%.2f%% %.2f%% %.2f%%", (float)data[0]*100/current_seed, (float)data[1]*100/current_seed, (float)data[2]*100/current_seed));
    }
}
