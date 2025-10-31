package com.mcic.steps;

import com.mcic.models.Libro;
import io.cucumber.java.es.*;
import io.cucumber.datatable.DataTable;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class BibliotecaSteps {

    private final List<Libro> catalogo = new ArrayList<>();
    private long contadorId = 1;
    private String ultimoError = null;
    private Libro ultimoLibro = null;

    // === PASOS COMUNES ===

    @Dado("que tengo un catálogo vacío")
    public void catalogoVacio() {
        catalogo.clear();
        contadorId = 1;
        ultimoError = null;
        ultimoLibro = null;
    }

    // === AGREGAR LIBROS ===

    @Cuando("agrego un libro con título {string}, autor {string} e ISBN {string}")
    public void agregarLibro(String titulo, String autor, String isbn) {
        try {
            Libro libro = new Libro(String.valueOf(contadorId++), titulo, autor, isbn);
            libro.validar();
            catalogo.add(libro);
            ultimoLibro = libro;
            ultimoError = null;
        } catch (IllegalArgumentException e) {
            ultimoError = e.getMessage();
        }
    }

    @Cuando("intento agregar un libro con título {string}, autor {string} e ISBN {string}")
    public void intentarAgregarLibro(String titulo, String autor, String isbn) {
        agregarLibro(titulo, autor, isbn);
    }

    @Entonces("el libro existe en el catálogo")
    public void libroExisteEnCatalogo() {
        assertNotNull(ultimoLibro, "El último libro debería existir");
        assertTrue(catalogo.contains(ultimoLibro), "El catálogo debería contener el libro");
    }

    @Y("el libro tiene un ID asignado")
    public void libroTieneId() {
        assertNotNull(ultimoLibro, "El último libro debería existir");
        assertNotNull(ultimoLibro.getId(), "El libro debería tener un ID");
        assertFalse(ultimoLibro.getId().isEmpty(), "El ID no debería estar vacío");
    }

    @Entonces("el total de libros es {int}")
    public void totalDeLibros(int cantidad) {
        assertEquals(cantidad, catalogo.size(), "El catálogo debería tener " + cantidad + " libro(s)");
    }

    @Entonces("se muestra un error {string}")
    public void seMuestraError(String mensajeEsperado) {
        assertNotNull(ultimoError, "Debería haber un error");
        assertEquals(mensajeEsperado, ultimoError, "El mensaje de error no coincide");
    }

    // === LISTAR Y ELIMINAR ===

    @Y("existen los siguientes libros:")
    public void existenLibros(DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        for (Map<String, String> row : rows) {
            Libro libro = new Libro(
                    String.valueOf(contadorId++),
                    row.get("titulo"),
                    row.get("autor"),
                    row.get("isbn")
            );
            catalogo.add(libro);
        }
    }

    @Cuando("consulto el catálogo")
    public void consultarCatalogo() {
        // Solo para mantener el contexto, el catálogo ya está disponible
    }

    @Entonces("veo {int} libro\\(s)")
    public void veoNLibros(int cantidad) {
        assertEquals(cantidad, catalogo.size(), "Debería ver " + cantidad + " libro(s)");
    }

    @Cuando("elimino el libro con título {string}")
    public void eliminarLibroPorTitulo(String titulo) {
        catalogo.removeIf(libro -> libro.getTitulo().equals(titulo));
    }

    @Y("el libro {string} no existe en el catálogo")
    public void libroNoExiste(String titulo) {
        boolean existe = catalogo.stream()
                .anyMatch(libro -> libro.getTitulo().equals(titulo));
        assertFalse(existe, "El libro '" + titulo + "' no debería existir en el catálogo");
    }
}