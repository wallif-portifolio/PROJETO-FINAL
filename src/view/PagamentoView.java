package view;

import service.PagamentoService;
import service.ReservaService;
import model.*;
import java.util.List;
import java.util.Scanner;

public class PagamentoView {
    private Scanner sc;
    private PagamentoService pagamentoService;
    private ReservaService reservaService;
    
    public PagamentoView() {
        this.sc = new Scanner(System.in);
        this.pagamentoService = new PagamentoService();
        this.reservaService = new ReservaService();
    }
    
    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- PAGAMENTOS ---");
            System.out.println("1. Registrar pagamento");
            System.out.println("2. Listar pagamentos");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();
            
            switch (opcao) {
                case 1:
                    registrarPagamento();
                    break;
                case 2:
                    listarPagamentos();
                    break;
            }
        } while (opcao != 0);
    }
    
    private void registrarPagamento() {
        try {
            System.out.println("Reservas pendentes:");
            List<Reserva> reservas = reservaService.listar();
            boolean temPendentes = false;
            
            for (Reserva reserva : reservas) {
                if (reserva.getStatus() == Reserva.Status.PENDENTE && reserva.getEspaco() != null) {
                    System.out.println("ID: " + reserva.getId() + 
                                     " | " + reserva.getEspaco().getNome() + 
                                     " | R$" + reserva.calcularTotal());
                    temPendentes = true;
                }
            }
            
            if (!temPendentes) {
                System.out.println("Nenhuma reserva pendente");
                return;
            }
            
            System.out.print("ID da reserva: ");
            int reservaId = sc.nextInt();
            sc.nextLine();
            
            Reserva reserva = null;
            for (Reserva r : reservas) {
                if (r.getId() == reservaId && r.getStatus() == Reserva.Status.PENDENTE) {
                    reserva = r;
                    break;
                }
            }
            
            if (reserva == null) {
                System.out.println("Reserva não encontrada");
                return;
            }
            
            double valorReserva = reserva.calcularTotal();
            System.out.println("Valor da reserva: R$" + valorReserva);
            
            System.out.print("Valor pago: R$");
            double valorPago = sc.nextDouble();
            sc.nextLine();
            
            System.out.println("Método de pagamento:");
            System.out.println("1. PIX");
            System.out.println("2. Cartão");
            System.out.println("3. Dinheiro");
            System.out.print("Escolha: ");
            int metodoOpcao = sc.nextInt();
            sc.nextLine();
            
            Pagamento.Metodo metodo;
            switch (metodoOpcao) {
                case 1:
                    metodo = Pagamento.Metodo.PIX;
                    break;
                case 2:
                    metodo = Pagamento.Metodo.CARTAO;
                    break;
                case 3:
                    metodo = Pagamento.Metodo.DINHEIRO;
                    break;
                default:
                    System.out.println("Método inválido");
                    return;
            }
            
            Pagamento pagamento = pagamentoService.registrar(reservaId, valorPago, metodo);
            System.out.println("Pagamento registrado - ID: " + pagamento.getId());
            System.out.println("Troco: R$" + (valorPago - valorReserva));
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void listarPagamentos() {
        try {
            List<Pagamento> pagamentos = pagamentoService.listar();
            for (Pagamento pagamento : pagamentos) {
                if (pagamento != null && pagamento.getReserva() != null) {
                    System.out.println("ID: " + pagamento.getId() + 
                                     " | Reserva: " + pagamento.getReserva().getId() + 
                                     " | Valor: R$" + pagamento.getValor() + 
                                     " | Método: " + pagamento.getMetodo());
                }
            }
            System.out.println("Total: " + pagamentos.size() + " pagamentos");
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}