package com.proyecto.cine.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "pelicula")

public class Pelicula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pelicula")
    private Integer idPelicula;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "sinopsis", columnDefinition = "TEXT", nullable = false)
    private String sinopsis;

    @Column(name = "duracion_minutos", nullable = false)
    private Integer duracionMinutos;

    @Column(name = "clasificacion_edad", length = 12, nullable = false)
    private String clasificacionEdad;

    @Column(name = "url_poster", length = 300)
    private String urlPoster;

    @Column(name = "estado_cartelera", nullable = false)
    private Boolean estadoCartelera;

}
