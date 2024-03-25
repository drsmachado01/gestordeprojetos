package br.com.darlan.gestordeprojetos.service;

import java.util.List;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;

public interface PessoaService extends BusinessService<PessoaDTO, Long> {

	List<PessoaDTO> listarGerentes();

}
