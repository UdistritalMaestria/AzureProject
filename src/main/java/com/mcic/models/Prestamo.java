package com.mcic.models;

import java.time.LocalDateTime;

public class Prestamo {
    private String id;
    private String usuarioId;
    private String libroId;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean devuelto;

    public Prestamo() {
    }

    public Prestamo(String id, String usuarioId, String libroId) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.libroId = libroId;
        this.fechaInicio = LocalDateTime.now();
        this.devuelto = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getLibroId() {
        return libroId;
    }

    public void setLibroId(String libroId) {
        this.libroId = libroId;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public boolean isDevuelto() {
        return devuelto;
    }

    public void setDevuelto(boolean devuelto) {
        this.devuelto = devuelto;
    }
}
