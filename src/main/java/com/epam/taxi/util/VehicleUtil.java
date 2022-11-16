package com.epam.taxi.util;

import com.epam.taxi.entity.VehicleSignal;

public class VehicleUtil {
    public static double distance(VehicleSignal oldVehicle, VehicleSignal newVehicle) {
        return Math.hypot(oldVehicle.getX()-newVehicle.getX(), oldVehicle.getY()-newVehicle.getY());
    }
}
