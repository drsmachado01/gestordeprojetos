package br.com.darlan.gestordeprojetos.controller;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildFuncionario;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildGerente;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.service.PessoaService;
import br.com.darlan.gestordeprojetos.util.ConverterUtil;


@SpringBootTest
class PessoaControllerTest {
	
	@Mock
	private PessoaService pessoaService;
	
	@Mock
	private ModelMap model;
	
	@Mock
	private BindingResult bindingResult;
	
	@InjectMocks
	private PessoaController controller;

    @Test
    void testPrepararParaAdicionarPessoa() {
    	PessoaDTO pessoaDTO = PessoaDTO.builder().build();
    	String result = controller.adicionarPessoa(model);
    	assertEquals("adicionar-pessoa", result);
    	verify(model, times(1)).addAttribute("pessoa", pessoaDTO);
    }
    
    @Test
    void testAdicionarPessoa() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	List<PessoaDTO> pessoas = List.of(funcionario);
    	when(pessoaService.persistir(funcionario)).thenReturn(funcionario);
    	when(pessoaService.listar()).thenReturn(pessoas);
    	when(bindingResult.hasErrors()).thenReturn(false);
    	
    	String result = controller.adicionarPessoa(funcionario, bindingResult, model);
    	assertEquals("redirect:/listar-pessoas", result);
    }
    
    @Test
    void testAdicionarPessoa_quandoExistemErrosDeValidacao() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	List<PessoaDTO> pessoas = List.of(funcionario);
    	when(bindingResult.hasErrors()).thenReturn(true);
    	
    	String result = controller.adicionarPessoa(funcionario, bindingResult, model);
    	assertEquals("adicionar-pessoa", result);
    }

    @Test
    void listarPessoas() {
    	List<PessoaDTO> pessoas = List.of(criaPessoaDTO(true));
    	when(pessoaService.listar()).thenReturn(pessoas);
    	
    	String result = controller.listarPessoas(model);
    	verify(model, times(1)).addAttribute("pessoas", pessoas);
    	assertEquals("listar-pessoas", result);
    }

    @Test
    void buscarPessoaPorId() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	when(pessoaService.pesquisarPorId(funcionario.getIdPessoa())).thenReturn(funcionario);
    	
    	String result = controller.buscarPessoaPorId(funcionario.getIdPessoa(), model);
    	verify(model, times(1)).addAttribute("pessoa", funcionario);
    	assertEquals("exibir-pessoa", result);
    }

    @Test
    void atualizarPessoa() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	when(pessoaService.pesquisarPorId(funcionario.getIdPessoa())).thenReturn(funcionario);
    	
    	
    	String result = controller.atualizarPessoa(funcionario.getIdPessoa(), funcionario, bindingResult, model);
    	assertEquals("redirect:/listar-pessoas", result);
    }

    @Test
    void testAtualizarPessoa() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	funcionario.setDataNascimento(ConverterUtil.toString(LocalDate.of(1980, 10,15), ConverterUtil.DATE_PATTERN));
    	when(pessoaService.pesquisarPorId(funcionario.getIdPessoa())).thenReturn(funcionario);
    	when(pessoaService.atualizar(funcionario.getIdPessoa(), funcionario)).thenReturn(funcionario);
    	
    	String result = controller.atualizarPessoa(funcionario.getIdPessoa(), funcionario, bindingResult, model);
    	assertEquals("redirect:/listar-pessoas", result);
    }

    @Test
    void testAtualizarPessoa_quandoExistemErrosDeValidacao() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	when(bindingResult.hasErrors()).thenReturn(true);
    	
    	
    	String result = controller.atualizarPessoa(funcionario.getIdPessoa(), funcionario, bindingResult, model);
    	assertEquals("editar-pessoa", result);
    }

    @Test
    void preparaExcluirPessoa() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	when(pessoaService.pesquisarPorId(funcionario.getIdPessoa())).thenReturn(funcionario);
    	
    	String result = controller.preparaExcluirPessoa(funcionario.getIdPessoa(), model);
    	assertEquals("excluir-pessoa", result);
    	verify(model, times(1)).addAttribute("pessoa", funcionario);
    }

    @Test
    void excluirPessoa() {
    	PessoaDTO funcionario = criaPessoaDTO(true);
    	funcionario.setIdPessoa(1L);
    	doNothing().when(pessoaService).excluir(funcionario.getIdPessoa());
    	
    	String result = controller.excluirPessoa(funcionario.getIdPessoa(), model);
    	assertEquals("redirect:/listar-pessoas", result);
    }
	
	private PessoaDTO criaPessoaDTO(boolean funcionario) {
		if(funcionario) {
			return buildFuncionario();
		}
		return buildGerente();
	}
}