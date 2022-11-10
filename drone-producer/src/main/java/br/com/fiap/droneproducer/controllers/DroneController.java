package br.com.fiap.droneproducer.controllers;

import br.com.fiap.droneproducer.model.Drone;
import br.com.fiap.droneproducer.producer.DroneProducer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/drone")
public class DroneController {

    private DroneProducer droneProducer;
    public DroneController(DroneProducer droneProducer) {
        this.droneProducer = droneProducer;
    }

    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publishMessage(@RequestBody @Valid Drone drone) {
        droneProducer.send(drone);
    }
}
