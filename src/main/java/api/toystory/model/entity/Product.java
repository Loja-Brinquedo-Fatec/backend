package api.toystory.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="product")
public class Product {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private String preco;
	private String descriçao;
	
	@ManyToOne //muitos prosutos podem ter uma categoria
	@JoinColumn(name = "categoria_id")
	private Category categoria;
	private String marca;
	private String imagem;
	private Integer quantidade;
	
	@Column(columnDefinition = "TEXT")  // Especifica que o tipo no banco de dados é TEXT
	private String detalhes;

	public Product(Integer id, String nome, String preco, String descriçao, Category categoria, String marca, String imagem, Integer quantidade, String detalhes) {

		this.id = id;
		this.nome = nome;
		this.preco = preco;
		this.descriçao = descriçao;
		this.categoria = categoria;
		this.marca = marca;
		this.imagem = imagem;
		this.quantidade = quantidade;
		this.detalhes = detalhes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getPreco() {
		return preco;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public String getDescriçao() {
		return descriçao;
	}

	public void setDescriçao(String descriçao) {
		this.descriçao = descriçao;
	}

	public Category getCategoria() {
		return categoria;
	}

	public void setCategoria(Category categoria) {
		this.categoria = categoria;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getDetalhes() {
		return detalhes;
	}

	public void setDetalhes(String detalhes) {
		this.detalhes = detalhes;
	}
	
	
	
}