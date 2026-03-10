package utils;

import javax.persistence.*;
import model.Erabiltzaile;

public class BDOOKudeatzailea {

    private static final String RUTA_BDOO = "src/data/erabiltzaileak.odb";

    public static Erabiltzaile login(String usuario, String password) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(RUTA_BDOO);
        EntityManager em = emf.createEntityManager();
        Erabiltzaile user = null;

        try {
            // Bilatzen dugu primary key-a
            user = em.find(Erabiltzaile.class, usuario);
            
            // Aurkitzen badu bilatzen dugu pasahitza
            if (user != null && user.getPasahitza().equals(password)) {
                return user; //Bueltatzen dugu erabiltzailea
            }
        } finally {
            em.close();
            emf.close();
        }
        
        return null;
    }
}