package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourseNotFoundException;

//classe de controles da JPA(controladores de dados)

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
	@Transactional(readOnly=true)
	public CategoryDTO findByID(Long id) {
		/*
		 * optional função JVA 8+ para tratar requisições com maior facilidade. Ex. usando o orElseThrow
		 * caso retorne vazio, lanço minha tratativa personalizada.
		 */
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(()-> new ResourseNotFoundException("Entity not found"));
		return new CategoryDTO(entity);
	}
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);
		return new CategoryDTO(entity);
	}
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		//comando da JPA para stanciar a entidade sem fazer uma requisição desnecessaria no banco.
		try {	
			Category entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}catch(EntityNotFoundException e){
			throw new ResourseNotFoundException("Id not found "+ id);
		}
	}
	
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}catch(EmptyResultDataAccessException e) {
			throw new ResourseNotFoundException("Id not found "+ id);
		}catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");
		}
		
	}
	
}
