package br.com.darlan.gestordeprojetos.service;

import java.util.List;

import br.com.darlan.gestordeprojetos.service.exception.NotFoundException;

public interface BusinessService<D, ID> {
	D persistir(D d);
	
	List<D> listar();
	
	D pesquisarPorId(ID id) throws NotFoundException;
	
	D atualizar(ID id, D d);
	
	void excluir(ID id) throws NotFoundException;
}
