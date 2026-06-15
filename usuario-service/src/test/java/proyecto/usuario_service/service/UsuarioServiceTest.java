package proyecto.usuario_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.usuario_service.dto.UsuarioRequestDTO;
import proyecto.usuario_service.dto.UsuarioResponseDTO;
import proyecto.usuario_service.model.Usuario;
import proyecto.usuario_service.repository.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    //variables globales
    private Usuario usuarioPrueba;
    private UsuarioRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Entidad falsa BD
        usuarioPrueba = new Usuario();
        usuarioPrueba.setIdUsuario(1);
        usuarioPrueba.setNombre("Alan Brito Delgado");
        usuarioPrueba.setEmail("alan@mail.com");
        usuarioPrueba.setPassword("abcdefg1123");
        usuarioPrueba.setFechaRegistro(LocalDateTime.now());

        // DTO falso
        requestDTO = new UsuarioRequestDTO();
        requestDTO.setNombre("Alan Brito Delgado");
        requestDTO.setEmail("alan@mail.com");
        requestDTO.setPassword("abcdefg1123");
    }

    @Test
    //retornar Usuario DTO cuando email no existe
    void guardar_TestEmailNoExiste() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPrueba);

        // metodo real
        UsuarioResponseDTO resultado = usuarioService.guardar(requestDTO);

        assertNotNull(resultado);
        assertEquals("Alan Brito Delgado", resultado.getNombre());
        assertEquals("alan@mail.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    //exception si mail ya existe
    void guardar_TestEmailExiste() {
        when(usuarioRepository.existsByEmail("alan@mail.com")).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.guardar(requestDTO);
        });
        assertEquals("El correo ya esta registrado", exception.getMessage());

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    //Deberia retornar lista de usuarios cuando hay datos
    void listarTodosTest() {
        List<Usuario> listaFalsa = Arrays.asList(usuarioPrueba);
        when(usuarioRepository.findAll()).thenReturn(listaFalsa);

        List<UsuarioResponseDTO> resultado = usuarioService.listarTodos();

        assertFalse(resultado.isEmpty()); // La lista no debe estar vacia
        assertEquals(1, resultado.size()); // Debe tener 1 elemento
        assertEquals("alan@mail.com", resultado.get(0).getEmail()); // El primer elemento debe ser Alan
        verify(usuarioRepository, times(1)).findAll();
    }

    //Retorna DTO si usuario existe
    @Test
    void buscarPorId_TestUsuarioExiste() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioPrueba));

        UsuarioResponseDTO resultado = usuarioService.buscarPorId(1);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdUsuario());
        verify(usuarioRepository, times(1)).findById(1);
    }
}