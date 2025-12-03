package exception;

@SuppressWarnings("serial")
public class IDInvalido extends RuntimeException {
    public IDInvalido(String mensagem) {
        super(mensagem);
    }
}