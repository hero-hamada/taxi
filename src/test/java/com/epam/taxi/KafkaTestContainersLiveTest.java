package com.epam.taxi;


import com.epam.taxi.consumer.KafkaInputConsumer;
import com.epam.taxi.consumer.KafkaOutputConsumer;
import com.epam.taxi.entity.VehicleSignal;
import com.epam.taxi.producer.VehicleInputProducer;
import com.epam.taxi.producer.VehicleOutputProducer;
import com.epam.taxi.storage.VehicleStorage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = com.epam.taxi.KafkaTestContainersLiveTest.KafkaTestContainersConfiguration.class)
@SpringBootTest(classes = TaxiApplication.class)
@TestPropertySource(locations="classpath:application.properties")
@DirtiesContext
public class KafkaTestContainersLiveTest {

    @ClassRule
    public static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"));

    @Value("${kafka.input.topic}")
    private String inputTopic;

    @Autowired
    private KafkaInputConsumer kafkaInputConsumer;

    @Autowired
    private KafkaOutputConsumer kafkaOutputConsumer;

    @Autowired
    private VehicleInputProducer vehicleInputProducer;

    @Autowired
    private VehicleStorage vehicleStorage;

    private VehicleSignal vehicle;

    @Test
    public void givenKafkaDockerContainer_whenSendingFirstTimeVehicleProducer_thenCorrectVehicleDistance() throws InterruptedException {
        // given
        vehicle = new VehicleSignal();
        vehicle.setId("XYZ7710469");
        vehicle.setX(0.0);
        vehicle.setY(100.0);

        // when
        vehicleInputProducer.send(inputTopic, vehicle);

        // then
        Thread.sleep(2000);
        assertThat(kafkaInputConsumer.getVehiclePayload())
                .as("Input consumer payload is not equal to passed one")
                .isEqualTo(vehicle);
        assertThat(vehicleStorage.get(vehicle.getId()))
                .as("Vehicle isn't stored correctly")
                .isEqualTo(vehicle);
        Thread.sleep(1000);
        assertThat(kafkaOutputConsumer.getVehiclePayload().getDistance())
                .as("Vehicle isn't stored correctly")
                .isEqualTo(0.0);
    }

    @Test
    public void givenKafkaDockerContainer_whenSendingSecondTimeVehicleProducer_thenCorrectVehicleDistance() throws InterruptedException {
        // given
        vehicle = new VehicleSignal();
        vehicle.setId("XYZ7779874");
        vehicle.setX(0.0);
        vehicle.setY(100.0);

        vehicleInputProducer.send(inputTopic, vehicle);
        vehicle.setX(100.0);

        // when
        vehicleInputProducer.send(inputTopic, vehicle);

        // then
        Thread.sleep(2000);
        assertThat(kafkaInputConsumer.getVehiclePayload()).isEqualTo(vehicle);
        assertThat(vehicleStorage.get(vehicle.getId())).isEqualTo(vehicle);
        Thread.sleep(1000);
        assertThat(kafkaOutputConsumer.getVehiclePayload().getDistance()).isEqualTo(100.0);
    }

    @TestConfiguration
    static class KafkaTestContainersConfiguration {

        @Value("${spring.kafka.bootstrap-servers}")
        private String bootstrapServers;

        public Map<String, Object> consumerConfig() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.springframework.kafka.support.serializer.JsonDeserializer.class);
            //  "at most once" consumer
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 100);
            return props;
        }

        @Bean
        public ConsumerFactory<String, VehicleSignal> consumerFactory() {
            return new DefaultKafkaConsumerFactory<>(consumerConfig());
        }

        @Bean
        public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, VehicleSignal>> factory(
                ConsumerFactory<String, VehicleSignal> consumerFactory
        ) {
            ConcurrentKafkaListenerContainerFactory<String, VehicleSignal> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory);
            factory.setBatchListener(true);
            return factory;
        }

        @Bean
        public Map<String, Object> producerConfig() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            // safe producer "at least once"
            props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
            props.put(ProducerConfig.ACKS_CONFIG, "all");
            props.put(ProducerConfig.RETRIES_CONFIG, String.valueOf(Integer.MAX_VALUE));
            props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "5");

            return props;
        }

        @Bean
        public ProducerFactory<String, VehicleSignal> producerFactory() {
            return new DefaultKafkaProducerFactory<>(producerConfig());
        }

        @Bean
        public KafkaTemplate<String, VehicleSignal> kafkaTemplate(ProducerFactory<String, VehicleSignal> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }
    }
}
