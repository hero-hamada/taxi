package com.epam.taxi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.input.topic}")
    private String input;

    @Value("${kafka.output.topic}")
    private String output;

    @Bean
    public NewTopic inputTopic() {
        return TopicBuilder.name(input)
                .partitions(5)
                .build();
    }

    @Bean
    public NewTopic outputTopic() {
        return TopicBuilder.name(output)
                .partitions(5)
                .build();
    }
}
