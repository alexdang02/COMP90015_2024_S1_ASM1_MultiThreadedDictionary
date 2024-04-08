package DictionaryServer;

import java.util.concurrent.BlockingQueue;

public class WorkerThread extends Thread {

    private long lastActiveTime;
    private boolean shutdown = false;
    private final BlockingQueue<Runnable> taskQueue;

    public WorkerThread(BlockingQueue<Runnable> taskQueue) {
        super();
        this.lastActiveTime = System.currentTimeMillis();
        this.taskQueue = taskQueue;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    @Override
    public void run() {
        super.run();
        while (!shutdown) { // Check shutdown flag before continuing
            try {
                Runnable task = taskQueue.take();
                task.run();
                ((WorkerThread) Thread.currentThread()).setLastActiveTime(System.currentTimeMillis());
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void setShutdown(boolean bool){
        shutdown = bool;
    }

    public long getIdleTime() {
        return System.currentTimeMillis() - lastActiveTime;
    }
}