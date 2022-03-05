/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.provider.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import xyz.kyngs.easydb.pool.DryBehavior;
import xyz.kyngs.easydb.pool.LimitReachBehavior;

public class RabbitMQConfig {

    protected final ConnectionFactory factory;
    protected final String id;
    protected int startChannelCount, maxChannelCount;
    protected DryBehavior dryBehavior;
    protected LimitReachBehavior limitReachBehavior;

    public RabbitMQConfig(String id) {
        this.id = id;
        factory = new ConnectionFactory();
        startChannelCount = 4;
        maxChannelCount = Integer.MAX_VALUE;

        dryBehavior = DryBehavior.POPULATE;
        limitReachBehavior = LimitReachBehavior.WAIT;
    }

    public RabbitMQConfig(ConnectionFactory factory, String id) {
        this.factory = factory;
        this.id = id;
    }

    public RabbitMQConfig setStartChannelCount(int startChannelCount) {
        this.startChannelCount = startChannelCount;
        return this;
    }

    public RabbitMQConfig setMaxChannelCount(int maxChannelCount) {
        this.maxChannelCount = maxChannelCount;
        return this;
    }

    public RabbitMQConfig setDryBehavior(DryBehavior dryBehavior) {
        this.dryBehavior = dryBehavior;
        return this;
    }

    public RabbitMQConfig setLimitReachBehavior(LimitReachBehavior limitReachBehavior) {
        this.limitReachBehavior = limitReachBehavior;
        return this;
    }

    public ConnectionFactory getFactory() {
        return factory;
    }

}
