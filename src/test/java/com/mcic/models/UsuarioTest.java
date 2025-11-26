package com.mcic.models;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("u1", "Juan Pérez", "juan@example.com", "3001234567", "1990-05-15");
    }

    @Test
    @DisplayName("Debe crear un usuario correctamente")
    void testCreacionUsuario() {
        assertNotNull(usuario);
        assertEquals("u1", usuario.getId());
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("juan@example.com", usuario.getEmail());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("1990-05-15", usuario.getFechaNacimiento());
        assertNotNull(usuario.getFechaRegistro());
    }

    @Test
    @DisplayName("Debe crear usuario vacío con constructor sin parámetros")
    void testUsuarioVacio() {
        Usuario usuarioVacio = new Usuario();
        assertNotNull(usuarioVacio);
        assertNull(usuarioVacio.getId());
        assertNull(usuarioVacio.getNombre());
        assertNull(usuarioVacio.getEmail());
        assertNull(usuarioVacio.getTelefono());
        assertNull(usuarioVacio.getFechaNacimiento());
        assertNull(usuarioVacio.getFechaRegistro());
    }

    @Test
    @DisplayName("Debe retornar valores correctos con getters")
    void testGetters() {
        assertEquals("u1", usuario.getId());
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("juan@example.com", usuario.getEmail());
        assertEquals("3001234567", usuario.getTelefono());
        assertEquals("1990-05-15", usuario.getFechaNacimiento());
    }

    @Test
    @DisplayName("Debe actualizar ID con setter")
    void testSetId() {
        usuario.setId("u999");
        assertEquals("u999", usuario.getId());
    }

    @Test
    @DisplayName("Debe actualizar nombre con setter")
    void testSetNombre() {
        usuario.setNombre("Carlos López");
        assertEquals("Carlos López", usuario.getNombre());
    }

    @Test
    @DisplayName("Debe actualizar email con setter")
    void testSetEmail() {
        usuario.setEmail("carlos@example.com");
        assertEquals("carlos@example.com", usuario.getEmail());
    }

    @Test
    @DisplayName("Debe actualizar teléfono con setter")
    void testSetTelefono() {
        usuario.setTelefono("3109876543");
        assertEquals("3109876543", usuario.getTelefono());
    }

    @Test
    @DisplayName("Debe actualizar fechaNacimiento con setter")
    void testSetFechaNacimiento() {
        usuario.setFechaNacimiento("1995-08-20");
        assertEquals("1995-08-20", usuario.getFechaNacimiento());
    }

    @Test
    @DisplayName("Debe actualizar fechaRegistro con setter")
    void testSetFechaRegistro() {
        LocalDateTime nuevaFecha = LocalDateTime.of(2025, 11, 20, 10, 30);
        usuario.setFechaRegistro(nuevaFecha);
        assertEquals(nuevaFecha, usuario.getFechaRegistro());
    }

    @Test
    @DisplayName("Debe permitir teléfono nulo")
    void testTelefonoNulo() {
        usuario.setTelefono(null);
        assertNull(usuario.getTelefono());
    }

    @Test
    @DisplayName("Debe permitir fechaNacimiento nula")
    void testFechaNacimientoNula() {
        usuario.setFechaNacimiento(null);
        assertNull(usuario.getFechaNacimiento());
    }

    @Test
    @DisplayName("Debe validar usuario correctamente")
    void testValidarUsuarioValido() {
        assertDoesNotThrow(() -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe validar usuario con teléfono y fecha opcionales")
    void testValidarUsuarioMinimo() {
        Usuario usuarioMinimo = new Usuario("u2", "Ana García", "ana@test.com", null, null);
        assertDoesNotThrow(() -> {
            usuarioMinimo.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si nombre es nulo")
    void testValidarNombreNulo() {
        usuario.setNombre(null);
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si nombre está vacío")
    void testValidarNombreVacio() {
        usuario.setNombre("   ");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si email es nulo")
    void testValidarEmailNulo() {
        usuario.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si email está vacío")
    void testValidarEmailVacio() {
        usuario.setEmail("   ");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si email tiene formato inválido")
    void testValidarEmailInvalido() {
        usuario.setEmail("emailsindominio");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });

        usuario.setEmail("email@");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });

        usuario.setEmail("@dominio.com");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe aceptar emails válidos con caracteres especiales")
    void testValidarEmailValidos() {
        usuario.setEmail("juan.perez+test@example.co.uk");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });

        usuario.setEmail("user_name@test.com");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si teléfono tiene menos de 7 dígitos")
    void testValidarTelefonoCorto() {
        usuario.setTelefono("123456");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe aceptar teléfonos con formato variado")
    void testValidarTelefonoConFormato() {
        usuario.setTelefono("300-123-4567");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });

        usuario.setTelefono("(300) 123-4567");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });

        usuario.setTelefono("300 123 4567");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha de nacimiento es futura")
    void testValidarFechaNacimientoFutura() {
        usuario.setFechaNacimiento("2030-12-31");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si fecha de nacimiento tiene formato inválido")
    void testValidarFechaNacimientoInvalida() {
        usuario.setFechaNacimiento("31/12/1990");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });

        usuario.setFechaNacimiento("1990-13-01");
        assertThrows(IllegalArgumentException.class, () -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe aceptar fecha de nacimiento válida")
    void testValidarFechaNacimientoValida() {
        usuario.setFechaNacimiento("1990-05-15");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });

        usuario.setFechaNacimiento("2005-01-01");
        assertDoesNotThrow(() -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe manejar valores nulos en campos opcionales")
    void testValidarCamposOpcionales() {
        Usuario usuarioOpcional = new Usuario("u3", "Test User", "test@example.com", "", "");
        assertDoesNotThrow(() -> {
            usuarioOpcional.validar();
        });
    }

    @Test
    @DisplayName("Debe permitir múltiples cambios de datos")
    void testMultiplesCambios() {
        usuario.setNombre("María Rodríguez");
        usuario.setEmail("maria@example.com");
        usuario.setTelefono("3115551234");
        usuario.setFechaNacimiento("1992-03-10");

        assertEquals("María Rodríguez", usuario.getNombre());
        assertEquals("maria@example.com", usuario.getEmail());
        assertEquals("3115551234", usuario.getTelefono());
        assertEquals("1992-03-10", usuario.getFechaNacimiento());

        assertDoesNotThrow(() -> {
            usuario.validar();
        });
    }

    @Test
    @DisplayName("Debe crear múltiples usuarios independientes")
    void testMultiplesUsuarios() {
        Usuario u2 = new Usuario("u2", "Pedro García", "pedro@example.com", "3201234567", "1988-10-25");
        Usuario u3 = new Usuario("u3", "Sofia López", "sofia@example.com", "3301234567", "1995-07-08");

        assertEquals("u1", usuario.getId());
        assertEquals("u2", u2.getId());
        assertEquals("u3", u3.getId());

        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("Pedro García", u2.getNombre());
        assertEquals("Sofia López", u3.getNombre());

        assertDoesNotThrow(() -> {
            usuario.validar();
            u2.validar();
            u3.validar();
        });
    }

}
