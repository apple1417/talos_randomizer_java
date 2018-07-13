package randomizer_bruteforce;

// Some basic stuff you need to start multithreading the generators

public abstract class RunnableThread extends Thread implements Runnable {
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
