package com.epam.taxi.util;

import com.epam.taxi.entity.Vehicle;

public class VehicleUtil {
    public static double distance(Vehicle oldVehicle, Vehicle newVehicle) {
        return Math.hypot(oldVehicle.getX()-newVehicle.getX(), oldVehicle.getY()-newVehicle.getY());
    }
}
