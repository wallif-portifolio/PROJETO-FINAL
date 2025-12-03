package service;

import dao.daoReserva;
import model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class RelatorioService {
    private daoReserva reservaDao;
    
    public RelatorioService() {
        this.reservaDao = new daoReserva();
    }

    // 1. Reservas realizadas em um período
    public List<Reserva> reservasPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        List<Reserva> todas = reservaDao.listar();
        List<Reserva> noPeriodo = new ArrayList<>();

        for (Reserva reserva : todas) {
            if (!reserva.getInicio().isBefore(inicio) &&
                !reserva.getFim().isAfter(fim)) {
                noPeriodo.add(reserva);
            }
        }
        return noPeriodo;
    }

    // 2. Faturamento por tipo de espaço
    public double getFaturamentoSalaReuniao() {
        List<Reserva> reservas = reservaDao.listar();
        double total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipo = getTipoEspaco(reserva.getEspaco());
                    if (tipo.equals("Sala de Reunião")) {
                        total += reserva.calcularTotal();
                    }
                }
            }
        }
        return total;
    }

    public double getFaturamentoCabineIndividual() {
        List<Reserva> reservas = reservaDao.listar();
        double total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipo = getTipoEspaco(reserva.getEspaco());
                    if (tipo.equals("Cabine Individual")) {
                        total += reserva.calcularTotal();
                    }
                }
            }
        }
        return total;
    }

    public double getFaturamentoAuditorio() {
        List<Reserva> reservas = reservaDao.listar();
        double total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipo = getTipoEspaco(reserva.getEspaco());
                    if (tipo.equals("Auditório")) {
                        total += reserva.calcularTotal();
                    }
                }
            }
        }
        return total;
    }

    public double getFaturamentoTotal() {
        return getFaturamentoSalaReuniao() + 
               getFaturamentoCabineIndividual() + 
               getFaturamentoAuditorio();
    }

    // 3. Utilização por TIPO de espaço (horas) - AGORA MOSTRA TIPO
    public String[] getUtilizacaoPorTipo() {
        List<Reserva> reservas = reservaDao.listar();
        List<String> tiposLista = new ArrayList<>();
        List<Double> horasLista = new ArrayList<>();
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipo = getTipoEspaco(reserva.getEspaco());
                    double horas = reserva.calcularHoras();
                    
                    boolean achou = false;
                    for (int i = 0; i < tiposLista.size(); i++) {
                        if (tiposLista.get(i).equals(tipo)) {
                            horasLista.set(i, horasLista.get(i) + horas);
                            achou = true;
                            break;
                        }
                    }
                    
                    if (!achou) {
                        tiposLista.add(tipo);
                        horasLista.add(horas);
                    }
                }
            }
        }
        
        String[] resultado = new String[tiposLista.size()];
        for (int i = 0; i < tiposLista.size(); i++) {
            resultado[i] = tiposLista.get(i) + " - " + horasLista.get(i) + " horas";
        }
        return resultado;
    }

    // 4. Top TIPOS de espaços mais utilizados (reservas)
    public String[] getTopTiposEspacosMaisUtilizados() {
        List<Reserva> reservas = reservaDao.listar();
        List<String> tiposLista = new ArrayList<>();
        List<Integer> contagensLista = new ArrayList<>();
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipo = getTipoEspaco(reserva.getEspaco());
                    
                    boolean achou = false;
                    for (int i = 0; i < tiposLista.size(); i++) {
                        if (tiposLista.get(i).equals(tipo)) {
                            contagensLista.set(i, contagensLista.get(i) + 1);
                            achou = true;
                            break;
                        }
                    }
                    
                    if (!achou) {
                        tiposLista.add(tipo);
                        contagensLista.add(1);
                    }
                }
            }
        }
        
        // Ordenar do maior para o menor
        for (int i = 0; i < contagensLista.size() - 1; i++) {
            for (int j = i + 1; j < contagensLista.size(); j++) {
                if (contagensLista.get(j) > contagensLista.get(i)) {
                    // Trocar contagens
                    int tempCont = contagensLista.get(i);
                    contagensLista.set(i, contagensLista.get(j));
                    contagensLista.set(j, tempCont);
                    
                    // Trocar tipos
                    String tempTipo = tiposLista.get(i);
                    tiposLista.set(i, tiposLista.get(j));
                    tiposLista.set(j, tempTipo);
                }
            }
        }
        
        String[] resultado = new String[tiposLista.size()];
        for (int i = 0; i < tiposLista.size(); i++) {
            resultado[i] = (i + 1) + ". " + tiposLista.get(i) + " (" + contagensLista.get(i) + " reservas)";
        }
        return resultado;
    }

    // Método auxiliar para pegar tipo formatado
    private String getTipoEspaco(model.Espaco espaco) {
        String className = espaco.getClass().getSimpleName();
        
        if (className.equals("SalaDeReuniao")) {
            return "Sala de Reunião";
        } else if (className.equals("CabineIndividual")) {
            return "Cabine Individual";
        } else if (className.equals("Auditorio")) {
            return "Auditório";
        } else {
            return className;
        }
    }
}