package com.devsuperior.dscatalog.services;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.UriDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.ResourseNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;

//classe de controles da JPA(controladores de dados)

//spring trata como um injeção de serviços, "respeitando a semantica".
@Service
public class ProductService {
	
	@Autowired
	private S3Service s3Service;
	
//Autowired spring tratar dependencia valida do Product repository : classe com anotation repository
	@Autowired 
	private ProductRepository repository;
	//injetando o category para a lista de categorias
	@Autowired
	private CategoryRepository categoryRepository;
	// transação somente leitura no banco
	//cria um metodo findAll alimentando uma lista de categoryDTO.
	@Transactional(readOnly=true)
	public Page<ProductDTO> findAllPaged(Long categoryId,String name, PageRequest pageRequest){
		//acrescentando o category para busca personalizada
		List<Category> categories = (categoryId==0) ? null : Arrays.asList(categoryRepository.getOne(categoryId));
		
		
		Page<Product> page = repository.find(categories,name, pageRequest);
		
		//chamada seca para implementar as categorias na lista de produtos.
		repository.findProductWithCategories(page.getContent());
		
		/*
		 * transforma uma lista category e uma categoryDTO, a entidade morre aqui e segue a ProductDTO
		 * para os controladores REST. Trasomação usando a função map de alta ordem, necessario transformar
		 * para stream e depois retornar o collect para uma lista novamente.
		 * Função lambda(arrow function) java 8 em diante.
		 * 
		 * troca list por page, ai ele n precisa mais ser convertido, já é considerado um stream
		 */
		return page.map(x -> new ProductDTO(x));
	}
	@Transactional(readOnly=true)
	public ProductDTO findByID(Long id) {
		/*
		 * optional função JVA 8+ para tratar requisições com maior facilidade. Ex. usando o orElseThrow
		 * caso retorne vazio, lanço minha tratativa personalizada.
		 * 
		 * retornando com o contrutor que tmb lega o categorias no argumento
		 */		
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(()-> new ResourseNotFoundException("Entity not found"));
		return new ProductDTO(entity, entity.getCategories());
	}
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		copyDtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		//comando da JPA para estanciar a entidade sem fazer uma requisição desnecessaria no banco.
		try {	
			Product entity = repository.getOne(id);
			copyDtoToEntity(dto, entity);;
			entity = repository.save(entity);
			return new ProductDTO(entity);
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
	private void copyDtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setDate(dto.getDate());
		entity.setImgUrl(dto.getImgUrl());
		entity.setPrice(dto.getPrice());
		//limpa a lista de categorias na entidade (precaução)
		entity.getCategories().clear();
		/*
		 * percorrendo a lista category da classe productDTO e chamando de catDto
		 * percorro a quantidade que veio por argumento
		 */
		for(CategoryDTO catDto : dto.getCategories()) {
			//estancio categoria usando o id que veio do argumento e o getOne para não mecher no banco
			Category category = categoryRepository.getOne(catDto.getId());
			//insiro a categoria na lista da entidade
			entity.getCategories().add(category);
		}
	}
	//retornando a URL da image
	public UriDTO uploadFile(MultipartFile file) {
		URL url = s3Service.uploadFile(file);
		return new UriDTO(url.toString());
	}
	
}
