/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import entiteti.Kategorija;
import entiteti.Korisnik;
import java.math.BigDecimal;

public class KreirajArtikal {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String naziv = delovi[1];
        String opis = delovi[2];
        String cena = delovi[3];
        String popust = delovi[4];
        String nazivKategorije = delovi[5];

        try {
            em.getTransaction().begin();

            // 1. Pronalazimo kategoriju po nazivu
            Kategorija kategorija;
            try {
                kategorija = em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv = :naziv", Kategorija.class)
                               .setParameter("naziv", nazivKategorije)
                               .getSingleResult();
            } catch (javax.persistence.NoResultException ex) {
                em.getTransaction().rollback();
                return "GRESKA: Kategorija '" + nazivKategorije + "' ne postoji!";
            }

            // 2. Kreiramo artikal
            Artikal noviArtikal = new Artikal();
            noviArtikal.setNaziv(naziv);
            noviArtikal.setOpis(opis);
            noviArtikal.setCena(new BigDecimal(cena));
            noviArtikal.setPopust(new BigDecimal(popust));
            
            // Onaj "koTrazi" (delovi[6]) je zapravo prodavac koji kreira artikal
            Korisnik prodavac = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korIme", Korisnik.class)
                      .setParameter("korIme", delovi[6]) // koTrazi je prosleđen kao 6. element
                      .getSingleResult();
            noviArtikal.setProdavac(prodavac); // Podesi na osnovu imena setera
            // PAŽNJA: Proveri u klasi Artikal.java kako se zove setter za kategoriju!
            // Ako ti podvuče crveno, verovatno je setIdKategorije ili setKategorijaId.
            noviArtikal.setIdKategorije(kategorija); 

            em.persist(noviArtikal);
            em.getTransaction().commit();

            return "Uspesno kreiran artikal: " + naziv;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri upisu artikla: " + e.getMessage();
        }
    }
}