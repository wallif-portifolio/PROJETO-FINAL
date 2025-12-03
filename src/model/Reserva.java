package model;

import java.time.Duration;
import java.time.LocalDateTime;

import exception.ReservaInvalida;

public class Reserva {
    private int id;
    private Espaco espaco;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private Status status;
    private double taxa;

    public enum Status {
        PENDENTE, CONFIRMADA, CANCELADA
    }

    public Reserva(int id, Espaco espaco, LocalDateTime inicio, LocalDateTime fim) {
        if (!inicio.isBefore(fim)) {
            throw new ReservaInvalida("Data inválida");
        }
        
        this.id = id;
        this.espaco = espaco;
        this.inicio = inicio;
        this.fim = fim;
        this.status = Status.PENDENTE;
        this.taxa = 0.0;
    }

    public double calcularHoras() {
        Duration tempo = Duration.between(inicio, fim);
        return tempo.toMinutes() / 60.0;
    }

    public double calcularTotal() {
        double horas = calcularHoras();
        return espaco.calcularCustoReserva(horas);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getTaxa() {
        return taxa;
    }

    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }
}