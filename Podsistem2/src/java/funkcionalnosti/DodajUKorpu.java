package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import entiteti.Korpa;
import entiteti.KorpaStavka;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DodajUKorpu {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoImeStr = delovi[1];
        String nazivArtikla = delovi[2];
        int kolicina = Integer.parseInt(delovi[3]);

        try {
            em.getTransaction().begin();

            // 1. Nalazimo artikal
            Artikal artikal;
            try {
                artikal = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class)
                            .setParameter("naziv", nazivArtikla)
                            .getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                em.getTransaction().rollback();
                return "GRESKA: Artikal '" + nazivArtikla + "' ne postoji!";
            }

            // 2. Nalazimo korpu ili kreiramo novu
            Korpa korpa;
            try {
                // Pretpostavljam da je u klasi Korpa polje nazvano "korisnickoIme" i da je String
                korpa = em.createQuery("SELECT k FROM Korpa k WHERE k.korisnickoIme = :korIme", Korpa.class)
                          .setParameter("korIme", korisnickoImeStr)
                          .getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                korpa = new Korpa();
                korpa.setKorisnickoIme(korisnickoImeStr); 
                korpa.setUkupnaCena(BigDecimal.ZERO);
                em.persist(korpa);
            }

            // --- RACUNANJE CENE SA POPUSTOM ---
            BigDecimal popust = artikal.getPopust() != null ? artikal.getPopust() : BigDecimal.ZERO;
            BigDecimal iznosPopusta = artikal.getCena().multiply(popust).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal cenaSaPopustom = artikal.getCena().subtract(iznosPopusta);
            BigDecimal ukupnoZaKolicinu = cenaSaPopustom.multiply(new BigDecimal(kolicina));
            // ----------------------------------

            // 3. Dodajemo na ukupnu cenu korpe
            korpa.setUkupnaCena(korpa.getUkupnaCena().add(ukupnoZaKolicinu));

            // 4. Dodajemo ili azuriramo KorpaStavka
            KorpaStavka stavka;
            try {
                // Paznja: Ovde uporedjujemo s.korisnickoIme sa celim objektom KORPA
                stavka = em.createQuery("SELECT s FROM KorpaStavka s WHERE s.korisnickoIme = :korpa AND s.idArtikla = :art", KorpaStavka.class)
                           .setParameter("korpa", korpa)
                           .setParameter("art", artikal)
                           .getSingleResult();
                           
                stavka.setKolicina(stavka.getKolicina() + kolicina);
            } catch (javax.persistence.NoResultException e) {
                stavka = new KorpaStavka();
                // Opet NetBeans trik: setter se zove setKorisnickoIme, ali prima objekat Korpa
                stavka.setKorisnickoIme(korpa); 
                stavka.setIdArtikla(artikal);
                stavka.setKolicina(kolicina);
                em.persist(stavka);
            }

            em.persist(korpa);
            em.getTransaction().commit();
            return "Uspesno dodato. Artikal: " + nazivArtikla + ", Kol: " + kolicina + ". Nova ukupna cena korpe: " + korpa.getUkupnaCena();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri dodavanju u korpu: " + e.getMessage();
        }
    }
}