package com.devsuperior.dscatalog.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;

//recursos/controladores (REST) endpoints da aplicação
@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
	/*
	 * novamente o autowired, injetando as dependencais automaticas do Service, que
	 * implementa o Repository que é interface da entidade.
	 */
	@Autowired
	private CategoryService service;

	// list trocado para page, confirar melhor a resposta.
	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy
			){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction),orderBy);
		
		Page<CategoryDTO> list = service.findAllPaged(pageRequest);
		return ResponseEntity.ok().body(list);
	}

	// acrescenta o endpoint id ao categories
	@GetMapping(value = "/{id}")
	// pathvariable para assimilar o parametro passado na função com o caminho do
	// endpoint id
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		CategoryDTO dto = service.findByID(id);
		return ResponseEntity.ok().body(dto);
	}

	/*
	 * Post controlador REST para inserir no banco de dados. Uma requisição de um
	 * objeto completo do body para inserir varios itens no banco, vale o mesmo para
	 * um update.
	 */
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto) {
		dto = service.insert(dto);
		/*
		 * comando grande, porem padrão no spring, para retornar no cabeçalho (headers)
		 * o ID do item inserido. status padrão de retorno o 201
		 */
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	// put metodo REST de atualização no banco
	@PutMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	// Delete metodo REST de deletar no banco
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<CategoryDTO> delete(@PathVariable Long id) {
		service.delete(id);
		// retorna um status 204 que o corpo esta vazio.
		return ResponseEntity.noContent().build();
	}
}
