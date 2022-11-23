package br.com.fiap.emailconsumer.consumer;

import br.com.fiap.emailconsumer.model.Drone;
import br.com.fiap.emailconsumer.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SendEmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(SendEmailConsumer.class);

    private final EmailService emailService;

    @Value("${topic.name.consumer}")
    String topicNameConsumer;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaConsumerGroupId;

    @Value("${spring.kafka.consumer.client-id}")
    String kafkaConsumerClientId;

    @Value("${email.to}")
    String emailTo;

    public SendEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "${topic.name.consumer}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "sendEmailListener")
    public void consume(Drone drone) {
        log.info(drone.toString());
        String emailText = "Drone " + drone.getId() + " esta em alerta a mais de 1 minuto devido temperatura ou umidade fora das condicoes normais.\n"
                + "Temperatura: " + drone.getTemperatura() + "C \n"
                + "Umidade: " + drone.getUmidade() + "% \n"
                + "Localizacao (latitude, longitude): (" + drone.getLatitude() + " , " + drone.getLongitude() + ")\n\n"
                + "Favor verificar se podera acionar instrumentos para atuar nas condicoes do clima.";

        emailService.sendSimpleMessage(emailTo, "Drone " + drone.getId() + " alerta!", emailText);
    }
}
