/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Narudzbina;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiSveNarudzbine {
    public static String izvrsi(String[] delovi, EntityManager em) {
        try {
            List<Narudzbina> narudzbine = em.createQuery("SELECT n FROM Narudzbina n", Narudzbina.class).getResultList();
            if (narudzbine.isEmpty()) return "Trenutno nema nijedne narudzbine u sistemu.";

            StringBuilder sb = new StringBuilder("--- SVE NARUDZBINE U SISTEMU ---\n");
            for (Narudzbina n : narudzbine) {
                String kupac = n.getKupac() != null ? n.getKupac().getKorisnickoIme() : "Nepoznato";
                sb.append("ID: ").append(n.getIdNarudzbine())
                  .append(" | Kupac: ").append(kupac)
                  .append(" | Vreme: ").append(n.getVremeKreiranja())
                  .append(" | Ukupno: ").append(n.getUkupnaCena()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Greska pri dohvatanju svih narudzbina: " + e.getMessage();
        }
    }
}
