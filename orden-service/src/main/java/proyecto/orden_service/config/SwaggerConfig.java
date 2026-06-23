package proyecto.orden_service.config;

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
                        .title("API Órdenes - Sistema de Cine")
                        .version("1.0")
                        .description("Documentación de los endpoints para compras de confitería, detalle de órdenes y reducción de stock."));
    }
}
