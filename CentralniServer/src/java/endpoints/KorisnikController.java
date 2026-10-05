/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints; // PAŽNJA: Proveri da li se tvoj paket zove tačno ovako!

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("korisnik")
@Stateless
public class KorisnikController {

    @Resource(lookup = "jms/IS1Factory")
    private ConnectionFactory cf;
    
    @Resource(lookup = "jms/Queue1")
    private Queue q1;
    
    @Resource(lookup = "jms/Queue2")
    private Queue q2;
    
    @Resource(lookup = "jms/Queue3")
    private Queue q3;
    
    @Resource(lookup = "jms/QueueResponse")
    private Queue qResponse;
    // =====================================================================
    // FUNKCIONALNOST 1: Provera korisnika
    // =====================================================================
    @GET
    @Path("provera/{korisnickoIme}/{sifra}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response proveriKorisnika(@PathParam("korisnickoIme") String korIme, @PathParam("sifra") String sifra) {
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q1);
            
            // Komanda 1;korisnickoIme;sifra
            TextMessage msg = sess.createTextMessage("1;" + korIme + ";" + sifra); 
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse); 
            
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(5000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
            
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }

    // =====================================================================
    // FUNKCIONALNOST 3: Kreiranje korisnika
    // =====================================================================
    // =====================================================================
    // FUNKCIONALNOST 3: Kreiranje korisnika (i sinhronizacija sa PS2, PS3)
    // =====================================================================
    @POST
    // Ubačen parametar {novac} između idGrada i idUloge
    @Path("kreiraj/{kIme}/{sifra}/{ime}/{prezime}/{adresa}/{idGrada}/{novac}/{idUloge}/{koSalje}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajKorisnika(
            @PathParam("kIme") String kIme,
            @PathParam("sifra") String sifra,
            @PathParam("ime") String ime,
            @PathParam("prezime") String prezime,
            @PathParam("adresa") String adresa,
            @PathParam("idGrada") int idGrada,
            @PathParam("novac") double novac, // DODATO: Početno stanje novca
            @PathParam("idUloge") int idUloge,
            @PathParam("koSalje") String koSalje) { 
        
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q1);
            
            // Format poruke proširen za novac: SifraZahteva(3) ; kIme ; sifra ; ime ; prezime ; adresa ; idGrada ; novac ; idUloge ; koSalje
            String tekstPoruke = "3;" + kIme + ";" + sifra + ";" + ime + ";" + prezime + ";" + adresa + ";" + idGrada + ";" + novac + ";" + idUloge + ";" + koSalje;
            TextMessage msg = sess.createTextMessage(tekstPoruke);
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse); 
            
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(5000);
            
            if (reply != null) {
                String odgovorPS1 = reply.getText();
                
                // AKO JE KORISNIK USPJEŠNO KREIRAN U PS1, SINHRONIZUJ GA SA PS2 i PS3
                // Sinhronizacija zahtijeva samo korisnicko ime (kIme), pa tu nema promjena!
                if (odgovorPS1.contains("Uspesno") || odgovorPS1.contains("ok") || odgovorPS1.contains("kreiran")) {
                    
                    try {
                        // Saljemo poruku Podsistemu 2
                        MessageProducer prodQ2 = sess.createProducer(q2); 
                        prodQ2.send(sess.createTextMessage("SYNC_USER;" + kIme));
                        
                        // Saljemo poruku Podsistemu 3
                        MessageProducer prodQ3 = sess.createProducer(q3); 
                        prodQ3.send(sess.createTextMessage("SYNC_USER;" + kIme));
                    } catch (Exception syncEx) {
                        System.err.println("Greška pri sinhronizaciji korisnika u PS2/PS3: " + syncEx.getMessage());
                    }
                }
                
                return Response.ok(odgovorPS1).build();
            } else {
                return Response.status(408).entity("Timeout: Podsistem 1 ne odgovara.").build();
            }
        } catch (Exception e) {
            return Response.serverError().entity("Greška: " + e.getMessage()).build();
        }
    }
    // =====================================================================
    // FUNKCIONALNOST 4: Dodavanje novca korisniku
    // =====================================================================
    @POST
    @Path("novac/{korisnickoIme}/{iznos}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dodajNovac(
            @PathParam("korisnickoIme") String korIme,
            @PathParam("iznos") String iznos,
            @PathParam("koTrazi") String koTrazi) {
        
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q1);
            
            // Format: 4;korisnikKomeDodajemo;iznos;adminKojiTrazi
            TextMessage msg = sess.createTextMessage("4;" + korIme + ";" + iznos + ";" + koTrazi); 
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse); 
            
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(5000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
            
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }
    // =====================================================================
    // FUNKCIONALNOST 5: Promena adrese i grada za korisnika
    // =====================================================================
    @POST
    @Path("promena/{korisnickoIme}/{novaAdresa}/{noviGrad}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniPodatke(
            @PathParam("korisnickoIme") String korIme,
            @PathParam("novaAdresa") String novaAdresa,
            @PathParam("noviGrad") String noviGrad,
            @PathParam("koTrazi") String koTrazi) {
        
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q1);
            
            // Format: 5;korisnickoIme;novaAdresa;noviGrad;koTrazi
            TextMessage msg = sess.createTextMessage("5;" + korIme + ";" + novaAdresa + ";" + noviGrad + ";" + koTrazi); 
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse); 
            
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(5000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
            
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }
    // =====================================================================
    // FUNKCIONALNOST 16:Dohvati sve korisnike
    // =====================================================================
    @GET
    @Path("{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSveKorisnike(@PathParam("koTrazi") String koTrazi) {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q1);
            
            TextMessage msg = sess.createTextMessage("16;"+koTrazi); // Komanda 16
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse);
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(5000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}