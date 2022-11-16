package br.com.fiap.droneconsumer.config;

import br.com.fiap.droneconsumer.model.Drone;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
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

    @Value("${spring.kafka.producer.bootstrap-servers}")
    String kafkaProducerBootstrapServers;

    @Value("${spring.kafka.producer.client-id}")
    String kafkaProducerClientId;

    @Value("${spring.kafka.producer.key-serializer}")
    String kafkaProducerKeyDeserializer;

    @Value("${spring.kafka.producer.value-serializer}")
    String kafkaProducerValueSerializer;

    @Value("${topic.name.producer}")
    String topicNameProducer;

    @Bean
    public ConsumerFactory<Long, Drone> consumerFactory() {
        JsonDeserializer<Drone> droneJsonDeserializer = new JsonDeserializer<>(Drone.class);
        droneJsonDeserializer.setRemoveTypeHeaders(false);
        droneJsonDeserializer.addTrustedPackages("br.com.fiap.droneconsumer.model.Drone");
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
    public ConcurrentKafkaListenerContainerFactory<Long, Drone> droneListener() {
        ConcurrentKafkaListenerContainerFactory<Long, Drone> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<Long, Drone> producerFactory() {
        Map<String, Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProducerBootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerKeyDeserializer);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerValueSerializer);
        config.put(ProducerConfig.CLIENT_ID_CONFIG, kafkaProducerClientId);

        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(topicNameProducer, 1, (short) 1);
    }

}
