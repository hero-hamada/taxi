package com.epam.taxi;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class KafkaListeners {

    @KafkaListener(topics = "${kafka.input.topic}", groupId = "${kafka.group.id}")
    void listener(List<Vehicle> vehicles) {
        System.out.println(vehicles + " 🎉 ");
    }
}
