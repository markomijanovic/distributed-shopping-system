/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import java.math.BigDecimal;

public class PromeniPopust {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String naziv = delovi[1];
        String noviPopust = delovi[2];

        try {
            em.getTransaction().begin();

            // Pronalazimo artikal po nazivu
            Artikal artikal = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class)
                                .setParameter("naziv", naziv)
                                .getSingleResult();

            // Postavljamo novi popust
            artikal.setPopust(new BigDecimal(noviPopust));

            em.persist(artikal);
            em.getTransaction().commit();

            return "Uspesno promenjen popust za artikal '" + naziv + "' na: " + noviPopust + "%";

        } catch (javax.persistence.NoResultException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA: Artikal sa nazivom '" + naziv + "' ne postoji!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri promeni popusta: " + e.getMessage();
        }
    }
}
