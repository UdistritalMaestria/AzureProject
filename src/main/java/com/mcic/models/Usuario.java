package com.mcic.models;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private String fechaNacimiento; // formato yyyy-MM-dd
    private LocalDateTime fechaRegistro;

    public Usuario() {
    }

    public Usuario(String id, String nombre, String email, String telefono, String fechaNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void validar() throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("El email no puede estar vacío");
        }
        String emailClean = email.trim();
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.compile(emailRegex).matcher(emailClean).matches()) {
            throw new IllegalArgumentException("El email no tiene un formato válido");
        }
        if (telefono != null && !telefono.trim().isEmpty()) {
            String telClean = telefono.replaceAll("[^0-9]","");
            if (telClean.length() < 7) {
                throw new IllegalArgumentException("El teléfono no parece válido");
            }
        }

        if (fechaNacimiento != null && !fechaNacimiento.trim().isEmpty()) {
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(fechaNacimiento);
                if (d.isAfter(java.time.LocalDate.now())) {
                    throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
                }
            } catch (java.time.format.DateTimeParseException ex) {
                throw new IllegalArgumentException("Formato de fecha de nacimiento inválido (usar yyyy-MM-dd)");
            }
        }
    }
}
