/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

public class DodavanjeNovca {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String ciljniKorisnikIme = delovi[1];
        String iznosStr = delovi[2];
        String koTraziA = delovi[3];

        // 1. PROVERA PRAVA (Da li je koTraziA administrator?)
        boolean isAdmin = false;
        try {
            Long count = em.createQuery(
                "SELECT COUNT(k) FROM Korisnik k JOIN k.ulogaList u WHERE k.korisnickoIme = :username AND u.naziv = 'administrator'", Long.class)
                .setParameter("username", koTraziA)
                .getSingleResult();
            isAdmin = (count > 0);
        } catch (Exception e) {
            System.out.println("Greska pri proveri admina: " + e.getMessage());
        }

        if (!isAdmin) {
            return "GRESKA: Korisnik '" + koTraziA + "' nema administratorske privilegije!";
        }

        // 2. DODAVANJE NOVCA
        try {
            em.getTransaction().begin();

            // Pronalazimo korisnika kojem se dodaje novac
            entiteti.Korisnik ciljniKorisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :username", entiteti.Korisnik.class)
                                               .setParameter("username", ciljniKorisnikIme)
                                               .getSingleResult();

            // Uzimamo staro stanje i dodajemo novi iznos
            BigDecimal trenutnoStanje = ciljniKorisnik.getStanjeNovca();
            if (trenutnoStanje == null) {
                trenutnoStanje = BigDecimal.ZERO; // Zastita ako je u bazi NULL
            }
            
            BigDecimal iznosZaDodavanje = new BigDecimal(iznosStr);
            ciljniKorisnik.setStanjeNovca(trenutnoStanje.add(iznosZaDodavanje));

            // Cuvanje promena
            em.persist(ciljniKorisnik); 
            em.getTransaction().commit();

            return "Uspesno dodato " + iznosStr + " novca korisniku '" + ciljniKorisnikIme + "'. Novo stanje: " + ciljniKorisnik.getStanjeNovca();

        } catch (javax.persistence.NoResultException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA: Korisnik '" + ciljniKorisnikIme + "' ne postoji u bazi!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri dodavanju novca: " + e.getMessage();
        }
    }
}