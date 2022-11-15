package com.epam.taxi.listener;

import com.epam.taxi.entity.Vehicle;
import com.epam.taxi.producer.VehicleInputProducer;
import com.epam.taxi.producer.VehicleOutputProducer;
import com.epam.taxi.storage.VehicleStorage;
import com.epam.taxi.util.VehicleUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class KafkaInputListeners {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleInputProducer.class.getName());

    private final VehicleStorage vehicleStorage;
    private final VehicleOutputProducer vehicleOutputProducer;

    @Value("${kafka.output.topic}")
    private String outputTopic;

    public KafkaInputListeners(VehicleStorage vehicleStorage, VehicleOutputProducer vehicleOutputProducer) {
        this.vehicleStorage = vehicleStorage;
        this.vehicleOutputProducer = vehicleOutputProducer;
    }

    @KafkaListener(topics = "${kafka.input.topic}", groupId = "${kafka.group.id}")
    void listener(@Payload Vehicle vehicle) {
        vehicle.setDistance(Objects.nonNull(vehicleStorage.get(vehicle.getId())) ?
                    VehicleUtil.distance(vehicleStorage.get(vehicle.getId()), vehicle) : 0);
        vehicleOutputProducer.send(outputTopic, vehicle);
        vehicleStorage.save(vehicle);
        LOG.info("Vehicle {} saved", vehicle);
    }
}
