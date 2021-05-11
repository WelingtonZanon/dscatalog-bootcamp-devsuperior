package com.devsuperior.dscatalog.tests.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.resources.ResourseNotFoundException;
import com.devsuperior.dscatalog.services.ProductService;

/**
 * 
 * classe de integração de componentes, o service com o repository.
 *
 */

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;

	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	private long countPCGamerProducts;
	private PageRequest pageRequest;

	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		pageRequest = PageRequest.of(0, 10);
	}
	
	@Test
	public void findAllPagedShouldReturnNothingWhenNameDoesNotExist() {
		
		String name = "Camera";
		Page<ProductDTO> result = service.findAllPaged(0L, name, pageRequest);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnAllProductsWhenNameIsEmpity() {
		
		String name = "";
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}	
	
	@Test
	public void findShouldReturnProductsWhenNameExistsIgnoreCase() {
		
		String name = "pC gaMer";
		
		Page<ProductDTO> result = service.findAllPaged(0L, name, pageRequest);
		
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, result.getTotalElements());
	}
	
	

	@Test
	public void deleteShouldDoNothingWhenIdExists() {

		/**
		 * executa o comando deletar para o repository e espera que nada retorne caso id
		 * correto.
		 */
		Assertions.assertDoesNotThrow(() -> {
			service.delete(existingId);
		});
	}

	@Test
	public void deleteShouldThrowResourseNotFoundExceptionWhenIdDoesExists() {

		Assertions.assertThrows(ResourseNotFoundException.class, () -> {
			service.delete(nonExistingId);
		});
	}

	/*
	 * @Test public void findAllPageShouldReturnPage() {
	 * 
	 * Long categoryId = 0L; String name = ""; PageRequest pageRequest =
	 * PageRequest.of(0, 10);
	 * 
	 * Page<ProductDTO> result = service.findAllPaged(categoryId, name,
	 * pageRequest);
	 * 
	 * Assertions.assertNotNull(result); Assertions.assertFalse(result.isEmpty());
	 * 
	 * Mockito.verify(repository, Mockito.times(1)).find(null, name, pageRequest); }
	 * 
	 * @Test public void findByIdShouldReturnProductWhenIdExists() { ProductDTO
	 * result = service.findByID(existingId);
	 * 
	 * Assertions.assertNotNull(result);
	 * 
	 * Mockito.verify(repository, Mockito.times(1)).findById(existingId); }
	 * 
	 * @Test public void
	 * findByIdShouldThrowResourseNotFoundExceptionWhenIdDoesNotExists() {
	 * Assertions.assertThrows(ResourseNotFoundException.class, () -> {
	 * service.findByID(nonExistingId); }); }
	 * 
	 * @Test public void updateShouldReturnProductDTOWhenIdExists() { ProductDTO dto
	 * = new ProductDTO();
	 * 
	 * ProductDTO result = service.update(existingId, dto);
	 * 
	 * Assertions.assertNotNull(result);
	 * 
	 * }
	 * 
	 * @Test public void
	 * updateShouldThrowResourseNotFoundExceptionWhenIdDoesNotExists() { ProductDTO
	 * dto = new ProductDTO();
	 * 
	 * Assertions.assertThrows(ResourseNotFoundException.class, () -> {
	 * service.update(nonExistingId, dto); }); }
	 */
}
