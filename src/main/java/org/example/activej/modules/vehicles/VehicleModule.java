package org.example.activej.modules.vehicles;

import com.dslplatform.json.DslJson;
import io.activej.http.RoutingServlet;
import io.activej.inject.annotation.Provides;
import io.activej.inject.module.AbstractModule;
import org.example.activej.modules.common.redis.Redis;
import org.example.activej.modules.vehicles.controller.VehicleController;
import org.example.activej.modules.vehicles.dao.VehicleDao;
import org.example.activej.modules.vehicles.service.VehicleService;
import org.example.activej.modules.vehicles.service.VehicleServiceImpl;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static io.activej.http.HttpMethod.GET;

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
    VehicleDao vehicleDao(Executor executor, DataSource dataSource) {
        return new VehicleDao(executor, dataSource);
    }

    @Provides
    VehicleService vehicleService(VehicleDao vehicleDao, Redis redis, DslJson<ArrayList<Object>> dslJson) {
        return new VehicleServiceImpl(vehicleDao, redis, dslJson);
    }

    @Provides
    VehicleController vehicleController(VehicleService vehicleService) {
        return new VehicleController(vehicleService);
    }

    @Provides
    RoutingServlet servlet(VehicleController vehicleController) {
        return RoutingServlet.create()
                .map(GET, "/years", vehicleController::getYear);
    }

}
