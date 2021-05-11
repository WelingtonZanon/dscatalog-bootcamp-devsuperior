package com.devsuperior.dscatalog.tests.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.tests.factory.ProductFactory;

/*
anotação para que carregue somente componentes da SpringJPA, no caso como é
somente para testar o repositorio na precisa carregar todos os componentes
da aplicação. 

*/
@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	private long countPCGamerProducts;
	private Pageable pageable;
	private long countCategory3Products;
	private PageRequest pageRequest;
	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		countPCGamerProducts = 21L;
		countCategory3Products = 23L;
		pageRequest = PageRequest.of(0, 10);
	}
	
	@Test
	public void findShouldReturnNothingWhenNameDoesNotExist() {
		
		String name = "Camera";
		Page<Product> result = repository.find(null, name, pageRequest);
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void findShouldReturnOnlySelectedProductWhenCategoryInformed() {
		List<Category> categories = new ArrayList<>();
		categories.add(new Category(3L, null));
		
		Page<Product> resut = repository.find(categories, "", pageable);
		
		Assertions.assertFalse(resut.isEmpty());
		Assertions.assertEquals(countCategory3Products, resut.getTotalElements());
		
	}
	
	@Test
	public void findShouldReturnAllProductWhenCategoryNotInformed() {
		List<Category> categories = null;
		
		Page<Product> resut = repository.find(categories, "", pageable);
		
		Assertions.assertFalse(resut.isEmpty());
		Assertions.assertEquals(countTotalProducts, resut.getTotalElements());
		
	}
	
	@Test
	public void findShouldReturnAllProductsWhenNameIsEmpity() {
		
		String name = "";
		
		Page<Product> resut = repository.find(null, name, pageable);
		
		Assertions.assertFalse(resut.isEmpty());
		Assertions.assertEquals(countTotalProducts, resut.getTotalElements());
	}	
	
	@Test
	public void findShouldReturnProductsWhenNameExistsIgnoreCase() {
		
		String name = "pC gaMer";
		
		Page<Product> resut = repository.find(null, name, pageable);
		
		Assertions.assertFalse(resut.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, resut.getTotalElements());
	}
	
	@Test
	public void findShouldReturnProductsWhenNameExists() {
		
		String name = "PC Gamer";
		
		Page<Product> resut = repository.find(null, name, pageable);
		
		Assertions.assertFalse(resut.isEmpty());
		Assertions.assertEquals(countPCGamerProducts, resut.getTotalElements());
	}
	
	
	@Test
	public void saveShouldPersistWihAutoincrementWhenIdIsNull() {
		
		Product product = ProductFactory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		Optional<Product> result = repository.findById(product.getId());
		
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1L, product.getId());		
		Assertions.assertTrue(result.isPresent());
		Assertions.assertSame(product, result.get());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result = repository.findById(existingId);
		
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowResultDataAccessExceptioWhenIdDoesNotExist() {
		
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(nonExistingId);
		});		
	}
}
