package com.mcic.models;

public class Libro {
    private String id;
    private String titulo;
    private String autor;
    private String isbn;
    private int cantidad;

    public Libro() {
    }

    public Libro(String id, String titulo, String autor, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.cantidad = 1;
    }

    public Libro(String id, String titulo, String autor, String isbn, int cantidad) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.cantidad = Math.max(0, cantidad);
    }

    // Método de validación
    public void validar() throws IllegalArgumentException {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("El autor no puede estar vacío");
        }
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("El ISBN no puede estar vacío");
        }
        // Remover guiones y espacios para validar longitud
        String isbnLimpio = isbn.replaceAll("[\\s-]", "");
        if (isbnLimpio.length() < 10) {
            throw new IllegalArgumentException("El ISBN debe tener al menos 10 caracteres");
        }
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = Math.max(0, cantidad);
    }

    public void incrementarStock(int n) {
        if (n <= 0) return;
        this.cantidad += n;
    }

    public void decrementarStock(int n) throws IllegalStateException {
        if (n <= 0) return;
        if (this.cantidad < n) {
            throw new IllegalStateException("No hay suficientes unidades disponibles");
        }
        this.cantidad -= n;
    }
}