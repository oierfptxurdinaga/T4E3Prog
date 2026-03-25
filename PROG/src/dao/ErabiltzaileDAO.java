package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import model.Erabiltzaile;


public class ErabiltzaileDAO {

	public boolean gordeErabiltzaileaODB(Erabiltzaile erab) {
	    String dbFile = "src/data/erabiltzaileak.odb";
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory(dbFile);
	    EntityManager em = emf.createEntityManager();
	    try {
	        em.getTransaction().begin();
	        em.persist(erab);	        
	        em.getTransaction().commit();
	        return true;
	    } catch (Exception e) {
	        if (em.getTransaction().isActive()) {
	            em.getTransaction().rollback();
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        em.close();
	        emf.close();
	    }
	}
	
}
