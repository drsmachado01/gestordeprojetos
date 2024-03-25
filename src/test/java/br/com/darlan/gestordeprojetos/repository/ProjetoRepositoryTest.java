package br.com.darlan.gestordeprojetos.repository;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildPessoaFuncionario;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildPessoaGerente;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildProjeto;
import static org.junit.jupiter.api.Assertions.*;

import br.com.darlan.gestordeprojetos.util.ConverterUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.darlan.gestordeprojetos.model.Pessoa;
import br.com.darlan.gestordeprojetos.model.Projeto;
import br.com.darlan.gestordeprojetos.model.StatusProjeto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnabledIf(value = "#{environment.getActiveProfiles()[0] == 'test'}", loadContext = true)
public class ProjetoRepositoryTest {
	@Autowired
	private ProjetoRepository projetoRepository;
	
	@Autowired
	private PessoaRepository pessoaRepository;

	@Test
	void testSave() {
		Projeto result = criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", false);
		assertNotNull(result);
		assertEquals(1L, result.getIdProjeto());
		assertEquals("Projeto 1", result.getNome());
		assertEquals("Projeto de teste", result.getDescricao());
		assertEquals(StatusProjeto.EM_ANALISE, result.getStatus());
		assertEquals("Baixo", result.getRisco());
		assertNull(result.getMembros());
		assertEquals(1L, result.getGerente().getIdPessoa());
		assertEquals("Carlos da Silva", result.getGerente().getNome());
		assertEquals(BigDecimal.valueOf(1000.0).doubleValue(), result.getOrcamento().doubleValue());
		assertEquals(LocalDate.of(2022, 10, 10), result.getDataInicio());
		assertEquals(LocalDate.of(2023, 10, 10), result.getDataPrevisaoFim());
		assertEquals(LocalDate.of(2023, 5, 10), result.getDataFim());
	}

	@Test
	void testSave_quandoProjetoTemMembros() {
		Projeto result = criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", true);
		assertNotNull(result);
		assertEquals(1L, result.getIdProjeto());
		assertEquals("Projeto 1", result.getNome());
		assertEquals("Projeto de teste", result.getDescricao());
		assertEquals(StatusProjeto.EM_ANALISE, result.getStatus());
		assertEquals("Baixo", result.getRisco());
		assertNotNull(result.getMembros());
		assertEquals(1, result.getMembros().size());
		assertEquals("Joaquim da Silva", result.getMembros().get(0).getNome());
		assertEquals(2L, result.getGerente().getIdPessoa());
		assertEquals("Carlos da Silva", result.getGerente().getNome());
		assertEquals(BigDecimal.valueOf(1000.0).doubleValue(), result.getOrcamento().doubleValue());
		assertEquals(LocalDate.of(2022, 10, 10), result.getDataInicio());
		assertEquals(LocalDate.of(2023, 10, 10), result.getDataPrevisaoFim());
		assertEquals(LocalDate.of(2023, 5, 10), result.getDataFim());
	}

	@Test
	void testPesquisarPorId() {
		criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", false);
		Projeto result = projetoRepository.findById(1L).get();
		assertEquals(1L, result.getIdProjeto());
		assertEquals("Projeto 1", result.getNome());
		assertEquals("Projeto de teste", result.getDescricao());
		assertEquals(StatusProjeto.EM_ANALISE, result.getStatus());
		assertEquals("Baixo", result.getRisco());
		assertNotNull(result.getMembros());
		assertEquals(1L, result.getGerente().getIdPessoa());
		assertEquals("Carlos da Silva", result.getGerente().getNome());
		assertEquals(BigDecimal.valueOf(1000.0).doubleValue(), result.getOrcamento().doubleValue());
		assertEquals(LocalDate.of(2022, 10, 10), result.getDataInicio());
		assertEquals(LocalDate.of(2023, 10, 10), result.getDataPrevisaoFim());
		assertEquals(LocalDate.of(2023, 5, 10), result.getDataFim());
	}

	@Test
	void testAtualizar() {
		Projeto result = criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", false);
		result.setNome("Projeto 2");
		result.setStatus(StatusProjeto.INICIADO);
		projetoRepository.save(result);
		Projeto result2 = projetoRepository.findById(1L).get();
		assertEquals("Projeto 2", result2.getNome());
		assertEquals("Projeto de teste", result2.getDescricao());
		assertEquals(StatusProjeto.INICIADO, result2.getStatus());
		assertEquals("Baixo", result2.getRisco());
		assertNotNull(result2.getMembros());
		assertEquals(1L, result2.getGerente().getIdPessoa());
		assertEquals("Carlos da Silva", result2.getGerente().getNome());
		assertEquals(BigDecimal.valueOf(1000.0).doubleValue(), result2.getOrcamento().doubleValue());
		assertEquals(LocalDate.of(2022, 10, 10), result2.getDataInicio());
		assertEquals(LocalDate.of(2023, 10, 10), result2.getDataPrevisaoFim());
		assertEquals(LocalDate.of(2023, 5, 10), result2.getDataFim());
	}

	@Test
	void testExcluir() {
		criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", false);
		projetoRepository.deleteById(1L);
		Optional<Projeto> result = projetoRepository.findById(1L);
		assertFalse(result.isPresent());
	}

	@Test
	void testExcluirProjetoComMembros() {
		criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", true);
		projetoRepository.deleteById(1L);
		Optional<Projeto> result = projetoRepository.findById(1L);
		assertFalse(result.isPresent());
	}

	@Test
	void testListar() {
		criaProjeto(StatusProjeto.EM_ANALISE, "Baixo", false);
		List<Projeto> result = projetoRepository.findAll();
		assertNotNull(result);
		assertEquals(1, result.size());
	}
	
	private Projeto criaProjeto(StatusProjeto status, String risco, boolean incluirMembro) {
		Projeto projeto = buildProjeto(status.getStatus(), risco);
		if(incluirMembro) {
			projeto.adicionarMembros(criaPessoa(true));
		}
		projeto.setGerente(criaPessoa(false));
		return projetoRepository.save(projeto);
	}

	private Pessoa criaPessoa(boolean funcionario) {
		if(funcionario) {
			return pessoaRepository.save(buildPessoaFuncionario());
		}
		return pessoaRepository.save(buildPessoaGerente());
	}
}
