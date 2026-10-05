package funkcionalnosti;

import javax.persistence.EntityManager;
import entiteti.Artikal;
import entiteti.Korisnik;
import entiteti.ListaZelja;
import entiteti.ListaZeljaStavka;

public class UkloniIzListeZelja {

    public static String izvrsi(String[] delovi, EntityManager em) {
        String korisnickoImeStr = delovi[1];
        String nazivArtikla = delovi[2];

        try {
            em.getTransaction().begin();

            Korisnik korisnik = em.createQuery("SELECT k FROM Korisnik k WHERE k.korisnickoIme = :korIme", Korisnik.class)
                             .setParameter("korIme", korisnickoImeStr)
                             .getSingleResult();

            Artikal artikal = em.createQuery("SELECT a FROM Artikal a WHERE a.naziv = :naziv", Artikal.class)
                                .setParameter("naziv", nazivArtikla)
                                .getSingleResult();

            ListaZelja listaZelja = em.createQuery("SELECT l FROM ListaZelja l WHERE l.korisnickoIme = :kor", ListaZelja.class)
                                      .setParameter("kor", korisnik)
                                      .getSingleResult();

            // 1. Pravimo kompozitni kljuc (apsolutno isto kao kod dodavanja)
            entiteti.ListaZeljaStavkaPK pk = new entiteti.ListaZeljaStavkaPK(listaZelja.getIdListe(), artikal.getIdArtikla());

            // 2. Nalazimo stavku direktno preko njenog primarnog kljuca
            ListaZeljaStavka stavka = em.find(ListaZeljaStavka.class, pk);

            // Zastita: Proveravamo da li je stavka zaista pronadjena pre brisanja
            if (stavka == null) {
                em.getTransaction().rollback();
                return "GRESKA: Artikal '" + nazivArtikla + "' se ne nalazi u listi zelja!";
            }

            // Sklanjamo artikal iz liste zelja
            em.remove(stavka);
            em.getTransaction().commit();

            return "Uspesno uklonjen artikal '" + nazivArtikla + "' iz liste zelja korisnika " + korisnickoImeStr + ".";

        } catch (javax.persistence.NoResultException e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "GRESKA: Korisnik, lista zelja ili artikal ne postoje u bazi!";
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Greska pri brisanju iz liste zelja: " + e.getMessage();
        }
    }
}