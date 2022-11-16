package br.com.fiap.emailconsumer.config;

import br.com.fiap.emailconsumer.model.Drone;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {


    @Value("${spring.kafka.consumer.bootstrap-servers}")
    String kafkaConsumerBootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaConsumerGroupId;

    @Value("${spring.kafka.consumer.client-id}")
    String kafkaConsumerClientId;

    @Value("${spring.kafka.consumer.key-deserializer}")
    String kafkaConsumerKeyDeserializer;

    @Bean
    public ConsumerFactory<Long, Drone> consumerFactory() {
        JsonDeserializer<Drone> droneJsonDeserializer = new JsonDeserializer<>(Drone.class);
        droneJsonDeserializer.setRemoveTypeHeaders(false);
        droneJsonDeserializer.addTrustedPackages("br.com.fiap.emailconsumer.model.Drone");
        droneJsonDeserializer.setUseTypeMapperForKey(true);

        Map<String, Object> config = new HashMap<>();

        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaConsumerBootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaConsumerGroupId);
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, kafkaConsumerClientId);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerKeyDeserializer);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, droneJsonDeserializer);

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return new DefaultKafkaConsumerFactory<>(config, new LongDeserializer(), droneJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Long, Drone> sendEmailListener() {
        ConcurrentKafkaListenerContainerFactory<Long, Drone> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

