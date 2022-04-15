package csci4490.uno.dealer.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;

/**
 * A scheduler which runs all scheduled jobs on a single thread. This thread
 * starts automatically when a job is scheduled. When no more jobs remain for
 * the thread to execute, it interrupts automatically. This process repeats
 * as many times as needed.
 *
 * @see #schedule(JobRunnable, int, Duration, Duration)
 * @see #scheduleForever(JobRunnable, Duration, Duration)
 */
public class ThreadedScheduler extends Scheduler {

    private Thread thread;

    @Override
    public @NotNull ScheduledJob schedule(@NotNull JobRunnable runner,
                                          int executeCount,
                                          @NotNull Duration initialDelay,
                                          @NotNull Duration repeatDelay) {
        ScheduledJob scheduled = super.schedule(runner, executeCount,
                initialDelay, repeatDelay);

        /*
         * If a thread was previously created, but was interrupted, nullify
         * it here so new is one created immediately after. This occurs when
         * a thread runs out of jobs to execute. This is so the user doesn't
         * need to interrupt it themselves.
         */
        if (thread != null && thread.isInterrupted()) {
            this.thread = null;
        }

        if (thread == null) {
            this.thread = new SchedulerThread(jobs);
            thread.start();
        }

        return scheduled;
    }

}
