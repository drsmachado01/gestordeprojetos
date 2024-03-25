package br.com.darlan.gestordeprojetos.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;
import br.com.darlan.gestordeprojetos.service.PessoaService;
import br.com.darlan.gestordeprojetos.service.ProjetoService;
import br.com.darlan.gestordeprojetos.service.exception.ExclusionNotAllowedException;
import br.com.darlan.gestordeprojetos.util.TesteUtil;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildFuncionario;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildGerente;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ProjetoControllerTest {
	
	@Mock
	private ModelMap model;
	
	@Mock
	private BindingResult bindingResult;
	
	@Mock
	private ProjetoService projetoService;
	
	@Mock
	private PessoaService pessoaService;
	
	@InjectMocks
	private ProjetoController controller;

    @Test
    void testListarProjetos() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	List<ProjetoDTO> projetos = List.of(criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", gerente, new ArrayList<PessoaDTO>()));
    	when(projetoService.listar()).thenReturn(projetos);
    	
    	String result = controller.listarProjetos(model);
    	assertEquals("listar-projetos", result);
    	verify(model, times(1)).addAttribute("projetos", projetos);
    }

	@Test
    void testListarProjetosGerentes() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	List<ProjetoDTO> projetos = List.of(criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", gerente, new ArrayList<PessoaDTO>()));
    	when(projetoService.listarPorGerente(gerente.getIdPessoa())).thenReturn(projetos);
    	
    	String result = controller.listarProjetosGerentes(gerente.getIdPessoa(), model);
    	assertEquals("listar-projetos-gerente", result);
    	verify(model, times(1)).addAttribute("projetos", projetos);
    }
	
	@Test
	void testPrepararAdicionarProjeto() {
		String result = controller.adicionarProjeto(model);
		assertEquals("adicionar-projeto", result);
		verify(model, times(1)).addAttribute("projeto", ProjetoDTO.builder().build());
	}

    @Test
    void testSalvarProjeto() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	when(projetoService.persistir(projeto)).thenReturn(projeto);
    	when(bindingResult.hasErrors()).thenReturn(false);
    
    	String result = controller.salvarProjeto(projeto, bindingResult, model);
    	assertEquals("redirect:/listar-projetos", result);
    }

    @Test
    void testSalvarProjeto_quandoExistemErrosDeValidacao() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	when(bindingResult.hasErrors()).thenReturn(true);
    
    	String result = controller.salvarProjeto(projeto, bindingResult, model);
    	assertEquals("adicionar-projeto", result);
    }

    @Test
    void testPrepararExcluirProjeto() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	
    	when(projetoService.recuperarProjetoParaExclusao(projeto.getIdProjeto())).thenReturn(projeto);
    	
    	String result = controller.prepararExcluirProjeto(projeto.getIdProjeto(), model);
    	assertEquals("excluir-projeto", result);
    	verify(model, times(1)).addAttribute("projeto", projeto);
    }

    @Test
    void testPrepararExcluirProjeto_quandoStatusProjetoImpedeExclusao() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.INICIADO, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	
    	doThrow(ExclusionNotAllowedException.class).when(projetoService).recuperarProjetoParaExclusao(anyLong());
    	try {
    		controller.prepararExcluirProjeto(projeto.getIdProjeto(), model);
    	} catch(ExclusionNotAllowedException e) {
    		assertNotNull(e);
    	}
    }

    @Test
    void testConfirmarExcluirProjeto() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.INICIADO, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	doNothing().when(projetoService).excluir(projeto.getIdProjeto());
    	
    	String result = controller.confirmarExcluirProjeto(projeto.getIdProjeto(), model);
    	assertEquals("redirect:/listar-projetos", result);
    }

    @Test
    void testPreparaEditarProjeto() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.INICIADO, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	when(projetoService.pesquisarPorId(projeto.getIdProjeto())).thenReturn(projeto);
    	
    	String result = controller.preparaEditarProjeto(projeto.getIdProjeto(), model);
    	assertEquals("editar-projeto", result);
    	verify(model, times(1)).addAttribute("projeto", projeto);
    }

    @Test
    void testConfirmaEditarProjeto() {
    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.INICIADO, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	when(projetoService.pesquisarPorId(projeto.getIdProjeto())).thenReturn(projeto);
    	when(bindingResult.hasErrors()).thenReturn(false);
    	
    	String result = controller.confirmaEditarProjeto(projeto.getIdProjeto(), projeto, bindingResult, model);
    	assertEquals("redirect:/listar-projetos", result);
    }

    @Test
    void testConfirmaEditarProjeto_quandoExistemErrosDeValidacao() {

    	PessoaDTO gerente = criaPessoaDTO(false);
    	gerente.setIdPessoa(1L);
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(2L);
    	ProjetoDTO projeto = criaProjeto(StatusProjeto.INICIADO, "Baixo", gerente, List.of(funcionario));
    	projeto.setIdProjeto(3L);
    	when(projetoService.pesquisarPorId(projeto.getIdProjeto())).thenReturn(projeto);
    	when(bindingResult.hasErrors()).thenReturn(true);
    	
    	String result = controller.preparaEditarProjeto(projeto.getIdProjeto(), model);
    	assertEquals("editar-projeto", result);    
    }

    private ProjetoDTO criaProjeto(StatusProjeto status, String risco, PessoaDTO gerente, List<PessoaDTO> membros) {
		ProjetoDTO projeto = TesteUtil.buildProjetoDTO(status.getStatus(), risco);
		projeto.setGerente(gerente.getIdPessoa());
		membros.forEach(m -> projeto.adicionarMembro(m));
		return projeto;
    }
	
	private PessoaDTO criaPessoaDTO(boolean funcionario) {
		if(funcionario) {
			return buildFuncionario();
		}
		return buildGerente();
	}
}