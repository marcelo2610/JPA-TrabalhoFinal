package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Disciplina;
import br.edu.faculdadedelta.util.JPAUtil;

public class DisciplinaTest {
	private EntityManager em;

	@Test
	public void devePesquisarDisciplina() {
		for (int i = 0; i < 10; i++) {
			deveSalvarDisciplina();
		}

		TypedQuery<Disciplina> query = em.createQuery("SELECT d FROM Disciplina d", Disciplina.class);

		List<Disciplina> disciplinas = query.getResultList();

		assertFalse("deve ter itens na lista.", disciplinas.isEmpty());
		assertTrue("deve ter pelo menos 10 itens na lista.", disciplinas.size() >= 10);
	}

	@Test
	public void deveSalvarDisciplina() {
		Disciplina disciplina = new Disciplina();
		disciplina.setNome("Sistemas Operacionais");
		disciplina.setCodigo("CMP-9874");
		assertTrue("não deve ter Id definido", disciplina.isTransient());

		em.getTransaction().begin();
		em.persist(disciplina);
		em.getTransaction().commit();

		assertNotNull("deve ter Id definido", disciplina.getId());

	}

	@Test
	public void deveAlterarDisciplina() {
		deveSalvarDisciplina();
		TypedQuery<Disciplina> query = em.createQuery("SELECT p FROM Disciplina d", Disciplina.class).setMaxResults(1);

		Disciplina disciplina = query.getSingleResult();
		assertNotNull("deve ter encontrado um disciplina", disciplina);
		Integer versao = disciplina.getVersion();
		em.getTransaction().begin();
		disciplina.setCodigo("CMP-1245");
		disciplina = em.merge(disciplina);
		em.getTransaction().commit();
		assertNotEquals("versao deve ser diferente", versao, disciplina.getVersion());

	}

	@Test
	public void deveExcluirDisciplina() {
		deveSalvarDisciplina();
		TypedQuery<Long> query = em.createQuery("SELECT MAX(d.id) FROM Disciplina d", Long.class);
		Long id = query.getSingleResult();
		em.getTransaction().begin();
		Disciplina disciplina = em.find(Disciplina.class, id);
		em.remove(disciplina);
		em.getTransaction().commit();
		Disciplina disciplinaExcluida = em.find(Disciplina.class, id);
		assertNull("não deve ter encontrado a disciplina", disciplinaExcluida);

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
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		entityManager.getTransaction().begin();

		Query query = entityManager.createQuery("DELETE FROM Disciplina d");
		int registrosExcluidos = query.executeUpdate();

		entityManager.getTransaction().commit();

		assertTrue("certifica que a base foi limpa", registrosExcluidos > 0);
	}
}
