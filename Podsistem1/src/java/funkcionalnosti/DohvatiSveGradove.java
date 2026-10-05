/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti; // Proveri da li se paket ovako zove kod tebe

import entiteti.Grad;
import entiteti.Korisnik;
import entiteti.Uloga;
import java.util.List;
import javax.persistence.EntityManager;

public class DohvatiSveGradove {
    
    // Promenjen potpis metode da prima i delove JMS poruke!
    public static String izvrsi(String[] delovi, EntityManager em) {
        
        // Ocekujemo da je poruka u formatu: "15;adminKorisnik"
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
                return "Zabranjen pristup: Samo administratori mogu videti listu gradova!";
            }

            // 2. AKO JE ADMIN, DOHVATI SVE GRADOVE (tvoj originalni kod)
            List<Grad> gradovi = em.createQuery("SELECT g FROM Grad g", Grad.class).getResultList();
            StringBuilder sb = new StringBuilder();
            
            for (Grad g : gradovi) {
                sb.append("ID: ").append(g.getIdGrada())
                  .append(", Naziv: ").append(g.getNaziv())
                  .append("\n");
            }
            
            return sb.length() > 0 ? sb.toString() : "Nema gradova u bazi.";

        } catch (Exception e) {
            return "Sistemska greska pri dohvatanju gradova: " + e.getMessage();
        }
    }
}