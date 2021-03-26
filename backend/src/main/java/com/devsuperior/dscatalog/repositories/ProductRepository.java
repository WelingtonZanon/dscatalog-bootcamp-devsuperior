package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Product;
//transformar em um componente injetavel pelo gerenciador do spring um repositorio
@Repository 
public interface ProductRepository extends JpaRepository<Product, Long>{

}
