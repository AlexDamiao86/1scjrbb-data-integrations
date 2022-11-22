package br.com.fiap.droneproducer.service;

import br.com.fiap.droneproducer.model.Drone;
import br.com.fiap.droneproducer.producer.DroneProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    private final DroneProducer droneProducer;
    public Scheduler(DroneProducer droneProducer) {
        this.droneProducer = droneProducer;
    }

    private Double temp = 12.0;
    private static final Double MAX_TEMP = 40.0;

    private Double umidade = 100.0;
    private static final Double MIN_UMIDADE = 10.0;

    private boolean rastreamento = false;

    @Scheduled(fixedRate = 10000)
    public void sendMessage() {
        if (temp <= MAX_TEMP) temp++;
        Drone drone1 = new Drone(9999999L, -80.54, 120.87, temp, 50.0, true);
        System.out.println("Auto drone " + drone1.getId() + " enviou mensagem.. ");
        droneProducer.send(drone1);

        if (umidade >= MIN_UMIDADE) umidade--;
        Drone drone2 = new Drone(8888888L, -72.23, 170.87, 34.0, umidade, true);
        System.out.println("Auto drone " + drone2.getId() + " enviou mensagem.. ");
        droneProducer.send(drone2);

        rastreamento = !rastreamento;
        Drone drone3 = new Drone(7777777L, 56.78, 102.87, temp, umidade, rastreamento);
        System.out.println("Auto drone " + drone3.getId() + " enviou mensagem.. ");
        droneProducer.send(drone3);
    }
}
