package br.com.fiap.droneconsumer.consumer;

import br.com.fiap.droneconsumer.model.Drone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Service
public class DroneConsumer {

    private static final Logger log = LoggerFactory.getLogger(DroneConsumer.class);
    private final Map<Long, LocalDateTime> dronesAlerta = new HashMap<>();

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
        log.info("Evento recebido: " + drone);

        // Caso o drone esteja com o rastreamento desligado, o alerta não funcionará
        if (drone.getRastrear() &&
                (drone.getTemperatura() <= 0 || drone.getTemperatura() >= 35 ||
                        drone.getUmidade() <= 15)) {
            // O e-mail será enviado apenas se houver o primeiro alerta ocorreu a mais de um minuto atrás
            if (dronesAlerta.containsKey(drone.getId())) {
                LocalDateTime primeiroAlerta = dronesAlerta.get(drone.getId());
                long tempoPrimeiroAlerta = primeiroAlerta.until(LocalDateTime.now(), ChronoUnit.MINUTES);
                if (tempoPrimeiroAlerta >= 1) {
                    log.warn("Enviado e-mail: " + drone);
                    dronesAlerta.remove(drone.getId());
                }
            } else {
                dronesAlerta.put(drone.getId(), LocalDateTime.now());
                log.warn("Drone " + drone.getId() + " em alerta!");
            }
        }
    }
}
