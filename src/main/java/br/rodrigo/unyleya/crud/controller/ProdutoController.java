package br.rodrigo.unyleya.crud.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
//tetse2
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.rodrigo.unyleya.crud.exception.ResourceNotFoundException;
import br.rodrigo.unyleya.crud.model.Produto;
import br.rodrigo.unyleya.crud.service.SequenceGeneratorService;
import br.rodrigo.unyleya.crud.repository.ProdutoRepository;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1")
public class ProdutoController {
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@GetMapping("/produtos")
	public List<Produto> getAllProdutos() {
		return produtoRepository.findAll();
	}

	@GetMapping("/produtos/{id}")
	public ResponseEntity<Produto> getProdutoById(@PathVariable(value = "id") Long produtoId)
			throws ResourceNotFoundException {
		Produto produto = produtoRepository.findById(produtoId)
				.orElseThrow(() -> new ResourceNotFoundException("Produto not found for this id :: " + produtoId));
		return ResponseEntity.ok().body(produto);
	}

	@PostMapping("/produtos")
	public Produto createProduto(@Valid @RequestBody Produto produto) {
		produto.setId(sequenceGeneratorService.generateSequence(Produto.SEQUENCE_NAME));
		return produtoRepository.save(produto);
	}

	@PutMapping("/produtos/{id}")
	public ResponseEntity<Produto> updateProduto(@PathVariable(value = "id") Long produtoId,
			@Valid @RequestBody Produto produtoDetails) throws ResourceNotFoundException {
		Produto produto = produtoRepository.findById(produtoId)
				.orElseThrow(() -> new ResourceNotFoundException("Produto not found for this id :: " + produtoId));

		produto.setNome(produtoDetails.getNome());
		produto.setPreco(produtoDetails.getPreco());
		final Produto updatedProduto = produtoRepository.save(produto);
		return ResponseEntity.ok(updatedProduto);
	}

	@DeleteMapping("/produtos/{id}")
	public Map<String, Boolean> deleteProduto(@PathVariable(value = "id") Long produtoId)
			throws ResourceNotFoundException {
		Produto produto = produtoRepository.findById(produtoId)
				.orElseThrow(() -> new ResourceNotFoundException("Produto not found for this id :: " + produtoId));

		produtoRepository.delete(produto);
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		return response;
	}
}
