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

@Path("korpa")
@Stateless
public class KorpaController {

    @Resource(lookup = "jms/IS1Factory")
    private ConnectionFactory cf;
    
    @Resource(lookup = "jms/Queue2") 
    private Queue q2;
    
    @Resource(lookup = "jms/QueueResponse")
    private Queue qResponse;

    // =====================================================================
    // FUNKCIONALNOST 10: Dodavanje artikla u korpu
    // =====================================================================
    @POST
    @Path("dodaj/{korisnickoIme}/{nazivArtikla}/{kolicina}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dodajUKorpu(
            @PathParam("korisnickoIme") String korisnickoIme,
            @PathParam("nazivArtikla") String nazivArtikla,
            @PathParam("kolicina") String kolicina) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 10;korisnickoIme;nazivArtikla;kolicina
            TextMessage msg = sess.createTextMessage("10;" + korisnickoIme + ";" + nazivArtikla + ";" + kolicina);
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
    // FUNKCIONALNOST 11: Brisanje artikla iz korpe (sa kolicinom)
    // =====================================================================
    @DELETE
    @Path("ukloni/{korisnickoIme}/{nazivArtikla}/{kolicina}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response ukloniIzKorpe(
            @PathParam("korisnickoIme") String korisnickoIme,
            @PathParam("nazivArtikla") String nazivArtikla,
            @PathParam("kolicina") String kolicina) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 11;korisnickoIme;nazivArtikla;kolicina
            TextMessage msg = sess.createTextMessage("11;" + korisnickoIme + ";" + nazivArtikla + ";" + kolicina);
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
    // FUNKCIONALNOST 19: Dohvatanje sadrzaja korpe
    // =====================================================================
    @GET
    @Path("{korisnickoIme}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSadrzajKorpe(@PathParam("korisnickoIme") String korisnickoIme) {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 19;korisnickoIme
            TextMessage msg = sess.createTextMessage("19;" + korisnickoIme);
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
