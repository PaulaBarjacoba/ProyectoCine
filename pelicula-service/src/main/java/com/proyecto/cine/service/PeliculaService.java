package com.proyecto.cine.service;

import com.proyecto.cine.model.Pelicula;
import com.proyecto.cine.repository.PeliculaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class PeliculaService {
    //hola :)

    @Autowired
    private PeliculaRepository peliculaRepository;

    //GET
    public List<Pelicula> findAll() {
        return peliculaRepository.findAll();
    }

    //POST
    public Pelicula save(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }
    // buscar x ID
    public Pelicula findById(Integer id) {
        return peliculaRepository.findById(id).orElse(null);
    }


    // DELETE
    public boolean eliminar(int id) {
        if (peliculaRepository.existsById(id)) {
            peliculaRepository.deleteById(id);
            return true;
        }
        return false;

    }
}

