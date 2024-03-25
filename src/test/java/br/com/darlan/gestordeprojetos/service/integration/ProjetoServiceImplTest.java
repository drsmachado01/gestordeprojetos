package br.com.darlan.gestordeprojetos.service.integration;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;
import br.com.darlan.gestordeprojetos.service.PessoaService;
import br.com.darlan.gestordeprojetos.service.ProjetoService;
import br.com.darlan.gestordeprojetos.service.exception.ExclusionNotAllowedException;
import br.com.darlan.gestordeprojetos.service.exception.NotFoundException;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnabledIf(value = "#{environment.getActiveProfiles()[0] == 'test'}", loadContext = true)
class ProjetoServiceImplTest {
	
	@Autowired
	private ProjetoService projetoService;
	
	@Autowired
	private PessoaService pessoaService;
	
	@Test
	void testListar() {
		ProjetoDTO dto = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
		dto.setGerente(criaEPersisteGerente().getIdPessoa());
		dto = projetoService.persistir(dto);
		
		List<ProjetoDTO> projetos = projetoService.listar();
		assertNotNull(projetos);
		assertEquals(1, projetos.size());
		ProjetoDTO result = projetos.get(0);
		assertNotNull(result);
		assertEquals(1L, result.getIdProjeto());
	}
	
	@Test
	void testAdicionar() {
		ProjetoDTO dto = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
		dto.setGerente(criaEPersisteGerente().getIdPessoa());
		dto = projetoService.persistir(dto);
		assertNotNull(dto);
		assertEquals(1L, dto.getIdProjeto());
	}
	
	@Test
	void testPesquisarPorId() {
		ProjetoDTO dto = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
		dto.setGerente(criaEPersisteGerente().getIdPessoa());
		dto = projetoService.persistir(dto);
		
		ProjetoDTO result = projetoService.pesquisarPorId(dto.getIdProjeto());
		assertNotNull(result);
		assertEquals(1L, result.getIdProjeto());
		assertEquals(BigDecimal.valueOf(1000.0).doubleValue(), result.getOrcamento().doubleValue());		
	}
	
	@Test
	void testAtualizar() {
		ProjetoDTO dto = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
		PessoaDTO gerente = criaEPersisteGerente();
		dto.setGerente(gerente.getIdPessoa());
		dto = projetoService.persistir(dto);
		Long idProjeto = dto.getIdProjeto();

		ProjetoDTO dtoUpdated = buildProjetoDTO(StatusProjeto.ANALISE_APROVADA.getStatus(), "ALTO");
		dtoUpdated.setGerente(gerente.getIdPessoa());
		
		ProjetoDTO result = projetoService.atualizar(idProjeto, dtoUpdated);
		assertNotNull(result);
		assertEquals(StatusProjeto.ANALISE_APROVADA.getStatus(), result.getStatus());
	}
	
	@Test
	void testExcluir_quandoStatusProjetoNaoPermiteExcluir() {
		ProjetoDTO dto = buildProjetoDTO(StatusProjeto.INICIADO.getStatus(), "ALTO");
		dto.setGerente(criaEPersisteGerente().getIdPessoa());
		dto = projetoService.persistir(dto);
		
		try {
			projetoService.excluir(dto.getIdProjeto());
		} catch (ExclusionNotAllowedException e) {
			assertNotNull(e);
			assertEquals("Projeto em status que impede a exclusão!", e.getMessage());
		}
	}
	
	@Test
	void testExcluir() {
		ProjetoDTO dto = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
		dto.setGerente(criaEPersisteGerente().getIdPessoa());
		dto = projetoService.persistir(dto);
		Long idProjeto = dto.getIdProjeto();
		projetoService.excluir(dto.getIdProjeto());
		
		try {
			projetoService.pesquisarPorId(idProjeto);
		} catch (NotFoundException e) {
			assertNotNull(e);
			assertEquals("Projeto não encontrado!", e.getMessage());
		}
		
	}

	private PessoaDTO criaEPersisteGerente() {
        PessoaDTO pessoaDTO = buildGerente();
        pessoaDTO.setDataNascimento("1980-10-20");
        return pessoaService.persistir(pessoaDTO);
	}
}
