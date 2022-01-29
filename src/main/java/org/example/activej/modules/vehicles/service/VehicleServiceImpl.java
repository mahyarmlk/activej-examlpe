package org.example.activej.modules.vehicles.service;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonWriter;
import io.activej.promise.Promise;
import org.example.activej.modules.common.redis.Redis;
import org.example.activej.modules.vehicles.dao.VehicleDao;

import java.io.IOException;
import java.util.ArrayList;

public class VehicleServiceImpl implements VehicleService {
    private final VehicleDao vehicleDao;
    private final Redis redis;
    private final DslJson<ArrayList<Object>> dslJson;

    private final String REDIS_YEARS_KEY = "years";
    private final Integer REDIS_YEARS_TTL = 3600;

    public VehicleServiceImpl(VehicleDao vehicleDao, Redis redis, DslJson<ArrayList<Object>> dslJson) {
        this.vehicleDao = vehicleDao;
        this.redis = redis;
        this.dslJson = dslJson;
    }

    public Promise<String> getYears() {
        return redis.getString(REDIS_YEARS_KEY).thenIfNull(() -> vehicleDao.getYears()
                .map(this::toJson)
                .then(result -> {
                    redis.putSting(REDIS_YEARS_KEY, result, REDIS_YEARS_TTL);
                    return Promise.of(result);
                })
        );
    }

    private String toJson(Object op) throws IOException {
        JsonWriter jsonWriter = dslJson.newWriter();
        dslJson.serialize(jsonWriter, op);
        return jsonWriter.toString();
    }
}
