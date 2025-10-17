package com.mcic.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

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

}
