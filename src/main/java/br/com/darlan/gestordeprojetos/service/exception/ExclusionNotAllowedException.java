package br.com.darlan.gestordeprojetos.service.exception;

public class ExclusionNotAllowedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3577792045468832202L;
	
	public ExclusionNotAllowedException(String message) {
		super(message);
	}
}
