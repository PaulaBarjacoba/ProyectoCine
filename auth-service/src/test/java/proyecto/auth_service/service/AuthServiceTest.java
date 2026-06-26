package proyecto.auth_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.auth_service.client.UsuarioClient;
import proyecto.auth_service.dto.UsuarioDTO;
import proyecto.auth_service.model.Rol;
import proyecto.auth_service.repository.RolRepository;
import proyecto.auth_service.util.JwtUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void login_Exito() {
        // Arrange
        String email = "test@mail.com";
        String password = "secret123";
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", email);
        credenciales.put("password", password);

        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setIdUsuario(1);
        mockUsuario.setEmail(email);

        Rol mockRol = new Rol();
        mockRol.setIdRol(1);
        mockRol.setIdUsuario(1);
        mockRol.setNombreRol("ROLE_USER");
        List<Rol> mockRoles = Collections.singletonList(mockRol);

        when(usuarioClient.validarCredenciales(credenciales)).thenReturn(true);
        when(usuarioClient.buscarUsuarioPorEmail(email)).thenReturn(mockUsuario);
        when(rolRepository.findByIdUsuario(1)).thenReturn(mockRoles);
        when(jwtUtil.generateToken(eq(email), any())).thenReturn("mocked-jwt-token");

        // Act
        String token = authService.login(email, password);

        // Assert
        assertEquals("mocked-jwt-token", token);
        verify(usuarioClient, times(1)).validarCredenciales(credenciales);
        verify(usuarioClient, times(1)).buscarUsuarioPorEmail(email);
        verify(rolRepository, times(1)).findByIdUsuario(1);
        verify(jwtUtil, times(1)).generateToken(eq(email), any());
    }

    @Test
    void login_CredencialesIncorrectas() {
        // Arrange
        String email = "test@mail.com";
        String password = "wrongpassword";
        Map<String, String> credenciales = new HashMap<>();
        credenciales.put("email", email);
        credenciales.put("password", password);

        when(usuarioClient.validarCredenciales(credenciales)).thenReturn(false);

        // Act
        String token = authService.login(email, password);

        // Assert
        assertNull(token);
        verify(usuarioClient, times(1)).validarCredenciales(credenciales);
        verify(usuarioClient, never()).buscarUsuarioPorEmail(anyString());
        verify(rolRepository, never()).findByIdUsuario(anyInt());
    }

    @Test
    void validarLogin_UsuarioExiste() {
        // Arrange
        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setEmail("test@mail.com");

        when(usuarioClient.buscarUsuarioPorEmail("test@mail.com")).thenReturn(mockUsuario);

        // Act
        boolean resultado = authService.validarLogin("test@mail.com", "password123");

        // Assert
        assertTrue(resultado);
        verify(usuarioClient, times(1)).buscarUsuarioPorEmail("test@mail.com");
    }

    @Test
    void validarLogin_UsuarioNoExiste() {
        // Arrange
        when(usuarioClient.buscarUsuarioPorEmail(anyString())).thenReturn(null);

        // Act
        boolean resultado = authService.validarLogin("unknown@mail.com", "secret123");

        // Assert
        assertFalse(resultado);
        verify(usuarioClient, times(1)).buscarUsuarioPorEmail("unknown@mail.com");
    }
}

