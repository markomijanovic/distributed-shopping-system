/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Kategorija;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiKategorije {
    public static String izvrsi(EntityManager em) {
        List<Kategorija> kategorije = em.createQuery("SELECT k FROM Kategorija k", Kategorija.class).getResultList();
        if (kategorije.isEmpty()) return "Nema kategorija u bazi.";
        
        StringBuilder sb = new StringBuilder("--- SPISAK KATEGORIJA ---\n");
        for (Kategorija k : kategorije) {
            // Proveri da li se getter za nadkategoriju zove ovako u tvojoj klasi
            String nadkat = (k.getNadkategorijaId() != null) ? k.getNadkategorijaId().getNaziv() : "Nema";
            sb.append("Naziv: ").append(k.getNaziv())
              .append(" | Nadkategorija: ").append(nadkat)
              .append("\n");
        }
        return sb.toString();
    }
}