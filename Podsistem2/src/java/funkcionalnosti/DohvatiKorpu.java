/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korpa;
import entiteti.KorpaStavka;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiKorpu {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];

        try {
            // 1. Nalazimo korpu korisnika
            Korpa korpa = em.createQuery("SELECT k FROM Korpa k WHERE k.korisnickoIme = :korIme", Korpa.class)
                            .setParameter("korIme", korisnickoIme)
                            .getSingleResult();

            // 2. Nalazimo sve stavke u toj korpi
            // Podsetnik: U tvojoj klasi KorpaStavka, atribut koji drzi korpu se zove "korisnickoIme"
            List<KorpaStavka> stavke = em.createQuery("SELECT s FROM KorpaStavka s WHERE s.korisnickoIme = :korpa", KorpaStavka.class)
                                         .setParameter("korpa", korpa)
                                         .getResultList();

            if (stavke.isEmpty()) {
                return "Korpa korisnika '" + korisnickoIme + "' je trenutno prazna. Ukupna cena: " + korpa.getUkupnaCena();
            }

            StringBuilder sb = new StringBuilder("--- SADRZAJ KORPE ZA: " + korisnickoIme.toUpperCase() + " ---\n");
            for (KorpaStavka s : stavke) {
                // Pretpostavka da su ovo tvoji getteri iz entiteta (s.getIdArtikla() vraca objekat Artikal)
                String nazivArtikla = s.getIdArtikla().getNaziv();
                int kolicina = s.getKolicina();
                java.math.BigDecimal cena = s.getIdArtikla().getCena();
                
                sb.append("- Artikal: ").append(nazivArtikla)
                  .append(" | Kolicina: ").append(kolicina)
                  .append(" | Osnovna cena: ").append(cena).append("\n");
            }
            sb.append("---------------------------------------------------\n");
            sb.append("UKUPNA CENA (SA URACUNATIM POPUSTIMA): ").append(korpa.getUkupnaCena());

            return sb.toString();

        } catch (javax.persistence.NoResultException e) {
            return "Korisnik '" + korisnickoIme + "' nema otvorenu korpu.";
        } catch (Exception e) {
            return "Greska pri dohvatanju korpe: " + e.getMessage();
        }
    }
}