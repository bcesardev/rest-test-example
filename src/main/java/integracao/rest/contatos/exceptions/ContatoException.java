package integracao.rest.contatos.exceptions;

@SuppressWarnings("serial")
public class ContatoException extends Exception {

	public ContatoException() {
		super();
	}

	public ContatoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ContatoException(String message, Throwable cause) {
		super(message, cause);
	}

	public ContatoException(String message) {
		super(message);
	}

	public ContatoException(Throwable cause) {
		super(cause);
	}

}
