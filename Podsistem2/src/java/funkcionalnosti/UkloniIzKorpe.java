package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import entiteti.Korpa;
import entiteti.KorpaStavka;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class UkloniIzKorpe {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoImeStr = delovi[1];
        String nazivArtikla = delovi[2];
        int kolicinaZaBrisanje = Integer.parseInt(delovi[3]);

        try {
            em.getTransaction().begin();

            Artikal artikal = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class)
                                .setParameter("naziv", nazivArtikla)
                                .getSingleResult();

            Korpa korpa = em.createQuery("SELECT k FROM Korpa k WHERE k.korisnickoIme = :korIme", Korpa.class)
                            .setParameter("korIme", korisnickoImeStr)
                            .getSingleResult();

            // Poredimo s.korisnickoIme sa objektom korpa
            KorpaStavka stavka = em.createQuery("SELECT s FROM KorpaStavka s WHERE s.korisnickoIme = :korpa AND s.idArtikla = :art", KorpaStavka.class)
                                   .setParameter("korpa", korpa)
                                   .setParameter("art", artikal)
                                   .getSingleResult();

            // Zastita: Ne mozemo obrisati vise komada nego sto korisnik ima
            if (kolicinaZaBrisanje > stavka.getKolicina()) {
                kolicinaZaBrisanje = stavka.getKolicina();
            }

            // --- RACUNANJE CENE SA POPUSTOM ZA ODUZIMANJE ---
            BigDecimal popust = artikal.getPopust() != null ? artikal.getPopust() : BigDecimal.ZERO;
            BigDecimal iznosPopusta = artikal.getCena().multiply(popust).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
            BigDecimal cenaSaPopustom = artikal.getCena().subtract(iznosPopusta);
            BigDecimal ukupnoZaOduzimanje = cenaSaPopustom.multiply(new BigDecimal(kolicinaZaBrisanje));
            // ------------------------------------------------

            // Umanjujemo ukupnu cenu korpe
            BigDecimal novaCenaKorpe = korpa.getUkupnaCena().subtract(ukupnoZaOduzimanje);
            if (novaCenaKorpe.compareTo(BigDecimal.ZERO) < 0) novaCenaKorpe = BigDecimal.ZERO;
            korpa.setUkupnaCena(novaCenaKorpe);

            int preostalaKolicina = stavka.getKolicina() - kolicinaZaBrisanje;
            
            if (preostalaKolicina <= 0) {
                em.remove(stavka); 
            } else {
                stavka.setKolicina(preostalaKolicina);
                em.persist(stavka);
            }

            em.persist(korpa);
            em.getTransaction().commit();

            return "Uspesno uklonjeno. Nova cena korpe: " + korpa.getUkupnaCena();

        } catch (javax.persistence.NoResultException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA: Korpa ili artikal ne postoje, ili artikal nije u korpi!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri brisanju iz korpe: " + e.getMessage();
        }
    }
}