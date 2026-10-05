/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem2;

import javax.annotation.Resource;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    @Resource(lookup = "jms/IS1Factory")
    private static ConnectionFactory cf;

    @Resource(lookup = "jms/Queue2")
    private static Queue q2;

    public static void main(String[] args) {
        System.out.println("Podsistem 2 se pokrece...");
        try {
            // Obavezno proveri da li ti se Persistence Unit zove "Podsistem2PU" u persistence.xml
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem2PU");

            Connection conn = cf.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            // Slusamo Queue2
            MessageConsumer consumer = session.createConsumer(q2);
            conn.start();

            System.out.println("Podsistem 2 uspesno povezan na JMS i ceka zahteve na Queue2...");

            while (true) {
                System.out.println("Podsistem 2 ceka zahteve...");
                Message msg = consumer.receive();
                if (msg instanceof TextMessage) {
                    obradiZahtev((TextMessage) msg, session, emf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void obradiZahtev(TextMessage msg, Session session, EntityManagerFactory emf) {
        EntityManager em = emf.createEntityManager();
        String odgovor = "Nepoznata komanda u Podsistemu 2";

        try {
            String tekstPoruke = msg.getText();
            System.out.println("Stigao zahtev (PS2): " + tekstPoruke);

            String[] delovi = tekstPoruke.split(";");
            String komanda = delovi[0];

            switch(komanda) {
                case "6": // Kreiranje kategorije
                    odgovor = funkcionalnosti.KreirajKategoriju.izvrsi(delovi, em);
                    break;
                case "7": // Kreiranje artikla
                    odgovor = funkcionalnosti.KreirajArtikal.izvrsi(delovi, em);
                    break;
                case "8": // Promena cene
                    odgovor = funkcionalnosti.PromeniCenu.izvrsi(delovi, em);
                    break;
                case "9": // Promena popusta
                    odgovor = funkcionalnosti.PromeniPopust.izvrsi(delovi, em);
                    break;
                case "10": // Dodavanje u korpu
                    odgovor = funkcionalnosti.DodajUKorpu.izvrsi(delovi, em);
                    break;
                case "11": // Brisanje iz korpe
                    odgovor = funkcionalnosti.UkloniIzKorpe.izvrsi(delovi, em);
                    break;
                case "12": // Dodavanje u listu zelja
                    odgovor = funkcionalnosti.DodajUListuZelja.izvrsi(delovi, em);
                    break;
                case "13": // Brisanje iz liste zelja
                    odgovor = funkcionalnosti.UkloniIzListeZelja.izvrsi(delovi, em);
                    break;
                case "17": // Dohvatanje kategorija
                    odgovor = funkcionalnosti.DohvatiKategorije.izvrsi(em);
                    break;
                case "18": // Dohvatanje artikala prodavca
                    // Proveravamo da li imamo dovoljno argumenata
                    if (delovi.length > 1) {
                        odgovor = funkcionalnosti.DohvatiArtikle.izvrsi(delovi, em);
                    } else {
                        odgovor = "GRESKA: Nije prosledjeno korisnicko ime prodavca!";
                    }
                    break;
                case "19": // Dohvatanje sadrzaja korpe
                    if (delovi.length > 1) {
                        odgovor = funkcionalnosti.DohvatiKorpu.izvrsi(delovi, em);
                    } else {
                        odgovor = "GRESKA: Nije prosledjeno korisnicko ime za dohvatanje korpe!";
                    }
                    break;
                case "20": // Dohvatanje sadrzaja liste zelja
                    if (delovi.length > 1) {
                        odgovor = funkcionalnosti.DohvatiListuZelja.izvrsi(delovi, em);
                    } else {
                        odgovor = "GRESKA: Nije prosledjeno korisnicko ime za dohvatanje liste zelja!";
                    }
                    break;
                case "GET_CART": 
                    odgovor = funkcionalnosti.DohvatiKorpuInterno.izvrsi(delovi, em);
                    break;
                case "EMPTY_CART": 
                    odgovor = funkcionalnosti.IsprazniKorpu.izvrsi(delovi, em);
                    break;
                case "SYNC_USER": 
                    odgovor = funkcionalnosti.SinhronizujKorisnika.izvrsi(delovi, em);
                    break;
                case "24":
                    odgovor = funkcionalnosti.BuildCena.izvrsi(delovi, em);
                case "25":
                    odgovor = funkcionalnosti.SigurnoBrisanjeArtikla.izvrsi(delovi, em);
            }

            posaljiOdgovor(session, msg, odgovor);

        } catch (Exception e) {
            e.printStackTrace();
            posaljiOdgovor(session, msg, "Sistemska greska u Podsistemu 2: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private static void posaljiOdgovor(Session session, TextMessage originalnaPoruka, String tekstOdgovora) {
        try {
            Destination replyTo = originalnaPoruka.getJMSReplyTo();
            if (replyTo != null) {
                MessageProducer producer = session.createProducer(replyTo);
                TextMessage odgovorMsg = session.createTextMessage(tekstOdgovora);
                odgovorMsg.setJMSCorrelationID(originalnaPoruka.getJMSCorrelationID());
                producer.send(odgovorMsg);
                System.out.println("Poslat odgovor (PS2): " + tekstOdgovora);
            } else {
                System.err.println("Greska: Originalna poruka nema JMSReplyTo adresu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}