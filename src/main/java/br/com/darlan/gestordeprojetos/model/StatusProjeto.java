package br.com.darlan.gestordeprojetos.model;

public enum StatusProjeto {
	EM_ANALISE("Em Análise"),
	ANALISE_REALIZADA("Análise Realizada"),
	ANALISE_APROVADA("Análise Aprovada"),
	INICIADO("Iniciado"),
	PLANEJADO("Planejado"),
	EM_ANDAMENTO("Em Andamento"),
	ENCERRADO("Encerrado"),
	CANCELADO("Cancelado");

	private String status;

	StatusProjeto(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return status;
	}

	public static StatusProjeto fromString(String status) {
		for(StatusProjeto sp : StatusProjeto.values()) {
			if(sp.getStatus().equals(status)) {
				return sp;
			}
		}
		return null;
	}
}
