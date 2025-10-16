package com.example.demo.model.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reserva_id")
    private Reserva reserva;

    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    private Metodo metodo = Metodo.nequi;

    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.pendiente;

    @Column(name = "qr_nequi")
    private String qrNequi;

    @Column(name = "referencia_transaccion")
    private String referenciaTransaccion;

    @Column(name = "pagado_en")
    private LocalDateTime pagadoEn;

    public enum Metodo {
        nequi, tarjeta, pse, efectivo
    }

    public enum Estado {
        pendiente, exitoso, fallido, reembolsado
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Metodo getMetodo() {
        return metodo;
    }

    public void setMetodo(Metodo metodo) {
        this.metodo = metodo;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getQrNequi() {
        return qrNequi;
    }

    public void setQrNequi(String qrNequi) {
        this.qrNequi = qrNequi;
    }

    public String getReferenciaTransaccion() {
        return referenciaTransaccion;
    }

    public void setReferenciaTransaccion(String referenciaTransaccion) {
        this.referenciaTransaccion = referenciaTransaccion;
    }

    public LocalDateTime getPagadoEn() {
        return pagadoEn;
    }

    public void setPagadoEn(LocalDateTime pagadoEn) {
        this.pagadoEn = pagadoEn;
    }

}
