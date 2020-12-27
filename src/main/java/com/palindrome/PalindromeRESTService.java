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

import com.palindrome.exception.PalindromeException;
import com.palindrome.exception.PalindromeExceptionType;

/**
 * Palindrome REST service used to perform CRUD operations
 * on a queue of messages
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
			return handleException(e);
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
			return handleException(e);
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
			return Response.ok("Message has been cleared.\n").build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return handleException(e);
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
			service.deleteMessage(message);
			return Response.ok(String.format("Message [%s] has been removed.\n", message)).build();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return handleException(e);
		}
	}

	private Response handleException(Exception e) {

		Response response = null;

		if (e instanceof PalindromeException){
			PalindromeException pe = (PalindromeException) e;

			if (pe.getType() == PalindromeExceptionType.INCORRECT_FORMAT ||
					pe.getType() == PalindromeExceptionType.DUPLICATE_MESSAGE) {
				response = Response.status(400).entity(e.getMessage()).build();
			} else if (pe.getType() == PalindromeExceptionType.NOT_FOUND) {
				response = Response.status(404).entity(e.getMessage()).build();
			} else {
				// this case is ok. it was an incorrect palindrome, but it is not an error
				response = Response.status(200).entity(e.getMessage()).build();
			}    		
		} else {
			response = Response.status(500).entity("General error occurred. See logs for details.").build();
		}

		return response;
	}

}
