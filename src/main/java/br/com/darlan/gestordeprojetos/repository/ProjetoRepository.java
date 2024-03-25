package br.com.darlan.gestordeprojetos.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.darlan.gestordeprojetos.model.Projeto;

@Repository
public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
	Optional<List<Projeto>> findByGerenteIdPessoa(Long gerenteId);
}
