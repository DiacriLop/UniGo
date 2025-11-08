package com.example.demo.model.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "viajes")
public class Viaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conductor_id")
    private Usuario conductor;

    private String origen;
    private String destino;

    @Column(name = "fecha_salida")
    private LocalDate fechaSalida;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    private BigDecimal precio;

    @Column(name = "tipo_servicio")
    private String tipoServicio;

    @Column(name = "cupos_totales")
    private int cuposTotales;

    @Column(name = "cupos_disponibles")
    private int cuposDisponibles;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.programado;

    @Column(name = "creado_en")
    private LocalDateTime creadoEn = LocalDateTime.now();

    public enum Estado {
        programado, en_curso, completado, cancelado
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getConductor() {
        return conductor;
    }

    public void setConductor(Usuario conductor) {
        this.conductor = conductor;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public LocalTime getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(LocalTime horaSalida) {
        this.horaSalida = horaSalida;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public String getTipoServicio() {
        return tipoServicio;
    }

    public void setTipoServicio(String tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getCuposTotales() {
        return cuposTotales;
    }

    public void setCuposTotales(int cuposTotales) {
        this.cuposTotales = cuposTotales;
    }

    public int getCuposDisponibles() {
        return cuposDisponibles;
    }

    public void setCuposDisponibles(int cuposDisponibles) {
        this.cuposDisponibles = cuposDisponibles;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    
}
