package com.mcic.store;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import com.mcic.models.Libro;
import com.mcic.models.Usuario;
import com.mcic.models.Prestamo;

@Component
public class InMemoryStore {
    private final List<Libro> libros = new ArrayList<>();
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Prestamo> prestamos = new ArrayList<>();

    private final AtomicLong libroIdCounter = new AtomicLong(1);
    private final AtomicLong usuarioIdCounter = new AtomicLong(1);
    private final AtomicLong prestamoIdCounter = new AtomicLong(1);

    public List<Libro> getLibros() {
        return libros;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public String nextLibroId() {
        return String.valueOf(libroIdCounter.getAndIncrement());
    }

    public String nextUsuarioId() {
        return String.valueOf(usuarioIdCounter.getAndIncrement());
    }

    public String nextPrestamoId() {
        return String.valueOf(prestamoIdCounter.getAndIncrement());
    }
}
