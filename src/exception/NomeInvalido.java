package exception;

@SuppressWarnings("serial")
public class NomeInvalido extends RuntimeException {
    public NomeInvalido(String mensagem) {
        super(mensagem);
    }
}