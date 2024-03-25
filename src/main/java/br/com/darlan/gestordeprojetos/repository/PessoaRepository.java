package br.com.darlan.gestordeprojetos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.darlan.gestordeprojetos.model.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

	List<Pessoa> findPessoaByGerenteIsTrue();

}
