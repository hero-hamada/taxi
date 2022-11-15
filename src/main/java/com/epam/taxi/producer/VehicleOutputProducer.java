package com.epam.taxi.producer;

import com.epam.taxi.entity.Vehicle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleOutputProducer {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleOutputProducer.class.getName());

    private final KafkaTemplate<String, Vehicle> kafkaTemplate;

    public VehicleOutputProducer(KafkaTemplate<String, Vehicle> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, Vehicle vehicle) {
        kafkaTemplate.send(topic, vehicle.getId(), vehicle);
        LOG.info("Producer sent distance {} m of vehicle with id {} to TOPIC {}", vehicle.getDistance(), vehicle.getId(), topic);
    }
}
