package br.com.darlan.gestordeprojetos.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.service.ProjetoService;

@RestController
@RequestMapping("/projetos")
public class MembroController {
	@Autowired
	private ProjetoService projetoService;
	
	@PostMapping("/{idProjeto}/membro")
	public ResponseEntity<ProjetoDTO> inserirMembro(@PathVariable("idProjeto") Long idProjeto, 
			@RequestBody PessoaDTO funcionario) {
		ProjetoDTO projetoDTO = projetoService.adicionarMembro(idProjeto, funcionario);
		return ResponseEntity.ok(projetoDTO);
	}
}
