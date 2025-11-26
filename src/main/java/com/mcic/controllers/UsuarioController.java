package com.mcic.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mcic.models.Usuario;
import com.mcic.store.InMemoryStore;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final InMemoryStore store;

    @Autowired
    public UsuarioController(InMemoryStore store) {
        this.store = store;
    }

    @GetMapping("")
    public List<Usuario> listar() {
        return store.getUsuarios();
    }

    @PostMapping("/agregar")
    public ResponseEntity<?> crear(@RequestBody Usuario usuario) {
        try{
            usuario.validar();
        }catch(IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        usuario.setId(store.nextUsuarioId());
        usuario.setFechaRegistro(LocalDateTime.now());
        store.getUsuarios().add(usuario);
        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtener(@PathVariable String id) {
        Optional<Usuario> u = store.getUsuarios().stream().filter(x -> x.getId().equals(id)).findFirst();
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable String id, @RequestBody Usuario datos) {
        Optional<Usuario> u = store.getUsuarios().stream().filter(x -> x.getId().equals(id)).findFirst();
        if (!u.isPresent()) return ResponseEntity.notFound().build();
        Usuario usuario = u.get();
        if (datos.getNombre() != null) usuario.setNombre(datos.getNombre());
        if (datos.getEmail() != null) usuario.setEmail(datos.getEmail());
        if (datos.getTelefono() != null) usuario.setTelefono(datos.getTelefono());
        if (datos.getFechaNacimiento() != null) usuario.setFechaNacimiento(datos.getFechaNacimiento());
        try{
            usuario.validar();
        }catch(IllegalArgumentException ex){
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
        return ResponseEntity.ok(usuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean removed = store.getUsuarios().removeIf(u -> u.getId().equals(id));
        if (!removed) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}
