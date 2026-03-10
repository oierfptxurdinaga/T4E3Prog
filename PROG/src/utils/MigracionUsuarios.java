package utils;

import javax.persistence.*;
import model.*;
import java.util.ArrayList;

public class MigracionUsuarios {

    public static void main(String[] args) {
        System.out.println("Iniciando migración de usuarios a ObjectDB...");

        // 1. Cargar los usuarios desde el viejo archivo .ser
        Federazioa federazioa = DatuKarga.kargatuFederazioa();
        ArrayList<Erabiltzaile> usuariosAntiguos = federazioa.getErabiltzaileak();

        if (usuariosAntiguos == null || usuariosAntiguos.isEmpty()) {
            System.out.println("No se encontraron usuarios en el archivo .ser");
            return;
        }

        // 2. Abrir conexión con ObjectDB (Creará el archivo erabiltzaileak.odb automáticamente)
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("src/data/erabiltzaileak.odb");
        EntityManager em = emf.createEntityManager();

        // 3. Guardar los usuarios en la base de datos orientada a objetos
        em.getTransaction().begin();
        for (Erabiltzaile user : usuariosAntiguos) {
            // Comprobar que no exista ya para evitar errores
            Erabiltzaile existente = em.find(Erabiltzaile.class, user.getErabiltzaile());
            if (existente == null) {
                em.persist(user);
                System.out.println("Guardado: " + user.getErabiltzaile() + " (" + user.getClass().getSimpleName() + ")");
            }
        }
        em.getTransaction().commit();

        // 4. Cerrar conexiones
        em.close();
        emf.close();
        System.out.println("¡Migración completada con éxito!");
    }
}