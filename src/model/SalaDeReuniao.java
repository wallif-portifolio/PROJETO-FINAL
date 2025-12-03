package model;

public class SalaDeReuniao extends Espaco {
    public boolean projetor;

    public SalaDeReuniao(int id, String nome, int capacidade, boolean disponivel, 
                        double precoPorHora, boolean projetor) {
        super(id, nome, capacidade, disponivel, precoPorHora);
        this.projetor = projetor;
    }

    public boolean isProjetor() {
        return projetor;
    }

    public void setProjetor(boolean projetor) {
        this.projetor = projetor;
    }

    @Override
    public double calcularCustoReserva(double horas) {
        double total = getPrecoPorHora() * horas;
        if (projetor) {
            total += 15.0;
        }
        return total;
    }
}