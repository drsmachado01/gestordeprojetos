package br.com.darlan.gestordeprojetos.util.impl;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;

@SpringBootTest
class PessoaMapperTest {

    @Autowired
    PessoaMapper mapper;

    @Test
    void entityToDTO() {
        PessoaDTO dto = mapper.entityToDTO(buildPessoaFuncionario());
        assertNotNull(dto);
        assertEquals("Joaquim da Silva", dto.getNome());
        assertEquals("12312312312", dto.getCpf());
        assertEquals("1980-10-20", dto.getDataNascimento());
        assertFalse(dto.getGerente());
        assertTrue(dto.getFuncionario());
    }

    @Test
    void dtoToEntity() {
        Pessoa e = mapper.dtoToEntity(buildFuncionario());
        assertNotNull(e);
        assertEquals(buildPessoaFuncionario(), e);
        assertTrue(e.getFuncionario());
    }

    @Test
    void listEntityToListDTO() {
        List<PessoaDTO> dtos = mapper.listEntityToListDTO(List.of(buildPessoaFuncionario(), buildPessoaGerente()));
        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertTrue(dtos.get(0).getFuncionario());
        assertFalse(dtos.get(1).getFuncionario());
    }

    @Test
    void listDTOToListEntity() {
        List<Pessoa> entities = mapper.listDTOToListEntity(List.of(buildFuncionario(), buildGerente()));
        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals(buildPessoaFuncionario(), entities.get(0));
        assertEquals(buildPessoaGerente(), entities.get(1));
        assertTrue(entities.get(0).getFuncionario());
        assertFalse(entities.get(1).getFuncionario());
    }
}