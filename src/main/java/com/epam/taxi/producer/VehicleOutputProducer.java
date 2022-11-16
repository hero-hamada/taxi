package com.epam.taxi.producer;

import com.epam.taxi.entity.VehicleDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class VehicleOutputProducer {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleOutputProducer.class.getName());

    private final KafkaTemplate<String, VehicleDistance> kafkaTemplate;

    public VehicleOutputProducer(KafkaTemplate<String, VehicleDistance> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(String topic, VehicleDistance vehicle) {
        kafkaTemplate.send(topic, vehicle.getId(), vehicle);
        LOG.info("Producer sent distance {} m of vehicle with id {} to TOPIC {}", vehicle.getDistance(), vehicle.getId(), topic);
    }
}
