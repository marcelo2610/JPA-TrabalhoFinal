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

import br.edu.faculdadedelta.base.test.BaseTest;
import br.edu.faculdadedelta.modelo.Professor;
import br.edu.faculdadedelta.modelo.Disciplina;
import br.edu.faculdadedelta.modelo.Aula;
import br.edu.faculdadedelta.util.JPAUtil;

public class AulaTest {
	private static final String MATRICULA_PADRAO = "111.111.111-11";
	private EntityManager em;

	@Test(expected = IllegalStateException.class)
	public void naoDeveFazerMergeEmCascata() {
		Aula aula = criarAula();
		aula.getDisciplina().add(criarDisciplina("Notebook", "Dell"));
		aula.getDisciplina().add(criarDisciplina("Mouse", "Razer"));

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
		professor.setNome("Marcelo de Souza Costa");

		Aula aula = new Aula();
		aula.setHorario(new Date());
		aula.setProfessor(professor);

		return aula;
	}
}
