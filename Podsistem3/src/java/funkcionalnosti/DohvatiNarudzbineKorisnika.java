/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korisnik;
import entiteti.Narudzbina;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiNarudzbineKorisnika {
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoIme = delovi[1];
        try {
            Korisnik kupac = em.find(Korisnik.class, korisnickoIme);
            if (kupac == null) return "Korisnik '" + korisnickoIme + "' nema zabelezenih narudzbina.";

            List<Narudzbina> narudzbine = em.createQuery("SELECT n FROM Narudzbina n WHERE n.kupac = :k", Narudzbina.class)
                                            .setParameter("k", kupac)
                                            .getResultList();

            if (narudzbine.isEmpty()) return "Korisnik '" + korisnickoIme + "' nema zabelezenih narudzbina.";

            StringBuilder sb = new StringBuilder("--- NARUDZBINE KORISNIKA " + korisnickoIme.toUpperCase() + " ---\n");
            for (Narudzbina n : narudzbine) {
                sb.append("ID: ").append(n.getIdNarudzbine())
                  .append(" | Vreme: ").append(n.getVremeKreiranja())
                  .append(" | Ukupno: ").append(n.getUkupnaCena())
                  .append(" | Adresa: ").append(n.getAdresaDostave()).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            return "Greska pri dohvatanju narudzbina korisnika: " + e.getMessage();
        }
    }
}