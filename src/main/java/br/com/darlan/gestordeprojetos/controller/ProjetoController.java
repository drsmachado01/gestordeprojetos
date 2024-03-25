package br.com.darlan.gestordeprojetos.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.service.ProjetoService;

@Controller
public class ProjetoController {
	@Autowired
	private ProjetoService projetoService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}
	
	@GetMapping("/listar-projetos")
	public String listarProjetos(ModelMap model) {
		List<ProjetoDTO> projetos = projetoService.listar();
		
		model.addAttribute("projetos", projetos);
		return "listar-projetos";
	}

	@GetMapping("/listar-projetos-gerente")
	public String listarProjetosGerentes(@RequestParam Long idGerente, ModelMap model) {
		List<ProjetoDTO> projetos = projetoService.listarPorGerente(idGerente);
		model.addAttribute("projetos", projetos);
		return "listar-projetos-gerente";
	}

	@PostMapping("/adicionar-projeto")
	public String salvarProjeto(@ModelAttribute("projeto") ProjetoDTO projeto, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "adicionar-projeto";
		}

		projetoService.persistir(projeto);
		return "redirect:/listar-projetos";
	}
	
	@GetMapping("/adicionar-projeto")
	public String adicionarProjeto(ModelMap model) {
		model.addAttribute("projeto", ProjetoDTO.builder().build());
		return "adicionar-projeto";
	}
	
	@GetMapping("/excluir-projeto")
	public String prepararExcluirProjeto(@RequestParam long id, ModelMap model) {
		ProjetoDTO projetoDTO = projetoService.recuperarProjetoParaExclusao(id);

		model.addAttribute("projeto", projetoDTO);
		return "excluir-projeto";
	}
	
	@GetMapping("/confirma-excluir-projeto")
	public String confirmarExcluirProjeto(@RequestParam Long id, ModelMap model) {
		projetoService.excluir(id);
		return "redirect:/listar-projetos";
	}
	
	@GetMapping("/editar-projeto")
	public String preparaEditarProjeto(@RequestParam Long id, ModelMap model) {
		ProjetoDTO projetoDTO = projetoService.pesquisarPorId(id);
		model.addAttribute("projeto", projetoDTO);
		return "editar-projeto";
	}
	
	@PostMapping(" /editar-projeto")
	public String confirmaEditarProjeto(@RequestParam Long id, ProjetoDTO projetoDTO, BindingResult result, ModelMap model) {
		if (result.hasErrors()) {
			return "editar-projeto";
		}
		projetoService.atualizar(id, projetoDTO);
		return "redirect:/listar-projetos";
	}
}
