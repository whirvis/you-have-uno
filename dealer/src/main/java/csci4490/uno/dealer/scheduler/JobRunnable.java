package csci4490.uno.dealer.scheduler;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface JobRunnable {

    interface NoParams {

        /**
         * Runs the scheduled job.
         *
         * @throws Exception if an error occurs. How this is handled is
         *                   dependent on the scheduler implementation.
         */
        void run() throws Exception;

    }

    /**
     * Runs the scheduled job.
     *
     * @param job the scheduled job.
     * @throws Exception if an error occurs. How this is handled is dependent
     *                   on the scheduler implementation.
     */
    void run(@NotNull ScheduledJob job) throws Exception;

}
