package model;

public class CabineIndividual extends Espaco {

    public CabineIndividual(int id, String nome, int capacidade, boolean disponivel, double precoPorHora) {
        super(id, nome, capacidade, disponivel, precoPorHora);
    }

    @Override
    public double calcularCustoReserva(double horas) {
        double total = getPrecoPorHora() * horas;
        if (horas > 4) {
            total = total * 0.9;
        }
        return total;
    }
}