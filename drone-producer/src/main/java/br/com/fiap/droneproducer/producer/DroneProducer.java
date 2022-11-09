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

    private final KafkaTemplate<Long, String> kafkaTemplate;

    public void send(Drone drone, String mensagem) {
        log.info("Drone: ", drone.getId());
        log.info("Temperatura: ", drone.getTemperatura());
        log.info("Umidade: ", drone.getUmidade());
        log.info("Rastreamento ativado: ", drone.getRastrear());

        kafkaTemplate.send(topicName, mensagem);
    }
}
