/*
 * Copyright (c) 2022 kyngs
 *
 * Please see the included "LICENSE" file for further information about licensing of this code.
 *
 * !!Removing this notice is a direct violation of the license!!
 */

package xyz.kyngs.easydb.provider.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import xyz.kyngs.easydb.ConnectionException;
import xyz.kyngs.easydb.EasyDB;
import xyz.kyngs.easydb.pool.StandardPool;
import xyz.kyngs.easydb.provider.AbstractProvider;
import xyz.kyngs.easydb.scheduler.ThrowableFunction;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class RabbitMQ extends AbstractProvider<Channel, IOException> {

    private final RabbitMQConfig config;
    private final AtomicReference<StandardPool<Channel>> outgoingPool;
    private final AtomicReference<Connection> connection;

    public RabbitMQ(RabbitMQConfig config) {
        if (true) throw new UnsupportedOperationException("NOT IMPLEMENTED YET");
        this.config = config;

        connection = new AtomicReference<>();
        outgoingPool = new AtomicReference<>();
    }

    public void addSubscriber(String queue) {
        //runTask(channel -> {
        //    var queueID = channel.queueDeclare("", false, true, true, null).getQueue();
        //    channel.exchangeDeclare()
        //})
    }

    @Override
    public void start(EasyDB<?, ?, ?> easyDB) throws ConnectionException {
        connection.set(openConnection());
        connectionCheck();
    }

    private Connection openConnection() {
        try {
            return config.factory.newConnection(config.id);
        } catch (IOException | TimeoutException e) {
            throw new ConnectionException(e);
        }
    }

    private Boolean healthCheck(Channel t) {
        return t.isOpen();
    }

    private Connection connectionCheck() {
        return connection.updateAndGet(connection1 -> {
            if (!connection1.isOpen()) {
                var oldPool = outgoingPool.get();

                var connection = openConnection();

                outgoingPool.set(new StandardPool<>(
                        config.dryBehavior,
                        config.limitReachBehavior,
                        this::openChannel,
                        this::channelDeath,
                        this::healthCheck,
                        config.startChannelCount,
                        config.maxChannelCount
                ));

                oldPool.close();

                try {
                    oldPool.awaitClose();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return connection;
            }
            return connection1;
        });
    }

    private Channel openChannel() {
        try {
            return connectionCheck().createChannel();
        } catch (IOException e) {
            throw new ConnectionException(e);
        }
    }

    private void channelDeath(Channel channel) {
        if (channel.isOpen()) {
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                throw new ConnectionException(e);
            }
        }


    }

    @Override
    public void stop() {

    }

    @Override
    public <V> V runTask(ThrowableFunction<Channel, V, IOException> task) throws IOException {
        super.runTask(task);

        var pool = outgoingPool.get();

        try {
            var channel = pool.obtain();

            try {
                return task.run(channel);
            } finally {
                pool.cycle(channel);
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean identifyConnectionException(Exception e) {
        return e instanceof IOException;
    }
}
