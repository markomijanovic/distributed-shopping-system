package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import entiteti.Korisnik;
import entiteti.ListaZelja;
import entiteti.ListaZeljaStavka;
import java.util.Date;

public class DodajUListuZelja {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoImeStr = delovi[1];
        String nazivArtikla = delovi[2];

        try {
            em.getTransaction().begin();

            // 1. Nalazimo korisnika (jer nam treba Korisnik objekat za Listu Zelja)
            Korisnik korisnik;
            try {
                korisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korIme", Korisnik.class)
                             .setParameter("korIme", korisnickoImeStr)
                             .getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                em.getTransaction().rollback();
                return "GRESKA: Korisnik '" + korisnickoImeStr + "' ne postoji u bazi Podsistema 2!";
            }

            // 2. Nalazimo artikal
            Artikal artikal;
            try {
                artikal = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class)
                            .setParameter("naziv", nazivArtikla)
                            .getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                em.getTransaction().rollback();
                return "GRESKA: Artikal '" + nazivArtikla + "' ne postoji!";
            }

            // 3. Nalazimo listu zelja ili kreiramo novu
            ListaZelja listaZelja;
            try {
                // Ovde prosledjujemo Korisnik objekat!
                listaZelja = em.createQuery("SELECT l FROM ListaZelja l WHERE l.korisnickoIme = :kor", ListaZelja.class)
                               .setParameter("kor", korisnik)
                               .getSingleResult();
            } catch (javax.persistence.NoResultException e) {
                listaZelja = new ListaZelja();
                listaZelja.setKorisnickoIme(korisnik); // Prima Korisnik objekat
                listaZelja.setDatumKreiranja(new Date()); 
                em.persist(listaZelja);
            }

            // 4. Proveravamo da li artikal vec postoji u listi zelja
            try {
                // Prema mappedBy="listaZelja", atribut u ListaZeljaStavka se zove listaZelja
                // Pretpostavka za artikal je da se atribut zove idArtikla (ili slicno)
                ListaZeljaStavka postojecaStavka = em.createQuery(
                    "SELECT s FROM ListaZeljaStavka s WHERE s.listaZelja = :lista AND s.artikal = :art", ListaZeljaStavka.class)
                    .setParameter("lista", listaZelja) 
                    .setParameter("art", artikal)
                    .getSingleResult();
                
                em.getTransaction().commit();
                return "Artikal '" + nazivArtikla + "' je vec u listi zelja korisnika " + korisnickoImeStr + ".";
                
            } catch (javax.persistence.NoResultException e) {
                ListaZeljaStavka novaStavka = new ListaZeljaStavka();
                
                // 1. Kreiramo kompozitni kljuc (proveri redosled parametara u klasi ListaZeljaStavkaPK)
                // Obicno ide (idListe, idArtikla)
                entiteti.ListaZeljaStavkaPK pk = new entiteti.ListaZeljaStavkaPK(listaZelja.getIdListe(), artikal.getIdArtikla());
                
                // 2. Setujemo kljuc u stavku
                // Proveri kako se zove setter, obicno je setListaZeljaStavkaPK()
                novaStavka.setListaZeljaStavkaPK(pk);
                
                // 3. Ostavljamo i objekte i datum za svaki slucaj
                novaStavka.setListaZelja(listaZelja); 
                novaStavka.setArtikal(artikal);
                novaStavka.setVremeDodavanja(new Date()); 
                
                em.persist(novaStavka);
                em.getTransaction().commit();
                
                return "Uspesno dodat artikal '" + nazivArtikla + "' u listu zelja.";
            }

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri dodavanju u listu zelja: " + e.getMessage();
        }
    }
}