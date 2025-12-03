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
            System.out.println("1. Reservas por período");
            System.out.println("2. Faturamento");
            System.out.println("3. Espaço mais usado");
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
                    espacoMaisUsado();
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
            
            List<Reserva> reservas = relatorioService.porPeriodo(inicio, fim);
            
            System.out.println("\nReservas no período:");
            for (Reserva reserva : reservas) {
                System.out.println(reserva.getEspaco().getNome() + " - " + 
                                 reserva.getInicio() + " - R$" + 
                                 reserva.calcularTotal());
            }
            System.out.println("Total: " + reservas.size() + " reservas");
            
        } catch (Exception e) {
            System.out.println("Erro: Formato de data inválido");
        }
    }
    
    private void faturamento() {
        try {
            double total = relatorioService.getFaturamentoTotal();
            System.out.println("\nFaturamento total: R$" + total);
            
            double sala = relatorioService.getFaturamentoSalaReuniao();
            double cabine = relatorioService.getFaturamentoCabine();
            double auditorio = relatorioService.getFaturamentoAuditorio();
            
            System.out.println("Sala de Reunião: R$" + sala);
            System.out.println("Cabine Individual: R$" + cabine);
            System.out.println("Auditório: R$" + auditorio);
            
        } catch (Exception e) {
            System.out.println("Erro ao gerar faturamento");
        }
    }
    
    private void espacoMaisUsado() {
        try {
            String tipo = relatorioService.getEspacoMaisUsado();
            int quantidade = relatorioService.getQuantidadeEspacoMaisUsado();
            String detalhes = relatorioService.getDetalhesEspacos();
            
            if (tipo.isEmpty()) {
                System.out.println("\nNenhuma reserva confirmada.");
            } else if (tipo.equals("Empate")) {
            	System.out.println("\nDetalhes: " + detalhes);
                System.out.println("Empate! com " + quantidade + " reserva(s)");
            } else {
                System.out.println("\n" + tipo + ": " + quantidade);
                System.out.println("Outros: " + detalhes.replace(tipo + ": " + quantidade + "  ", ""));
            }
            
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }
}