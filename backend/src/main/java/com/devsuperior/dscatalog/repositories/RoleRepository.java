package com.devsuperior.dscatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatalog.entities.Role;
//transformar em um componente injetavel pelo gerenciador do spring um repositorio
@Repository 
public interface RoleRepository extends JpaRepository<Role, Long>{

}
