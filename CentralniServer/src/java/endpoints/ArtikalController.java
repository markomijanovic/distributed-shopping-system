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

@Path("artikal")
@Stateless
public class ArtikalController {

    @Resource(lookup = "jms/IS1Factory")
    private ConnectionFactory cf;
    
    // PAŽNJA: Komuniciramo sa Podsistemom 2 preko Queue2
    @Resource(lookup = "jms/Queue2") 
    private Queue q2;
    
    @Resource(lookup = "jms/QueueResponse")
    private Queue qResponse;

    // =====================================================================
    // FUNKCIONALNOST 6: Kreiranje kategorije
    // =====================================================================
    @POST
    @Path("kategorija/{naziv}/{nadkategorija}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajKategoriju(
            @PathParam("naziv") String naziv,
            @PathParam("nadkategorija") String nadkategorija, 
            @PathParam("koTrazi") String koTrazi) {
        
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 6;naziv;nadkategorija;koTrazi
            TextMessage msg = sess.createTextMessage("6;" + naziv + ";" + nadkategorija + ";" + koTrazi);
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
    // FUNKCIONALNOST 7: Kreiranje artikla
    // =====================================================================
    @POST
    @Path("{naziv}/{opis}/{cena}/{popust}/{nazivKategorije}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response kreirajArtikal(
            @PathParam("naziv") String naziv,
            @PathParam("opis") String opis,
            @PathParam("cena") String cena,
            @PathParam("popust") String popust,
            @PathParam("nazivKategorije") String nazivKategorije,
            @PathParam("koTrazi") String koTrazi) {
        
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 7;naziv;opis;cena;popust;nazivKategorije;koTrazi
            TextMessage msg = sess.createTextMessage("7;" + naziv + ";" + opis + ";" + cena + ";" + popust + ";" + nazivKategorije + ";" + koTrazi);
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
    // FUNKCIONALNOST 8: Promena cene artikla
    // =====================================================================
    @POST
    @Path("cena/{naziv}/{novaCena}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniCenu(
            @PathParam("naziv") String naziv,
            @PathParam("novaCena") String novaCena,
            @PathParam("koTrazi") String koTrazi) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q2);
            // Format: 8;nazivArtikla;novaCena;koTrazi
            TextMessage msg = sess.createTextMessage("8;" + naziv + ";" + novaCena + ";" + koTrazi);
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
    // FUNKCIONALNOST 9: Postavljanje popusta za artikal
    // =====================================================================
    @POST
    @Path("popust/{naziv}/{noviPopust}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response promeniPopust(
            @PathParam("naziv") String naziv,
            @PathParam("noviPopust") String noviPopust,
            @PathParam("koTrazi") String koTrazi) {
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q2);
            // Format: 9;nazivArtikla;noviPopust;koTrazi
            TextMessage msg = sess.createTextMessage("9;" + naziv + ";" + noviPopust + ";" + koTrazi);
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
    // FUNKCIONALNOST 17: Dohvatanje svih kategorija
    // =====================================================================
    @GET
    @Path("kategorije")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSveKategorije() {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 17
            TextMessage msg = sess.createTextMessage("17");
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
    // FUNKCIONALNOST 18: Dohvatanje svih artikala koje prodaje korisnik
    // =====================================================================
    @GET
    @Path("prodavac/{korisnickoIme}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiArtikleProdavca(@PathParam("korisnickoIme") String korisnickoIme) {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q2);
            
            // Format: 18;korisnickoIme
            TextMessage msg = sess.createTextMessage("18;" + korisnickoIme);
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
    // FUNKCIONALNOST 24: Poskupljenje za kategoriju
    // =====================================================================
    @GET
    @Path("poskupljenje/{kategorija}/{procenat}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response BuildujCenu(
            @PathParam("kategorija") String kategorija,
            @PathParam("procenat") String procenat){
        
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q2);
            // Format: 9;nazivArtikla;noviPopust;koTrazi
            TextMessage msg = sess.createTextMessage("24;" + kategorija + ";" + procenat);
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse);
            
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(5000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
            
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }
    //utanja je artikal/sigurno_brisanje/{idart}
    @GET
    @Path("artikal/sigurno_brisanje/{idart}/{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response SigurnoBrisanje(@PathParam("idart")String idArtikla,@PathParam("koTrazi")String koTrazi){
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q2);
            // Format: 25;nazivArtikla;noviPopust;koTrazi
            TextMessage msg = sess.createTextMessage("24;" + idArtikla+";"+koTrazi);
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
