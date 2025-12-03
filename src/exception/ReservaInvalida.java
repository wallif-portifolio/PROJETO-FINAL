package exception;

@SuppressWarnings("serial")
public class ReservaInvalida extends RuntimeException {
    public ReservaInvalida(String mensagem) {
        super(mensagem);
    }
}