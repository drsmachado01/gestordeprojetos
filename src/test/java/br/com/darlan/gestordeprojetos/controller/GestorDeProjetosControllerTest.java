package br.com.darlan.gestordeprojetos.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class GestorDeProjetosControllerTest {

    @Mock
    private ModelMap model;

    @Autowired
    @InjectMocks
    private GestorDeProjetosController portfolioManagerController;

    @Test
    void index() {
        String index = portfolioManagerController.index(model);
        assertEquals("welcome", index);
    }
}