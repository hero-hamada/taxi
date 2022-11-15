package com.epam.taxi.controller;

import com.epam.taxi.entity.Vehicle;
import com.epam.taxi.producer.VehicleInputProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    private final VehicleInputProducer vehicleInputProducer;

    @Value("${kafka.input.topic}")
    private String inputTopic;

    public VehicleController(VehicleInputProducer vehicleInputProducer) {
        this.vehicleInputProducer = vehicleInputProducer;
    }

    @PostMapping
    public void submit(@RequestBody Vehicle vehicle) {
        vehicleInputProducer.send(inputTopic, vehicle);
    }
}
