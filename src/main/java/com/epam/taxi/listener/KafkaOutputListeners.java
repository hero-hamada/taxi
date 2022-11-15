package com.epam.taxi.listener;

import com.epam.taxi.entity.Vehicle;
import com.epam.taxi.producer.VehicleInputProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaOutputListeners {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleInputProducer.class.getName());

    @KafkaListener(topics = "${kafka.output.topic}", groupId = "${kafka.group.id}")
    void listener(@Payload Vehicle vehicle) {
        LOG.info("Vehicle with id {} passed distance {}", vehicle.getId(), vehicle.getDistance());
    }
}
