package br.com.fiap.droneconsumer.consumer;

import br.com.fiap.droneconsumer.model.Drone;
import br.com.fiap.droneconsumer.producer.EmailProducer;
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

    private EmailProducer emailProducer;
    public DroneConsumer(EmailProducer emailProducer) {
        this.emailProducer = emailProducer;
    }

    private static final Logger log = LoggerFactory.getLogger(DroneConsumer.class);
    private final Map<Long, LocalDateTime> dronesAlerta = new HashMap<>();

    @Value("${topic.name.consumer}")
    String topicNameConsumer;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaConsumerGroupId;

    @Value("${spring.kafka.consumer.client-id}")
    String kafkaConsumerClientId;

    @KafkaListener(topics = "${topic.name.consumer}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "droneListener")
    public void consume(Drone drone) {
        log.info(drone.toString());
        // Caso o drone esteja com o rastreamento desligado, o alerta não funcionará
        if (drone.getRastrear() &&
                (drone.getTemperatura() <= 0 || drone.getTemperatura() >= 35 ||
                        drone.getUmidade() <= 15)) {
            // O e-mail será enviado apenas se o primeiro alerta ocorreu a mais de um minuto atrás
            if (dronesAlerta.containsKey(drone.getId())) {
                LocalDateTime primeiroAlerta = dronesAlerta.get(drone.getId());
                long tempoPrimeiroAlerta = primeiroAlerta.until(LocalDateTime.now(), ChronoUnit.MINUTES);
                if (tempoPrimeiroAlerta >= 1) {
                    emailProducer.send(drone);
                    log.warn("Notifica por e-mail >> Drone " + drone.getId() + " a mais de 1 min em situação de alerta!!!");
                    dronesAlerta.remove(drone.getId());
                }
            } else {
                dronesAlerta.put(drone.getId(), LocalDateTime.now());
                log.warn("Drone " + drone.getId() + " em alerta!");
            }
        }
    }
}
