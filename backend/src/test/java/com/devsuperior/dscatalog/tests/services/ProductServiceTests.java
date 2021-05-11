package com.devsuperior.dscatalog.tests.services;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.resources.ResourseNotFoundException;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.tests.factory.ProductFactory;

/**
 * Teste unitario da classe, não pode chamar o repository, vamos testar somente
 * o service. Utilização do Mockito que ja vem no security-test.
 * 
 * o ExtendWith faz com que não seja carregado os contextos da aplicação, sem
 * conteiner etc.
 */
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	// classe enjetada que sera testada
	@InjectMocks
	private ProductService service;

	/**
	 * classe dependencia, "mockada"! Tem que configurar o comportamento simulado
	 * dele.
	 */
	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private Product product;
	private PageImpl<Product> page;

	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = ProductFactory.createProduct();
		// um tipo de implementação page simulada com um produtinho dentro para testar o
		// find page
		page = new PageImpl<>(List.of(product));

		// configurando o mockito para fazer nada se existir o ID no banco.
		Mockito.doNothing().when(repository).deleteById(existingId);

		// configurando o mockito para lançar a exeção caso id não existir.
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);

		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

//------- simulações com Mockito ------
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		// chamada com qualquer valor para retornar uma pagina
		Mockito.when(repository.find(ArgumentMatchers.any(), ArgumentMatchers.anyString(), ArgumentMatchers.any()))
				.thenReturn(page);
		// simulando a persistencia de qualquer produto
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		
		//getOne para testar o update
		Mockito.when(repository.getOne(existingId)).thenReturn(product);
		Mockito.doThrow(EntityNotFoundException.class).when(repository).getOne(nonExistingId);
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		/**
		 * exeção do delete - teste unitario - isolando a camada de serviço. delete
		 * executa o comando deletar para o repository e espera que nada retorne caso id
		 * correto.
		 */
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
		/**
		 * checa se o mockito foi usado. Ele consegue reconhecer todas as chamadas do
		 * repository referentes a classe em teste. Usando com uma sobrecarga de times,
		 * para saber quantidade de vezes chamado.
		 */
		Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}

	@Test
	public void deleteShouldThrowResourseNotFoundExceptionWhenIdDoesExists() {
		Assertions.assertThrows(ResourseNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenIdDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
	}

	@Test
	public void findAllPageShouldReturnPage() {

		Long categoryId = 0L;
		String name = "";
		PageRequest pageRequest = PageRequest.of(0, 10);

		Page<ProductDTO> result = service.findAllPaged(categoryId, name, pageRequest);

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());

		Mockito.verify(repository, Mockito.times(1)).find(null, name, pageRequest);
	}

	@Test
	public void findByIdShouldReturnProductWhenIdExists() {
		ProductDTO result = service.findByID(existingId);

		Assertions.assertNotNull(result);

		Mockito.verify(repository, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void findByIdShouldThrowResourseNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourseNotFoundException.class, () -> {
			service.findByID(nonExistingId);
		});
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = new ProductDTO();	
		
		ProductDTO result = service.update(existingId, dto);
		
		Assertions.assertNotNull(result);
		
	}

	@Test
	public void updateShouldThrowResourseNotFoundExceptionWhenIdDoesNotExists() {
		ProductDTO dto = new ProductDTO();			
		
		Assertions.assertThrows(ResourseNotFoundException.class, () -> {
			service.update(nonExistingId, dto);
		});
	}

}
