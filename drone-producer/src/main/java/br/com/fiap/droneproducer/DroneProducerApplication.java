package br.com.fiap.droneproducer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DroneProducerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DroneProducerApplication.class, args);
	}

}
