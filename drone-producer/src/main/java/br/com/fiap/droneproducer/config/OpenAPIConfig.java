package br.com.fiap.droneproducer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Drone Monitor",
                description = "API disponibilizada para gerenciar sistema de drones.",
                contact = @Contact(
                        name = "GitHub",
                        url = "https://github.com/AlexDamiao86/1scjrbb-data-integrations"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://api.github.com/licenses/mit"
                )
        )
)
public class OpenAPIConfig {
}
