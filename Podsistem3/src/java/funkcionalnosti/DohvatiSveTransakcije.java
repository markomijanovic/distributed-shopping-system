/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Transakcija;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiSveTransakcije {
    public static String izvrsi(String[] delovi, EntityManager em) {
        try {
            List<Transakcija> transakcije = em.createQuery("SELECT t FROM Transakcija t", Transakcija.class).getResultList();
            if (transakcije.isEmpty()) return "Trenutno nema nijedne transakcije u sistemu.";

            StringBuilder sb = new StringBuilder("--- SVE TRANSAKCIJE U SISTEMU ---\n");
            for (Transakcija t : transakcije) {
                // Paznja: Prilagodi getter za Narudzbinu ako ga je NetBeans drugacije nazvao!
                int idNar = t.getIdNarudzbine() != null ? t.getIdNarudzbine().getIdNarudzbine() : 0;
                sb.append("ID Transakcije: ").append(t.getIdTransakcije())
                  .append(" | ID Narudzbine: ").append(idNar)
                  .append(" | Vreme: ").append(t.getVremePlacanja())
                  .append(" | Suma: ").append(t.getSuma()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Greska pri dohvatanju transakcija: " + e.getMessage();
        }
    }
}