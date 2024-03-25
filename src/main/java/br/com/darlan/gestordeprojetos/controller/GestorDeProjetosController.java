package br.com.darlan.gestordeprojetos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GestorDeProjetosController {
	@GetMapping("/")
	public String index(ModelMap model) {
		return "welcome";
	}
}
