package br.com.fiap.droneconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class DroneAlerta {

    private Long id;
    private Double latitude;
    private Double longitude;
    private Double temperatura;
    private Double umidade;
    private Boolean rastrear;
    private LocalDateTime dataHoraRecebido;

    public DroneAlerta(Drone drone) {
        this.id = drone.getId();
        this.latitude = drone.getLatitude();
        this.latitude = drone.getLatitude();
        this.temperatura = drone.getTemperatura();
        this.umidade = drone.getTemperatura();
        this.rastrear = drone.getRastrear();
        this.dataHoraRecebido = LocalDateTime.now();
    }
}
