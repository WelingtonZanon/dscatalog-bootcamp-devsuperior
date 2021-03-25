package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

//spring trata como um injeção de serviços, "respeitando a semantica".
@Service
public class CategoryService {
	
//Autowired spring tratar dependencia valida do Category repository : classe com anotation repository
	@Autowired 
	private CategoryRepository repository;
	// transação somente leitura no banco
	//cria um metodo findAll alimentando uma lista de categoryDTO.
	@Transactional(readOnly=true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		/*
		 * transforma uma lista category e uma categoryDTO, a entidade morre aqui e segue a CategoryDTO
		 * para os controladores REST. Trasomação usando a função map de alta ordem, necessario transformar
		 * para stream e depois retornar o collect para uma lista novamente.
		 * Função lambda(arrow function) java 8 em diante.
		 */
		return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
	}
}
