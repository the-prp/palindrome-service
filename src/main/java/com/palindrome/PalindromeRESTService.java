package com.palindrome;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Palindrome REST service used to perform CRUD operations
 * on a queue of messages
 * @author matt
 *
 */
@Path("/messages")
public class PalindromeRESTService {

    /**
     * Palindrome Service for processing of CRUD operations
     */
    @Autowired
    PalindromeService service;

    /**
     * Gets a numbered list of messages previously entered.
     * @return A String of messages that are in numbered list format
     */
    @GET
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response getMessages() {
        String messages = service.getMessages();
        return Response.ok(messages).build();
    }

    /**
     * Creates a message in the queue
     * @param message String format message. Can be a single
     * word or a sentence. This value must be less than 100 characters
     * @return A message of success or failure in String format
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response createMessage(@FormParam("message") String message) {
        try {
            service.createMessage(message);
            return Response.ok(String.format("Palindrome [%s] added to messages.\n", message)).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Response response = Response.ok(e.getMessage()).build();
            return response;
        }
    }

    /**
     * Updates a message in the message queue
     * @param message The existing message in String format
     * @param updatedMessage The new message in String format
     * @return A message representing the success or failure of the operation
     */
    @POST
    @Path("/{message}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response updateMessage(@PathParam("message") String message,
            @FormParam("updatedMessage") String updatedMessage) {
        try {
            service.updateMessage(message, updatedMessage);
            return Response.ok(String.format("Palindrome [%s] replaced with [%s]\n", message, updatedMessage)).build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Response response = Response.ok(e.getMessage()).build();
            return response;
        }
    }

    /**
     * Clears the message queue
     * @return A message representing the success or failure of the operation
     */
    @DELETE
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response clearMessages() {
        try {
            service.clearMessages();
            return Response.ok("Messages have been cleared.\n").build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Response response = Response.ok(e.getMessage()).build();
            return response;
        }
    }

    /**
     * Deletes a given message
     * @param message The message to be deleted from the message queue in
     * String format
     * @return A message representing the success or failure of the operation
     */
    @DELETE
    @Path("/{message}")
    @Produces(MediaType.APPLICATION_FORM_URLENCODED)
    public Response deleteMessage(@PathParam("message") String message) {
        try {
            boolean wasRemoved = service.deleteMessage(message);
            if (wasRemoved) {
                return Response.ok(String.format("Message [%s] has been removed.\n", message)).build();
            } else {
                return Response.ok(String.format("Message [%s] was not removed. It may not exist anymore", message)).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Response response = Response.ok(e.getMessage()).build();
            return response;
        }
    }

}
