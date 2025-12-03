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

    public List<Reserva> porPeriodo(LocalDateTime inicio, LocalDateTime fim) {
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

    public String getEspacoMaisUsado() {
        List<Reserva> reservas = reservaDao.listar();
        
        if (reservas.size() == 0) {
            return "";
        }
        
        String[] tipos = new String[reservas.size()];
        int[] contagens = new int[reservas.size()];
        int total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipoEspaco = getTipoEspaco(reserva.getEspaco());
                    
                    boolean achou = false;
                    for (int i = 0; i < total; i++) {
                        if (tipos[i].equals(tipoEspaco)) {
                            contagens[i]++;
                            achou = true;
                            break;
                        }
                    }
                    
                    if (!achou) {
                        tipos[total] = tipoEspaco;
                        contagens[total] = 1;
                        total++;
                    }
                }
            }
        }
        
        if (total == 0) {
            return "";
        }
        
        // Encontrar a maior quantidade
        int maior = contagens[0];
        for (int i = 1; i < total; i++) {
            if (contagens[i] > maior) {
                maior = contagens[i];
            }
        }
        
        // Verificar se há empate
        int empate = 0;
        for (int i = 0; i < total; i++) {
            if (contagens[i] == maior) {
                empate++;
            }
        }
        
        // Se tiver empate, retorna "Empate"
        if (empate > 1) {
            return "Empate";
        }
        
        // Se não tiver empate, retorna o tipo
        for (int i = 0; i < total; i++) {
            if (contagens[i] == maior) {
                return tipos[i];
            }
        }
        
        return "";
    }

    public int getQuantidadeEspacoMaisUsado() {
        List<Reserva> reservas = reservaDao.listar();
        
        if (reservas.size() == 0) {
            return 0;
        }
        
        String[] tipos = new String[reservas.size()];
        int[] contagens = new int[reservas.size()];
        int total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipoEspaco = getTipoEspaco(reserva.getEspaco());
                    
                    boolean achou = false;
                    for (int i = 0; i < total; i++) {
                        if (tipos[i].equals(tipoEspaco)) {
                            contagens[i]++;
                            achou = true;
                            break;
                        }
                    }
                    
                    if (!achou) {
                        tipos[total] = tipoEspaco;
                        contagens[total] = 1;
                        total++;
                    }
                }
            }
        }
        
        if (total == 0) {
            return 0;
        }
        
        int maior = contagens[0];
        for (int i = 1; i < total; i++) {
            if (contagens[i] > maior) {
                maior = contagens[i];
            }
        }
        
        return maior;
    }

    public String getDetalhesEspacos() {
        List<Reserva> reservas = reservaDao.listar();
        
        if (reservas.size() == 0) {
            return "";
        }
        
        String[] tipos = new String[reservas.size()];
        int[] contagens = new int[reservas.size()];
        int total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                if (reserva.getEspaco() != null) {
                    String tipoEspaco = getTipoEspaco(reserva.getEspaco());
                    
                    boolean achou = false;
                    for (int i = 0; i < total; i++) {
                        if (tipos[i].equals(tipoEspaco)) {
                            contagens[i]++;
                            achou = true;
                            break;
                        }
                    }
                    
                    if (!achou) {
                        tipos[total] = tipoEspaco;
                        contagens[total] = 1;
                        total++;
                    }
                }
            }
        }
        
        if (total == 0) {
            return "";
        }
        
        // Construir string com todos os tipos e quantidades
        String resultado = "";
        for (int i = 0; i < total; i++) {
            resultado += tipos[i] + ": " + contagens[i] + "  ";
        }
        
        return resultado.trim();
    }

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

    public double getFaturamentoTotal() {
        List<Reserva> reservas = reservaDao.listar();
        double total = 0;
        
        for (Reserva reserva : reservas) {
            if (reserva.getStatus() == Reserva.Status.CONFIRMADA) {
                total += reserva.calcularTotal();
            }
        }
        
        return total;
    }

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

    public double getFaturamentoCabine() {
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
}