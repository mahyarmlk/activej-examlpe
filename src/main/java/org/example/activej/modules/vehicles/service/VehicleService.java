package org.example.activej.modules.vehicles.service;

import io.activej.promise.Promise;

public interface VehicleService {

    Promise<String> getYears();
}
