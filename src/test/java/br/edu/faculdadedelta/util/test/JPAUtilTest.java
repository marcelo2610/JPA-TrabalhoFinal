package br.edu.faculdadedelta.util.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.persistence.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.util.JPAUtil;

public class JPAUtilTest {

	private EntityManager em;
	
	@Test
	public void deveTerInstnciaDoEntityManagerDefinida(){
		assertNotNull("deve ter instanciado o EntityManager", em);
	}
	
	@Test
	public void deveFecharEntityManagerDefinida(){
		em.close();
		assertFalse("deve fechar o EntityManager", em.isOpen());
	}
	
	@Test
	public void deveAbrirUmaTransacao(){

		assertFalse("transação deve estar fechada", em.getTransaction().isActive());
		em.getTransaction().begin();
		assertTrue("transação deve estar aberta", em.getTransaction().isActive());
	}
	@Before
	public void instanciarEntityManager(){
		em = JPAUtil.INSTANCE.getEntityManager();
	}
	@After
	public void fecharEntityManager() {
		if (em.isOpen()){
			em.close();
		}
	}
}
