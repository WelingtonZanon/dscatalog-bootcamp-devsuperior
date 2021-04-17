package com.devsuperior.dscatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
//transformar em um componente injetavel pelo gerenciador do spring um repositorio
@Repository 
public interface ProductRepository extends JpaRepository<Product, Long>{
/*
 * pageable mais generico que o page request, no caso do repositorio tem que
 * ser o pageable = > domain
 */
	
/*@Query anotação JPA para executar JPQL e SQL queries	
 * JPQL é diferente de um SQL puro exe abaixo.
 * SELECT "novevariavel" FROM Product "repeteOnomeDaVariavel" where
 * ":" ande da variavel faz referencia ao objeto do find.
 * o Product faz referencia a entidade e n a tabela.
 * 
 * (INNER JOIN obj.categories cats) acrescentado para uma assossiação de muitos
 */	
	@Query("SELECT DISTINCT obj FROM Product obj INNER JOIN obj.categories cats where"
			/*+ ":category IN obj.categories")*/
			+ "(COALESCE(:categories) IS NULL OR cats IN :categories) AND"
			+ "(LOWER(obj.name) LIKE LOWER(CONCAT('%',:name,'%')) )")
	Page<Product> find (List<Category> categories,String name, Pageable pageable);
}
