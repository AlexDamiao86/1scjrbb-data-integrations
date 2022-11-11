package br.com.fiap.droneproducer.config.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class DroneError {

    private String campo;
    private String mensagem;
}
