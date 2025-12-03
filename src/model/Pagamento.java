package model;

import java.time.LocalDateTime;

public class Pagamento {
    private int id;
    private Reserva reserva;
    private double valor;
    private LocalDateTime data;
    private Metodo metodo;

    public enum Metodo {
        PIX, CARTAO, DINHEIRO
    }

    public Pagamento(int id, Reserva reserva, double valor, LocalDateTime data, Metodo metodo) {
        this.id = id;
        this.reserva = reserva;
        this.valor = valor;
        this.data = data;
        this.metodo = metodo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Metodo getMetodo() {
        return metodo;
    }

    public void setMetodo(Metodo metodo) {
        this.metodo = metodo;
    }
}