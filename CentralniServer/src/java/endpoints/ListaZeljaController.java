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

@Path("lista_zelja")
@Stateless
public class ListaZeljaController {

    @Resource(lookup = "jms/IS1Factory")
    private ConnectionFactory cf;
    
    @Resource(lookup = "jms/Queue2") // Ide u Podsistem 2
    private Queue q2;
    
    @Resource(lookup = "jms/QueueResponse")
    private Queue qResponse;

    // =====================================================================
    // FUNKCIONALNOST 12: Dodavanje artikla u listu zelja
    // =====================================================================
    @POST
    @Path("dodaj/{korisnickoIme}/{nazivArtikla}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dodajUListuZelja(
            @PathParam("korisnickoIme") String korisnickoIme,
            @PathParam("nazivArtikla") String nazivArtikla) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 12;korisnickoIme;nazivArtikla
            TextMessage msg = sess.createTextMessage("12;" + korisnickoIme + ";" + nazivArtikla);
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
    // FUNKCIONALNOST 13: Brisanje artikla iz liste zelja
    // =====================================================================
    @DELETE
    @Path("ukloni/{korisnickoIme}/{nazivArtikla}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response ukloniIzListeZelja(
            @PathParam("korisnickoIme") String korisnickoIme,
            @PathParam("nazivArtikla") String nazivArtikla) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 13;korisnickoIme;nazivArtikla
            TextMessage msg = sess.createTextMessage("13;" + korisnickoIme + ";" + nazivArtikla);
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
    // FUNKCIONALNOST 20: Dohvatanje sadrzaja liste zelja
    // =====================================================================
    @GET
    @Path("{korisnickoIme}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiListuZelja(@PathParam("korisnickoIme") String korisnickoIme) {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 20;korisnickoIme
            TextMessage msg = sess.createTextMessage("20;" + korisnickoIme);
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