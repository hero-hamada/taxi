package com.epam.taxi.consumer;

import com.epam.taxi.entity.VehicleDistance;
import com.epam.taxi.producer.VehicleInputProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaOutputConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleInputProducer.class.getName());
    private VehicleDistance vehiclePayload;

    @KafkaListener(topics = "${kafka.output.topic}", groupId = "${kafka.group.id}")
    public void listener(@Payload VehicleDistance vehicleDistance) {
        vehiclePayload = vehicleDistance;
        LOG.info("Vehicle with id {} passed distance {}", vehicleDistance.getId(), vehicleDistance.getDistance());
    }

    public VehicleDistance getVehiclePayload() {
        return vehiclePayload;
    }
}
