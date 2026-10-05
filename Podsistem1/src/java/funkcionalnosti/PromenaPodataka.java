/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;

public class PromenaPodataka {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korIme = delovi[1];
        String novaAdresa = delovi[2];
        String nazivNovogGrada = delovi[3];
        String koTraziA = delovi[4];

        // 1. PROVERA ADMINA
        boolean isAdmin = false;
        try {
            Long count = em.createQuery(
                "SELECT COUNT(k) FROM Korisnik k JOIN k.ulogaList u WHERE k.korisnickoIme = :username AND u.naziv = 'administrator'", Long.class)
                .setParameter("username", koTraziA)
                .getSingleResult();
            isAdmin = (count > 0);
        } catch (Exception e) { System.out.println("Greska admin: " + e.getMessage()); }

        if (!isAdmin) return "GRESKA: Korisnik '" + koTraziA + "' nema administratorske privilegije!";

        // 2. AZURIRANJE PODATAKA
        try {
            em.getTransaction().begin();

            // Pronalazimo korisnika
            entiteti.Korisnik korisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :username", entiteti.Korisnik.class)
                                           .setParameter("username", korIme)
                                           .getSingleResult();

            // Pronalazimo novi grad
            entiteti.Grad noviGrad = em.createQuery("SELECT g FROM Grad g WHERE g.naziv = :naziv", entiteti.Grad.class)
                                         .setParameter("naziv", nazivNovogGrada)
                                         .getSingleResult();

            // Menjamo podatke
            korisnik.setAdresa(novaAdresa);
            korisnik.setIdGrada(noviGrad);

            em.getTransaction().commit();
            return "Uspesno promenjeni podaci za korisnika: " + korIme;

        } catch (javax.persistence.NoResultException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA: Korisnik ili grad ne postoje u bazi!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri promeni podataka: " + e.getMessage();
        }
    }
}
