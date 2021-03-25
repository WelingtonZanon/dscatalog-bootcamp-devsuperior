package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;
//recursos/controladores (REST) endpoints da aplicação
@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
/*
 * novamente o autowired, injetando as dependencais automaticas do Service,
 * que implementa o Repository que é interface da entidade.
 */
	@Autowired
	private CategoryService service;
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll(){
		List<CategoryDTO> list = service.findAll();
		return ResponseEntity.ok().body(list);
	}
	//acrescenta o endpoint id ao categories
	@GetMapping(value="/{id}")
	//pathvariable para assimilar o parametro passado na função com o caminho do endpoint id
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO dto = service.findByID(id);
		return ResponseEntity.ok().body(dto);
	}
}
