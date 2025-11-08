package com.example.demo.model.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "perfiles_conductor")
public class PerfilConductor {

    @Id
    @Column(name = "usuario_id")
    private Long usuarioId;

    private String carrera;
    private String semestre;

    @Column(name = "numero_licencia")
    private String numeroLicencia;

    @Column(name = "informacion_vehiculo")
    private String informacionVehiculo;

    @OneToOne
    @MapsId
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
    }

    public String getInformacionVehiculo() {
        return informacionVehiculo;
    }

    public void setInformacionVehiculo(String informacionVehiculo) {
        this.informacionVehiculo = informacionVehiculo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}

