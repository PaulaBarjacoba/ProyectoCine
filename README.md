# Sistema de Gestión de Cine - Arquitectura de Microservicios


Sistema de gestión integral para cines, desarrollado bajo una arquitectura distribuida de microservicios utilizando Spring Boot. El sistema permite la administración completa del negocio de forma descentralizada, cubriendo areas como la gestión de usuarios, cartelera, programación de funciones, control de salas y butacas, sistema de reservas, venta de confitería y notificaciones.

Proyecto desarrollado para la asignatura **Desarrollo Fullstack 1**.

## Integrantes
| Nombre | GitHub |
| :--- | :--- |
| Paula Barjacoba | [@PaulaBarjacoba](https://github.com/PaulaBarjacoba) |
| Vicente Guzmán | [@vguzc](https://github.com/vguzc) |

---

## Arquitectura del Sistema

El proyecto está estructurado cumpliendo los **10 microservicios**, más la infraestructura de red necesaria para su operación. Todo el código está organizado bajo el patrón de diseño **CSR (Controller-Service-Repository)**, asegurando una separación real de responsabilidades.

### Areas del Negocio Cubiertos

* **Autenticación y Usuarios:** Gestión de credenciales, roles y perfiles.
* **Catálogo y Programación:** Administración de películas, salas físicas y horarios de funciones.
* **Ventas y Reservas:** Control de disponibilidad de butacas espaciales, generación de reservas de entradas y cálculo de totales.
* **Confitería y Órdenes:** Catálogo de snacks, control de stock y vinculación de pedidos de comida a los asistentes.
* **Notificaciones:** Registro de trazabilidad y alertas de confirmación para el usuario final.

---

## Tecnologías Utilizadas

| Tecnología | Versión | Uso en el Sistema |
| :--- | :--- | :--- |
| **Java** | 21 | Lenguaje de programación principal |
| **Spring Boot** | - | Framework base para el desarrollo de los microservicios |
| **Spring Cloud Eureka** | -| Servidor de registro y descubrimiento dinámico |
| **Spring Cloud OpenFeign** | -| Comunicación REST síncrona entre microservicios |
| **Spring Data JPA** | - | Capa de persistencia de datos |
| **Hibernate** | - | ORM (Object-Relational Mapping) |
| **MySQL** | - | Motor de base de datos relacional (Esquemas independientes) |
| **Lombok** | - | Reducción de código boilerplate (Getters, Setters, Constructores) |
| **Bean Validation (JSR 380)**| - | Validación estricta de datos de entrada mediante DTOs |
| **SLF4J** | - | Trazabilidad y logs estructurados de eventos de negocio |
| **Git / GitHub** | - | Control de versiones y trabajo colaborativo |

---

## Características Técnicas Destacadas

1. **Comunicación entre Microservicios:**
   Los servicios no utilizan URLs fijas en el código. Se implementó **OpenFeign** junto con **Eureka Server** para que los microservicios se descubran dinámicamente por su nombre. Esto permite validar reglas de negocio complejas de forma distribuida (por ejemplo, verificar que una función y un usuario existan antes de procesar una reserva).

2. **Validaciones e Integridad de Datos:**
   Se utiliza **Bean Validation** con anotaciones (como `@NotNull`, `@NotBlank`, etc.) en la capa de los controladores, separando limpiamente las Entidades de los DTOs. El sistema rechaza peticiones con datos inválidos antes de que lleguen a la capa de servicio o base de datos.

3. **Manejo de Excepciones y Respuestas HTTP:**
   Las APIs responden exclusivamente con JSON estructurado. Se implementó el uso correcto de `ResponseEntity` para retornar los códigos HTTP semánticos adecuados (200 OK, 201 Created, 400 Bad Request, 500 Internal Server Error) dependiendo del resultado de las operaciones y las reglas de negocio.

4. **Trazabilidad y Monitoreo (Logs):**
   Uso intensivo de **SLF4J** en las capas estratégicas del código. Se registran eventos relevantes (como el inicio de una transacción de reserva o la falla en la búsqueda de un registro) garantizando la trazabilidad entre capas y facilitando la depuración.

5. **Persistencia Real y Modelado:**
   Cada microservicio cuenta con su propia base de datos normalizada. Se gestionan correctamente las relaciones (ej. `@ManyToOne`, `@OneToMany`), el uso de JpaRepository para las operaciones CRUD, y se asegura la integridad referencial.

---

## Listado de Microservicios Implementados

El sistema consta de **10 microservicios funcionales independientes** y **2 servicios de infraestructura**, cada uno configurado con su propia base de datos aislada y puerto específico:

| Servicio | Tipo | Puerto | Base de Datos | Descripción |
| :--- | :--- | :--- | :--- | :--- |
| **eureka-server** | Infraestructura | `8761` | *N/A* | Servidor de registro y descubrimiento de servicios |
| **api-gateway** | Infraestructura | `8080` | *N/A* | Gateway y punto de entrada centralizado con rutas YAML |
| **pelicula-service** | Funcional | `8081` | `db_peliculas` | Administración del catálogo de películas y cartelera |
| **usuario-service** | Funcional | `8082` | `db_usuarios` | Gestión de usuarios, perfiles y credenciales de acceso |
| **auth-service** | Funcional | `8083` | `db_auth` | Emisión de tokens JWT, roles y validación de sesiones |
| **sala-service** | Funcional | `8084` | `db_salas` | Administración de salas físicas y sus configuraciones |
| **funcion-service** | Funcional | `8085` | `db_funciones` | Programación de funciones, asignación de películas y horarios |
| **asiento-service** | Funcional | `8086` | `db_asientos` | Control y estados de butacas (Disponibles, Ocupados) |
| **reserva-service** | Funcional | `8087` | `db_reservas` | Creación de reservas de entradas y cálculo de totales |
| **snack-service** | Funcional | `8088` | `db_snacks` | Gestión del inventario y catálogo de confitería |
| **orden-service** | Funcional | `8089` | `db_ordenes` | Compra de confitería y vinculación de tickets a reservas |
| **notificacion-service** | Funcional | `8090` | `db_notificaciones` | Historial de alertas y correos de confirmación al usuario |

---

## Configuración y Enrutamiento del API Gateway

El **API Gateway** unifica los puntos de entrada para el cliente en el puerto `8080` usando rutas declaradas en [application.yml](file:///c:/Users/Asus/IdeaProjects/ProyectoCineFullstack/api-gateway/src/main/resources/application.yml):

* **Rutas del Catálogo y Programación:**
  * Películas: `http://localhost:8080/api/v1/peliculas/**` -> redirigido a `pelicula-service`
  * Salas: `http://localhost:8080/api/v1/salas/**` -> redirigido a `sala-service`
  * Funciones: `http://localhost:8080/api/v1/funciones/**` -> redirigido a `funcion-service`
  * Asientos: `http://localhost:8080/api/v1/asientos/**` -> redirigido a `asiento-service`

* **Rutas de Autenticación y Cuentas:**
  * Usuarios: `http://localhost:8080/api/v1/usuarios/**` -> redirigido a `usuario-service`
  * Autenticación / Login: `http://localhost:8080/api/v1/auth/**` -> redirigido a `auth-service`

* **Rutas de Negocio y Transacciones:**
  * Reservas: `http://localhost:8080/api/v1/reservas/**` -> redirigido a `reserva-service`
  * Snacks: `http://localhost:8080/api/v1/snacks/**` -> redirigido a `snack-service`
  * Órdenes: `http://localhost:8080/api/v1/ordenes/**` -> redirigido a `orden-service`
  * Notificaciones: `http://localhost:8080/api/v1/notificaciones/**` -> redirigido a `notificacion-service`

---

## Enlaces a la Documentación Técnica (Swagger UI)

Cada microservicio expone de forma interactiva la especificación Swagger/OpenAPI de sus endpoints en su respectivo puerto local:

* 📄 **pelicula-service:** [Swagger UI](http://localhost:8081/swagger-ui/index.html)
* 📄 **usuario-service:** [Swagger UI](http://localhost:8082/swagger-ui/index.html)
* 📄 **auth-service:** [Swagger UI](http://localhost:8083/swagger-ui/index.html)
* 📄 **sala-service:** [Swagger UI](http://localhost:8084/swagger-ui/index.html)
* 📄 **funcion-service:** [Swagger UI](http://localhost:8085/swagger-ui/index.html)
* 📄 **asiento-service:** [Swagger UI](http://localhost:8086/swagger-ui/index.html)
* 📄 **reserva-service:** [Swagger UI](http://localhost:8087/swagger-ui/index.html)
* 📄 **snack-service:** [Swagger UI](http://localhost:8088/swagger-ui/index.html)
* 📄 **orden-service:** [Swagger UI](http://localhost:8089/swagger-ui/index.html)
* 📄 **notificacion-service:** [Swagger UI](http://localhost:8090/swagger-ui/index.html)

---

## Instrucciones de Ejecución Local y Remota

### Requisitos previos:
* **Java Development Kit (JDK)** versión 21.
* **MySQL Server** en ejecución en `localhost:3306`. Las bases de datos necesarias para cada microservicio se crearán automáticamente al arrancar la aplicación si se usa el usuario `root` sin contraseña (configuración por defecto).

### Pasos para ejecución local:

1. **Paso 1: Clonar e Importar el Proyecto**
   Importa el proyecto raíz como un proyecto Maven en IntelliJ IDEA o VS Code.

2. **Paso 2: Compilar el Proyecto Completo**
   Desde la raíz del proyecto, ejecuta el siguiente comando en la terminal para compilar todos los microservicios a la vez:
   ```bash
   .\mvnw.cmd clean compile
   ```

3. **Paso 3: Arrancar Eureka Server**
   Inicia primero el microservicio `eureka-server` ejecutando su clase principal `EurekaServerApplication.java` o usando Maven Wrapper:
   ```bash
   .\mvnw.cmd spring-boot:run -pl eureka-server
   ```
   Accede a la consola de Eureka en [http://localhost:8761](http://localhost:8761) y asegúrate de que esté listo.

4. **Paso 4: Arrancar los 10 Microservicios Funcionales**
   Inicia cada uno de los microservicios funcionales en cualquier orden. A medida que arranquen, Flyway creará automáticamente sus esquemas de base de datos (`db_auth`, `db_usuarios`, etc.) y las tablas requeridas.
   Verifica en la consola de Eureka que todos los servicios estén correctamente registrados (deberán aparecer en verde).

5. **Paso 5: Arrancar el API Gateway**
   Por último, arranca `api-gateway`. Este servirá como el proxy de entrada en el puerto `8080`:
   ```bash
   .\mvnw.cmd spring-boot:run -pl api-gateway
   ```

### Pasos para ejecución remota (Despliegue):

* **Docker (Contenedores):** Cada servicio contiene su respectivo `.mvn` y archivo descriptor para empaquetarse en una imagen Docker. Se pueden generar los JARs listos ejecutando `.\mvnw.cmd package -DskipTests` y construir contenedores de manera independiente.
* **Railway / Render:** Configura variables de entorno para anular las propiedades de base de datos en producción (`spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`) apuntando a tu instancia de base de datos en la nube.
