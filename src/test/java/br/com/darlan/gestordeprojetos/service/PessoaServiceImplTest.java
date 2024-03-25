package br.com.darlan.gestordeprojetos.service;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.repository.PessoaRepository;
import br.com.darlan.gestordeprojetos.service.impl.PessoaServiceImpl;
import br.com.darlan.gestordeprojetos.util.impl.PessoaMapper;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class PessoaServiceImplTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private PessoaMapper pessoaMapper;

    @InjectMocks
    private PessoaServiceImpl pessoaService;


    @Test
    void testPersistir() {
        when(pessoaMapper.dtoToEntity(buildFuncionario())).thenReturn(buildPessoaFuncionario());
        when(pessoaRepository.save(buildPessoaFuncionario())).thenReturn(buildPessoaFuncionario());
        when(pessoaMapper.entityToDTO(buildPessoaFuncionario())).thenReturn(buildFuncionario());
        PessoaDTO pessoaDTO = pessoaService.persistir(buildFuncionario());
        assertNotNull(pessoaDTO);
        assertEquals("Joaquim da Silva", pessoaDTO.getNome());
        assertEquals("12312312312", pessoaDTO.getCpf());
        assertEquals("1980-10-20", pessoaDTO.getDataNascimento());
        assertTrue(pessoaDTO.getFuncionario());
        assertFalse(pessoaDTO.getGerente());
    }

    @Test
    void testListar() {
        when(pessoaRepository.findAll()).thenReturn(List.of(buildPessoaGerente(),buildPessoaFuncionario()));
        when(pessoaMapper.listEntityToListDTO(List.of(buildPessoaGerente(),buildPessoaFuncionario()))).thenReturn(List.of(buildGerente(),buildFuncionario()));
        List<PessoaDTO> pessoas = pessoaService.listar();
        assertNotNull(pessoas);
        assertEquals(2, pessoas.size());
        assertTrue(pessoas.get(0).getGerente());
        assertTrue(pessoas.get(1).getFuncionario());
    }

    @Test
    void testPesquisarPorId() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.ofNullable(buildPessoaFuncionario()));
        when(pessoaMapper.entityToDTO(buildPessoaFuncionario())).thenReturn(buildFuncionario());

        PessoaDTO pessoaDTO = pessoaService.pesquisarPorId(1L);
        assertNotNull(pessoaDTO);
        assertEquals("Joaquim da Silva", pessoaDTO.getNome());
        assertEquals("12312312312", pessoaDTO.getCpf());
        assertEquals("1980-10-20", pessoaDTO.getDataNascimento());
        assertTrue(pessoaDTO.getFuncionario());
        assertFalse(pessoaDTO.getGerente());
    }

    @Test
    void testAtualizar() {

        PessoaDTO funcionarioDTO = buildFuncionario();
        Pessoa funcionario = buildPessoaFuncionario();
        funcionario.setDataNascimento(LocalDate.of(1980, 10,15));

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(funcionario));
        when(pessoaMapper.dtoToEntity(funcionarioDTO)).thenReturn(funcionario);
        when(pessoaRepository.save(funcionario)).thenReturn(funcionario);
        funcionarioDTO.setDataNascimento("15/10/1980");
        when(pessoaMapper.entityToDTO(funcionario)).thenReturn(funcionarioDTO);

        PessoaDTO pessoaDTO = pessoaService.atualizar(1L, funcionarioDTO);
        assertNotNull(pessoaDTO);
        assertEquals("Joaquim da Silva", pessoaDTO.getNome());
        assertEquals("12312312312", pessoaDTO.getCpf());
        assertEquals("15/10/1980", pessoaDTO.getDataNascimento());
        assertTrue(pessoaDTO.getFuncionario());
        assertFalse(pessoaDTO.getGerente());
    }

    @Test
    void testExcluir() {
        Pessoa funcionario = buildPessoaFuncionario();
        funcionario.setIdPessoa(1L);
        PessoaDTO funcionarioDTO = buildFuncionario();
        funcionarioDTO.setIdPessoa(1L);

        when(pessoaRepository.findById(anyLong())).thenReturn(Optional.of(funcionario));
        when(pessoaMapper.entityToDTO(funcionario)).thenReturn(funcionarioDTO);

        pessoaService.excluir(funcionario.getIdPessoa());
        verify(pessoaRepository, times(1)).deleteById(1L);
    }
}