package org.example.activej.modules.common.redis;

import io.activej.promise.Promise;
import io.activej.redis.RedisClient;
import io.activej.redis.RedisRequest;
import io.activej.redis.RedisResponse;

public class RedisImpl implements Redis {

    private final RedisClient redisClient;

    public RedisImpl(RedisClient redisClient) {
        this.redisClient = redisClient;
    }

    @Override
    public Promise<String> getString(String key) {
        return redisClient.connect()
                .then(connection -> connection.cmd(RedisRequest.of("GET", key), RedisResponse.BYTES_UTF8)
                        .then((result, e) -> connection.quit()
                                .then(() -> e == null ?
                                        Promise.of(result) :
                                        Promise.ofException(e))));
    }

    public Promise<Void> putSting(String key, String value) {
        return putSting(key, value, null);
    }

    public Promise<Void> putSting(String key, String value, Integer ttl) {
        RedisRequest request = ttl == null ? RedisRequest.of("SET", key, value) : RedisRequest.of("SET", key, value, "EX", String.valueOf(ttl));

        return redisClient.connect()
                .then(connection -> connection.cmd(request, RedisResponse.OK)
                        .then((result, e) -> connection.quit()));
    }
}
