package br.com.fiap.droneproducer.controllers;

import br.com.fiap.droneproducer.model.Drone;
import br.com.fiap.droneproducer.producer.DroneProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drone")
public class DroneController {

    private DroneProducer droneProducer;
    public DroneController(DroneProducer droneProducer) {
        this.droneProducer = droneProducer;
    }

    @PostMapping("/publish")
    public String publishMessage(@RequestBody Drone drone) {
        droneProducer.send(drone);
        return "Mensagem enviada";
    }
}
