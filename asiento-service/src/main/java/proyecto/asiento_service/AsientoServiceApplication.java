package proyecto.asiento_service; // Asegúrate de que el paquete coincida con el tuyo

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class AsientoServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsientoServiceApplication.class, args);
	}

}