package exception;

@SuppressWarnings("serial")
public class CapacidadeInvalida extends RuntimeException {
    public CapacidadeInvalida(String mensagem) {
        super(mensagem);
    }
}