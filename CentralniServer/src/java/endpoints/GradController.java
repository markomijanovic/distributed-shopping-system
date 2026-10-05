/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package endpoints;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.*;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.UUID;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Path("grad")
@Stateless
public class GradController {

    @Resource(lookup = "jms/IS1Factory")
    private ConnectionFactory cf;
    
    @Resource(lookup = "jms/Queue1")
    private Queue q1;
    
    @Resource(lookup = "jms/QueueResponse")
    private Queue qResponse;
    // =====================================================================
    // FUNKCIONALNOST 2: Kreiranje grada
    // =====================================================================
    @POST
    @Path("{naziv}/{username}") // Putanja prima samo naziv i korisnicko ime
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED) // OVO JE KLJUČNO REŠENJE
    public Response kreirajGrad(@PathParam("naziv") String naziv, @PathParam("username") String username) {
        String correlationId = UUID.randomUUID().toString();
        
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();

            MessageProducer prod = sess.createProducer(q1);
            // Format poruke: "2;NazivGrada;UsernameKojiTraziAkciju"
            TextMessage msg = sess.createTextMessage("2;" + naziv + ";" + username); 
            msg.setJMSCorrelationID(correlationId);
            msg.setJMSReplyTo(qResponse);
            prod.send(msg);

            TextMessage reply = (TextMessage) cons.receive(15000);
            return reply != null ? Response.ok(reply.getText()).build() : Response.status(408).entity("Timeout").build();
            
        } catch (Exception e) {
            return Response.serverError().entity("Greska: " + e.getMessage()).build();
        }
    }
    // =====================================================================
    // FUNKCIONALNOST 15:Dohvati sve gradove
    // =====================================================================
    @GET
    @Path("{koTrazi}")
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Response dohvatiSveGradove(@PathParam("koTrazi") String koTrazi) {
        String correlationId = UUID.randomUUID().toString();
        try (Connection conn = cf.createConnection();
             Session sess = conn.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            MessageConsumer cons = sess.createConsumer(qResponse, "JMSCorrelationID = '" + correlationId + "'");
            conn.start();
            MessageProducer prod = sess.createProducer(q1);
            
            TextMessage msg = sess.createTextMessage("15;"+koTrazi); // Komanda 15
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