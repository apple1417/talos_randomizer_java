package randomizer_bruteforce.generic;

public abstract class RunnableThread extends Thread {
    private String threadName;
    public RunnableThread(String name) {
        threadName = name;
    }

    private Thread t;
    protected long min;
    protected long max;
    public void start(long min, long max) {
        this.min = min;
        this.max = max;
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        } else {
            System.out.println(String.format("Thread %s already running", threadName));
        }
    }

    public void waitFinished() {
        try {
            t.join();
        } catch (InterruptedException e) {}
    }

    public abstract void run();
}
