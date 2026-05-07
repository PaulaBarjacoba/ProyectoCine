package com.proyecto.cine.dto;

import lombok.Data;

@Data
public class PeliculaResponseDTO {

    private Integer idPelicula;
    private String titulo;
    private String sinopsis;
    private Integer duracionMinutos;
    private String clasificacionEdad;
    private String urlPoster;
    private Boolean estadoCartelera;
}
