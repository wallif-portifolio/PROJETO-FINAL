package exception;

@SuppressWarnings("serial")
public class EspacoInvalido extends RuntimeException {
    public EspacoInvalido(String mensagem) {
        super(mensagem);
    }
}