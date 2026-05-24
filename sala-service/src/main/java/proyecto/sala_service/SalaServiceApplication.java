package proyecto.sala_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class SalaServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalaServiceApplication.class, args);
	}

}
