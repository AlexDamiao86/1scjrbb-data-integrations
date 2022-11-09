package br.com.fiap.droneproducer.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class Drone {

    private Long id;
    private Double latitude;
    private Double longitude;
    // For a valid (latitude, longitude) pair: -90<=X<=+90 and -180<=Y<=180
    private Double temperatura;
    private Double umidade;
    private Boolean rastrear;

}
