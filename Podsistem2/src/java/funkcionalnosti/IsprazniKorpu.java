/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korpa;
import javax.persistence.EntityManager;

public class IsprazniKorpu {
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];
        try {
            em.getTransaction().begin();
            Korpa korpa = em.createQuery("SELECT k FROM Korpa k WHERE k.korisnickoIme = :korIme", Korpa.class)
                            .setParameter("korIme", korisnickoIme)
                            .getSingleResult();
            
            // Brisemo sve stavke koje pripadaju ovoj korpi
            em.createQuery("DELETE FROM KorpaStavka s WHERE s.korisnickoIme = :korpa")
              .setParameter("korpa", korpa)
              .executeUpdate();
            
            // Vracamo ukupnu cenu korpe na nulu
            korpa.setUkupnaCena(java.math.BigDecimal.ZERO);
            
            em.getTransaction().commit();
            return "OK";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA";
        }
    }
}