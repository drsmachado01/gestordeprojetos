package br.com.darlan.gestordeprojetos.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ProjetoDTO {
	private Long idProjeto;
	private String nome;
	private Date dataInicio;
	private Date dataPrevisaoFim;
	private Date dataFim;
	private String descricao;
	private String status;
	private BigDecimal orcamento;
	private String risco;
	private Long gerente;
	private List<PessoaDTO> membros;

	public void adicionarMembro(PessoaDTO membro) {
		if(null == getMembros()) {
			setMembros(new ArrayList<>());
		}
		getMembros().add(membro);
	}
}
