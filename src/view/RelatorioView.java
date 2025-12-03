package view;

import service.RelatorioService;
import model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class RelatorioView {
    private Scanner sc;
    private RelatorioService relatorioService;
    
    public RelatorioView() {
        this.sc = new Scanner(System.in);
        this.relatorioService = new RelatorioService();
    }
    
    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- RELATÓRIOS ---");
            System.out.println("1. Reservas realizadas em um período");
            System.out.println("2. Faturamento por tipo de espaço");
            System.out.println("3. Utilização por espaço");
            System.out.println("4. Top espaços mais utilizados");
            System.out.println("0. Voltar");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine();
            
            switch (opcao) {
                case 1:
                    reservasPeriodo();
                    break;
                case 2:
                    faturamento();
                    break;
                case 3:
                    utilizacaoPorTipo();
                    break;
                case 4:
                    topTiposEspacos();
                    break;
            }
        } while (opcao != 0);
    }
    
    private void reservasPeriodo() {
        try {
            System.out.print("Data inicial (ex: 2024-01-01T00:00): ");
            String inputInicio = sc.nextLine();
            
            System.out.print("Data final (ex: 2024-01-31T23:59): ");
            String inputFim = sc.nextLine();
            
            LocalDateTime inicio = LocalDateTime.parse(inputInicio);
            LocalDateTime fim = LocalDateTime.parse(inputFim);
            
            List<Reserva> reservas = relatorioService.reservasPorPeriodo(inicio, fim);
            
            System.out.println("\n=== RESERVAS NO PERÍODO ===");
            for (Reserva reserva : reservas) {
                System.out.println("• " + reserva.getEspaco().getNome() + 
                                 " | " + reserva.getInicio() + 
                                 " | R$" + reserva.calcularTotal());
            }
            System.out.println("Total: " + reservas.size() + " reservas");
            
        } catch (Exception e) {
            System.out.println("Erro: Formato de data inválido");
        }
    }
    
    private void faturamento() {
        try {
            double total = relatorioService.getFaturamentoTotal();
            double sala = relatorioService.getFaturamentoSalaReuniao();
            double cabine = relatorioService.getFaturamentoCabineIndividual();
            double auditorio = relatorioService.getFaturamentoAuditorio();
            
            System.out.println("\n FATURAMENTO POR TIPO ");
            System.out.println("Sala de Reunião: R$" + sala);
            System.out.println("Cabine Individual: R$" + cabine);
            System.out.println("Auditório: R$" + auditorio);
            System.out.println("\n");
            System.out.println("TOTAL GERAL: R$" + total);
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar faturamento");
        }
    }
    
    // AGORA: Utilização por TIPO de espaço
    private void utilizacaoPorTipo() {
        try {
            String[] utilizacao = relatorioService.getUtilizacaoPorTipo();
            
            System.out.println("\n UTILIZAÇÃO POR ESPAÇO (HORAS) ");
            if (utilizacao.length == 0) {
                System.out.println("Nenhuma reserva confirmada.");
            } else {
                for (String item : utilizacao) {
                    System.out.println("• " + item);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar utilização");
        }
    }
    
    private void topTiposEspacos() {
        try {
            String[] top = relatorioService.getTopTiposEspacosMaisUtilizados();
            
            System.out.println("\n TOP ESPAÇOS MAIS UTILIZADOS ");
            if (top.length == 0) {
                System.out.println("Nenhuma reserva confirmada.");
            } else {
                for (String item : top) {
                    System.out.println(item);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar ranking");
        }
    }
}