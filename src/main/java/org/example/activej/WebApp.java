package org.example.activej;

import com.zaxxer.hikari.HikariDataSource;
import io.activej.http.AsyncServlet;
import io.activej.http.RoutingServlet;
import io.activej.inject.Key;
import io.activej.inject.binding.Multibinder;
import io.activej.inject.binding.Multibinders;
import io.activej.inject.module.Module;
import io.activej.inject.module.ModuleBuilder;
import io.activej.inject.module.Modules;
import io.activej.launcher.Launcher;
import io.activej.launchers.http.MultithreadedHttpServerLauncher;
import io.activej.service.ServiceGraphModule;
import io.activej.service.adapter.ServiceAdapters;
import org.example.activej.modules.common.CommonModule;
import org.example.activej.modules.vehicles.VehicleModule;

public class WebApp extends MultithreadedHttpServerLauncher {

    public static final Multibinder<RoutingServlet> SERVLET_MULTIBINDER = Multibinders.ofBinaryOperator((servlet1, servlet2) ->
            servlet1.merge(servlet2));

    @Override
    protected Module getBusinessLogicModule() {
        return Modules.combine(
                CommonModule.create(),
                VehicleModule.create(),
                ModuleBuilder.create()
                        .bind(AsyncServlet.class).to(RoutingServlet.class)
                        .multibind(Key.of(RoutingServlet.class), SERVLET_MULTIBINDER)
                        .build()
        );
    }

    @Override
    protected Module getOverrideModule() {
        return Modules.combine(
                ServiceGraphModule.create().register(HikariDataSource.class, ServiceAdapters.combinedAdapter(
                        ServiceAdapters.forCloseable(),
                        ServiceAdapters.forDataSource()))
        );
    }

    public static void main(String[] args) throws Exception {
        Launcher launcher = new WebApp();
        launcher.launch(args);
    }
}