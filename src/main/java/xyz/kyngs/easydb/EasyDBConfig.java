/*
 * Copyright (c) 2021 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb;

import xyz.kyngs.easydb.provider.Provider;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EasyDBConfig {

    /**
     * Set of all providers.
     */
    protected final Set<Provider> providers;
    /**
     * Makes EasyDB use one shared Scheduler workers per VM, instead of creating a new one with each instance, useful in plugin systems.
     * Enabled by default.
     */
    protected boolean useGlobalScheduler;
    protected ExecutorService executor;

    public EasyDBConfig() {
        providers = new HashSet<>();
        useGlobalScheduler = true;

    }

    protected void build() {
        if (executor == null) executor = Executors.newWorkStealingPool(Runtime.getRuntime().availableProcessors());
    }

    public EasyDBConfig addProvider(Provider... providers) {
        Collections.addAll(this.providers, providers);
        return this;
    }

    public EasyDBConfig useGlobalWorkers(boolean use) {
        this.useGlobalScheduler = use;
        return this;
    }

    public EasyDBConfig setExecutor(ExecutorService executor) {
        this.executor = executor;
        return this;
    }

}
