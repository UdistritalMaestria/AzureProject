package com.mcic.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.time.LocalDateTime;

public class PrestamoTest {

    private Prestamo prestamo;
    private LocalDateTime ahora;

    @BeforeEach
    void setUp() {
        prestamo = new Prestamo("p1", "u1", "l1");
        ahora = LocalDateTime.now();
    }

    @Test
    @DisplayName("Debe crear un préstamo correctamente")
    void testCreacionPrestamo() {
        assertNotNull(prestamo);
        assertEquals("p1", prestamo.getId());
        assertEquals("u1", prestamo.getUsuarioId());
        assertEquals("l1", prestamo.getLibroId());
        assertNotNull(prestamo.getFechaInicio());
        assertFalse(prestamo.isDevuelto());
    }

    @Test
    @DisplayName("Debe crear préstamo vacío con constructor sin parámetros")
    void testPrestamoVacio() {
        Prestamo prestamoVacio = new Prestamo();
        assertNotNull(prestamoVacio);
        assertNull(prestamoVacio.getId());
        assertNull(prestamoVacio.getUsuarioId());
        assertNull(prestamoVacio.getLibroId());
        assertNull(prestamoVacio.getFechaInicio());
        assertFalse(prestamoVacio.isDevuelto());
    }

    @Test
    @DisplayName("Debe retornar valores correctos con getters")
    void testGetters() {
        assertEquals("p1", prestamo.getId());
        assertEquals("u1", prestamo.getUsuarioId());
        assertEquals("l1", prestamo.getLibroId());
    }

    @Test
    @DisplayName("Debe actualizar ID con setter")
    void testSetId() {
        prestamo.setId("p999");
        assertEquals("p999", prestamo.getId());
    }

    @Test
    @DisplayName("Debe actualizar usuarioId con setter")
    void testSetUsuarioId() {
        prestamo.setUsuarioId("u99");
        assertEquals("u99", prestamo.getUsuarioId());
    }

    @Test
    @DisplayName("Debe actualizar libroId con setter")
    void testSetLibroId() {
        prestamo.setLibroId("l99");
        assertEquals("l99", prestamo.getLibroId());
    }

    @Test
    @DisplayName("Debe actualizar usuarioNombre con setter")
    void testSetUsuarioNombre() {
        prestamo.setUsuarioNombre("Juan Pérez");
        assertEquals("Juan Pérez", prestamo.getUsuarioNombre());
    }

    @Test
    @DisplayName("Debe actualizar libroTitulo con setter")
    void testSetLibroTitulo() {
        prestamo.setLibroTitulo("Clean Code");
        assertEquals("Clean Code", prestamo.getLibroTitulo());
    }

    @Test
    @DisplayName("Debe inicializar usuarioNombre como nulo")
    void testUsuarioNombrePorDefecto() {
        assertNull(prestamo.getUsuarioNombre());
    }

    @Test
    @DisplayName("Debe inicializar libroTitulo como nulo")
    void testLibroTituloPorDefecto() {
        assertNull(prestamo.getLibroTitulo());
    }

    @Test
    @DisplayName("Debe actualizar fechaInicio con setter")
    void testSetFechaInicio() {
        LocalDateTime nuevaFecha = LocalDateTime.of(2025, 11, 20, 10, 30);
        prestamo.setFechaInicio(nuevaFecha);
        assertEquals(nuevaFecha, prestamo.getFechaInicio());
    }

    @Test
    @DisplayName("Debe actualizar fechaFin con setter")
    void testSetFechaFin() {
        LocalDateTime fechaFin = LocalDateTime.of(2025, 12, 20, 10, 30);
        prestamo.setFechaFin(fechaFin);
        assertEquals(fechaFin, prestamo.getFechaFin());
    }

    @Test
    @DisplayName("Debe inicializar fechaFin como nulo")
    void testFechaFinPorDefecto() {
        assertNull(prestamo.getFechaFin());
    }

    @Test
    @DisplayName("Debe inicializar devuelto como false")
    void testDevueltoPorDefecto() {
        assertFalse(prestamo.isDevuelto());
    }

    @Test
    @DisplayName("Debe cambiar estado devuelto a true con setter")
    void testSetDevueltoTrue() {
        prestamo.setDevuelto(true);
        assertTrue(prestamo.isDevuelto());
    }

    @Test
    @DisplayName("Debe cambiar estado devuelto a false con setter")
    void testSetDevueltoFalse() {
        prestamo.setDevuelto(true);
        prestamo.setDevuelto(false);
        assertFalse(prestamo.isDevuelto());
    }

    @Test
    @DisplayName("Debe manejar valores nulos en usuarioNombre")
    void testUsuarioNombreNulo() {
        prestamo.setUsuarioNombre("Test");
        prestamo.setUsuarioNombre(null);
        assertNull(prestamo.getUsuarioNombre());
    }

    @Test
    @DisplayName("Debe manejar valores nulos en libroTitulo")
    void testLibroTituloNulo() {
        prestamo.setLibroTitulo("Test");
        prestamo.setLibroTitulo(null);
        assertNull(prestamo.getLibroTitulo());
    }

    @Test
    @DisplayName("Debe manejar múltiples cambios de estado devuelto")
    void testMultiplescambiosDevuelto() {
        assertFalse(prestamo.isDevuelto());
        prestamo.setDevuelto(true);
        assertTrue(prestamo.isDevuelto());
        prestamo.setDevuelto(false);
        assertFalse(prestamo.isDevuelto());
        prestamo.setDevuelto(true);
        assertTrue(prestamo.isDevuelto());
    }

    @Test
    @DisplayName("Debe permitir acceso a todos los campos simultáneamente")
    void testAccesoCompleto() {
        prestamo.setUsuarioNombre("María García");
        prestamo.setLibroTitulo("1984");
        prestamo.setFechaFin(LocalDateTime.of(2025, 12, 25, 15, 0));
        prestamo.setDevuelto(true);

        assertEquals("p1", prestamo.getId());
        assertEquals("u1", prestamo.getUsuarioId());
        assertEquals("l1", prestamo.getLibroId());
        assertEquals("María García", prestamo.getUsuarioNombre());
        assertEquals("1984", prestamo.getLibroTitulo());
        assertTrue(prestamo.isDevuelto());
        assertNotNull(prestamo.getFechaInicio());
        assertNotNull(prestamo.getFechaFin());
    }

    @Test
    @DisplayName("Debe crear múltiples préstamos independientes")
    void testMultiplesPrestamos() {
        Prestamo p2 = new Prestamo("p2", "u2", "l2");
        Prestamo p3 = new Prestamo("p3", "u3", "l3");

        p2.setUsuarioNombre("Pedro");
        p3.setUsuarioNombre("Ana");

        assertEquals("p1", prestamo.getId());
        assertEquals("p2", p2.getId());
        assertEquals("p3", p3.getId());

        assertNull(prestamo.getUsuarioNombre());
        assertEquals("Pedro", p2.getUsuarioNombre());
        assertEquals("Ana", p3.getUsuarioNombre());
    }

}
