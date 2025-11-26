package com.mcic.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mcic.models.Libro;
import com.mcic.store.InMemoryStore;

@Controller
public class BibliotecaController {

    private final InMemoryStore store;

    @Autowired
    public BibliotecaController(InMemoryStore store) {
        this.store = store;
    }

    // Listar todos los libros
    @GetMapping("/")
    public String home(Model model) {
        // dashboard principal
        return "dashboard";
    }

    @GetMapping("/modulos/libros")
    public String modLibros(Model model) {
        model.addAttribute("libros", store.getLibros());
        model.addAttribute("libro", new Libro());
        return "fragments/libros";
    }

    @GetMapping("/modulos/usuarios")
    public String modUsuarios(Model model) {
        model.addAttribute("usuarios", store.getUsuarios());
        return "fragments/usuarios";
    }

    @GetMapping("/modulos/prestamos")
    public String modPrestamos(Model model) {
        model.addAttribute("prestamos", store.getPrestamos());
        model.addAttribute("usuarios", store.getUsuarios());
        model.addAttribute("libros", store.getLibros());
        return "fragments/prestamos";
    }

    // NOTE: legacy endpoints removed — use the JSON API under /api/libros/**

    // API JSON para crear libro (usada por JS)
    @PostMapping("/api/libros/agregar")
    public ResponseEntity<?> apiAgregarLibro(@RequestBody Map<String, Object> body) {
        String titulo = (String) body.getOrDefault("titulo", "");
        String autor = (String) body.getOrDefault("autor", "");
        String isbn = (String) body.getOrDefault("isbn", "");
        int cantidad = 1;
        try {
            Object c = body.get("cantidad");
            if (c != null) cantidad = Integer.parseInt(c.toString());
        } catch (Exception e) { cantidad = 1; }

        Libro libro = new Libro();
        libro.setId(store.nextLibroId());
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setIsbn(isbn);
        libro.setCantidad(Math.max(0, cantidad));
        try {
            libro.validar();
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        store.getLibros().add(libro);
        return ResponseEntity.ok(libro);
    }

    // API para eliminar/incrementar/decrementar usados por JS
    @PostMapping("/api/libros/incrementar/{id}")
    public ResponseEntity<?> apiIncrementar(@PathVariable String id) {
        store.getLibros().stream().filter(l -> l.getId().equals(id)).findFirst().ifPresent(l -> l.incrementarStock(1));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/libros/decrementar/{id}")
    public ResponseEntity<?> apiDecrementar(@PathVariable String id) {
        store.getLibros().stream().filter(l -> l.getId().equals(id)).findFirst().ifPresent(l -> {
            try { l.decrementarStock(1); } catch (IllegalStateException ex) { }
        });
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/libros/eliminar/{id}")
    public ResponseEntity<?> apiEliminar(@PathVariable String id) {
        boolean removed = store.getLibros().removeIf(l -> l.getId().equals(id));
        if (!removed) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }


}
