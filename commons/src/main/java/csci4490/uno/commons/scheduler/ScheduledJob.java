package csci4490.uno.commons.scheduler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A job that has been scheduled to execute by a {@link ThreadedScheduler}.
 * These can run one time, multiple times, or even forever.
 */
public final class ScheduledJob {

    public static final int EXECUTE_FOREVER = -1;

    private final @NotNull JobRunnable runner;
    private final int executeCount;
    private final @NotNull Duration initialDelay;
    private final @NotNull Duration repeatDelay;

    private final long initialDelayMs;
    private final long repeatDelayMs;
    private long lastExecutionTime;
    private long timesExecuted;
    private boolean executing;
    private boolean cancelled;

    private @Nullable Consumer<ScheduledJob> finishCallback;
    private @Nullable Consumer<ScheduledJob> cancelCallback;

    /**
     * @param runner       the code to execute on each run.
     * @param executeCount the amount of times to execute this job.
     *                     A negative value means it will run forever.
     * @param initialDelay how long to wait before executing this job
     *                     <i>the first time.</i>
     * @param repeatDelay  how long to wait before executing this job
     *                     <i>after the first execution.</i>
     */
    ScheduledJob(@NotNull JobRunnable runner, int executeCount,
                 @NotNull Duration initialDelay,
                 @NotNull Duration repeatDelay) {

        /* @formatter:off */
        this.runner = Objects.requireNonNull(runner,
                "runner cannot be null");
        this.initialDelay = Objects.requireNonNull(initialDelay,
                "initialDelay cannot be null");
        this.repeatDelay = Objects.requireNonNull(repeatDelay,
                "repeatDelay cannot be null");
        /* @formatter:on */

        if (executeCount == 0) {
            String msg = "executeCount must be at least one or negative";
            throw new IllegalArgumentException(msg);
        }

        this.initialDelayMs = initialDelay.toMillis();
        this.repeatDelayMs = repeatDelay.toMillis();
        this.executeCount = executeCount;
    }

    /**
     * Sets the callback for when this job finishes execution.
     *
     * @param callback the code to execute when this job finishes execution.
     *                 A value of {@code null} is permitted, and will result
     *                 in nothing being executed.
     * @return this scheduled job.
     */
    public @NotNull ScheduledJob onFinish(@Nullable Consumer<ScheduledJob> callback) {
        this.finishCallback = callback;
        return this;
    }

    /**
     * Sets the callback for when this job is cancelled.
     *
     * @param callback the code to execute when this job is cancelled.
     *                 a value of {@code null} is permitted, and will
     *                 result in nothing being executed.
     * @return this scheduled job.
     */
    public @NotNull ScheduledJob onCancel(@Nullable Consumer<ScheduledJob> callback) {
        this.cancelCallback = callback;
        return this;
    }

    /**
     * @return how many times this job will execute before being finished.
     * Negative values indicate this job will execute forever.
     * @see #getTimesExecuted()
     */
    public int getExecuteCount() {
        return this.executeCount;
    }

    /**
     * @return how many more times this job will be executed. If this job
     * runs forever, a value of {@code -1} will be returned.
     * @see #executesForever()
     */
    public int getRemainingExecutions() {
        if (this.executesForever()) {
            return -1;
        }
        return (int) (this.executeCount - this.timesExecuted);
    }

    /**
     * @return {@code true} if this job will execute forever, {@code false}
     * otherwise.
     */
    public boolean executesForever() {
        return this.executeCount < 0;
    }

    /**
     * @return how long to wait before executing this job for the first time.
     */
    public @NotNull Duration getInitialDelay() {
        return this.initialDelay;
    }

    /**
     * @return how long to wait before executing this job after the first
     * invocation.
     */
    public @NotNull Duration getRepeatDelay() {
        return this.repeatDelay;
    }

    /**
     * The time is determined via {@link System#currentTimeMillis()}.
     *
     * @return the last time this job was executed.
     */
    public long getLastExecutionTime() {
        return this.lastExecutionTime;
    }

    /**
     * @return how many times this job has executed.
     * @see #getExecuteCount()
     */
    public long getTimesExecuted() {
        return this.timesExecuted;
    }

    /**
     * @return {@code true} if this job is finished (and should therefore
     * no longer be executed), {@code false} otherwise.
     * @see #cancel()
     */
    public boolean isFinished() {
        if (this.isCancelled()) {
            return true;
        } else if (this.executesForever()) {
            return false;
        }
        return timesExecuted >= executeCount;
    }

    /**
     * @return {@code true} if this job should no longer run (even if it has
     * remaining executions), {@code false} otherwise.
     */
    public boolean isCancelled() {
        return this.cancelled;
    }

    /**
     * Cancels this scheduled job. Once a job is cancelled, it will no longer
     * be run, even if it has remaining executions. If this job has already
     * been cancelled, this method will have no effect.
     *
     * @return this scheduled job.
     * @throws CancelledJobException if this method is called while the job
     *                               is executing. This allows for a job to
     *                               halt execution when cancelled.
     */
    public synchronized @NotNull ScheduledJob cancel() {
        if(this.isCancelled()) {
            return this;
        }
        this.cancelled = true;
        if (cancelCallback != null) {
            cancelCallback.accept(this);
        }
        if (executing) {
            throw new CancelledJobException();
        }
        return this;
    }

    private boolean shouldExecute() {
        if (this.isFinished()) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        if (lastExecutionTime <= 0) {
            this.lastExecutionTime = currentTime;
        }

        long delta = currentTime - lastExecutionTime;
        if (timesExecuted == 0) {
            return delta >= initialDelayMs;
        } else {
            return delta >= repeatDelayMs;
        }
    }

    /* package-private for Scheduler */
    synchronized void execute() throws Exception {
        if (!this.shouldExecute()) {
            return;
        }

        this.executing = true;

        long currentTime = System.currentTimeMillis();
        runner.run(this);
        this.timesExecuted++;
        this.lastExecutionTime = currentTime;

        this.executing = false;

        if (this.isFinished() && finishCallback != null) {
            finishCallback.accept(this);
        }
    }

}
