package br.com.kanleitos.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.kanleitos.models.Funcionario;
import br.com.kanleitos.repository.FuncionarioRepository;
import br.com.kanleitos.util.Response;
import br.com.kanleitos.validators.FuncionarioValidator;

@Controller
public class FuncionarioController {

	@Autowired
	private FuncionarioRepository repo;

	@InitBinder("funcionario")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new FuncionarioValidator());
	}

	@PostMapping("/funcionario")
	public @ResponseBody ResponseEntity<Response<Long>> cadastrarFuncionario(
			@Valid @RequestBody Funcionario funcionario, BindingResult result) {
		Response<Long> response = new Response<Long>();

		return persistFuncionario(funcionario, result, response);
	}

	@GetMapping("/funcionario")
	public @ResponseBody ResponseEntity<Response<List<Funcionario>>> getFuncionariosByStatus(
			@RequestParam(value = "somenteAtivos", required = false) boolean somenteAtivos) {
		Response<List<Funcionario>> response = new Response<List<Funcionario>>();

		if (somenteAtivos) {
			response.setData(repo.findAllByInativo(false));
		} else {
			response.setData(repo.findAll());
		}

		return ResponseEntity.ok(response);
	}

	@GetMapping("/medicos")
	public @ResponseBody ResponseEntity<Response<List<Funcionario>>> getMedicos() {
		Response<List<Funcionario>> response = new Response<List<Funcionario>>();

		response.setData(repo.findAllByCrmNotNull());
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/funcionariosNaoMedicos")
	public @ResponseBody ResponseEntity<Response<List<Funcionario>>> getFuncionariosNaoMedicos() {
		Response<List<Funcionario>> response = new Response<List<Funcionario>>();

		response.setData(repo.findAllByCrmNull());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/funcionario")
	public @ResponseBody ResponseEntity<Response<Long>> atualizarFuncionario(@RequestBody Funcionario funcionario,
			BindingResult result) {
		Response<Long> response = new Response<Long>();

		return persistFuncionario(funcionario, result, response);
	}

	@PutMapping("/funcionario/alterarStatus")
	public @ResponseBody ResponseEntity<Response<Long>> alteraStatus(@RequestParam Long idFuncionario) {
		Response<Long> response = new Response<Long>();
		Funcionario funcionario = repo.findOne(idFuncionario);

		if (funcionario == null) {
			response.addError("Tipo de pendencia não encontrado");
			return ResponseEntity.badRequest().body(response);
		} else {
			funcionario.setInativo(!funcionario.isInativo());
			repo.save(funcionario);
			response.setData(idFuncionario);
			return ResponseEntity.ok(response);
		}
	}

	private ResponseEntity<Response<Long>> persistFuncionario(Funcionario funcionario, BindingResult result,
			Response<Long> response) {
		if (result.hasErrors()) {
			response.setData(null);
			result.getAllErrors().forEach(error -> response.addError(error.getCode()));

			return ResponseEntity.badRequest().body(response);
		} else {
			funcionario = repo.save(funcionario);
			response.setData(funcionario.getIdFuncionario());

			return ResponseEntity.ok(response);
		}
	}

}
