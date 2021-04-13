package com.devsuperior.dscatalog.entities;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

//Classe, entidade (Objeto => atributos e metodos)
//transforma em entidade gerenciada pelo spring
@Entity
//renomeia tabela pro banco na JPA
@Table(name ="tb_category")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	
	//anotation JPA para declarar o instante no banco retirando o time zona, deixando no formato UTC( time univerçal)
	@Column (columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;
	@Column (columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updateAt;
	
	//faz a ligação com a associação de muitos para muitos com a outra tabela que ja tem o mapiamento.
	@ManyToMany(mappedBy = "categories")
	private Set<Product> products = new HashSet<>();
	
	public Category() {		
	}
	
	public Category(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

	public Instant getCreatedAt() {
		return createdAt;
	}



	public Instant getUpdateAt() {
		return updateAt;
	}
	//JPA própria para registrar o stant da iserção, n colocar o set fazer isso automatico para persistir e atualizar.
	@PrePersist
	public void prePersist() {
		createdAt = Instant.now();
	}
	@PreUpdate
	public void preUpdate() {
		updateAt = Instant.now();
	}	

	public Set<Product> getProducts() {
		return products;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
}
