package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Aula;
import br.edu.faculdadedelta.modelo.Disciplina;
import br.edu.faculdadedelta.modelo.Professor;
import br.edu.faculdadedelta.util.JPAUtil;

public class AulaTest {
	private static final String MATRICULA_PADRAO = "111.111.111-0";
	private EntityManager em;
	
	@Test
	public void deveConsultarQuantidadeDeAulasDeUmProfessor() {
		Aula aula = criarAula("999.999.999-0");
		
		for (int i = 0; i < 10; i++) {
			aula.getDisciplina().add(criarDisciplina("Nome " + i, "Código " + i));
		}
		
		em.getTransaction().begin();
		em.persist(aula);
		em.getTransaction().commit();
		
		assertFalse("deve ter persistido a aula", aula.isTransient());
		
		int qtdAulas = aula.getDisciplina().size();
		
		assertTrue("lista de produtos deve ter itens", qtdAulas > 0);
		
		StringBuilder jpql = new StringBuilder();
		jpql.append(" SELECT COUNT(d.id) ");
		jpql.append("   FROM Aula a ");
		jpql.append("  INNER JOIN a.disciplina d ");
		jpql.append("  INNER JOIN a.professor p ");
		jpql.append("  WHERE p.matricula = :matricula ");
		
		Query query = em.createQuery(jpql.toString());
		query.setParameter("matricula", "999.999.999-0");

		Long qtdTotal = (Long) query.getSingleResult();

	}
	@Test(expected = IllegalStateException.class)
	public void naoDeveFazerMergeEmCascata() {
		Aula aula = criarAula();
		aula.getDisciplina().add(criarDisciplina("POO", "CMP-1111"));
		aula.getDisciplina().add(criarDisciplina("METODOLOGIA ÁGIL", "CMP-5555"));

		assertTrue("aula não foi persistida ainda", aula.isTransient());

		em.getTransaction().begin();
		aula = em.merge(aula);
		em.getTransaction().commit();

		assertFalse("aula foi persistida", aula.isTransient());

		assertFalse("professor foi persistido", aula.getProfessor().isTransient());

		fail("não deveria ter feito merge em cascata");
	}

	@Test
	public void deveSalvarAulaComRelacionamentoEmCascata() {
		Aula aula = criarAula();
		aula.getDisciplina().add(criarDisciplina("Banco de Dados", "CMP-1587"));
		aula.getDisciplina().add(criarDisciplina("Redes I", "CMP-1472"));

		assertTrue("aula não foi persistida ainda", aula.isTransient());

		em.getTransaction().begin();
		em.persist(aula);
		em.getTransaction().commit();

		assertFalse("aula foi persistida", aula.isTransient());

		assertFalse("professor foi persistido", aula.getProfessor().isTransient());

		aula.getDisciplina().forEach(disciplina -> {
			assertFalse("disciplina foi persistido", disciplina.isTransient());
		});
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

	@AfterClass
	public static void deveLimparBase() {
		EntityManager em = JPAUtil.INSTANCE.getEntityManager();

		em.getTransaction().begin();

		Query query = em.createQuery("delete from Aula v");

		int registrosExcluidos = query.executeUpdate();

		em.getTransaction().commit();

		assertTrue("deve ter excluido registros", registrosExcluidos > 0);

	}

	private Disciplina criarDisciplina(String nome, String codigo) {
		Disciplina disciplina = new Disciplina();
		disciplina.setNome(nome);
		disciplina.setCodigo(codigo);
		return disciplina;
	}

	private Aula criarAula() {
		return criarAula(null);
	}

	private Aula criarAula(String matricula) {
		Professor professor = new Professor();
		professor.setMatricula(matricula != null ? matricula : MATRICULA_PADRAO);
		professor.setNome("MARCELO DE SOUZA");

		Aula aula = new Aula();
		aula.setHorario(new Date());
		aula.setProfessor(professor);

		return aula;
	}
}
