package br.com.kanleitos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.kanleitos.models.Paciente;
import br.com.kanleitos.models.PedidoInternacao;
import br.com.kanleitos.models.RegistroInternacao;
import br.com.kanleitos.util.StatusRegistro;

@Repository
public interface RegistroInternacaoRepository extends JpaRepository<RegistroInternacao, Long> {

	List<RegistroInternacao> findAllByStatusRegistro(StatusRegistro statusRegistro);

	@Query("SELECT ri.pedidoInternacao.paciente FROM RegistroInternacao ri "
			+ "WHERE ri.statusRegistro = 'EM_ANDAMENTO' AND ri.enfermaria.idEnfermaria = :idEnfermaria")
	List<Paciente> findAllPacientesbyEnfermaria(@Param("idEnfermaria") long idEnfermaria);

	List<PedidoInternacao> findAllByPedidoInternacaoAndStatusRegistro(PedidoInternacao pedidoInternacao,
			StatusRegistro statusRegistro);

}
