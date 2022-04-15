package csci4490.uno.dealer.scheduler;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple scheduler used to execute scheduled jobs. How these jobs are
 * executed depends on the arguments provided when they are scheduled.
 *
 * @see #schedule(JobRunnable, int, Duration, Duration)
 * @see #scheduleForever(JobRunnable, Duration, Duration)
 */
public abstract class Scheduler {

    protected final List<ScheduledJob> jobs;

    public Scheduler() {
        this.jobs = new ArrayList<>();
    }

    /**
     * Schedules a job to run.
     *
     * @param runner       the code to execute on each run.
     * @param executeCount the amount of times to execute this job.
     *                     A negative value means it will run forever.
     * @param initialDelay how long to wait before executing this job
     *                     <i>the first time.</i>
     * @param repeatDelay  how long to wait before executing this job
     *                     <i>after the first execution.</i>
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner}, {@code initialDelay},
     *                              or {@code repeatDelay} are {@code null}.
     */
    public @NotNull ScheduledJob schedule(@NotNull JobRunnable runner,
                                          int executeCount,
                                          @NotNull Duration initialDelay,
                                          @NotNull Duration repeatDelay) {
        synchronized (jobs) {
            ScheduledJob scheduled = new ScheduledJob(runner, executeCount,
                    initialDelay, repeatDelay);
            jobs.add(scheduled);
            return scheduled;
        }
    }

    /**
     * Schedules a job to run one time.
     *
     * @param runner the code to execute.
     * @param delay  how long to wait before executing this job.
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner} or {@code delay}
     *                              are {@code null}.
     */
    public final @NotNull ScheduledJob schedule(@NotNull JobRunnable runner,
                                                @NotNull Duration delay) {
        return this.schedule(runner, 1, delay, Duration.ZERO);
    }

    /**
     * Schedules a job to run as soon as possible.
     *
     * @param runner the code to execute.
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner} is {@code null}.
     */
    public final @NotNull ScheduledJob schedule(@NotNull JobRunnable runner) {
        return this.schedule(runner, Duration.ZERO);
    }

    /**
     * Schedules a job to run forever. Execution of a job can be halted via
     * {@link ScheduledJob#cancel()}. This will prevent it from executing
     * anymore, even though it was scheduled to run forever.
     *
     * @param runner       the code to execute on each run.
     * @param initialDelay how long to wait before executing this job
     *                     <i>the first time.</i>
     * @param repeatDelay  how long to wait before executing this job
     *                     <i>after the first execution.</i>
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner}, {@code initialDelay},
     *                              or {@code repeatDelay} are {@code null}.
     */
    /* @formatter:off */
    public final @NotNull ScheduledJob
            scheduleForever(@NotNull JobRunnable runner,
                            @NotNull Duration initialDelay,
                            @NotNull Duration repeatDelay) {
        return this.schedule(runner, ScheduledJob.EXECUTE_FOREVER,
                initialDelay, repeatDelay);
    }
    /* @formatter:on */

    /**
     * Schedules a job to run.
     *
     * @param runner       the code to execute on each run.
     * @param executeCount the amount of times to execute this job.
     *                     A negative value means it will run forever.
     * @param initialDelay how long to wait before executing this job
     *                     <i>the first time.</i>
     * @param repeatDelay  how long to wait before executing this job
     *                     <i>after the first execution.</i>
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner}, {@code initialDelay},
     *                              or {@code repeatDelay} are {@code null}.
     */
    /* @formatter:off */
    public final @NotNull ScheduledJob
            schedule(@NotNull Runnable runner, int executeCount,
                     @NotNull Duration initialDelay,
                     @NotNull Duration repeatDelay) {
        Objects.requireNonNull(runner, "runner cannot be null");
        return this.schedule((job) -> runner.run(), executeCount,
                initialDelay, repeatDelay);
    }
    /* @formatter:on */

    /**
     * Schedules a job to run one time.
     *
     * @param runner the code to execute.
     * @param delay  how long to wait before executing this job.
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner} or {@code delay}
     *                              are {@code null}.
     */
    public final @NotNull ScheduledJob schedule(@NotNull Runnable runner,
                                                @NotNull Duration delay) {
        return this.schedule(runner, 1, delay, Duration.ZERO);
    }

    /**
     * Schedules a job to run as soon as possible.
     *
     * @param runner the code to execute.
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner} is {@code null}.
     */
    public final @NotNull ScheduledJob schedule(@NotNull Runnable runner) {
        return this.schedule(runner, Duration.ZERO);
    }

    /**
     * Schedules a job to run forever. Execution of a job can be halted via
     * {@link ScheduledJob#cancel()}. This will prevent it from executing
     * anymore, even though it was scheduled to run forever.
     *
     * @param runner       the code to execute on each run.
     * @param initialDelay how long to wait before executing this job
     *                     <i>the first time.</i>
     * @param repeatDelay  how long to wait before executing this job
     *                     <i>after the first execution.</i>
     * @return the scheduled job.
     * @throws NullPointerException if {@code runner}, {@code initialDelay},
     *                              or {@code repeatDelay} are {@code null}.
     */
    /* @formatter:off */
    public final @NotNull ScheduledJob
            scheduleForever(@NotNull Runnable runner,
                            @NotNull Duration initialDelay,
                            @NotNull Duration repeatDelay) {
        return this.schedule(runner, ScheduledJob.EXECUTE_FOREVER,
                initialDelay, repeatDelay);
    }
    /* @formatter:on */

}
