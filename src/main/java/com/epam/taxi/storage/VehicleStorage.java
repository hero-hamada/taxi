package com.epam.taxi.storage;

import com.epam.taxi.entity.VehicleSignal;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VehicleStorage {
    private final Map<String, VehicleSignal> vehicles = new ConcurrentHashMap<>();

    public void save(VehicleSignal vehicle) {
        vehicles.put(vehicle.getId(), vehicle);
    }

    public VehicleSignal get(String id) {
        return vehicles.get(id);
    }
}
