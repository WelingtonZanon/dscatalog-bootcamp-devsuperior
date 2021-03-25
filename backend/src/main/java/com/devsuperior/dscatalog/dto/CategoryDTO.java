package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;
/*
 * Classe DTO para que as entidade não passe para as camadas de controle e morra nas camadas de serviço.
 * "DATA Transfer Objects
 */

public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	
	public CategoryDTO() {
		
	}

	public CategoryDTO(Long id, String name) {
		this.id = id;
		this.name = name;
	}
	public CategoryDTO(Category entity) {
		this.id=entity.getId();
		this.name=entity.getName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
