package br.com.darlan.gestordeprojetos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.service.PessoaService;

@Controller
public class PessoaController {

	private static final String REDIRECT_LISTAR_PESSOAS = "redirect:/listar-pessoas";
	private static final String PESSOA = "pessoa";
	@Autowired
	private PessoaService pessoaService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@GetMapping("/adicionar-pessoa")
	public String adicionarPessoa(ModelMap model) {
		model.addAttribute(PESSOA, PessoaDTO.builder().build());
		return "adicionar-pessoa";
	}

	@GetMapping("/listar-pessoas")
	public String listarPessoas(ModelMap model) {
		List<PessoaDTO> pessoas = pessoaService.listar();
		model.addAttribute("pessoas", pessoas);
		return "listar-pessoas";
	}

	@PostMapping("/adicionar-pessoa")
	public String adicionarPessoa(@Valid PessoaDTO pessoa, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "adicionar-pessoa";
		}
		
		if(!Boolean.TRUE.equals(pessoa.getGerente())) {
			pessoa.setFuncionario(Boolean.TRUE);
			pessoa.setGerente(Boolean.FALSE);
		} else {
			pessoa.setFuncionario(Boolean.FALSE);
			pessoa.setGerente(Boolean.TRUE);
		}
		
		pessoaService.persistir(pessoa);
		return REDIRECT_LISTAR_PESSOAS;
	}

	@GetMapping("/buscarPessoaPorId")
	public String buscarPessoaPorId(@RequestParam Long id, ModelMap model) {
		PessoaDTO pessoa = pessoaService.pesquisarPorId(id);
		model.addAttribute(PESSOA, pessoa);
		return "exibir-pessoa";
	}

	@GetMapping("/editar-pessoa")
	public String atualizarPessoa(@RequestParam Long id, ModelMap model) {
		PessoaDTO pessoa = pessoaService.pesquisarPorId(id);
		model.addAttribute(PESSOA, pessoa);
		return "editar-pessoa";
	}

	/**
	 * Nota: Os metodos de edicao deveriam ser PUT, porem, o form:form do JSTL 
	 * bloqueia o uso de metodos que nao sejam GET ou POST
	 * */
//	@PutMapping("/editar-pessoa")
	@PostMapping("/editar-pessoa")
	public String atualizarPessoa(@RequestParam Long id, @Valid PessoaDTO pessoa, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "editar-pessoa";
		}

		pessoaService.atualizar(id, pessoa);
		return REDIRECT_LISTAR_PESSOAS;
	}

	@GetMapping("/excluir-pessoa")
	public String preparaExcluirPessoa(@RequestParam Long id, ModelMap model) {
		PessoaDTO pessoaDTO = pessoaService.pesquisarPorId(id);
		model.addAttribute(PESSOA, pessoaDTO);
		return "excluir-pessoa";
	}

//	@DeleteMapping("/excluir-pessoa")
	@GetMapping("/confirmar-excluir-pessoa")
	public String excluirPessoa(@RequestParam Long id, ModelMap model) {
		pessoaService.excluir(id);
		return REDIRECT_LISTAR_PESSOAS;
	}
	
	@GetMapping("/listar-gerentes")
	public @ResponseBody List<PessoaDTO> listarGerentes(ModelMap model) {
		return pessoaService.listarGerentes();
	}
}
