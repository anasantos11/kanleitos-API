package br.com.kanleitos.predicates;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;

import br.com.kanleitos.models.Filtro;
import br.com.kanleitos.models.QPedidoInternacao;
import br.com.kanleitos.models.enums.StatusPedido;

public class PedidoInternacaoPredicate {

	@SuppressWarnings("deprecation")
	public static Predicate filtroPesquisa(Filtro filtros) {
		List<Predicate> listPredicates = new ArrayList<>();

		QPedidoInternacao pedido = QPedidoInternacao.pedidoInternacao;
		
		if(filtros.getStatus() != null && !filtros.getStatus().isEmpty()) {
			listPredicates.add(pedido.statusPedido.eq(StatusPedido.fromName(filtros.getStatus())));		
		}

		if (filtros.getIdAla() > 0) {
			listPredicates.add(pedido.ala.idAla.eq(filtros.getIdAla()));
		}

		if (filtros.getIdMedico() > 0) {
			listPredicates.add(pedido.medicoResponsavel.idFuncionario.eq(filtros.getIdMedico()));
		}
		
		if (filtros.getIdResidente() > 0) {
			listPredicates.add(pedido.residenteResponsavel.idFuncionario.eq(filtros.getIdResidente()));
		}

		if (filtros.getIdIsolamento() > 0) {
			listPredicates.add(pedido.isolamento.idIsolamento.eq(filtros.getIdIsolamento()));
		}

		if (!filtros.getNomePaciente().isEmpty() && filtros.getNomePaciente() != null) {
			listPredicates.add(pedido.paciente.nomePaciente
					.like(Expressions.asString("%").concat(filtros.getNomePaciente()).concat("%")));
		}

		if (filtros.getNumProntuario() > 0) {
			listPredicates.add(pedido.paciente.numProntuario.eq(filtros.getNumProntuario()));
		}

		if (filtros.getDataAdmissao() != null) {
			Date startDate = new Date(filtros.getDataAdmissao().getTime());
			startDate.setHours(0);
			startDate.setMinutes(0);
			startDate.setSeconds(0);
			
			Date endDate = new Date(filtros.getDataAdmissao().getTime());
			endDate.setHours(23);
			endDate.setMinutes(59);
			endDate.setSeconds(59);
			listPredicates.add(pedido.dataAdmissao.between(startDate, endDate));
		}

		BooleanBuilder bb = new BooleanBuilder();
		return bb.orAllOf(listPredicates.toArray(new Predicate[listPredicates.size()]));

	}
}
