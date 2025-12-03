package service;

import dao.daoEspaco;
import dao.daoReserva;
import model.CabineIndividual;
import model.Espaco;
import java.util.List;
import exception.NomeInvalido;
import exception.CapacidadeInvalida;
import exception.IDInvalido;
import exception.EspacoInvalido;

public class EspacoService {
    private daoEspaco espacoDao;
    
    public EspacoService() {
        this.espacoDao = new daoEspaco();
        new daoReserva();
    }

    public void criar(Espaco espaco) {
        if (espaco.getNome() == null || espaco.getNome().trim().isEmpty()) {
            throw new NomeInvalido("Nome obrigatório");
        }
        if (espaco.getCapacidade() <= 0) {
            throw new CapacidadeInvalida("Capacidade inválida");
        }
        if (espaco instanceof CabineIndividual && espaco.getCapacidade() != 1) {
            throw new CapacidadeInvalida("Cabine só para 1 pessoa");
        }
        espacoDao.salvar(espaco);
    }

    public List<Espaco> listar() {
        List<Espaco> espacos = espacoDao.listar();
        if (espacos.isEmpty()) {
            throw new EspacoInvalido("Sem espaços");
        }
        return espacos;
    }

    public Espaco buscar(int id) {
        Espaco espaco = espacoDao.buscar(id);
        if (espaco == null) {
            throw new IDInvalido("ID não existe");
        }
        return espaco;
    }

    public void atualizar(Espaco espaco) {
        if (espaco.getNome() == null || espaco.getNome().trim().isEmpty()) {
            throw new NomeInvalido("Nome obrigatório");
        }
        if (espaco.getCapacidade() <= 0) {
            throw new CapacidadeInvalida("Capacidade inválida");
        }
        if (espaco instanceof CabineIndividual && espaco.getCapacidade() != 1) {
            throw new CapacidadeInvalida("Cabine só para 1 pessoa");
        }
        espacoDao.atualizar(espaco);
    }

    public void remover(int id) {
        Espaco espaco = espacoDao.buscar(id);
        if (espaco == null) {
            throw new IDInvalido("ID não existe");
        }
        espacoDao.deletar(id);
    }
}