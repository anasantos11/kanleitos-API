package br.com.kanleitos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.kanleitos.models.Isolamento;

@Repository
public interface IsolamentoRepository extends JpaRepository<Isolamento, Long> {

}
