/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Path("narudzbina")
@Stateless
public class NarudzbinaController {

    @Resource(lookup = "jms/IS1Factory")
    private ConnectionFactory cf;
    
    @Resource(lookup = "jms/Queue3")
    private Queue q3;
    
    @Resource(lookup = "jms/QueueResponse")
    private Queue qResponse;

    // =====================================================================
    // FUNKCIONALNOST 14: Placanje (kreiranje narudzbine)
    // =====================================================================
    @POST
    @Path("placanje/{korisnickoIme}/{adresa}/{grad}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response placanje(
            @PathParam("korisnickoIme") String korisnickoIme,
            @PathParam("adresa") String adresa,
            @PathParam("grad") String grad) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q3);
            
            // Format: 14;korisnickoIme;adresa;grad
            TextMessage msg = sess.createTextMessage("14;" + korisnickoIme + ";" + adresa + ";" + grad);
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse);
            
            prod.send(msg);
            
            // Cekamo malo duze (10 sekundi) jer PS3 prica sa PS1 i PS2 u pozadini
            TextMessage reply = (TextMessage) cons.receive(10000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout: Placanje traje predugo").build();
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }
    // =====================================================================
    // FUNKCIONALNOST 21: Dohvatanje narudzbina korisnika
    // =====================================================================
    @GET
    @Path("korisnik/{korisnickoIme}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiNarudzbineKorisnika(@PathParam("korisnickoIme") String korisnickoIme) {
        return posaljiJmsZahtev("21;" + korisnickoIme);
    }

    // =====================================================================
    // FUNKCIONALNOST 22: Dohvatanje svih narudzbina
    // =====================================================================
    @GET
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSveNarudzbine() {
        return posaljiJmsZahtev("22");
    }

    // =====================================================================
    // FUNKCIONALNOST 23: Dohvatanje svih transakcija
    // =====================================================================
    @GET
    @Path("transakcije")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSveTransakcije() {
        return posaljiJmsZahtev("23");
    }

    // Pomoćna metoda da ne ponavljamo onaj dugačak try-catch JMS kod tri puta
    private Response posaljiJmsZahtev(String tekstPoruke) {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q3);
            
            TextMessage msg = sess.createTextMessage(tekstPoruke);
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse);
            
            prod.send(msg);
            TextMessage reply = (TextMessage) cons.receive(5000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }
}