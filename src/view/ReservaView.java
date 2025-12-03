package view;

import service.ReservaService;
import service.EspacoService;
import model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ReservaView {
    private Scanner sc;
    private ReservaService reservaService;
    private EspacoService espacoService;
    
    public ReservaView() {
        this.sc = new Scanner(System.in);
        this.reservaService = new ReservaService();
        this.espacoService = new EspacoService();
    }
    
    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- RESERVAS ---");
            System.out.println("1. Nova reserva");
            System.out.println("2. Cancelar reserva");
            System.out.println("3. Listar reservas");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();
            
            switch (opcao) {
                case 1:
                    novaReserva();
                    break;
                case 2:
                    cancelarReserva();
                    break;
                case 3:
                    listarReservas();
                    break;
            }
        } while (opcao != 0);
    }
    
    private void novaReserva() {
        try {
            System.out.println("Espaços disponíveis:");
            List<Espaco> espacos = espacoService.listar();
            for (Espaco espaco : espacos) {
                if (espaco.isDisponivel()) {
                    System.out.println("ID: " + espaco.getId() + 
                                     " | " + espaco.getNome() + 
                                     " | R$" + espaco.getPrecoPorHora() + "/hora");
                }
            }
            
            System.out.print("ID do espaço: ");
            int idEspaco = sc.nextInt();
            sc.nextLine();
            
            Espaco espaco = espacoService.buscar(idEspaco);
            
            System.out.print("Data/hora início (AAAA-MM-DDTHH:MM): ");
            LocalDateTime inicio = LocalDateTime.parse(sc.nextLine());
            
            System.out.print("Data/hora fim (AAAA-MM-DDTHH:MM): ");
            LocalDateTime fim = LocalDateTime.parse(sc.nextLine());
            
            Reserva reserva = new Reserva(0, espaco, inicio, fim);
            reservaService.criar(reserva);
            
            System.out.println("Reserva criada - Valor: R$" + reserva.calcularTotal());
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private void cancelarReserva() {
        try {
            System.out.println("Reservas ativas:");
            List<Reserva> reservas = reservaService.listar();
            for (Reserva reserva : reservas) {
                if (reserva.getStatus() == Reserva.Status.CONFIRMADA || 
                    reserva.getStatus() == Reserva.Status.PENDENTE) {
                    System.out.println("ID: " + reserva.getId() + 
                                     " | " + reserva.getEspaco().getNome() + 
                                     " | " + reserva.getInicio() + 
                                     " | R$" + reserva.calcularTotal());
                }
            }
            
            System.out.print("ID da reserva: ");
            int id = sc.nextInt();
            sc.nextLine();
            
            System.out.print("Confirmar cancelamento? (s/n): ");
            String confirmacao = sc.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                reservaService.cancelar(id);
                System.out.println("Reserva cancelada");
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
    
    private void listarReservas() {
        try {
            List<Reserva> reservas = reservaService.listar();
            for (Reserva reserva : reservas) {
                String status = "";
                switch (reserva.getStatus()) {
                    case PENDENTE:
                        status = "Pendente";
                        break;
                    case CONFIRMADA:
                        status = "Confirmada";
                        break;
                    case CANCELADA:
                        status = "Cancelada";
                        break;
                }
                System.out.println("ID: " + reserva.getId() + 
                                 " | " + reserva.getEspaco().getNome() + 
                                 " | " + status + 
                                 " | R$" + reserva.calcularTotal());
            }
            System.out.println("Total: " + reservas.size() + " reservas");
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}