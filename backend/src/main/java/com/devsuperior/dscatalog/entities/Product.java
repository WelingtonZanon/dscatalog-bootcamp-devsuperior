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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
//Classe, entidade (Objeto => atributos e metodos)
//transforma em entidade gerenciada pelo spring
@Entity
//renomeia tabela pro banco na JPA
@Table(name ="tb_product")
public class Product implements Serializable{	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	//anotation para campo grande de texto no banco(troca de varchar para text)	
	@Column(columnDefinition = "TEXT")
	private String description;
	private Double price;
	private String imgUrl;
	//anotation JPA para declarar o instante no banco retirando o time zona, deixando no formato UTC( time univerçal)
	@Column (columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	private Instant date;
/*
 * Set ao inves de lista para evitar repetição dos pares. Set(interface tem que ser implementado) sando
 * o HashSet para implementar. Um set de categoria para resolver uma associação de muitos para muitos.
 * uma lista de categorias. Tbm se implementa o Set da coleção somente o get, pq a coleção não sera alterada
 * quando ela for enserida, vc somente vai querer visualizar.	
 */
	//anotation de associação
	@ManyToMany
	//anotation primeiro nome da tabela da associação
	@JoinTable(name = "tb_product_category",
	//nome da coluna que vai ser associada
	joinColumns =@JoinColumn(name="product_id"),
	//coluna da outra entidade que sera associada
	inverseJoinColumns = @JoinColumn(name="category_id"))
	Set<Category> categories = new HashSet<>();
	
	//anotation JPA para declarar o instante no banco retirando o time zona, deixando no formato UTC( time univerçal)
	@Column (columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	private Instant createdAt;
	@Column (columnDefinition= "TIMESTAMP WITHOUT TIME ZONE")
	private Instant updateAt;
	
	public Product() {
		
	}
	//coleções não vai em construtores, não tem a lista.
	public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
		
	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}
			
	public Set<Category> getCategories() {
		return categories;
	}
	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdateAt() {
		return updateAt;
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
		Product other = (Product) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
