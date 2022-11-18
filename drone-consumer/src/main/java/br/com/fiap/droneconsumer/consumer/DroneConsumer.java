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

    private final EmailProducer emailProducer;
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

    public static final Double TEMPERATURA_MINIMA_RECOMENDADA = 0.0;
    public static final Double TEMPERATURA_MAXIMA_RECOMENDADA = 35.0;
    public static final Double UMIDADE_MINIMA_RECOMENDADA = 15.0;
    public static final long DURACAO_MAXIMA_DRONE_EM_ALERTA = 1;

    @KafkaListener(topics = "${topic.name.consumer}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "droneListener")
    public void consume(Drone drone) {
        log.info(drone.toString());
        // Caso o drone esteja com o rastreamento desligado, o alerta não funcionará
        if (drone.getRastrear() &&
                (drone.getTemperatura() <= TEMPERATURA_MINIMA_RECOMENDADA ||
                        drone.getTemperatura() >= TEMPERATURA_MAXIMA_RECOMENDADA ||
                        drone.getUmidade() <= UMIDADE_MINIMA_RECOMENDADA)) {
            // O e-mail será enviado apenas se o primeiro alerta ocorreu a mais de um minuto atrás
            if (dronesAlerta.containsKey(drone.getId())) {
                LocalDateTime dataHoraPrimeiroAlerta = dronesAlerta.get(drone.getId());
                long duracaoAlertaMinutos = dataHoraPrimeiroAlerta.until(LocalDateTime.now(), ChronoUnit.MINUTES);
                if (duracaoAlertaMinutos >= DURACAO_MAXIMA_DRONE_EM_ALERTA) {
                    emailProducer.send(drone);
                    log.warn("Notifica por e-mail >> Drone " + drone.getId() + " a mais de " + DURACAO_MAXIMA_DRONE_EM_ALERTA + " min em situação de alerta!!!");
                    dronesAlerta.remove(drone.getId());
                }
            } else {
                dronesAlerta.put(drone.getId(), LocalDateTime.now());
                log.warn("Drone " + drone.getId() + " em alerta!");
            }
        } else {
            // Caso o drone esteja com rastreamento desligado ou está com temperatura e umidade dentro
            // das condições de normalidade estabelecidas, desativa-o de dronesAlerta (se houver)
            if (dronesAlerta.containsKey(drone.getId())) {
                log.warn("Drone " + drone.getId() + " fora da situação de alerta...");
            }
            dronesAlerta.remove(drone.getId());
        }
    }
}
