package br.com.darlan.gestordeprojetos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.darlan.gestordeprojetos.controller.GestorDeProjetosController;

@SpringBootTest
@ActiveProfiles("test")
class GestordeprojetosApplicationTests {

	@Value("${gestordeprojetos.profile}")
	private String profile;
	


	@Autowired
	private GestorDeProjetosController controller;

	@Test
	void contextLoads() {
		GestordeprojetosApplication.main(new String[] {});
		assertEquals("test", profile);
		assertNotNull(controller);
	}

}
