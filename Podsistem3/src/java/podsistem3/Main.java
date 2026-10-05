package podsistem3;

import javax.annotation.Resource;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {

    @Resource(lookup = "jms/IS1Factory")
    private static ConnectionFactory cf;

    @Resource(lookup = "jms/Queue3")
    private static Queue q3;

    public static void main(String[] args) {
        System.out.println("Podsistem 3 se pokrece...");
        try {
            // KRITICNO: Mora da se poklapa sa imenom u persistence.xml za Podsistem 3!
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem3PU");

            Connection conn = cf.createConnection();
            Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            // Slusamo Queue3
            MessageConsumer consumer = session.createConsumer(q3);
            conn.start();

            System.out.println("Podsistem 3 uspesno povezan na JMS i ceka zahteve na Queue3...");

            while (true) {
                System.out.println("Podsistem 3 ceka zahteve...");
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
        String odgovor = "Nepoznata komanda u Podsistemu 3";

        try {
            String tekstPoruke = msg.getText();
            System.out.println("Stigao zahtev (PS3): " + tekstPoruke);

            String[] delovi = tekstPoruke.split(";");
            String komanda = delovi[0];

            switch(komanda) {
                case "14": // Kreiranje narudzbine i placanje
                    // Prosledjujemo i session kako bismo komunicirali sa PS1 i PS2
                    odgovor = funkcionalnosti.KreirajNarudzbinu.izvrsi(delovi, em, session);
                    break;
                case "21": 
                    odgovor = funkcionalnosti.DohvatiNarudzbineKorisnika.izvrsi(delovi, em);
                    break;
                case "22": 
                    odgovor = funkcionalnosti.DohvatiSveNarudzbine.izvrsi(delovi, em);
                    break;
                case "23": 
                    odgovor = funkcionalnosti.DohvatiSveTransakcije.izvrsi(delovi, em);
                    break;
                case "SYNC_USER": 
                    odgovor = funkcionalnosti.SinhronizujKorisnika.izvrsi(delovi, em);
                    break;
            }

            posaljiOdgovor(session, msg, odgovor);

        } catch (Exception e) {
            e.printStackTrace();
            posaljiOdgovor(session, msg, "Sistemska greska u Podsistemu 3: " + e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
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
                System.out.println("Poslat odgovor (PS3): " + tekstOdgovora);
            } else {
                System.err.println("Greska (PS3): Originalna poruka nema JMSReplyTo adresu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}