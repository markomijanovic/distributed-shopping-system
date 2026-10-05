/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korisnik;
import entiteti.Uloga; // DODATO ZA PROVERU
import java.util.List;
import javax.persistence.EntityManager;

public class DohvatiSveKorisnike {
    
    // Promenjen potpis metode da prima i delove JMS poruke!
    public static String izvrsi(String[] delovi, EntityManager em) {
        
        // Ocekujemo da je poruka u formatu: "16;adminKorisnik"
        if (delovi.length < 2) {
            return "GRESKA: Nije prosleđen korisnik koji traži podatke.";
        }
        
        String koTrazi = delovi[1].trim();

        try {
            // 1. BEZBEDNOSNA PROVERA
            Korisnik admin = em.find(Korisnik.class, koTrazi);
            if (admin == null) {
                return "Zabranjen pristup: Korisnik '" + koTrazi + "' ne postoji.";
            }

            boolean isAdmin = false;
            // Provera uloga
            if (admin.getUlogaList() != null) {
                for (Uloga u : admin.getUlogaList()) {
                    if (u.getIdUloge() == 1 || u.getNaziv().toLowerCase().contains("admin")) {
                        isAdmin = true;
                        break;
                    }
                }
            }

            if (!isAdmin) {
                return "Zabranjen pristup: Samo administratori mogu videti sve korisnike!";
            }

            // 2. AKO JE ADMIN, DOHVATI SVE KORISNIKE (tvoj originalni kod sačuvan)
            List<Korisnik> korisnici = em.createQuery("SELECT k FROM Korisnik k", Korisnik.class).getResultList();
            StringBuilder sb = new StringBuilder();
            
            for (Korisnik k : korisnici) {
                sb.append("Korisnik: ").append(k.getKorisnickoIme())
                  .append(", Ime: ").append(k.getIme())
                  .append(", Prezime: ").append(k.getPrezime())
                  .append(", Adresa: ").append(k.getAdresa())
                  .append(", Grad: ").append(k.getIdGrada().getNaziv())
                  .append(", Novac: ").append(k.getStanjeNovca())
                  .append("\n");
            }
            
            return sb.length() > 0 ? sb.toString() : "Nema korisnika u bazi.";

        } catch (Exception e) {
            return "Sistemska greska pri dohvatanju korisnika: " + e.getMessage();
        }
    }
}