package br.com.fiap.droneconsumer.consumer;

import br.com.fiap.droneconsumer.model.Drone;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class DroneConsumer {

    @Value("${topic.name.consumer}")
    String topicName;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaGroupId;

    @Value("${spring.kafka.consumer.client-id}")
    String kafkaClientId;

    @KafkaListener(topics = "${topic.name.consumer}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "droneListener")
    public void consume(Drone drone) {
        System.out.println(drone);
    }

}
