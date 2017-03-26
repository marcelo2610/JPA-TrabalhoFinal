package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aula;
import br.edu.faculdadedelta.modelo.Disciplina;
import br.edu.faculdadedelta.modelo.Professor;
import br.edu.faculdadedelta.util.JPAUtil;

public class CriteriaTest {
	private static final String MATRICULA_PADRAO = "111.111.111-0";
	private EntityManager em;

	@Test
	@SuppressWarnings("unchecked")
	public void deveConsultarIdENomeConverterProfessor() {
		salvarProfessor(3);
		
		ProjectionList projection = Projections.projectionList()
				// SELECT field_a, field_b, field_c
				.add(Projections.property("p.id").as("id"))
				.add(Projections.property("p.nome").as("nome"));
		
		Criteria criteria = createCriteria(Professor.class, "p")
				.setProjection(projection);
		
		List<Professor> professors = criteria
				.setResultTransformer(Transformers.aliasToBean(Professor.class))
				.list();
		
		assertTrue("verifica se a quantidade de professors é pelo menos 3", professors.size() >= 3);
		
		professors.forEach(professor -> {
			assertTrue("ID deve estar preenchido", professor.getId() != null);
			assertTrue("Nome deve estar prenchido", professor.getNome() != null);
			assertTrue("Matricula não deve estar preenchido", professor.getMatricula() == null);
		});
	}
	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultaIdENomeDisciplinaEmMap() {
		salvarDisciplina(3);

		ProjectionList projection = Projections.projectionList().add(Projections.property("p.id").as("id"))
				.add(Projections.property("p.nome").as("nome"));

		Criteria criteria = createCriteria(Disciplina.class, "p").setProjection(projection)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List<Map<String, Object>> disciplinas = criteria.list();
		disciplinas.forEach(disciplinaMap -> {
			disciplinaMap.forEach((chave, valor) -> {
				assertNotNull(chave);
				assertNotNull(valor);
				
			});
		});
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultaIdENomeDisciplina() {
		salvarDisciplina(3);

		ProjectionList projection = Projections.projectionList().add(Projections.property("d.id").as("id"))
				.add(Projections.property("d.nome").as("nome"));

		Criteria criteria = createCriteria(Disciplina.class, "d").setProjection(projection)
				.setResultTransformer(Criteria.PROJECTION);

		List<Object[]> disciplinas = criteria.list();
		assertFalse("deve ter disciplinas", disciplinas.isEmpty());
		disciplinas.forEach(disciplina -> {
			assertTrue("primeiro item é o ID",  disciplina[0] instanceof Long);
			assertTrue("segundo item é o nome", disciplina[1] instanceof String);
		});
	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarAulasPorNomeProfessorCasoExista() {
		salvarAulas(1);

		Criteria criteria = createCriteria(Aula.class, "a").createAlias("a.professor", "p", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.ilike("p.nome", "MARCELO DE SOUZA", MatchMode.START))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<Aula> aulas = criteria.list();
		assertFalse("deve ter encontrado aulas para MARCELO DE SOUZA", aulas.isEmpty());
	}


	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarDisciplinaContendoParteDoNome() {
		salvarDisciplina(3);
		Criteria criteria = createCriteria(Disciplina.class, "d")
				.add(Restrictions.ilike("d.nome", "COMPUTADORES", MatchMode.ANYWHERE))
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		List<Disciplina> disciplina = criteria.list();

		assertFalse("deve ter encontrado disciplinas", disciplina.isEmpty());
	}

	@Test
	public void devConsultarQuantidadeAulasDisciplinaProfessor() {
		salvarAulas(3);
		Criteria criteria = createCriteria(Aula.class, "a").createAlias("a.professor", "p")
				.add(Restrictions.eq("p.matricula", MATRICULA_PADRAO)).setProjection(Projections.rowCount());

		Long qtdRegistros = (Long) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
		assertTrue("verifica se a quantidade de aulas é pelo menos 3", qtdRegistros >= 3);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void deveConsultarDezPrimeirasDisciplinas() {
		salvarDisciplina(30);

		Criteria criteria = createCriteria(Disciplina.class, "d").setFirstResult(1).setMaxResults(10);

		List<Disciplina> disciplinas = criteria.list();

		assertFalse("deve ter disciplinas", disciplinas.isEmpty());

		assertTrue("deve ter só 10 itens", disciplinas.size() == 10);

		disciplinas.forEach(disciplina -> assertFalse(disciplina.isTransient()));

	}

	@Test
	public void deveConsultarAulasDoProximoMes() {
		salvarAulas(3);
		Calendar proximoMes = Calendar.getInstance();
		proximoMes.add(Calendar.MONTH, 1);

		Criteria criteria = createCriteria(Aula.class, "a")
				.add(Restrictions.between("a.horario", proximoMes.getTime(), new Date()))
				.setProjection(Projections.rowCount());
		@SuppressWarnings("unused")
		Long qtdAulas = (Long) criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).uniqueResult();
	}

	@Test
	public void deveConsultarTodasAulas() {
		salvarAulas(3);
		Criteria criteria = createCriteria(Aula.class, "a");
		@SuppressWarnings("unchecked")
		List<Aula> aulas = criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

		assertTrue("lista deve ter pelo menos 3", aulas.size() >= 3);
		aulas.forEach(aula -> assertFalse(aula.isTransient()));
	}

	@Test
	public void deveConsultarMaiorIdProfessor() {
		salvarProfessor(3);
		Criteria criteria = createCriteria(Professor.class, "p").setProjection(Projections.max("p.id"));
		Long maiorId = (Long) criteria.setResultTransformer(Criteria.PROJECTION).uniqueResult();

		assertTrue("ID deve ser pelo menos 3", maiorId >= 3);

	}

	private Session getSession() {
		return (Session) em.getDelegate();
	}

	@SuppressWarnings("unused")
	private Criteria createCriteria(Class<?> clazz) {
		return getSession().createCriteria(clazz);
	}

	private Criteria createCriteria(Class<?> clazz, String alias) {
		return getSession().createCriteria(clazz, alias);
	}

	public void salvarProfessor(int quantidade) {
		em.getTransaction().begin();
		for (int i = 0; i < quantidade; i++) {
			Professor professor = new Professor();
			professor.setNome("MARCELO");
			professor.setMatricula(MATRICULA_PADRAO);
			em.persist(professor);
		}
		em.getTransaction().commit();
	}

	public void salvarDisciplina(int quantidade) {
		em.getTransaction().begin();
		for (int i = 0; i < quantidade; i++) {
			Disciplina disciplina = salvarDisciplina("REDES DE COMPUTADORES", "CMP-8520");
			em.persist(disciplina);
		}
		em.getTransaction().commit();
	}

	public void salvarAulas(int quantidade) {
		em.getTransaction().begin();
		for (int i = 0; i < quantidade; i++) {
			Aula aula = salvarAula();
			aula.getDisciplina().add(salvarDisciplina("Álgebra Linear", "MAF-7878"));
			aula.getDisciplina().add(salvarDisciplina("Matemática Finita", "MAF-1010"));

			em.persist(aula);
		}
		em.getTransaction().commit();
	}

	@Before
	public void instanciarEntityManager() {
		em = JPAUtil.INSTANCE.getEntityManager();
	}

	@After
	public void fecharEntityManager() {
		if (em.isOpen()) {
			em.close();
		}
	}

	private Disciplina salvarDisciplina(String nome, String codigo) {
		Disciplina disciplina = new Disciplina();
		disciplina.setNome(nome);
		disciplina.setCodigo(codigo);

		return disciplina;
	}

	private Aula salvarAula() {
		return salvarAula(null);
	}

	private Aula salvarAula(String matricula) {
		Professor professor = new Professor();
		professor.setMatricula(matricula != null ? matricula : MATRICULA_PADRAO);
		professor.setNome("MARCELO DE SOUZA");

		Aula aula = new Aula();
		aula.setHorario(new Date());
		aula.setProfessor(professor);

		return aula;
	}
}
