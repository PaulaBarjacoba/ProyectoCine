package com.proyecto.cine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PeliculaRequestDTO {

    @NotBlank(message = "El titulo de la pelicula no puede estar vacio")
    private String titulo;

    @NotBlank(message = "La sinopsis no puede estar vacia")
    private String sinopsis;

    @NotNull(message = "La duración no puede estar vacia")
    @Min(value = 1, message = "La duración debe ser mayor a 0 minutos")
    private Integer duracionMinutos;

    @NotBlank(message = "La clasificación de edad no puede estar vacia")
    private String clasificacionEdad;

    @NotBlank(message = "El url no puede estar vacio")
    private String urlPoster;

    @NotNull(message = "Debe indicar si esta en cartelera (true/false)")
    private Boolean estadoCartelera;

}
