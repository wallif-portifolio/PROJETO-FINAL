package exception;

@SuppressWarnings("serial")
public class ReservaNaoEncontrada extends RuntimeException {
    public ReservaNaoEncontrada(String mensagem) {
        super(mensagem);
    }
}