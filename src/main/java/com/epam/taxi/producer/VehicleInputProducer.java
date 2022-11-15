package com.epam.taxi.producer;

import com.epam.taxi.entity.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleInputProducer {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleInputProducer.class.getName());

    private final KafkaTemplate<String, Vehicle> kafkaTemplate;

    public VehicleInputProducer(KafkaTemplate<String, Vehicle> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Vehicle vehicle) {
        kafkaTemplate.send(topic, vehicle.getId(), vehicle);
        LOG.info("Producer sent vehicle {} to TOPIC {}", vehicle, topic);
    }
}
