package br.edu.faculdadedelta.util.test;

import static org.junit.Assert.*;


import javax.persistence.*;

import org.junit.*;


import br.edu.faculdadedelta.util.JPAUtil;

public class JPAUtilTest {

	private EntityManager em;
	
	@Before
	public void instanciarEntityManager(){
		//em = Persistence.createEntityManagerFactory("DeltaPU").createEntityManager(); 
		em = JPAUtil.INSTANCE.getEntityManager();
	}
	
	@After
	public void fecharEntityManager() {
		if (em.isOpen()){
			em.close();
		}
	}
	
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
	

}
