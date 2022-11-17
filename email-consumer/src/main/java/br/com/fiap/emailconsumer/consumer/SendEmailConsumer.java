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

    private static EmailService emailService;

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
        String emailText = "Drone " + drone.getId() + " está em alerta a mais de um minuto devido temperatura ou umidade extremas (de risco).\n"
                + "Temperatura: " + drone.getTemperatura() + "ºC \n"
                + "Umidade: " + drone.getUmidade() + "% \n"
                + "Localização (latitude, longitude): (" + drone.getLatitude() + " , " + drone.getLongitude() + ")\n\n"
                + "<i><b>Favor verificar se poderá acionar instrumentos para amenizar clima extremo</b></i>";

        emailService.sendSimpleMessage(emailTo, "Drone " + drone.getId() + " alerta!", emailText);
    }
}
