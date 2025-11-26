package com.mcic.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcic.models.Libro;
import com.mcic.models.Prestamo;
import com.mcic.store.InMemoryStore;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoController {

    private final InMemoryStore store;

    @Autowired
    public PrestamoController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping("")
    public List<Prestamo> listar() {
        return store.getPrestamos();
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody Map<String, String> body) {
        String usuarioId = body.get("usuarioId");
        String libroId = body.get("libroId");
        if (usuarioId == null || usuarioId.trim().isEmpty() || libroId == null || libroId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Faltan usuarioId o libroId");
        }

        // validar usuario
        Optional<?> u = store.getUsuarios().stream().filter(x -> x.getId().equals(usuarioId)).findFirst();
        if (!u.isPresent()) return ResponseEntity.badRequest().body("Usuario no encontrado");

        // validar libro
        Optional<Libro> l = store.getLibros().stream().filter(x -> x.getId().equals(libroId)).findFirst();
        if (!l.isPresent()) return ResponseEntity.badRequest().body("Libro no encontrado");
        Libro libro = l.get();

        try {
            libro.decrementarStock(1);
        } catch (IllegalStateException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

        Prestamo p = new Prestamo(store.nextPrestamoId(), usuarioId, libroId);
        p.setFechaInicio(LocalDateTime.now());
        store.getPrestamos().add(p);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/devolver/{id}")
    public ResponseEntity<?> devolver(@PathVariable String id) {
        Optional<Prestamo> p = store.getPrestamos().stream().filter(x -> x.getId().equals(id)).findFirst();
        if (!p.isPresent()) return ResponseEntity.notFound().build();
        Prestamo prestamo = p.get();
        if (prestamo.isDevuelto()) return ResponseEntity.badRequest().body("Préstamo ya devuelto");

        // marcar devuelto
        prestamo.setDevuelto(true);
        prestamo.setFechaFin(LocalDateTime.now());

        // aumentar stock del libro
        Optional<Libro> l = store.getLibros().stream().filter(x -> x.getId().equals(prestamo.getLibroId())).findFirst();
        if (l.isPresent()) {
            l.get().incrementarStock(1);
        }

        return ResponseEntity.ok(prestamo);
    }
}
