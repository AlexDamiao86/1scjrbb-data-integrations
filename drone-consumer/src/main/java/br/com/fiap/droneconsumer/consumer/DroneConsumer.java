package br.com.fiap.droneconsumer.consumer;

import br.com.fiap.droneconsumer.model.Drone;
import br.com.fiap.droneconsumer.model.DroneAlerta;
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
    private Map<Long, DroneAlerta> dronesAlerta = new HashMap<Long, DroneAlerta>();

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
                DroneAlerta droneAlerta = dronesAlerta.get(drone.getId());
                long minutos = droneAlerta.getDataHoraRecebido().until(LocalDateTime.now(), ChronoUnit.MINUTES);
                if (minutos >= 1) {
                    log.warn("Enviado e-mail: " + drone);
                    dronesAlerta.remove(drone.getId());
                }
            } else {
                DroneAlerta droneAlerta = new DroneAlerta(drone);
                dronesAlerta.put(droneAlerta.getId(), droneAlerta);
                log.warn("Inserido drone " + droneAlerta.getId() + " no alerta");
            }
        }
    }

}
