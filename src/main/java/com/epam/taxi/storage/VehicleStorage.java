package com.epam.taxi.storage;

import com.epam.taxi.entity.Vehicle;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class VehicleStorage {
    private final Map<String, Vehicle> vehicles = new ConcurrentHashMap<>();

    public void save(Vehicle vehicle) {
        vehicles.put(vehicle.getId(), vehicle);
    }

    public Vehicle get(String id) {
        return vehicles.get(id);
    }
}
