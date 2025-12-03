package model;

public class Auditorio extends Espaco {
    private static final double TAXA = 100.0;

    public Auditorio(int id, String nome, int capacidade, boolean disponivel, double precoPorHora) {
        super(id, nome, capacidade, disponivel, precoPorHora);
    }

    public double getTaxa() {
        return TAXA;
    }

    @Override
    public double calcularCustoReserva(double horas) {
        return (getPrecoPorHora() * horas) + TAXA;
    }
}