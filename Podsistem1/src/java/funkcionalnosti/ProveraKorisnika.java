/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;

public class ProveraKorisnika {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String korIme = delovi[1];
        String sifra = delovi[2];
        
        try {
            // JPQL upit koji broji da li postoji taj korisnik sa tom sifrom
            Long count = em.createQuery(
                "SELECT COUNT(k) FROM Korisnik k WHERE k.korisnickoIme = :username AND k.sifra = :password", Long.class)
                .setParameter("username", korIme)
                .setParameter("password", sifra)
                .getSingleResult();
            
            if (count > 0) {
                return "Korisnik uspesno pronadjen.";
            } else {
                return "GRESKA: Korisnik ne postoji ili su kredencijali pogresni.";
            }
        } catch (Exception e) {
            return "Sistemska greska pri proveri korisnika: " + e.getMessage();
        }
    }
}