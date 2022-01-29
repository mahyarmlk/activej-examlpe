package org.example.activej.modules.common;

import com.dslplatform.json.DslJson;
import com.zaxxer.hikari.HikariDataSource;
import io.activej.config.Config;
import io.activej.eventloop.Eventloop;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import io.activej.redis.RedisClient;
import org.example.activej.modules.common.redis.Redis;
import org.example.activej.modules.common.redis.RedisImpl;
import org.example.activej.modules.common.utils.ArrayListObjectJsonReader;
import org.example.activej.modules.common.utils.HikariConfigConverter;

import javax.sql.DataSource;
import java.util.ArrayList;

import static io.activej.config.converter.ConfigConverters.ofInetSocketAddress;

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
    RedisClient redisClient(Eventloop eventloop, Config config) {
        return RedisClient.create(eventloop, config.get(ofInetSocketAddress(), "redis.address"));
    }

    @Provides
    DslJson<ArrayList<Object>> dslJson() {
        DslJson<ArrayList<Object>> dslJson = new DslJson<>();

        dslJson.registerReader(ArrayList.class, ArrayListObjectJsonReader.JSON_READER);

        return dslJson;
    }

    @Provides
    Redis redis(RedisClient redisClient) {
        return new RedisImpl(redisClient);
    }
}
