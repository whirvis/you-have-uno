package csci4490.uno.commons.scheduler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("BusyWait")
class SchedulerThread extends Thread {

    private final List<ScheduledJob> jobs;

    SchedulerThread(@NotNull List<ScheduledJob> jobs) {
        this.jobs = jobs;
    }

    private boolean executeJob(ScheduledJob job) {
        try {
            job.execute();
            return job.isFinished();
        } catch (CancelledJobException e) {
            return true;
        } catch (Throwable cause) {
            cause.printStackTrace();
            return true;
        }
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            /* lowers CPU usage */
            try {
                Thread.sleep(0, 1);
            } catch (InterruptedException e) {
                this.interrupt();
                continue;
            }

            /* executes all current jobs */
            synchronized (jobs) {
                jobs.removeIf(this::executeJob);
            }

            /* stop thread if no more jobs */
            if (jobs.isEmpty()) {
                this.interrupt();
            }
        }
    }

}
