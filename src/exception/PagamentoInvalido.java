package exception;

@SuppressWarnings("serial")
public class PagamentoInvalido extends RuntimeException {
    public PagamentoInvalido(String mensagem) {
        super(mensagem);
    }
}