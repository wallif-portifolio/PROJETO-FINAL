package service;

import model.Pagamento;
import model.Reserva;
import dao.daoPagamento;
import dao.daoReserva;
import java.time.LocalDateTime;
import java.util.List;
import exception.IDInvalido;
import exception.ReservaInvalida;
import exception.PagamentoInvalido;

public class PagamentoService {
    private daoPagamento pagamentoDao;
    private daoReserva reservaDao;

    public PagamentoService() {
        this.pagamentoDao = new daoPagamento();
        this.reservaDao = new daoReserva();
    }

    public Pagamento registrar(int reservaId, double valor, Pagamento.Metodo metodo) {
        Reserva reserva = reservaDao.buscar(reservaId);

        if (reserva == null) {
            throw new IDInvalido("Reserva não existe");
        }
        if (reserva.getStatus() == Reserva.Status.CANCELADA) {
            throw new ReservaInvalida("Reserva cancelada");
        }

        double total = reserva.calcularTotal();
        if (valor < total) {
            throw new RuntimeException("Valor insuficiente");
        }

        Pagamento pagamento = new Pagamento(0, reserva, valor, LocalDateTime.now(), metodo);
        pagamentoDao.salvar(pagamento);
        
        reserva.setStatus(Reserva.Status.CONFIRMADA);
        reservaDao.atualizar(reserva);
        return pagamento;
    }

    public List<Pagamento> listar() {
        List<Pagamento> pagamentos = pagamentoDao.listar();
        if (pagamentos.isEmpty()) {
            throw new PagamentoInvalido("Sem pagamentos");
        }
        return pagamentos;
    }
}