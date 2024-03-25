package br.com.darlan.gestordeprojetos.service.integration;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildFuncionario;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.repository.PessoaRepository;
import br.com.darlan.gestordeprojetos.service.exception.NotFoundException;
import br.com.darlan.gestordeprojetos.service.impl.PessoaServiceImpl;
import br.com.darlan.gestordeprojetos.util.impl.PessoaMapper;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnabledIf(value = "#{environment.getActiveProfiles()[0] == 'test'}", loadContext = true)
class PessoaServiceImplTest {
    @Mock
    private PessoaRepository repo;

    @Mock
    private PessoaMapper pessoaMapper;

    @Autowired
    @InjectMocks
    private PessoaServiceImpl pessoaService;
    
    @Test
    void testListar() {
        PessoaDTO pessoaDTO = buildFuncionario();
        pessoaDTO.setDataNascimento("1980-10-20");
        pessoaDTO = pessoaService.persistir(pessoaDTO);
        
        List<PessoaDTO> pessoas = pessoaService.listar();

        assertNotNull(pessoas);
        assertEquals(1, pessoas.size());
    }

    @Test
    void testAtualizar() {
        PessoaDTO pessoaDTO = buildFuncionario();
        pessoaDTO.setDataNascimento("1980-10-20");
        pessoaDTO = pessoaService.persistir(pessoaDTO);
        
        Long idPessoa = pessoaDTO.getIdPessoa();
        PessoaDTO editedPessoaDTO = buildFuncionario();
        editedPessoaDTO.setDataNascimento("1980-10-15");
        
        PessoaDTO result = pessoaService.atualizar(idPessoa, editedPessoaDTO);

        assertNotNull(result);
        assertEquals(pessoaDTO.getIdPessoa(), result.getIdPessoa());
        assertEquals(pessoaDTO.getNome(), result.getNome());
        assertEquals("1980-10-15", result.getDataNascimento());
    }

    @Test
    void testPersistir() {
        PessoaDTO pessoaDTO = buildFuncionario();
        pessoaDTO.setDataNascimento("1980-10-20");
        pessoaDTO = pessoaService.persistir(pessoaDTO);
        assertNotNull(pessoaDTO);
        assertEquals(1, pessoaDTO.getIdPessoa());
    }

    @Test
    void pesquisarPorId() {
        PessoaDTO pessoaDTO = buildFuncionario();
        pessoaDTO.setDataNascimento("1980-10-20");
        pessoaDTO = pessoaService.persistir(pessoaDTO);
        
        PessoaDTO result = pessoaService.pesquisarPorId(pessoaDTO.getIdPessoa());

        assertNotNull(result);
        assertEquals(pessoaDTO.getIdPessoa(), result.getIdPessoa());
        assertEquals(pessoaDTO.getNome(), result.getNome());
    }

    @Test
    void testExcluir() {
        PessoaDTO pessoaDTO = buildFuncionario();
        pessoaDTO.setDataNascimento("1980-10-20");
        pessoaDTO = pessoaService.persistir(pessoaDTO);
        Long idPessoa = pessoaDTO.getIdPessoa();
        pessoaService.excluir(idPessoa);

        try {
            pessoaService.pesquisarPorId(idPessoa);
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("Pessoa não encontrada!", e.getMessage());
        }
    }

    @Test
    void testExcluir_quandoPessoaNaoExiste() {
        try {
            pessoaService.excluir(100L);
        } catch (NotFoundException e) {
            assertNotNull(e);
            assertEquals("Pessoa não encontrada!", e.getMessage());
        }
    }

    @Test
    void testPesquisarPorId_quandoPessoaNaoExiste() {
        try {
            pessoaService.pesquisarPorId(100L);
        } catch (Exception e) {
            assertNotNull(e);
            assertEquals("Pessoa não encontrada!", e.getMessage());
        }
    }
}