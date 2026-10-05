/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;

public class KreirajGrad {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String nazivGrada = delovi[1];
        String koTrazi = delovi[2];
        
        // 1. PROVERA PRAVA 
        boolean isAdmin = false;
        try {
            Long count = em.createQuery(
                "SELECT COUNT(k) FROM Korisnik k JOIN k.ulogaList u WHERE k.korisnickoIme = :username AND u.naziv = 'administrator'", Long.class)
                .setParameter("username", koTrazi)
                .getSingleResult();
            isAdmin = (count > 0);
        } catch (Exception e) {
            System.out.println("Greska pri proveri admina: " + e.getMessage());
        }

        if (!isAdmin) {
            return "GRESKA: Korisnik '" + koTrazi + "' nema administratorske privilegije!";
        }
        // --- ODBRANA OD DUPLIKATA GRADA ---
        try {
            Long brojGradova = em.createQuery("SELECT COUNT(g) FROM Grad g WHERE g.naziv = :naziv", Long.class)
                                 .setParameter("naziv", nazivGrada)
                                 .getSingleResult();
            if (brojGradova > 0) {
                return "GRESKA: Grad sa nazivom '" + nazivGrada + "' vec postoji u bazi!";
            }
        } catch (Exception e) {
            return "Greska pri proveri grada: " + e.getMessage();
        }
        // ----------------------------------
        // 2. UPIS U BAZU
        try {
            em.getTransaction().begin();
            entiteti.Grad noviGrad = new entiteti.Grad();
            noviGrad.setNaziv(nazivGrada);
            
            em.persist(noviGrad);
            em.getTransaction().commit();
            
            return "Uspesno kreiran grad: " + nazivGrada;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri upisu grada: " + e.getMessage();
        }
    }
}
