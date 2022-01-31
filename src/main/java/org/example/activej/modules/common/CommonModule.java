package org.example.activej.modules.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import io.activej.config.Config;
import io.activej.eventloop.Eventloop;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import io.activej.redis.RedisClient;
import io.activej.worker.annotation.Worker;
import org.example.activej.modules.common.redis.Redis;
import org.example.activej.modules.common.redis.RedisImpl;
import org.example.activej.modules.common.utils.HikariConfigConverter;

import javax.sql.DataSource;

import static io.activej.config.converter.ConfigConverters.ofInetSocketAddress;

@SuppressWarnings("unused")
public final class CommonModule extends AbstractModule {
    private CommonModule() {
    }

    public static CommonModule create() {
        return new CommonModule();
    }

    @Provides
    DataSource dataSource(Config config) {
        HikariConfigConverter converter = HikariConfigConverter.create().withAllowMultiQueries();
        return new HikariDataSource(config.get(converter, "hikari"));
    }

    @Provides
    @Worker
    RedisClient redisClient(Eventloop eventloop, Config config) {
        return RedisClient.create(eventloop, config.get(ofInetSocketAddress(), "redis.address"));
    }

    @Provides
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Provides
    @Worker
    Redis redis(Eventloop eventloop, RedisClient redisClient) {
        return new RedisImpl(eventloop, redisClient);
    }
}
