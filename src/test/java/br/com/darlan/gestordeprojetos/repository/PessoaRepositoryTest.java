package br.com.darlan.gestordeprojetos.repository;

import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildPessoaFuncionario;
import static br.com.darlan.gestordeprojetos.util.TesteUtil.buildPessoaGerente;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.darlan.gestordeprojetos.model.Pessoa;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnabledIf(value = "#{environment.getActiveProfiles()[0] == 'test'}", loadContext = true)
public class PessoaRepositoryTest {
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Test
	void testSave_quandoFuncionario() {
		Pessoa result = criaPessoa(true);
		assertNotNull(result);
		assertEquals(1L, result.getIdPessoa());
		assertTrue(result.getFuncionario());
		assertFalse(result.getGerente());
	}

	@Test
	void testSave_quandoGerente() {
		Pessoa result = criaPessoa(false);
		assertNotNull(result);
		assertEquals(1L, result.getIdPessoa());
		assertFalse(result.getFuncionario());
		assertTrue(result.getGerente());
	}
	
	@Test
	void testListar() {
		criaPessoa(true);
		criaPessoa(false);
		
		List<Pessoa> result = pessoaRepository.findAll();
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.get(0).getFuncionario());
		assertTrue(result.get(1).getGerente());
	}
	
	@Test
	void testAtualizar() {
		Pessoa pes = criaPessoa(true);
		pes.setGerente(Boolean.TRUE);
		pes.setFuncionario(Boolean.FALSE);
		
		Pessoa result = pessoaRepository.save(pes);
		assertNotNull(result);
		assertTrue(result.getGerente());
		assertFalse(result.getFuncionario());
	}
	
	@Test
	void testExcluir() {
		criaPessoa(false);
		pessoaRepository.deleteById(1L);
		Pessoa result = pessoaRepository.findById(1L).orElse(null);
		assertNull(result);
	}
	
	private Pessoa criaPessoa(boolean funcionario) {
		if(funcionario) {
			return pessoaRepository.save(buildPessoaFuncionario());
		}
		return pessoaRepository.save(buildPessoaGerente());
	}
}
