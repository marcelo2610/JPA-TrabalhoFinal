package br.edu.faculdadedelta.modelo;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Disciplina extends BaseEntity<Long> {

	private static final long serialVersionUID = -571450929835176999L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_disciplina", nullable = false, unique = true)
	private Long id;

	@Column(name = "codigo_disciplina", length = 20)
	private String codigo;

	@Column(name = "nome_disciplina", length = 50)
	private String nome;

	public Disciplina() {
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}