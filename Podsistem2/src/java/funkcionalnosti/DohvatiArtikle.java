package funkcionalnosti;

import entiteti.Artikal;
import entiteti.Korisnik;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiArtikle {
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];

        // 1. Nalazimo prodavca (Korisnik objekat)
        Korisnik prodavacKorisnik;
        try {
            prodavacKorisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korIme", Korisnik.class)
                         .setParameter("korIme", korisnickoIme)
                         .getSingleResult();
        } catch (Exception e) {
            return "GRESKA: Korisnik (prodavac) '" + korisnickoIme + "' ne postoji!";
        }

        // 2. Trazimo artikle gde je prodavac taj korisnik
        // ISPRAVLJENO: Koristimo a.prodavac umesto a.korisnickoIme
        List<Artikal> artikli = em.createQuery("SELECT a FROM Artikal a WHERE a.prodavac = :prodavacObj", Artikal.class)
                                  .setParameter("prodavacObj", prodavacKorisnik)
                                  .getResultList();

        if (artikli.isEmpty()) return "Korisnik '" + korisnickoIme + "' ne prodaje nijedan artikal.";
        
        StringBuilder sb = new StringBuilder("--- ARTIKLI PRODAVCA " + korisnickoIme + " ---\n");
        for (Artikal a : artikli) {
            sb.append("Naziv: ").append(a.getNaziv())
              .append(" | Cena: ").append(a.getCena())
              .append(" | Popust: ").append(a.getPopust()).append("%\n");
        }
        return sb.toString();
    }
}