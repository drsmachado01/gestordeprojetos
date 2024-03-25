package br.com.darlan.gestordeprojetos.util.impl;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildGerente;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildProjeto;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildProjetoDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.Projeto;

@SpringBootTest
class ProjetoMapperTest {

    @Autowired
    private ProjetoMapper mapper;

    @Autowired
    private PessoaMapper pessoaMapper;

    @Test
    void entityToDTO() {
        Projeto projeto = buildProjeto("Iniciado", "ALTO");
        ProjetoDTO dto = mapper.entityToDTO(projeto);
        assertNotNull(dto);
        assertEquals(projeto.getNome(), dto.getNome());
        assertEquals(projeto.getOrcamento(), dto.getOrcamento());
        assertEquals(projeto.getRisco(), dto.getRisco());
        assertEquals(projeto.getStatus().getStatus(), dto.getStatus());
    }

    @Test
    void dtoToEntity() {
        Projeto e = mapper.dtoToEntity(buildProjetoDTO("Iniciado", "ALTO"));
        e.setGerente(pessoaMapper.dtoToEntity(buildGerente()));
        assertNotNull(e);
        assertEquals(buildProjeto("Iniciado", "ALTO"), e);
    }

    @Test
    void listEntityToListDTO() {
        Projeto projeto = buildProjeto("Iniciado", "ALTO");
        List<ProjetoDTO> listDTO = mapper.listEntityToListDTO(List.of(projeto));
        assertNotNull(listDTO);
        assertEquals(1, listDTO.size());
        ProjetoDTO dto = listDTO.get(0);
        assertNotNull(dto);
        assertEquals(projeto.getNome(), dto.getNome());
        assertEquals(projeto.getOrcamento(), dto.getOrcamento());
        assertEquals(projeto.getRisco(), dto.getRisco());
        assertEquals(projeto.getStatus().getStatus(), dto.getStatus());
        assertEquals(projeto.getGerente().getIdPessoa(), dto.getGerente());
    }

    @Test
    void listDTOToListEntity() {
        List<Projeto> listEntity = mapper.listDTOToListEntity(List.of(buildProjetoDTO("Iniciado", "ALTO")));
        assertNotNull(listEntity);
        assertEquals(1, listEntity.size());
    }

}