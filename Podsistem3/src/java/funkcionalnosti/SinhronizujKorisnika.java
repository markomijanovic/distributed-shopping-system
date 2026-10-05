/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korisnik;
import javax.persistence.EntityManager;

public class SinhronizujKorisnika {
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];
        try {
            em.getTransaction().begin();
            // Proveravamo da li slucajno vec postoji
            Korisnik k = em.find(Korisnik.class, korisnickoIme);
            if (k == null) {
                k = new Korisnik();
                k.setKorisnickoIme(korisnickoIme);
                em.persist(k);
            }
            em.getTransaction().commit();
            return "OK";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri sinhronizaciji korisnika: " + e.getMessage();
        }
    }
}