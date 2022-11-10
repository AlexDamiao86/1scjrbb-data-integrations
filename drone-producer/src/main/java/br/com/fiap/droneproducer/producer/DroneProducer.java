package br.com.fiap.droneproducer.producer;

import br.com.fiap.droneproducer.model.Drone;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneProducer {

    @Value("${topic.name.producer}")
    private String topicName;

    private final KafkaTemplate<Long, Drone> kafkaTemplate;

    public void send(Drone drone) {
        System.out.println(drone);
        kafkaTemplate.send(topicName, drone.getId(), drone);
    }
}
