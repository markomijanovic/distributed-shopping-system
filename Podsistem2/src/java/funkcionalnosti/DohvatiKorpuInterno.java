/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korpa;
import entiteti.KorpaStavka;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiKorpuInterno {
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];
        try {
            Korpa korpa = em.createQuery("SELECT k FROM Korpa k WHERE k.korisnickoIme = :korIme", Korpa.class)
                            .setParameter("korIme", korisnickoIme)
                            .getSingleResult();

            List<KorpaStavka> stavke = em.createQuery("SELECT s FROM KorpaStavka s WHERE s.korisnickoIme = :korpa", KorpaStavka.class)
                                         .setParameter("korpa", korpa)
                                         .getResultList();

            if (stavke.isEmpty()) return "PRAZNA";

            StringBuilder sb = new StringBuilder();
            for (KorpaStavka s : stavke) {
                // Paznja: Prilagodi gettere svom entitetu ako se zovu drugacije
                sb.append(s.getIdArtikla().getIdArtikla()).append(",")
                  .append(s.getKolicina()).append(",")
                  .append(s.getIdArtikla().getCena()).append(";");
            }
            sb.append("|").append(korpa.getUkupnaCena());
            return sb.toString();

        } catch (Exception e) {
            return "GRESKA";
        }
    }
}
