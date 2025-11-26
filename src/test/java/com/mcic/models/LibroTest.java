package com.mcic.models;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LibroTest {

    private Libro libro;

    @BeforeEach
    void setUp() {
        libro = new Libro("1", "Cien años de soledad", "Gabriel García Márquez", "978-8437604947");
    }

    @Test
    @DisplayName("Debe crear un libro correctamente")
    void testCreacionLibro() {
        assertNotNull(libro);
        assertEquals("1", libro.getId());
        assertEquals("Cien años de soledad", libro.getTitulo());
        assertEquals("Gabriel García Márquez", libro.getAutor());
        assertEquals("978-8437604947", libro.getIsbn());
    }

    @Test
    @DisplayName("Debe actualizar propiedades con setters")
    void testSetters() {
        libro.setId("99");
        libro.setTitulo("Nuevo Título");
        libro.setAutor("Nuevo Autor");
        libro.setIsbn("999-9999999");

        assertEquals("99", libro.getId());
        assertEquals("Nuevo Título", libro.getTitulo());
        assertEquals("Nuevo Autor", libro.getAutor());
        assertEquals("999-9999999", libro.getIsbn());
    }

    @Test
    @DisplayName("Debe retornar valores correctos con getters")
    void testGetters() {
        assertEquals("1", libro.getId());
        assertEquals("Cien años de soledad", libro.getTitulo());
        assertEquals("Gabriel García Márquez", libro.getAutor());
        assertEquals("978-8437604947", libro.getIsbn());
    }

    @Test
    @DisplayName("Debe crear libro vacío con constructor sin parámetros")
    void testLibroVacio() {
        Libro libroVacio = new Libro();

        assertNotNull(libroVacio);
        assertNull(libroVacio.getId());
        assertNull(libroVacio.getTitulo());
        assertNull(libroVacio.getAutor());
        assertNull(libroVacio.getIsbn());
    }

    @Test
    @DisplayName("Debe manejar valores nulos correctamente")
    void testValoresNulos() {
        libro.setTitulo(null);
        libro.setAutor(null);
        libro.setIsbn(null);

        assertNull(libro.getTitulo());
        assertNull(libro.getAutor());
        assertNull(libro.getIsbn());
    }

    @Test
    @DisplayName("Debe permitir cambiar ID múltiples veces")
    void testCambioMultipleId() {
        libro.setId("A");
        assertEquals("A", libro.getId());

        libro.setId("B");
        assertEquals("B", libro.getId());

        libro.setId("C");
        assertEquals("C", libro.getId());
    }

    @Test
    @DisplayName("Debe inicializar cantidad en 1 por defecto")
    void testCantidadPorDefecto() {
        Libro libroNuevo = new Libro("2", "El Quijote", "Cervantes", "978-8437604947");
        assertEquals(1, libroNuevo.getCantidad());
    }

    @Test
    @DisplayName("Debe inicializar cantidad desde el constructor")
    void testCantidadEnConstructor() {
        Libro libroConCantidad = new Libro("3", "1984", "George Orwell", "978-0451524935", 5);
        assertEquals(5, libroConCantidad.getCantidad());
    }

    @Test
    @DisplayName("Debe asegurar que cantidad no sea negativa")
    void testCantidadNegativaEnConstructor() {
        Libro libroNegativo = new Libro("4", "Ficción", "Autor", "978-0451524935", -3);
        assertEquals(0, libroNegativo.getCantidad());
    }

    @Test
    @DisplayName("Debe permitir actualizar cantidad con setCantidad")
    void testSetCantidad() {
        libro.setCantidad(10);
        assertEquals(10, libro.getCantidad());

        libro.setCantidad(0);
        assertEquals(0, libro.getCantidad());
    }

    @Test
    @DisplayName("Debe evitar cantidad negativa en setCantidad")
    void testSetCantidadNegativa() {
        libro.setCantidad(-5);
        assertEquals(0, libro.getCantidad());
    }

    @Test
    @DisplayName("Debe incrementar stock correctamente")
    void testIncrementarStock() {
        libro.setCantidad(5);
        libro.incrementarStock(3);
        assertEquals(8, libro.getCantidad());
    }

    @Test
    @DisplayName("Debe ignorar incremento con valor <= 0")
    void testIncrementarStockInvalido() {
        libro.setCantidad(5);
        libro.incrementarStock(0);
        assertEquals(5, libro.getCantidad());

        libro.incrementarStock(-2);
        assertEquals(5, libro.getCantidad());
    }

    @Test
    @DisplayName("Debe decrementar stock correctamente")
    void testDecrementarStock() throws IllegalStateException {
        libro.setCantidad(10);
        libro.decrementarStock(3);
        assertEquals(7, libro.getCantidad());
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando no hay suficiente stock")
    void testDecrementarStockInsuficiente() {
        libro.setCantidad(5);
        assertThrows(IllegalStateException.class, () -> {
            libro.decrementarStock(10);
        });
        assertEquals(5, libro.getCantidad()); // stock sin cambiar
    }

    @Test
    @DisplayName("Debe ignorar decremento con valor <= 0")
    void testDecrementarStockInvalido() throws IllegalStateException {
        libro.setCantidad(5);
        libro.decrementarStock(0);
        assertEquals(5, libro.getCantidad());

        libro.decrementarStock(-2);
        assertEquals(5, libro.getCantidad());
    }

    @Test
    @DisplayName("Debe validar libro correctamente")
    void testValidarLibroValido() {
        assertDoesNotThrow(() -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si título es nulo")
    void testValidarTituloNulo() {
        libro.setTitulo(null);
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si título está vacío")
    void testValidarTituloVacio() {
        libro.setTitulo("   ");
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si autor es nulo")
    void testValidarAutorNulo() {
        libro.setAutor(null);
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si autor está vacío")
    void testValidarAutorVacio() {
        libro.setAutor("   ");
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si ISBN es nulo")
    void testValidarISBNNulo() {
        libro.setIsbn(null);
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si ISBN está vacío")
    void testValidarISBNVacio() {
        libro.setIsbn("   ");
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si ISBN es muy corto")
    void testValidarISBNCorto() {
        libro.setIsbn("123456789"); // 9 caracteres, necesita 10
        assertThrows(IllegalArgumentException.class, () -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe aceptar ISBN con guiones y espacios")
    void testValidarISBNConFormato() {
        libro.setIsbn("978-84-376-0494-7");
        assertDoesNotThrow(() -> {
            libro.validar();
        });
    }

    @Test
    @DisplayName("Debe permitir cantidad 0 (caso límite válido)")
    void testValidarCantidadCero() {
        libro.setCantidad(0);
        assertDoesNotThrow(() -> {
            libro.validar();
        });
    }

}
