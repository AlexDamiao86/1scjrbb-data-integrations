package br.com.fiap.droneconsumer.producer;

import br.com.fiap.droneconsumer.model.Drone;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailProducer {

    @Value("${topic.name.producer}")
    private String topicName;

    private final KafkaTemplate<Long, Drone> kafkaTemplate;

    public void send(Drone drone) {
        kafkaTemplate.send(topicName, drone.getId(), drone);
    }
}
