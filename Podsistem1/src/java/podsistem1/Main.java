/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package podsistem1;

import javax.annotation.Resource;
import javax.jms.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    // Konekcija na server
    @Resource(lookup = "jms/IS1Factory")
    private static ConnectionFactory connectionFactory;
    
    // Red sa kojeg Podsistem 1 čita svoje zahteve
    @Resource(lookup = "jms/Queue1")
    private static Queue queue1;

    public static void main(String[] args) {
        System.out.println("Podsistem 1 se pokreće...");
        
        // 1. Kreiranje EntityManagerFactory (pravi se samo jednom na početku!)
        // Obavezno proveri da li se tvoj Persistence Unit u persistence.xml zove tačno ovako
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Podsistem1PU");
        
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            // 2. Pravimo slušaoca za Queue1
            MessageConsumer consumer = session.createConsumer(queue1);
            connection.start();
            System.out.println("Podsistem 1 uspesno povezan na JMS ");

            // 3. Beskonačna petlja - srce Enterprise klijenta
            while (true) {
                System.out.println("Podsistem 1 ceka zahteve...");
                Message msg = consumer.receive();
                if (msg instanceof TextMessage) {
                    obradiZahtev((TextMessage) msg, session, emf);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (emf != null) emf.close();
        }
    }

private static void obradiZahtev(TextMessage msg, Session session, EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager(); // Otvaramo EM jednom za ceo zahtev
    String odgovor = "Nepoznata komanda"; // Default odgovor
    
    try {
        String tekstPoruke = msg.getText();
        System.out.println("Stigao zahtev: " + tekstPoruke);
        
        String[] delovi = tekstPoruke.split(";");
        String komanda = delovi[0];
        
        switch(komanda) {
            case "1": // FUNKCIONALNOST 1: Provera postojanja korisnika
                odgovor = funkcionalnosti.ProveraKorisnika.izvrsi(delovi, em);
                break;
            case "2": // FUNKCIONALNOST 2: Kreiranje grada
                odgovor = funkcionalnosti.KreirajGrad.izvrsi(delovi, em);
                break;
            case "3": // FUNKCIONALNOST 3: Kreiranje korisnika
                odgovor = funkcionalnosti.KreirajKorisnika.izvrsi(delovi, em);
                break;
            case "4": // FUNKCIONALNOST 4: Dodavanje novca
                odgovor = funkcionalnosti.DodavanjeNovca.izvrsi(delovi, em);
                break;
            case "5": // FUNKCIONALNOST 5: Promena adrese i grada
                odgovor = funkcionalnosti.PromenaPodataka.izvrsi(delovi, em);
                break;
            case "15": // FUNKCIONALNOST 15: Dohvatanje svih gradova
                odgovor = funkcionalnosti.DohvatiSveGradove.izvrsi(delovi,em);
                break;
            case "16": // FUNKCIONALNOST 16: Dohvatanje svih korisnika
                odgovor = funkcionalnosti.DohvatiSveKorisnike.izvrsi(delovi,em);
                break;
            case "DEDUCT_MONEY": 
                    odgovor = funkcionalnosti.UmanjiNovac.izvrsi(delovi, em);
                    break;
        }
        
        // Na kraju, bez obzira koji je case bio, vracamo odgovor Centralnom serveru
        posaljiOdgovor(session, msg, odgovor);
        
    } catch (Exception e) {
        e.printStackTrace();
        posaljiOdgovor(session, msg, "Sistemska greska u Podsistemu 1: " + e.getMessage());
    } finally {
        em.close(); // Uvek zatvaramo konekciju ka bazi na kraju
    }
}

    // Pomoćna metoda koju ćemo zvati iz svakog "case-a" da vratimo odgovor
    private static void posaljiOdgovor(Session session, TextMessage originalnaPoruka, String tekstOdgovora) {
    try {
        // Citamo povratnu adresu koju je Centralni server zakacio
        Destination replyTo = originalnaPoruka.getJMSReplyTo(); 
        
        if (replyTo != null) {
            MessageProducer producer = session.createProducer(replyTo);
            TextMessage odgovorMsg = session.createTextMessage(tekstOdgovora);
            // Vracamo i ID
            odgovorMsg.setJMSCorrelationID(originalnaPoruka.getJMSCorrelationID());
            
            producer.send(odgovorMsg);
            System.out.println("Poslat odgovor na replyTo destinaciju: " + tekstOdgovora);
        } else {
            System.err.println("Greska: Originalna poruka nema JMSReplyTo adresu!");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}