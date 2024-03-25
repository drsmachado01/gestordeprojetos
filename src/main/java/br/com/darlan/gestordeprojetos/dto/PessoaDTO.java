package br.com.darlan.gestordeprojetos.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PessoaDTO {
	private Long idPessoa;
	private String nome;
	private String dataNascimento;
	private String cpf;
	private Boolean funcionario;
	private Boolean gerente;
}
