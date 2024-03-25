package br.com.darlan.gestordeprojetos.controller;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildFuncionario;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildGerente;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildProjetoDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;
import br.com.darlan.gestordeprojetos.service.PessoaService;
import br.com.darlan.gestordeprojetos.service.ProjetoService;

@SpringBootTest
class MembroControllerTest {

	@Mock
	private ProjetoService projetoService;
	
	@Mock
	private PessoaService pessoaService;
	
	@InjectMocks
	private MembroController controller;

    @Test
    void testInserirMembro() {
    	PessoaDTO funcionario = criaPessoa(true);
    	when(pessoaService.pesquisarPorId(anyLong())).thenReturn(funcionario);
    	ProjetoDTO projetoDTO = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "Baixo");
    	projetoDTO.adicionarMembro(funcionario);
    	when(projetoService.adicionarMembro(anyLong(), any())).thenReturn(
    			projetoDTO);
    	
    	ResponseEntity<ProjetoDTO> result = controller.inserirMembro(1L, funcionario);
    	assertNotNull(result);
    	ProjetoDTO dto = result.getBody();
    	assertNotNull(dto);
    	assertNotNull(dto.getMembros());
    	assertEquals(1, dto.getMembros().size());
    }
	
	private PessoaDTO criaPessoa(boolean funcionario) {
		if(funcionario) {
			return buildFuncionario();
		}
		return buildGerente();
	}
}