package br.com.fiap.emailconsumer.consumer;

import br.com.fiap.emailconsumer.model.Drone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SendEmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(SendEmailConsumer.class);

    @Value("${topic.name.consumer}")
    String topicNameConsumer;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaConsumerGroupId;

    @Value("${spring.kafka.consumer.client-id}")
    String kafkaConsumerClientId;

    @KafkaListener(topics = "${topic.name.consumer}",
            groupId = "${spring.kafka.consumer.group-id}",
            clientIdPrefix = "${spring.kafka.consumer.client-id}",
            containerFactory = "sendEmailListener")
    public void consume(Drone drone) {
        log.info(drone.toString());
    }
}
