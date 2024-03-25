package br.com.darlan.gestordeprojetos.service;

import java.util.List;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.service.exception.ExclusionNotAllowedException;
import br.com.darlan.gestordeprojetos.service.exception.NotFoundException;

public interface ProjetoService extends BusinessService<ProjetoDTO, Long> {

	ProjetoDTO recuperarProjetoParaExclusao(Long idPessoa) throws ExclusionNotAllowedException, NotFoundException;

	ProjetoDTO adicionarMembro(Long idProjeto, PessoaDTO funcionario);

    List<ProjetoDTO> listarPorGerente(Long idGerente);
}
