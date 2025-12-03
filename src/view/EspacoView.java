package view;

import service.EspacoService;
import model.*;
import java.util.List;
import java.util.Scanner;

public class EspacoView {
    private Scanner sc;
    private EspacoService espacoService;
    
    public EspacoView() {
        this.sc = new Scanner(System.in);
        this.espacoService = new EspacoService();
    }
    
    public void menu() {
        int opcao;
        do {
            System.out.println("\n--- ESPAÇOS ---");
            System.out.println("1. Cadastrar");
            System.out.println("2. Listar");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar");
            System.out.println("5. Deletar");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            opcao = sc.nextInt();
            sc.nextLine();
            
            switch (opcao) {
                case 1:
                    cadastrar();
                    break;
                case 2:
                    listar();
                    break;
                case 3:
                    buscarPorId();
                    break;
                case 4:
                    atualizar();
                    break;
                case 5:
                    deletar();
                    break;
            }
        } while (opcao != 0);
    }
    
    private void cadastrar() {
        try {
            System.out.print("Nome: ");
            String nome = sc.nextLine();
            
            System.out.print("Capacidade: ");
            int capacidade = sc.nextInt();
            sc.nextLine();
            
            System.out.println("Tipo:");
            System.out.println("1. Sala de Reunião");
            System.out.println("2. Cabine Individual");
            System.out.println("3. Auditório");
            System.out.print("Escolha: ");
            int tipo = sc.nextInt();
            sc.nextLine();
            
            Espaco espaco;
            switch (tipo) {
                case 1:
                    System.out.print("Incluir projetor? (sim/nao): ");
                    String resposta = sc.nextLine();
                    boolean projetor = resposta.equalsIgnoreCase("sim");
                    espaco = new SalaDeReuniao(0, nome, capacidade, true, 50.0, projetor);
                    break;
                case 2:
                    espaco = new CabineIndividual(0, nome, capacidade, true, 30.0);
                    break;
                case 3:
                    espaco = new Auditorio(0, nome, capacidade, true, 100.0);
                    break;
                default:
                    System.out.println("Tipo inválido");
                    return;
            }
            
            espacoService.criar(espaco);
            System.out.println("Espaço cadastrado com sucesso");
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void listar() {
        try {
            List<Espaco> espacos = espacoService.listar();
            for (Espaco espaco : espacos) {
                if (espaco != null) {
                    System.out.println("ID: " + espaco.getId() + 
                                     " | Nome: " + espaco.getNome() + 
                                     " | Capacidade: " + espaco.getCapacidade() +
                                     " | Preço: R$" + espaco.getPrecoPorHora() +
                                     " | Tipo: " + espaco.getClass().getSimpleName());
                    
                    if (espaco instanceof SalaDeReuniao) {
                        SalaDeReuniao sala = (SalaDeReuniao) espaco;
                        System.out.println("  Projetor: " + (sala.isProjetor() ? "Sim" : "Não"));
                    }
                }
            }
            System.out.println("Total: " + espacos.size() + " espaços");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void buscarPorId() {
        try {
            System.out.print("ID do espaço: ");
            int id = sc.nextInt();
            sc.nextLine();
            
            Espaco espaco = espacoService.buscar(id);
            if (espaco != null) {
                System.out.println("ID: " + espaco.getId());
                System.out.println("Nome: " + espaco.getNome());
                System.out.println("Capacidade: " + espaco.getCapacidade());
                System.out.println("Preço: R$" + espaco.getPrecoPorHora());
                System.out.println("Tipo: " + espaco.getClass().getSimpleName());
                System.out.println("Disponível: " + (espaco.isDisponivel() ? "Sim" : "Não"));
                
                if (espaco instanceof SalaDeReuniao) {
                    SalaDeReuniao sala = (SalaDeReuniao) espaco;
                    System.out.println("Projetor: " + (sala.isProjetor() ? "Sim" : "Não"));
                } else if (espaco instanceof Auditorio) {
                    Auditorio auditorio = (Auditorio) espaco;
                    System.out.println("Taxa fixa: R$" + auditorio.getTaxa());
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void atualizar() {
        try {
            System.out.print("ID do espaço: ");
            int id = sc.nextInt();
            sc.nextLine();
            
            Espaco espaco = espacoService.buscar(id);
            if (espaco == null) {
                System.out.println("Espaço não encontrado");
                return;
            }
            
            System.out.print("Novo nome: ");
            espaco.setNome(sc.nextLine());
            
            System.out.print("Nova capacidade: ");
            espaco.setCapacidade(sc.nextInt());
            sc.nextLine();
            
            if (espaco instanceof SalaDeReuniao) {
                System.out.print("Incluir projetor? (sim/nao): ");
                String resposta = sc.nextLine();
                boolean projetor = resposta.equalsIgnoreCase("sim");
                ((SalaDeReuniao) espaco).setProjetor(projetor);
            }
            
            espacoService.atualizar(espaco);
            System.out.println("Espaço atualizado");
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void deletar() {
        try {
            System.out.print("ID do espaço: ");
            int id = sc.nextInt();
            sc.nextLine();
            
            System.out.print("Confirmar deleção? (s/n): ");
            String confirmacao = sc.nextLine();
            
            if (confirmacao.equalsIgnoreCase("s")) {
                espacoService.remover(id);
                System.out.println("Espaço deletado");
            }
            
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
}