package org.example.activej.modules.vehicles.controller;

import io.activej.http.HttpRequest;
import io.activej.http.HttpResponse;
import io.activej.promise.Promise;
import org.example.activej.modules.vehicles.service.VehicleService;
import org.jetbrains.annotations.NotNull;

public class VehicleController {
    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @NotNull
    public Promise<HttpResponse> getYear(HttpRequest request) {
        return vehicleService.getYears().map(years -> HttpResponse.ok200().withJson(years));
    }
}
