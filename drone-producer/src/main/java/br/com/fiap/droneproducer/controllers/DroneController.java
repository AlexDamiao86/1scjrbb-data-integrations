package br.com.fiap.droneproducer.controllers;

import br.com.fiap.droneproducer.model.Drone;
import br.com.fiap.droneproducer.producer.DroneProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/drone")
public class DroneController {

    private final DroneProducer droneProducer;

    @GetMapping
    public void send() {
        Drone drone = new Drone(1232344L, -90., -180., 35.4, 23., true);
        String mensagem = "mensagem de um drone";
        droneProducer.send(drone, mensagem);
    }
}
