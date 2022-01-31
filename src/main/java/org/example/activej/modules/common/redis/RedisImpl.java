package org.example.activej.modules.common.redis;

import io.activej.async.service.EventloopService;
import io.activej.eventloop.Eventloop;
import io.activej.promise.Promise;
import io.activej.redis.RedisClient;
import io.activej.redis.RedisConnection;
import io.activej.redis.RedisRequest;
import io.activej.redis.RedisResponse;
import org.jetbrains.annotations.NotNull;

public class RedisImpl implements Redis, EventloopService {

    private final RedisClient redisClient;

    private final Eventloop eventloop;

    private RedisConnection redisConnection;

    public RedisImpl(Eventloop eventloop, RedisClient redisClient) {
        this.eventloop = eventloop;
        this.redisClient = redisClient;
    }

    @Override
    public Promise<String> getString(String key) {
        return this.redisConnection.cmd(RedisRequest.of("GET", key), RedisResponse.BYTES_UTF8);
    }

    public Promise<Void> putString(String key, String value) {
        return putString(key, value, null);
    }

    public Promise<Void> putString(String key, String value, Integer ttl) {
        RedisRequest request = ttl == null ? RedisRequest.of("SET", key, value) : RedisRequest.of("SET", key, value, "EX", String.valueOf(ttl));

        return this.redisConnection.cmd(request, RedisResponse.OK);
    }

    @Override
    public @NotNull Eventloop getEventloop() {
        return eventloop;
    }

    public @NotNull Promise<RedisConnection> start() {
        return redisClient.connect().whenResult(redisConnection -> this.redisConnection = redisConnection);
    }

    @Override
    public @NotNull Promise<?> stop() {
        return redisConnection.quit();
    }
}
