package proyecto.notificacion_service.config;

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
                        .title("API Notificación - Sistema de Cine")
                        .version("1.0")
                        .description("Documentación de los endpoints para el registro y trazabilidad de notificaciones enviadas a los usuarios."));
    }
}
