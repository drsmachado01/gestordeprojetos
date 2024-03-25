package br.com.darlan.gestordeprojetos.controller.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import br.com.darlan.gestordeprojetos.aspect.annotation.LogExecution;
import br.com.darlan.gestordeprojetos.service.exception.ExclusionNotAllowedException;

@ControllerAdvice
public class GestorDeProjetosExceptionHandlingController {

	@ExceptionHandler({ ExclusionNotAllowedException.class })
	@LogExecution
	public ModelAndView gerenciarErroExclusaoNaoPermitida(HttpServletRequest req, ExclusionNotAllowedException ex) {
		ModelAndView mav = new ModelAndView();
		mav.addObject("error", ex.getMessage());
		mav.addObject("url", req.getRequestURL());
		mav.setViewName("erros-projetos");
		return mav;
	}
}
