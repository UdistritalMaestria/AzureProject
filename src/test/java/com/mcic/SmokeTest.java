package com.mcic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.mcic.controllers.BibliotecaController;
import com.mcic.controllers.PrestamoController;
import com.mcic.controllers.UsuarioController;
import com.mcic.models.Libro;
import com.mcic.models.Prestamo;
import com.mcic.models.Usuario;
import com.mcic.store.InMemoryStore;

@DisplayName("Smoke Tests - Verificación de Componentes")
public class SmokeTest {

    @Test
    @DisplayName("Debe poder instanciar InMemoryStore")
    void testInMemoryStoreInstantiation() {
        InMemoryStore store = new InMemoryStore();
        assertNotNull(store, "InMemoryStore debe instanciarse correctamente");
    }

    @Test
    @DisplayName("InMemoryStore debe tener listas inicializadas")
    void testInMemoryStoreInitialized() {
        InMemoryStore store = new InMemoryStore();
        assertNotNull(store.getLibros(), "Lista de libros debe estar inicializada");
        assertNotNull(store.getUsuarios(), "Lista de usuarios debe estar inicializada");
        assertNotNull(store.getPrestamos(), "Lista de préstamos debe estar inicializada");
    }

    @Test
    @DisplayName("InMemoryStore debe retornar listas vacías al inicio")
    void testInMemoryStoreEmpty() {
        InMemoryStore store = new InMemoryStore();
        assertTrue(store.getLibros().isEmpty(), "Lista de libros debe estar vacía al inicio");
        assertTrue(store.getUsuarios().isEmpty(), "Lista de usuarios debe estar vacía al inicio");
        assertTrue(store.getPrestamos().isEmpty(), "Lista de préstamos debe estar vacía al inicio");
    }

    @Test
    @DisplayName("Debe poder instanciar BibliotecaController con InMemoryStore")
    void testBibliotecaControllerInstantiation() {
        InMemoryStore store = new InMemoryStore();
        BibliotecaController controller = new BibliotecaController(store);
        assertNotNull(controller, "BibliotecaController debe instanciarse correctamente");
    }

    @Test
    @DisplayName("Debe poder instanciar UsuarioController con InMemoryStore")
    void testUsuarioControllerInstantiation() {
        InMemoryStore store = new InMemoryStore();
        UsuarioController controller = new UsuarioController(store);
        assertNotNull(controller, "UsuarioController debe instanciarse correctamente");
    }

    @Test
    @DisplayName("Debe poder instanciar PrestamoController con InMemoryStore")
    void testPrestamoControllerInstantiation() {
        InMemoryStore store = new InMemoryStore();
        PrestamoController controller = new PrestamoController(store);
        assertNotNull(controller, "PrestamoController debe instanciarse correctamente");
    }

    @Test
    @DisplayName("Debe poder crear y almacenar Libro en InMemoryStore")
    void testLibroIntegration() {
        InMemoryStore store = new InMemoryStore();
        Libro libro = new Libro("1", "Test Libro", "Test Autor", "978-0451524935");
        libro.setId(store.nextLibroId());
        store.getLibros().add(libro);
        
        assertFalse(store.getLibros().isEmpty(), "Debe haber al menos un libro en el store");
        assertEquals(1, store.getLibros().size(), "Debe haber exactamente un libro");
    }

    @Test
    @DisplayName("Debe poder crear y almacenar Usuario en InMemoryStore")
    void testUsuarioIntegration() {
        InMemoryStore store = new InMemoryStore();
        Usuario usuario = new Usuario("u1", "Test User", "test@example.com", "3001234567", "1990-05-15");
        usuario.setId(store.nextUsuarioId());
        store.getUsuarios().add(usuario);
        
        assertFalse(store.getUsuarios().isEmpty(), "Debe haber al menos un usuario en el store");
        assertEquals(1, store.getUsuarios().size(), "Debe haber exactamente un usuario");
    }

    @Test
    @DisplayName("Debe poder crear y almacenar Prestamo en InMemoryStore")
    void testPrestamoIntegration() {
        InMemoryStore store = new InMemoryStore();
        Prestamo prestamo = new Prestamo(store.nextPrestamoId(), "u1", "l1");
        store.getPrestamos().add(prestamo);
        
        assertFalse(store.getPrestamos().isEmpty(), "Debe haber al menos un préstamo en el store");
        assertEquals(1, store.getPrestamos().size(), "Debe haber exactamente un préstamo");
    }

    @Test
    @DisplayName("InMemoryStore debe generar IDs únicos para libros")
    void testLibroIdGeneration() {
        InMemoryStore store = new InMemoryStore();
        String id1 = store.nextLibroId();
        String id2 = store.nextLibroId();
        
        assertNotNull(id1, "ID del libro 1 no debe ser nulo");
        assertNotNull(id2, "ID del libro 2 no debe ser nulo");
        assertNotEquals(id1, id2, "Los IDs deben ser únicos");
    }

    @Test
    @DisplayName("InMemoryStore debe generar IDs únicos para usuarios")
    void testUsuarioIdGeneration() {
        InMemoryStore store = new InMemoryStore();
        String id1 = store.nextUsuarioId();
        String id2 = store.nextUsuarioId();
        
        assertNotNull(id1, "ID del usuario 1 no debe ser nulo");
        assertNotNull(id2, "ID del usuario 2 no debe ser nulo");
        assertNotEquals(id1, id2, "Los IDs deben ser únicos");
    }

    @Test
    @DisplayName("InMemoryStore debe generar IDs únicos para préstamos")
    void testPrestamoIdGeneration() {
        InMemoryStore store = new InMemoryStore();
        String id1 = store.nextPrestamoId();
        String id2 = store.nextPrestamoId();
        
        assertNotNull(id1, "ID del préstamo 1 no debe ser nulo");
        assertNotNull(id2, "ID del préstamo 2 no debe ser nulo");
        assertNotEquals(id1, id2, "Los IDs deben ser únicos");
    }

    @Test
    @DisplayName("Modelos deben poder validarse correctamente")
    void testModelValidation() {
        Libro libro = new Libro("1", "Clean Code", "Robert Martin", "978-0451524935");
        Usuario usuario = new Usuario("u1", "Juan", "juan@test.com", "3001234567", "1990-05-15");
        
        assertDoesNotThrow(() -> libro.validar(), "Libro válido debe pasar validación");
        assertDoesNotThrow(() -> usuario.validar(), "Usuario válido debe pasar validación");
    }

}


