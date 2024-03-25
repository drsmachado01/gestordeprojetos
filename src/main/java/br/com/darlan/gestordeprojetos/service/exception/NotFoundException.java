package br.com.darlan.gestordeprojetos.service.exception;

public class NotFoundException extends RuntimeException {

	private static final long serialVersionUID = 4993882481328643745L;

	public NotFoundException(String message) {
		super(message);
	}
}
