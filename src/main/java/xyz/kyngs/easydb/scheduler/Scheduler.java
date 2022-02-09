/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.scheduler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public record Scheduler(ExecutorService executor) {

    public void schedule(Runnable run) {
        executor.submit(run);
    }

    public void stop() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(90, TimeUnit.SECONDS)) {
                System.out.println("[EasyDB] Some tasks didn't finish, continuing shutdown");
            }
        } catch (InterruptedException e) {
            System.err.println("[EasyDB] The program was interrupted while waiting for DB tasks to finish, continuing shutdown");
        }

    }

}
