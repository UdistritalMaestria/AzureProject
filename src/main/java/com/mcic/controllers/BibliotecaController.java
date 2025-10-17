package com.mcic.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mcic.models.Libro;

@Controller
public class BibliotecaController {

    final private List<Libro> libros = new ArrayList<>();
    final private AtomicLong idCounter = new AtomicLong();

    public BibliotecaController() {
        idCounter.set(1);
    }

    // Listar todos los libros
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("libros", libros);
        model.addAttribute("libro", new Libro());
        return "index";
    }

    // Agregar nuevo libro
    @PostMapping("/agregar")
    public String agregarLibro(@ModelAttribute Libro libro) {
        libro.setId(String.valueOf(idCounter.getAndIncrement()));
        libros.add(libro);
        return "redirect:/";
    }

    // Eliminar libro
    @GetMapping("/eliminar/{id}")
    public String eliminarLibro(@PathVariable String id) {
        libros.removeIf(libro -> libro.getId().equals(id));
        return "redirect:/";
    }

}
