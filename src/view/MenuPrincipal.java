package view;

import java.util.Scanner;

public class MenuPrincipal {
    private Scanner sc;
    
    public MenuPrincipal() {
        this.sc = new Scanner(System.in);
    }
    
    public void mostrarMenu() {
        int opcao;
        do {
            System.out.println("\n=== COWORKING MANAGER ===");
            System.out.println("1. Gerenciar Espaços");
            System.out.println("2. Gerenciar Reservas");
            System.out.println("3. Gerenciar Pagamentos");
            System.out.println("4. Relatórios");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();
            
            switch (opcao) {
                case 1:
                    new EspacoView().menu();				
                    break;
                case 2:
                    new ReservaView().menu();
                    break;
                case 3:
                    new PagamentoView().menu();
                    break;
                case 4:
                    new RelatorioView().menu();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }
    
    public static void main(String[] args) {
        new MenuPrincipal().mostrarMenu();
    }
}