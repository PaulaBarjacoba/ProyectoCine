package proyecto.funcion_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients; // 1. Importa esto

@SpringBootApplication
@EnableFeignClients
public class FuncionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuncionServiceApplication.class, args);
	}
}