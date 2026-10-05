/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Kategorija; // Prilagodi import ako ti se entitet zove drugacije
import java.util.List;

public class KreirajKategoriju {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String naziv = delovi[1];
        String nazivNadkategorije = delovi[2];
        // String koTrazi = delovi[3]; // Trenutno preskacemo proveru admina da ne bismo duplirali tabele, oslanjamo se na API ogranicenja

        try {
            em.getTransaction().begin();
            List<Kategorija> provera = em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv = :naziv", Kategorija.class)
                             .setParameter("naziv", naziv)
                             .getResultList();
            if (!provera.isEmpty()) {
                return "GRESKA: Kategorija sa nazivom '" + naziv + "' vec postoji u bazi!";
            }

            Kategorija novaKategorija = new Kategorija();
            novaKategorija.setNaziv(naziv);

            // Ako je korisnik u URL-u uneo ime nadkategorije (nije prosledio "null")
            if (!nazivNadkategorije.equals("null")) {
                try {
                    Kategorija nadkategorija = em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv = :naziv", Kategorija.class)
                                                 .setParameter("naziv", nazivNadkategorije)
                                                 .getSingleResult();
                    novaKategorija.setNadkategorijaId(nadkategorija);
                } catch (javax.persistence.NoResultException ex) {
                    em.getTransaction().rollback();
                    return "GRESKA: Nadkategorija '" + nazivNadkategorije + "' ne postoji u bazi!";
                }
            }

            em.persist(novaKategorija);
            em.getTransaction().commit();

            return "Uspesno kreirana kategorija: " + naziv;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri upisu kategorije: " + e.getMessage();
        }
    }
}