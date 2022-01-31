package org.example.activej.modules.vehicles;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import io.activej.worker.annotation.Worker;
import org.example.activej.modules.common.redis.Redis;
import org.example.activej.modules.vehicles.controller.VehicleController;
import org.example.activej.modules.vehicles.dao.VehicleDao;
import org.example.activej.modules.vehicles.service.VehicleService;
import org.example.activej.modules.vehicles.service.VehicleServiceImpl;

import javax.sql.DataSource;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.activej.http.HttpMethod.GET;

@SuppressWarnings("unused")
public final class VehicleModule extends AbstractModule {
    private VehicleModule() {
    }

    public static VehicleModule create() {
        return new VehicleModule();
    }

    @Provides
    Executor executor() {
        return Executors.newCachedThreadPool();
    }

    @Provides
    @Worker
    VehicleDao vehicleDao(Executor executor, DataSource dataSource) {
        return new VehicleDao(executor, dataSource);
    }

    @Provides
    @Worker
    VehicleService vehicleService(VehicleDao vehicleDao, Redis redis, ObjectMapper objectMapper) {
        return new VehicleServiceImpl(vehicleDao, redis, objectMapper);
    }

    @Provides
    @Worker
    VehicleController vehicleController(VehicleService vehicleService) {
        return new VehicleController(vehicleService);
    }

    @Provides
    @Worker
    RoutingServlet servlet(VehicleController vehicleController) {
        return RoutingServlet.create()
                .map(GET, "/years", vehicleController::getYear);
    }

}
