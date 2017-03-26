package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.LazyInitializationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Professor;
import br.edu.faculdadedelta.util.JPAUtil;

public class ProfessorTest {
	private EntityManager em;

	@Test(expected = LazyInitializationException.class)
	public void naoDeveAcessarAtributoLazydaEntidadeForaEscopoPersistencia() {
		deveSalvarProfessor();

		Professor professor = em.find(Professor.class, 1L);

		assertNotNull("verifica se encontrou um registro", professor);
		// em.detach(professor);
		em.clear();

		professor.getAulas().size();

		fail("deve disparar LazyInicializationException ao acessar "
				+ "atributo Lazy de um objeto fora do escopo do EntityManager");

	}

	@Test
	public void deveAcessarAtributoLazy() {
		deveSalvarProfessor();

		Professor professor = em.find(Professor.class, 1L);

		assertNotNull("verifica se encontrou um registro", professor);

		assertNotNull("lista lazy não deve ser null", professor.getAulas().size());

	}

	@Test(expected = NoResultException.class)
	public void naoDeveFuncionarSingleResultComNenhumRegistro() {
		deveSalvarProfessor();
		deveSalvarProfessor();

		Query query = em.createQuery("SELECT p.id FROM Professor p WHERE p.matricula = :matricula");
		query.setParameter("matricula", "222.222.222-22");

		query.getSingleResult();
		fail("deve ter dado NoResultException");
	}

	@Test(expected = NonUniqueResultException.class)
	public void naoDeveFuncionarSingleResultComMuitosRegistros() {
		deveSalvarProfessor();
		deveSalvarProfessor();
		Query query = em.createQuery("SELECT p.id FROM Professor p");
		query.getSingleResult();
		fail("deve ter dado NonUniqueResultException");
	}


	@Test
	public void deveConsultarProfessor() {
		deveSalvarProfessor();
		String filtro = "SOUZA";

		Query query = em.createQuery("SELECT p.matricula FROM Professor p WHERE p.nome LIKE :nome");
		query.setParameter("nome", "%" + filtro + "%");
		@SuppressWarnings("unchecked")
		List<String> listaMatricula = query.getResultList();
		assertFalse("verifica se há registros na lista", listaMatricula.isEmpty());
	}

	@Test
	public void deveAlterarProfessor() {
		deveSalvarProfessor();

		@SuppressWarnings("unchecked")
		TypedQuery<Professor> query = (TypedQuery<Professor>) em
				.createQuery("SELECT p FROM Professor p").setMaxResults(1);

		Professor professor = query.getSingleResult();

		assertNotNull("deve ter encontrado um professor", professor);

		Integer versao = professor.getVersion();

		em.getTransaction().begin();

		professor.setTitulacao("DOUTOR");

		professor = em.merge(professor);

		em.getTransaction().commit();

		assertNotEquals("deve ter versao incrementada", versao.intValue(), professor.getVersion().intValue());
	}

	@Test
	public void deveExcluirProfessor() {
		deveSalvarProfessor();
		TypedQuery<Long> query = em.createQuery("SELECT MAX(p.id) FROM Professor p", Long.class);
		Long id = query.getSingleResult();
		em.getTransaction().begin();
		Professor professor = em.find(Professor.class, id);
		em.remove(professor);
		em.getTransaction().commit();
		Professor professorExcluido = em.find(Professor.class, id);
		assertNull("não deve ter encontrado o professor", professorExcluido);

	}


	public void deveSalvarProfessor() {
		Professor professor = new Professor();
		professor.setNome("MARCELO DE SOUZA");
		professor.setMatricula("111.111.111-0");

		//assertTrue("não deve ter ID definido", professor.isTransient());

		em.getTransaction().begin();
		em.persist(professor);
		em.getTransaction().commit();

		//assertFalse("deve ter ID definido", professor.isTransient());

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

		Query query = entityManager.createQuery("DELETE FROM Professor p");
		int registrosExcluidos = query.executeUpdate();

		entityManager.getTransaction().commit();

		assertTrue("certifica que a base foi limpa", registrosExcluidos > 0);
	}

}
