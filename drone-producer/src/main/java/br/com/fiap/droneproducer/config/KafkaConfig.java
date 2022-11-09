package br.com.fiap.droneproducer.config;

import br.com.fiap.droneproducer.model.Drone;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    String kafkaBootstrapServers;

    @Value("${spring.kafka.producer.key-serializer}")
    String kafkaKeySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    String kafkaValueSerializer;

    @Value("${spring.kafka.producer.client-id}")
    String kafkaClientId;

    @Value("${topic.name.producer}")
    String topicName;

    @Bean
    public ProducerFactory<Long, Drone> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaKeySerializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaValueSerializer);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaClientId);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(topicName, 1, (short) 1);
    }
}
