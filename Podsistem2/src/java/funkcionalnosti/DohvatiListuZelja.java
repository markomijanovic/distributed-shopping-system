/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korisnik;
import entiteti.ListaZelja;
import entiteti.ListaZeljaStavka;
import javax.persistence.EntityManager;
import java.util.List;

public class DohvatiListuZelja {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoImeStr = delovi[1];

        try {
            // 1. Nalazimo korisnika
            Korisnik korisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korIme", Korisnik.class)
                                  .setParameter("korIme", korisnickoImeStr)
                                  .getSingleResult();

            // 2. Nalazimo listu zelja za tog korisnika
            ListaZelja listaZelja = em.createQuery("SELECT l FROM ListaZelja l WHERE l.korisnickoIme = :kor", ListaZelja.class)
                                      .setParameter("kor", korisnik)
                                      .getSingleResult();

            // 3. Izvlačimo sve stavke koje pripadaju toj listi zelja
            List<ListaZeljaStavka> stavke = em.createQuery("SELECT s FROM ListaZeljaStavka s WHERE s.listaZelja = :lista", ListaZeljaStavka.class)
                                              .setParameter("lista", listaZelja)
                                              .getResultList();

            if (stavke.isEmpty()) {
                return "Lista zelja korisnika '" + korisnickoImeStr + "' je trenutno prazna.";
            }

            StringBuilder sb = new StringBuilder("--- LISTA ZELJA ZA: " + korisnickoImeStr.toUpperCase() + " ---\n");
            for (ListaZeljaStavka s : stavke) {
                // Koristimo .getArtikal() na osnovu prethodnih ispravki
                String nazivArtikla = s.getArtikal().getNaziv();
                java.math.BigDecimal cena = s.getArtikal().getCena();
                
                sb.append("- Artikal: ").append(nazivArtikla)
                  .append(" | Cena: ").append(cena)
                  .append(" | Vreme dodavanja: ").append(s.getVremeDodavanja())
                  .append("\n");
            }

            return sb.toString();

        } catch (javax.persistence.NoResultException e) {
            return "Korisnik '" + korisnickoImeStr + "' trenutno nema kreiranu listu zelja.";
        } catch (Exception e) {
            return "Greska pri dohvatanju liste zelja: " + e.getMessage();
        }
    }
}