package com.proyecto.cine.service;

import com.proyecto.cine.dto.PeliculaRequestDTO;
import com.proyecto.cine.dto.PeliculaResponseDTO;
import com.proyecto.cine.model.Pelicula;
import com.proyecto.cine.repository.PeliculaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculaServiceTest {

    @Mock
    private PeliculaRepository peliculaRepository;

    @InjectMocks
    private PeliculaService peliculaService;

    private Pelicula peliculaPrueba;
    private PeliculaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {

        peliculaPrueba = new Pelicula();
        peliculaPrueba.setIdPelicula(1);
        peliculaPrueba.setTitulo("Spider-Man: Across the Spider-Verse");
        peliculaPrueba.setSinopsis("Aventura en el multiverso.");
        peliculaPrueba.setDuracionMinutos(140);
        peliculaPrueba.setClasificacionEdad("TE");
        peliculaPrueba.setUrlPoster("https://link.com/spiderman.jpg");
        peliculaPrueba.setEstadoCartelera(true);

        requestDTO = new PeliculaRequestDTO();
        requestDTO.setTitulo("Spider-Man: Across the Spider-Verse");
        requestDTO.setSinopsis("Aventura en el multiverso.");
        requestDTO.setDuracionMinutos(140);
        requestDTO.setClasificacionEdad("TE");
        requestDTO.setUrlPoster("https://link.com/spiderman.jpg");
        requestDTO.setEstadoCartelera(true);
    }

    @Test
    void listarTodas_TestListaPeliculasDTOSiExisten() {

        when(peliculaRepository.findAll()).thenReturn(Arrays.asList(peliculaPrueba));

        List<PeliculaResponseDTO> resultado = peliculaService.listarTodas();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Spider-Man: Across the Spider-Verse", resultado.get(0).getTitulo());
        verify(peliculaRepository, times(1)).findAll();
    }

    @Test
    void buscarPorId_TestRetornarDTOSiPeliculaExiste() {
        when(peliculaRepository.findById(1)).thenReturn(Optional.of(peliculaPrueba));

        PeliculaResponseDTO resultado = peliculaService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals("Spider-Man: Across the Spider-Verse", resultado.getTitulo());
        assertEquals(140, resultado.getDuracionMinutos());
        verify(peliculaRepository, times(1)).findById(1);
    }

    @Test
    void buscarPorId_TestNullCuandoPeliculaNoExiste() {
        when(peliculaRepository.findById(99)).thenReturn(Optional.empty());

        PeliculaResponseDTO resultado = peliculaService.buscarPorId(99);

        assertNull(resultado);
        verify(peliculaRepository, times(1)).findById(99);
    }

    @Test
    void guardar_TestRetornarDTO_CuandoTituloNoExiste() {
        when(peliculaRepository.existsByTitulo("Spider-Man: Across the Spider-Verse")).thenReturn(false);
        when(peliculaRepository.save(any(Pelicula.class))).thenReturn(peliculaPrueba);

        PeliculaResponseDTO resultado = peliculaService.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals("TE", resultado.getClasificacionEdad());
        assertTrue(resultado.getEstadoCartelera());
        verify(peliculaRepository, times(1)).save(any(Pelicula.class));
    }

    @Test
    void guardar_TestExceptionCuandoTituloYaExiste() {

        when(peliculaRepository.existsByTitulo("Spider-Man: Across the Spider-Verse")).thenReturn(true);


        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            peliculaService.guardar(requestDTO);
        });

        assertEquals("La pelicula ya esta registrada", exception.getMessage());
        verify(peliculaRepository, never()).save(any(Pelicula.class));
    }

    @Test
    void actualizar_TestActualizarDTOCuandoPeliculaExiste() {

        PeliculaRequestDTO dtoActualizado = new PeliculaRequestDTO();
        dtoActualizado.setTitulo("Dragon Ball Super: Broly");
        dtoActualizado.setSinopsis("Goku y Vegeta se enfrentan a Broly...");
        dtoActualizado.setDuracionMinutos(100);
        dtoActualizado.setClasificacionEdad("TE");
        dtoActualizado.setUrlPoster("https://link.com/dbs-broly.jpg");
        dtoActualizado.setEstadoCartelera(true);

        when(peliculaRepository.findById(1)).thenReturn(Optional.of(peliculaPrueba));
        when(peliculaRepository.save(any(Pelicula.class))).thenReturn(peliculaPrueba);


        PeliculaResponseDTO resultado = peliculaService.actualizar(1, dtoActualizado);

        assertNotNull(resultado);
        verify(peliculaRepository, times(1)).findById(1);
        verify(peliculaRepository, times(1)).save(any(Pelicula.class));
    }

    @Test
    void actualizar_TestNullCuandoPeliculaNoExiste() {

        when(peliculaRepository.findById(99)).thenReturn(Optional.empty());


        PeliculaResponseDTO resultado = peliculaService.actualizar(99, requestDTO);


        assertNull(resultado);
        verify(peliculaRepository, times(1)).findById(99);
        verify(peliculaRepository, never()).save(any(Pelicula.class));
    }

    @Test
    void eliminar_TestRetornarTrueCuandoPeliculaExiste() {

        when(peliculaRepository.existsById(1)).thenReturn(true);
        doNothing().when(peliculaRepository).deleteById(1);


        boolean resultado = peliculaService.eliminar(1);


        assertTrue(resultado);
        verify(peliculaRepository, times(1)).existsById(1);
        verify(peliculaRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminar_TestRetornarFalseCuandoPeliculaNoExiste() {

        when(peliculaRepository.existsById(99)).thenReturn(false);

        boolean resultado = peliculaService.eliminar(99);

        assertFalse(resultado);
        verify(peliculaRepository, times(1)).existsById(99);
        verify(peliculaRepository, never()).deleteById(anyInt());
    }
}