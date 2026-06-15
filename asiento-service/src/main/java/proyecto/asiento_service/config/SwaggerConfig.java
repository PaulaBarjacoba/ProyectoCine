package proyecto.asiento_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Asientos - Sistema de Cine")
                        .version("1.0")
                        .description("Documentación completa de los endpoints para la gestión y disponibilidad de las butacas de las salas del cine."));
    }
}
