package br.com.darlan.gestordeprojetos.util.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.util.BusinessMapper;
import br.com.darlan.gestordeprojetos.util.ConverterUtil;

@Component
public class PessoaMapper implements BusinessMapper<Pessoa, PessoaDTO> {

	@Override
	public PessoaDTO entityToDTO(Pessoa e) {
		return PessoaDTO.builder()
				.idPessoa(e.getIdPessoa())
				.nome(e.getNome())
				.cpf(e.getCpf())
				.dataNascimento(ConverterUtil.toString(e.getDataNascimento(), ConverterUtil.DATE_PATTERN))
				.funcionario(e.getFuncionario())
				.gerente(e.getGerente())
				.build();
	}

	@Override
	public Pessoa dtoToEntity(PessoaDTO d) {
		return Pessoa.builder()
				.idPessoa(d.getIdPessoa())
				.nome(d.getNome())
				.cpf(d.getCpf())
				.dataNascimento(ConverterUtil.toLocalDate(d.getDataNascimento(), ConverterUtil.DATE_PATTERN))
				.funcionario(d.getFuncionario())
				.gerente(d.getGerente())
				.build();
	}

	@Override
	public List<PessoaDTO> listEntityToListDTO(List<Pessoa> listE) {
		return listE.stream().map(this::entityToDTO).toList();
	}

	@Override
	public List<Pessoa> listDTOToListEntity(List<PessoaDTO> listD) {
		return listD.stream().map(this::dtoToEntity).toList();
	}

}
