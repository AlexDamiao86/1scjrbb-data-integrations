package br.com.fiap.droneproducer.controllers;

import br.com.fiap.droneproducer.model.Drone;
import br.com.fiap.droneproducer.producer.DroneProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/drone")
@Tag(name="/drone", description = "Gerencia Drones")
public class DroneController {

    private DroneProducer droneProducer;
    public DroneController(DroneProducer droneProducer) {
        this.droneProducer = droneProducer;
    }

    @Operation(summary = "Recebe dados de um drone",
            description = "Recebe dados de um drone, valida e publica mensagem em tópico drone-data")
    @PostMapping(value = "/publish", consumes = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void publishMessage(@RequestBody @Valid Drone drone) {
        droneProducer.send(drone);
    }
}
