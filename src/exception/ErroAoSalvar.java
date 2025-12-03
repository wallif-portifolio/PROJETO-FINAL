package exception;

@SuppressWarnings("serial")
public class ErroAoSalvar extends RuntimeException {
    public ErroAoSalvar(String mensagem) {
        super(mensagem);
    }
}