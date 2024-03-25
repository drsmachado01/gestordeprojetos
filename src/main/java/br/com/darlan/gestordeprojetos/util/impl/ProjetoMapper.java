package br.com.darlan.gestordeprojetos.util.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.model.Projeto;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;
import br.com.darlan.gestordeprojetos.util.BusinessMapper;
import br.com.darlan.gestordeprojetos.util.ConverterUtil;

@Component
public class ProjetoMapper implements BusinessMapper<Projeto, ProjetoDTO> {

	@Autowired
	private PessoaMapper pessoaMapper;

	@Override
	public ProjetoDTO entityToDTO(Projeto e) {
		return ProjetoDTO.builder()
				.idProjeto(e.getIdProjeto())
				.nome(e.getNome())
				.descricao(e.getDescricao())
				.dataInicio(ConverterUtil.toDate(e.getDataInicio()))
				.dataPrevisaoFim(ConverterUtil.toDate(e.getDataPrevisaoFim()))
				.dataFim(ConverterUtil.toDate(e.getDataFim()))
				.orcamento(e.getOrcamento())
				.status(e.getStatus().getStatus())
				.gerente(mapearGerente(e.getGerente()))
				.risco(e.getRisco())
				.membros(getMembros(e))
				.build();
	}

	private Long mapearGerente(Pessoa gerente) {
		if(null == gerente) {
			return null;
		}
		return pessoaMapper.entityToDTO(gerente).getIdPessoa();
	}

	private List<PessoaDTO> getMembros(Projeto e) {
		if (e.getMembros() == null) {
			return new ArrayList<>();
		}
		return pessoaMapper.listEntityToListDTO(e.getMembros());
	}

	@Override
	public Projeto dtoToEntity(ProjetoDTO d) {
		return Projeto.builder()
				.idProjeto(d.getIdProjeto())
				.nome(d.getNome())
				.descricao(d.getDescricao())
				.dataInicio(ConverterUtil.toLocalDate(d.getDataInicio()))
				.dataPrevisaoFim(ConverterUtil.toLocalDate(d.getDataPrevisaoFim()))
				.dataFim(ConverterUtil.toLocalDate(d.getDataFim()))
				.orcamento(d.getOrcamento())
				.risco(d.getRisco())
				.status(StatusProjeto.fromString(d.getStatus()))
				.membros(getMembros(d))
				.build();
	}

	private List<Pessoa> getMembros(ProjetoDTO d) {
		if (d.getMembros() == null) {
			return null;
		}
		return pessoaMapper.listDTOToListEntity(d.getMembros());
	}

	@Override
	public List<ProjetoDTO> listEntityToListDTO(List<Projeto> listE) {
		return listE.stream().map(this::entityToDTO).toList();
	}

	@Override
	public List<Projeto> listDTOToListEntity(List<ProjetoDTO> listD) {
		return listD.stream().map(this::dtoToEntity).toList();
	}

}
