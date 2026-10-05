/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package funkcionalnosti;

import entiteti.Korisnik;
import entiteti.Narudzbina;
import entiteti.StavkaNarudzbine;
import entiteti.Transakcija;
import java.math.BigDecimal;
import java.util.Date;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.jms.*;

public class KreirajNarudzbinu {

    public static String izvrsi(String[] delovi, EntityManager em, Session session) {
        String korisnickoIme = delovi[1];
        String adresa = delovi[2];
        String grad = delovi[3];

        try {
            InitialContext ic = new InitialContext();
            Queue q1 = (Queue) ic.lookup("jms/Queue1");
            Queue q2 = (Queue) ic.lookup("jms/Queue2");

            // =========================================================
            // FAZA 1: Pitamo PS2 za sadrzaj korpe
            // =========================================================
            String korpaOdgovor = posaljiJmsZahtev(session, q2, "GET_CART;" + korisnickoIme);
            if (korpaOdgovor == null || korpaOdgovor.equals("GRESKA")) {
                return "GRESKA: Sistem ne moze da pristupi korpi (Podsistem 2 ne odgovara).";
            }
            if (korpaOdgovor.equals("PRAZNA")) {
                return "GRESKA: Vasa korpa je prazna!";
            }

            // Formatiranje: "idArtikla,kol,cena;idArtikla,kol,cena|ukupnaCena"
            String[] glavniDelovi = korpaOdgovor.split("\\|");
            String stavkeStr = glavniDelovi[0];
            BigDecimal ukupnaCena = new BigDecimal(glavniDelovi[1]);

            // =========================================================
            // FAZA 2: Trazimo od PS1 da skine novac korisniku
            // =========================================================
            String novacOdgovor = posaljiJmsZahtev(session, q1, "DEDUCT_MONEY;" + korisnickoIme + ";" + ukupnaCena);
            if (novacOdgovor == null) {
                return "GRESKA: Sistem za naplatu trenutno nije dostupan (Podsistem 1 ne odgovara).";
            }
            if (novacOdgovor.equals("NEMA_PARA")) {
                return "GRESKA: Nemate dovoljno sredstava na racunu za ovu kupovinu! (Potrebno: " + ukupnaCena + ")";
            }
            if (!novacOdgovor.equals("OK")) {
                return "GRESKA: Problem pri obradi transakcije.";
            }

            // =========================================================
            // FAZA 3: Upisujemo u bazu Podsistema 3
            // =========================================================
            em.getTransaction().begin();

            // 1. Obezbedjujemo postojanje korisnika u PS3
            Korisnik kupac = em.find(Korisnik.class, korisnickoIme);
            if (kupac == null) {
                kupac = new Korisnik();
                kupac.setKorisnickoIme(korisnickoIme);
                em.persist(kupac);
            }

            // 2. Kreiramo Narudzbinu
            Narudzbina narudzbina = new Narudzbina();
            narudzbina.setKupac(kupac);
            narudzbina.setAdresaDostave(adresa);
            
            // ISPRAVKA: Pretvaramo String sa klijenta u Integer (ID grada)
            int idGrada = Integer.parseInt(grad);
            narudzbina.setGradDostave(idGrada); 
            
            narudzbina.setUkupnaCena(ukupnaCena);
            narudzbina.setVremeKreiranja(new Date());
            em.persist(narudzbina);
            em.flush(); // Moramo uraditi flush da bi narudzbina dobila ID!

            // ... (ostatak koda za stavke i transakciju ostaje isti) ...

            // 3. Upisujemo stavke
            String[] pojedinacneStavke = stavkeStr.split(";");
            for (String stavka : pojedinacneStavke) {
                if (stavka.isEmpty()) continue;
                String[] detalji = stavka.split(",");
                int idArtikla = Integer.parseInt(detalji[0]);
                int kolicina = Integer.parseInt(detalji[1]);
                BigDecimal cenaArtikla = new BigDecimal(detalji[2]);

                StavkaNarudzbine sn = new StavkaNarudzbine();
                sn.setIdNarudzbine(narudzbina); // Zove se setIdNarudzbine, ali proveri u entitetu!
                sn.setIdArtikla(idArtikla);
                sn.setKolicina(kolicina);
                sn.setJedinicnaCena(cenaArtikla);
                em.persist(sn);
            }

            // 4. Kreiramo transakciju
            Transakcija transakcija = new Transakcija();
            transakcija.setIdNarudzbine(narudzbina); // Proveri i ovaj setter u entitetu
            transakcija.setSuma(ukupnaCena);
            transakcija.setVremePlacanja(new Date());
            em.persist(transakcija);

            em.getTransaction().commit();

            // =========================================================
            // FAZA 4: Javimo PS2 da isprazni korpu jer je uspesno placeno
            // =========================================================
            posaljiJmsZahtev(session, q2, "EMPTY_CART;" + korisnickoIme);

            return "Uspesno kreirana narudzbina! Ukupno placeno: " + ukupnaCena;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return "Kriticna greska pri placanju: " + e.getMessage();
        }
    }

    // Pomocna metoda koja glumi "mini-klijenta" i ceka odgovor od drugog podsistema
    private static String posaljiJmsZahtev(Session session, Queue targetQueue, String tekstPoruke) {
        try {
            TemporaryQueue tempQueue = session.createTemporaryQueue();
            MessageProducer producer = session.createProducer(targetQueue);
            
            TextMessage msg = session.createTextMessage(tekstPoruke);
            msg.setJMSReplyTo(tempQueue);
            
            producer.send(msg);
            
            MessageConsumer consumer = session.createConsumer(tempQueue);
            TextMessage reply = (TextMessage) consumer.receive(4000); // Cekamo max 4 sekunde
            
            consumer.close();
            producer.close();
            tempQueue.delete();
            
            return reply != null ? reply.getText() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}