/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import entiteti.Kategorija;
import java.math.BigDecimal;
import java.util.List;

public class BuildCena {
    
    public static String izvrsi(String[] delovi, EntityManager em) {
        String kategorija = delovi[1];
        String procenatS = delovi[2];

        try {
            em.getTransaction().begin();

           double procenat=Double.parseDouble(procenatS);
           
           List<Kategorija> katList=em.createQuery("SELECT k FROM Kategorija k WHERE k.naziv=:naziv",Kategorija.class)
                   .setParameter("naziv", kategorija).getResultList();
           if(katList.isEmpty()){
               em.getTransaction().rollback();
               return "Ne postoji kategorija : "+kategorija;
           }
           
           Kategorija pronadjenaKat=katList.get(0);
           
           List<Artikal> artikli=em.createQuery("SELECT a FROM Artikal a WHERE a.idKategorije= :idkat",Artikal.class)
                   .setParameter("idkat", pronadjenaKat).getResultList();
           int brojA=0;
           for(Artikal a:artikli){
                BigDecimal staraCena=a.getCena();
                BigDecimal uvecanje=BigDecimal.valueOf(procenat/100);
                uvecanje=uvecanje.multiply(staraCena);
                
                a.setCena(uvecanje);
                brojA++;
           }
           em.getTransaction().commit();
           return "Uspjesno azuriranih "+brojA;
        }catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri promenama cene: " + e.getMessage();
        }
    }
}
