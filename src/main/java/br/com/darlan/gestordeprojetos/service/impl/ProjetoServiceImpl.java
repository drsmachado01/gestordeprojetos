package br.com.darlan.gestordeprojetos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.model.Projeto;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;
import br.com.darlan.gestordeprojetos.repository.PessoaRepository;
import br.com.darlan.gestordeprojetos.repository.ProjetoRepository;
import br.com.darlan.gestordeprojetos.service.ProjetoService;
import br.com.darlan.gestordeprojetos.service.exception.ExclusionNotAllowedException;
import br.com.darlan.gestordeprojetos.service.exception.NotFoundException;
import br.com.darlan.gestordeprojetos.util.BusinessMapper;

@Service
public class ProjetoServiceImpl implements ProjetoService {

	@Autowired
	private ProjetoRepository projetoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepo;
	
	@Autowired
	private BusinessMapper<Projeto, ProjetoDTO> projetoMapper;
	
	@Autowired
	private BusinessMapper<Pessoa, PessoaDTO> pessoaMapper;

	private List<StatusProjeto> statusQueNaoPodemSerExcluidos = List.of(StatusProjeto.INICIADO, StatusProjeto.EM_ANDAMENTO,
			StatusProjeto.ENCERRADO);

	@Override
	public ProjetoDTO persistir(ProjetoDTO dto) {
		Pessoa gerente = pessoaRepo.findById(dto.getGerente()).orElseThrow(() -> new NotFoundException("Gerente não encontrado"));
		dto.setStatus(StatusProjeto.EM_ANALISE.getStatus());
		Projeto projeto = projetoMapper.dtoToEntity(dto);
		projeto.setGerente(gerente);
		return projetoMapper.entityToDTO(projetoRepository.save(projeto));
	}

	@Override
	public List<ProjetoDTO> listar() {
		return projetoMapper.listEntityToListDTO(projetoRepository.findAll());
	}

	@Override
	public ProjetoDTO pesquisarPorId(Long id) throws NotFoundException {
		return projetoMapper.entityToDTO(projetoRepository.findById(id).orElseThrow(() -> new NotFoundException("Projeto não encontrado!")));
	}

	@Override
	public ProjetoDTO atualizar(Long id, ProjetoDTO d) {
		pesquisarPorId(id);
		d.setIdProjeto(id);
		return projetoMapper.entityToDTO(projetoRepository.save(projetoMapper.dtoToEntity(d)));
	}

	@Override
	public void excluir(Long id) throws NotFoundException {
		ProjetoDTO projetoDTO = pesquisarPorId(id);
		if(validarStatusParaExcluir(projetoDTO.getStatus())) {
			projetoRepository.deleteById(id);
			return;
		}
		throw new ExclusionNotAllowedException("Projeto em status que impede a exclusão!");
	}

	@Override
	public ProjetoDTO recuperarProjetoParaExclusao(Long idPessoa) throws ExclusionNotAllowedException, NotFoundException {
		ProjetoDTO projetoDTO = pesquisarPorId(idPessoa);
		if(validarStatusParaExcluir(projetoDTO.getStatus())) {
			return projetoDTO;
		}
		throw new ExclusionNotAllowedException("Projeto em status que impede a exclusão!");
	}

	private boolean validarStatusParaExcluir(String status) {
		for(StatusProjeto sp : statusQueNaoPodemSerExcluidos) {
			if(sp.getStatus().equals(status)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ProjetoDTO adicionarMembro(Long idProjeto, PessoaDTO funcionario) {

		funcionario.setFuncionario(Boolean.TRUE);
		funcionario.setGerente(Boolean.FALSE);
		Pessoa pessoa = pessoaRepo.save(pessoaMapper.dtoToEntity(funcionario));
		
		ProjetoDTO projetoDTO = pesquisarPorId(idProjeto);
		projetoDTO.adicionarMembro(pessoaMapper.entityToDTO(pessoa));
		return projetoMapper.entityToDTO(projetoRepository.save(projetoMapper.dtoToEntity(projetoDTO)));
	}

	@Override
	public List<ProjetoDTO> listarPorGerente(Long idGerente) {
		return projetoMapper.listEntityToListDTO(projetoRepository.findByGerenteIdPessoa(idGerente)
				.orElseThrow(() -> new NotFoundException("Nenhum projeto encontrado!")));
	}
}
