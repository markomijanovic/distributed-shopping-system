package funkcionalnosti;

import entiteti.Grad;
import entiteti.Korisnik;
import entiteti.Uloga;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import java.util.ArrayList; // DODATO OVO

public class KreirajKorisnika {

    public static String izvrsi(String[] delovi, EntityManager em) {
        // Parsiranje 9 parametara
        String kIme = delovi[1];
        String sifra = delovi[2];
        String ime = delovi[3];
        String prezime = delovi[4];
        String adresa = delovi[5];
        int idGrada;
        double pocetniNovac;
        int idUloge;
        String koSalje = delovi[9];

        try {
            idGrada = Integer.parseInt(delovi[6]);
            pocetniNovac = Double.parseDouble(delovi[7]);
            idUloge = Integer.parseInt(delovi[8]);
        } catch (NumberFormatException e) {
            return "Greska: ID grada, ID uloge i novac moraju biti brojevi.";
        }

        try {
            em.getTransaction().begin();

            // 1. BEZBEDNOSNA PROVERA
            Korisnik adminKorisnik = em.find(Korisnik.class, koSalje);
            if (adminKorisnik == null) {
                em.getTransaction().rollback();
                return "Odbijeno: Korisnik koji salje zahtev (" + koSalje + ") ne postoji.";
            }

            boolean isAdmin = false;
            // Zastita od null listi kod vec postojecih korisnika
            if (adminKorisnik.getUlogaList() != null) {
                for (Uloga u : adminKorisnik.getUlogaList()) { 
                    if (u.getIdUloge() == 1 || u.getNaziv().toLowerCase().contains("admin")) {
                        isAdmin = true;
                        break;
                    }
                }
            }

            if (!isAdmin) {
                em.getTransaction().rollback();
                return "Odbijeno: Nemate administratorske privilegije za kreiranje korisnika.";
            }

            // 2. PROVERA DA LI KORISNIK VEC POSTOJI
            Korisnik postojeci = em.find(Korisnik.class, kIme);
            if (postojeci != null) {
                em.getTransaction().rollback();
                return "Greska: Korisnik sa imenom '" + kIme + "' vec postoji.";
            }

            // 3. PROVERA GRADA
            Grad grad = em.find(Grad.class, idGrada);
            if (grad == null) {
                em.getTransaction().rollback();
                return "Greska: Grad sa ID " + idGrada + " ne postoji u bazi.";
            }

            // 4. PROVERA ULOGE
            Uloga novaUloga = em.find(Uloga.class, idUloge);
            if (novaUloga == null) {
                em.getTransaction().rollback();
                return "Greska: Uloga sa ID " + idUloge + " ne postoji u bazi.";
            }

            // 5. KREIRANJE KORISNIKA
            Korisnik noviKorisnik = new Korisnik();
            noviKorisnik.setKorisnickoIme(kIme);
            noviKorisnik.setSifra(sifra);
            noviKorisnik.setIme(ime);
            noviKorisnik.setPrezime(prezime);
            noviKorisnik.setAdresa(adresa);
            noviKorisnik.setIdGrada(grad);
            noviKorisnik.setStanjeNovca(BigDecimal.valueOf(pocetniNovac));

            // 6. POVEZIVANJE ULOGE - OVDE JE BILA GRESKA!
            // Ako je lista null, moramo mi da je inicijalizujemo pre dodavanja
            if (noviKorisnik.getUlogaList() == null) {
                noviKorisnik.setUlogaList(new ArrayList<>());
            }
            noviKorisnik.getUlogaList().add(novaUloga);

            em.persist(noviKorisnik);
            em.getTransaction().commit();

            return "Uspesno kreiran korisnik '" + kIme + "' sa ulogom '" + novaUloga.getNaziv() + "' i stanjem " + pocetniNovac;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            // Dodali smo e.toString() da vidimo tačan tip greške ako opet pukne
            return "Sistemska greska pri kreiranju korisnika: " + e.toString();
        }
    }
}