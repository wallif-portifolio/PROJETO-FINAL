package service;

import model.Reserva;
import dao.daoEspaco;
import dao.daoReserva;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import exception.ReservaInvalida;

public class ReservaService {
    private daoReserva reservaDao;

    public ReservaService() {
        new daoEspaco();
        this.reservaDao = new daoReserva();
    }

    private boolean temSobreposicao(Reserva nova) {
        List<Reserva> todas = reservaDao.listar();
        for (Reserva existente : todas) {
            if (nova.getEspaco().getId() == existente.getEspaco().getId() &&
                !nova.getFim().isBefore(existente.getInicio()) &&
                !nova.getInicio().isAfter(existente.getFim())) {
                return true;
            }
        }
        return false;
    }

    public Reserva criar(Reserva reserva) {
        if (!reserva.getInicio().isBefore(reserva.getFim())) {
            throw new ReservaInvalida("Data inválida");
        }
        if (!reserva.getEspaco().isDisponivel()) {
            throw new ReservaInvalida("Espaço indisponível");
        }
        if (temSobreposicao(reserva)) {
            throw new ReservaInvalida("Horário ocupado");
        }
        reservaDao.salvar(reserva);
        return reserva;
    }

    public void cancelar(int id) {
        Reserva reserva = reservaDao.buscar(id);
        if (reserva == null) {
            throw new ReservaInvalida("Reserva não existe");
        }

        LocalDateTime agora = LocalDateTime.now();
        Duration diferenca = Duration.between(agora, reserva.getInicio());
        double horas = diferenca.toHours();

        if (horas < 24) {
            double total = reserva.calcularTotal();
            double taxa = total * 0.20;
            reserva.setTaxa(taxa);
        }
        reserva.setStatus(Reserva.Status.CANCELADA);
        reservaDao.atualizar(reserva);
    }

    public List<Reserva> listar() {
        List<Reserva> reservas = reservaDao.listar();
        if (reservas.isEmpty()) {
            throw new ReservaInvalida("Sem reservas");
        }
        return reservas;
    }
}