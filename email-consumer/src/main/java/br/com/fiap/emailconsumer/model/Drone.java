package br.com.fiap.emailconsumer.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Drone {

    private Long id;
    private Double latitude;
    private Double longitude;
    private Double temperatura;
    private Double umidade;
    private Boolean rastrear;

}
