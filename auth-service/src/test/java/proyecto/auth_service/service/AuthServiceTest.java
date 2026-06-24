package proyecto.auth_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import proyecto.auth_service.client.UsuarioClient;
import proyecto.auth_service.dto.UsuarioDTO;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private AuthService authService;

    @Test
    void validarLogin_Exito() {
        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setEmail("test@mail.com");
        mockUsuario.setPassword("secret123");

        when(usuarioClient.buscarUsuarioPorEmail("test@mail.com")).thenReturn(mockUsuario);

        boolean resultado = authService.validarLogin("test@mail.com", "secret123");

        assertTrue(resultado);
        verify(usuarioClient, times(1)).buscarUsuarioPorEmail("test@mail.com");
    }

    @Test
    void validarLogin_PasswordIncorrecto() {
        UsuarioDTO mockUsuario = new UsuarioDTO();
        mockUsuario.setEmail("test@mail.com");
        mockUsuario.setPassword("secret123");

        when(usuarioClient.buscarUsuarioPorEmail("test@mail.com")).thenReturn(mockUsuario);

        boolean resultado = authService.validarLogin("test@mail.com", "wrongpassword");

        assertFalse(resultado);
        verify(usuarioClient, times(1)).buscarUsuarioPorEmail("test@mail.com");
    }

    @Test
    void validarLogin_UsuarioNoExiste() {
        when(usuarioClient.buscarUsuarioPorEmail(anyString())).thenReturn(null);

        boolean resultado = authService.validarLogin("unknown@mail.com", "secret123");

        assertFalse(resultado);
        verify(usuarioClient, times(1)).buscarUsuarioPorEmail("unknown@mail.com");
    }
}
