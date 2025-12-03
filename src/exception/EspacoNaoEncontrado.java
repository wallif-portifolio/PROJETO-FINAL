package exception;

@SuppressWarnings("serial")
public class EspacoNaoEncontrado extends RuntimeException {
    public EspacoNaoEncontrado(String mensagem) {
        super(mensagem);
    }
}