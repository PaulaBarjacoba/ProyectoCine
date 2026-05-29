package com.proyecto.cine.repository;

import com.proyecto.cine.model.Pelicula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {

    boolean existsByTitulo(String titulo);

    List<Pelicula> findByClasificacionEdad(String clasificacionEdad);

    }

