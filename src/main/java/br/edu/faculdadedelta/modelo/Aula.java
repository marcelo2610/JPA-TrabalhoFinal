package br.edu.faculdadedelta.modelo;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Aula extends BaseEntity<Long> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1059043256442040098L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_aula", unique = true, nullable = false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dh_aula")
	private Date horario;

	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_professor", referencedColumnName = "id_professor", insertable = true, updatable = false, nullable = false)
	private Professor professor;

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST })
	@JoinTable(name = "aula_disciplina",
			joinColumns = @JoinColumn(name = "id_disciplina"),
			inverseJoinColumns = @JoinColumn(name = "id_disciplina"))
	private List<Disciplina> disciplina;


	public Aula() {

	}


	public Aula(Long id, Date horario, Professor professor, List<Disciplina> disciplina) {
		super();
		this.id = id;
		this.horario = horario;
		this.professor = professor;
		this.disciplina = disciplina;
	}


	public Long getId() {
		return id;
	}


	public Aula setId(Long id) {
		this.id = id;
		return this;
	}


	public Date getHorario() {
		return horario;
	}


	public Aula setHorario(Date horario) {
		this.horario = horario;
		return this;
	}


	public Professor getProfessor() {
		return professor;
	}


	public void setProfessor(Professor professor) {
		this.professor = professor;
	}


	public List<Disciplina> getDisciplina() {
		return disciplina;
	}


	public void setDisciplina(List<Disciplina> disciplina) {
		this.disciplina = disciplina;
	}


}