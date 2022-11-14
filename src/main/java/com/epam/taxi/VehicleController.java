package com.epam.taxi;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    private KafkaTemplate<String, Vehicle> kafkaTemplate;

    public VehicleController(KafkaTemplate<String, Vehicle> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    public void submit(@RequestBody Vehicle vehicle) {
        kafkaTemplate.send("input", vehicle);
    }
}
