/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import java.math.BigDecimal;

public class SigurnoBrisanjeArtikla {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String idA = delovi[1];
        String koSalje = delovi[2];

        try {
            em.getTransaction().begin();
            
            

            // Pronalazimo artikal po nazivu
            Artikal artikal = em.createQuery("SELECT a FROM Artikal a WHERE a.idArtikla = :id", Artikal.class)
                                .setParameter("id", idA)
                                .getSingleResult();
            if(artikal!=null){
                em.remove(artikal);
            }
            int executeUpdate = em.createQuery("DELETE FROM ListaZeljaStavka lzk WHERE lzk.artikal=:artikal").
                    setParameter("artikal", artikal).executeUpdate();
            
            int executeUpdate1 = em.createQuery("DELETE FROM KorpaStavka k where k.idArtikla=:art")
                    .setParameter("art", idA).executeUpdate();
            
            
            return "Uspjesno obrisano";
        } catch (javax.persistence.NoResultException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA: Artikal sa id '" + idA + "' ne postoji!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri promeni cene: " + e.getMessage();
        }
        
    }
}
