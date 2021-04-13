/*
 * no DTO do usuario nao implementamos o passoword pq n é 
 * viavel que ele fique transitando para frente da aplicação
 */


package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.devsuperior.dscatalog.entities.User;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private List<RoleDTO> roles = new ArrayList<>();

	public UserDTO() {

	}

	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		/*
		 * usando o Eager na classe, a lista de role já vem pindurado
		 * na resposta, para cada item que veio da resposta setamos um
		 * novo role passado pelo argumento
		 */
		entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


	public List<RoleDTO> getRoles() {
		return roles;
	}

}
