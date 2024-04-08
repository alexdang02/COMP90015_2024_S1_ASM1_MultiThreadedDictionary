package DictionaryServer;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ThreadPool {
    private final int minThreads;
    private final int maxThreads;
    private int curThreads;
    private final long timeoutInterval;
    private final long updateInterval;
    private final List<WorkerThread> workerThreads;
    private final BlockingDeque<Runnable> taskQueue;

    private SystemResource systemResource;

    public ThreadPool(int minThreads, int maxThreads, long updateInterval, long timeoutInterval) {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.curThreads = minThreads;
        this.timeoutInterval = timeoutInterval;
        this.updateInterval = updateInterval;

        systemResource = new SystemResource();
        taskQueue = new LinkedBlockingDeque<>();
        workerThreads = new ArrayList<>(maxThreads);

        // Monitoring pool on a separate thread
        new Thread(this::monitorPoolSize, "MONITOR").start();

        // Initialising pool at minimum number of threads
        startWorkerThreads(curThreads);
    }

    private void startWorkerThreads(int numThreads) {
        for (int i = 0; i < numThreads; i++) {
            WorkerThread thread = new WorkerThread(taskQueue);
            thread.setName(STR."ThreadNo-\{i}");
            workerThreads.add(thread);
            thread.start();
        }
    }

    public void submitTask(Runnable task) throws InterruptedException {
        taskQueue.put(task);
    }

    // Monitor idle threads, adjusting pool size and handling termination gracefully
    private void monitorPoolSize() {
        while (true) {
            try {
                // Shut down idle thread if there's queue
                Iterator<WorkerThread> iterator = workerThreads.iterator();
                while (iterator.hasNext() && (!taskQueue.isEmpty())) {
                    WorkerThread thread = iterator.next();
                    if (thread.getIdleTime() > timeoutInterval) {
                        System.out.println(STR."Shutting down \{thread.getName()} due to \{timeoutInterval} timeout.");
                        iterator.remove();
                        thread.setShutdown(true);
                        thread.interrupt();
                    }
                }

                // Update resource data and adjust pool size based on criteria
                systemResource = systemResource.updateSystemResource();
                System.out.println(STR."[THREADPOOL] Avail thread: \{curThreads} - Resource: \{systemResource}");

                int adjustedThreads = calculatePoolSize();
                if (adjustedThreads > curThreads) {
                    startWorkerThreads(adjustedThreads - curThreads);
                    curThreads = adjustedThreads;
                }

                Thread.sleep(updateInterval);
            } catch (InterruptedException e) {
                System.out.println(STR."THREADPOOL MONITOR GOT INTERRUPTED \{e.getMessage()}");
            }
        }
    }

    private int calculatePoolSize() {
        int queueLength = taskQueue.size();
        if (queueLength > 10 && curThreads < maxThreads) {
            return Math.min(curThreads + 1, maxThreads);
        } else if (queueLength < 5 && curThreads > minThreads) {
            return Math.max(curThreads - 1, minThreads);
        } else {
            return curThreads;
        }
    }

}
