package br.com.darlan.gestordeprojetos.service;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.darlan.gestordeprojetos.dto.PessoaDTO;
import br.com.darlan.gestordeprojetos.dto.ProjetoDTO;
import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.model.Projeto;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;
import br.com.darlan.gestordeprojetos.repository.PessoaRepository;
import br.com.darlan.gestordeprojetos.repository.ProjetoRepository;
import br.com.darlan.gestordeprojetos.service.exception.ExclusionNotAllowedException;
import br.com.darlan.gestordeprojetos.service.impl.ProjetoServiceImpl;
import br.com.darlan.gestordeprojetos.util.BusinessMapper;
import br.com.darlan.gestordeprojetos.util.ConverterUtil;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class ProjetoServiceImplTest {
    @Mock
    private ProjetoRepository projetoRepository;

    @Mock
    private BusinessMapper<Projeto, ProjetoDTO> projetoMapper;

    @Mock
    private PessoaRepository pessoaRepo;

    @Mock
    private BusinessMapper<Pessoa, PessoaDTO> pessoaMapper;

    @InjectMocks
    private ProjetoServiceImpl projetoService;

    @Test
    void testPersistir() {
        Projeto projeto = buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        ProjetoDTO projetoDTO = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        projetoDTO.setGerente(1L);
        Pessoa gerente = buildPessoaGerente();
        when(pessoaRepo.findById(anyLong())).thenReturn(Optional.of(gerente));
        when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);
        when(projetoMapper.dtoToEntity(any())).thenReturn(projeto);
        when(projetoMapper.entityToDTO(any())).thenReturn(projetoDTO);

        ProjetoDTO result = projetoService.persistir(projetoDTO);
        assertNotNull(result);
    }

    @Test
    void testListar() {
        when(projetoRepository.findAll()).thenReturn(List.of(buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO")));
        when(projetoMapper.listEntityToListDTO(List.of(buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO")))).thenReturn(List.of(buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO")));
        List<ProjetoDTO> result = projetoService.listar();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(StatusProjeto.EM_ANALISE.getStatus(), result.get(0).getStatus());
        assertEquals("ALTO", result.get(0).getRisco());
        assertEquals("Projeto 1", result.get(0).getNome());
    }

    @Test
    void test_PesquisarPorId() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.ofNullable(buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO")));
        when(projetoMapper.entityToDTO(buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO"))).thenReturn(buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO"));
        ProjetoDTO result = projetoService.pesquisarPorId(1L);
        assertNotNull(result);
        assertEquals(StatusProjeto.EM_ANALISE.getStatus(), result.getStatus());
        assertEquals(1L, result.getIdProjeto());
        assertEquals("Projeto 1", result.getNome());
        assertEquals("ALTO", result.getRisco());
        assertEquals("Projeto de teste", result.getDescricao());
        assertEquals(ConverterUtil.toDate(LocalDate.of(2022, 10, 10)), result.getDataInicio());
        assertEquals(ConverterUtil.toDate(LocalDate.of(2023, 10, 10)), result.getDataPrevisaoFim());
        assertEquals(ConverterUtil.toDate(LocalDate.of(2023, 5, 10)), result.getDataFim());
        assertEquals(BigDecimal.valueOf(1000.0), result.getOrcamento());
        assertNull(result.getMembros());
    }

    @Test
    void test_Atualizar() {
        Projeto projeto = buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        projeto.setIdProjeto(1L);
        projeto.setStatus(StatusProjeto.EM_ANALISE);
        ProjetoDTO projetoDTO = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        projetoDTO.setStatus(StatusProjeto.ANALISE_APROVADA.getStatus());

        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(Mockito.any(Projeto.class))).thenReturn(projeto);
        when(projetoMapper.dtoToEntity(any())).thenReturn(projeto);
        when(projetoMapper.entityToDTO(any())).thenReturn(projetoDTO);

        ProjetoDTO result = projetoService.atualizar(1L, projetoDTO);
        assertNotNull(result);
        assertEquals(StatusProjeto.ANALISE_APROVADA.getStatus(), result.getStatus());
        assertEquals("ALTO", result.getRisco());
        assertEquals("Projeto 1", result.getNome());
    }

    @Test
    void test_Excluir() {
        Projeto projeto = buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        projeto.setIdProjeto(1L);
        ProjetoDTO projetoDTO = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        projetoDTO.setIdProjeto(1L);
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoMapper.entityToDTO(projeto)).thenReturn(projetoDTO);
        doNothing().when(projetoRepository).deleteById(1L);
        try {
            projetoService.excluir(1L);
        } catch (ExclusionNotAllowedException e) {
            assertNull(e);
        }
        Mockito.verify(projetoRepository).deleteById(1L);
    }

    @Test
    void test_Excluir_quandoStatusNaoPermitirExcluir() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(buildProjeto(StatusProjeto.INICIADO.getStatus(), "ALTO")));
        when(projetoMapper.entityToDTO(buildProjeto(StatusProjeto.INICIADO.getStatus(),
                "ALTO"))).thenReturn(buildProjetoDTO(StatusProjeto.INICIADO.getStatus(), "ALTO"));
        try {
            projetoService.excluir(1L);
        } catch (ExclusionNotAllowedException e) {
            assertNotNull(e);
            assertEquals("Projeto em status que impede a exclusão!", e.getMessage());
        }
    }

    @Test
    void test_RecuperarProjetoParaExclusao() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO")));
        when(projetoMapper.entityToDTO(buildProjeto(StatusProjeto.EM_ANALISE.getStatus(),
                "ALTO"))).thenReturn(buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO"));
        ProjetoDTO result = projetoService.recuperarProjetoParaExclusao(1L);
        assertNotNull(result);
        assertEquals(StatusProjeto.EM_ANALISE.getStatus(), result.getStatus());
        assertEquals("ALTO", result.getRisco());
        assertEquals("Projeto 1", result.getNome());
    }

    @Test
    void test_RecuperarProjetoParaExclusao_quandoStatusNaoPermitirExcluir() {
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(buildProjeto(StatusProjeto.EM_ANDAMENTO.getStatus(), "ALTO")));
        when(projetoMapper.entityToDTO(buildProjeto(StatusProjeto.EM_ANDAMENTO.getStatus(), "ALTO"))).thenReturn(buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO"));

        try {
            ProjetoDTO result = projetoService.recuperarProjetoParaExclusao(1L);
        } catch (ExclusionNotAllowedException e) {
            assertNotNull(e);
            assertEquals("Projeto em andamento não pode ser excluido", e.getMessage());
        }
    }

    @Test
    void test_AdicionarMembro() {
        Pessoa funcionario = buildPessoaFuncionario();
        PessoaDTO funcionarioDTO = buildFuncionario();
        Projeto projeto = buildProjeto(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        ProjetoDTO projetoDTO = buildProjetoDTO(StatusProjeto.EM_ANALISE.getStatus(), "ALTO");
        projeto.adicionarMembros(funcionario);
        projetoDTO.adicionarMembro(funcionarioDTO);
        when(pessoaRepo.findById(1L)).thenReturn(Optional.ofNullable(funcionario));
        when(projetoRepository.findById(1L)).thenReturn(Optional.of(projeto));
        when(projetoRepository.save(any())).thenReturn(projeto);
        when(projetoMapper.entityToDTO(projeto)).thenReturn(projetoDTO);
        ProjetoDTO result = projetoService.adicionarMembro(1L, funcionarioDTO);

        assertNotNull(result);
        assertEquals(StatusProjeto.EM_ANALISE.getStatus(), result.getStatus());
        assertEquals("ALTO", result.getRisco());
        assertEquals("Projeto 1", result.getNome());

        assertNotNull(result.getMembros());
        assertEquals(2, result.getMembros().size());
        assertEquals(funcionarioDTO, result.getMembros().get(0));
    }
}