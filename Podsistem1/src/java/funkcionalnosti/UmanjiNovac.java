/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korisnik;
import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class UmanjiNovac {
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];
        BigDecimal iznosZaSkidanje = new BigDecimal(delovi[2]);

        try {
            em.getTransaction().begin();
            Korisnik korisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korIme", Korisnik.class)
                                  .setParameter("korIme", korisnickoIme)
                                  .getSingleResult();

            if (korisnik.getStanjeNovca().compareTo(iznosZaSkidanje) < 0) {
                em.getTransaction().rollback();
                return "NEMA_PARA"; // Korisnik nema dovoljno novca
            }

            // Umanjujemo novac
            korisnik.setStanjeNovca(korisnik.getStanjeNovca().subtract(iznosZaSkidanje));
            em.getTransaction().commit();
            
            return "OK";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA";
        }
    }
}