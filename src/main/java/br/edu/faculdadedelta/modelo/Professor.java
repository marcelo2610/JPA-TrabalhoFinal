package br.edu.faculdadedelta.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Professor extends BaseEntity<Long> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6564823279996476223L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_professor", nullable = false, unique = true)
	private Long id;

	@Column(name = "mat_professor", nullable = false, unique = true)
	private String matricula;
	
	@Column(name = "nome_professor", nullable = false, length = 80)
	private String nome;

	@Column(name = "tit_professor", length = 20)	
	private String titulacao;

	@Column(name = "tel_professor", length = 15)	
	private String telefone;
	
	@Column(name = "email_professor", length = 100)
	private String email;

	@Temporal(TemporalType.DATE)
	@Column(name = "dtadm_professor")
	private Date dataAdmissao;

	@OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
	private List<Aula> aulas;

	public Professor() {

	}

	public Professor(Long id, String matricula, String nome, String titulacao, String telefone, String email,
			Date dataAdmissao) {
		super();
		this.id = id;
		this.matricula = matricula;
		this.nome = nome;
		this.titulacao = titulacao;
		this.telefone = telefone;
		this.email = email;
		this.dataAdmissao = dataAdmissao;
	}

	public Long getId() {
		return id;
	}

	public Professor setId(Long id) {
		this.id = id;
		return this;
	}

	public String getMatricula() {
		return matricula;
	}

	public Professor setMatricula(String matricula) {
		this.matricula = matricula;
		return this;
	}

	public String getNome() {
		return nome;
	}

	public Professor setNome(String nome) {
		this.nome = nome;
		return this;
	}

	public String getTitulacao() {
		return titulacao;
	}

	public Professor setTitulacao(String titulacao) {
		this.titulacao = titulacao;
		return this;
	}

	public String getTelefone() {
		return telefone;
	}

	public Professor setTelefone(String telefone) {
		this.telefone = telefone;
		return this;
	}

	public String getEmail() {
		return email;
	}

	public Professor setEmail(String email) {
		this.email = email;
		return this;
	}

	public Date getDataAdmissao() {
		return dataAdmissao;
	}

	public Professor setDataAdmissao(Date dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
		return this;
	}

	public List<Aula> getAulas() {
		return aulas;
	}

	public void setAulas(List<Aula> aulas) {
		this.aulas = aulas;
	}

}