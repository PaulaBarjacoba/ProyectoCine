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
