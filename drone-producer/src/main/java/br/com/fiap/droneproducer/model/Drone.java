package br.com.fiap.droneproducer.model;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Drone {

    @NotNull
    private Long id;
    @NotNull @Min(-90) @Max(90)
    private Double latitude;
    @NotNull @Min(-180) @Max(180)
    private Double longitude;
    // For a valid (latitude, longitude) pair: -90<=X<=+90 and -180<=Y<=180
    @NotNull @Min(-25) @Max(40)
    private Double temperatura;
    @NotNull @Min(0) @Max(100)
    private Double umidade;
    @NotNull
    private Boolean rastrear;

}
