package org.example.activej.modules.vehicles.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.activej.promise.Promise;
import org.example.activej.modules.common.redis.Redis;
import org.example.activej.modules.vehicles.dao.VehicleDao;

import java.io.IOException;

public class VehicleServiceImpl implements VehicleService {
    private final VehicleDao vehicleDao;
    private final Redis redis;
    private final ObjectMapper objectMapper;

    private final String REDIS_YEARS_KEY = "years";
    private final Integer REDIS_YEARS_TTL = 3600;

    public VehicleServiceImpl(VehicleDao vehicleDao, Redis redis, ObjectMapper objectMapper) {
        this.vehicleDao = vehicleDao;
        this.redis = redis;
        this.objectMapper = objectMapper;
    }

    public Promise<String> getYears() {
        return redis.getString(REDIS_YEARS_KEY).thenIfNull(() -> vehicleDao.getYears()
                .map(this::toJson)
                .then(result -> {
                    redis.putString(REDIS_YEARS_KEY, result, REDIS_YEARS_TTL);
                    return Promise.of(result);
                })
        );
    }

    private String toJson(Object op) throws IOException {
        return objectMapper.writeValueAsString(op);
    }
}
