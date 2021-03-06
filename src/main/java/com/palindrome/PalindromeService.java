package com.palindrome;

import java.util.LinkedList;

import org.jboss.logging.Logger;

import com.palindrome.exception.PalindromeException;
import com.palindrome.exception.PalindromeExceptionType;

/**
 * Service for managing the messaging queue for a list of messages. The
 * service determines if a message is a palindrome and if so, performs
 * CRUD operations on them
 *
 */
public class PalindromeService {

    private static final Logger LOG = Logger.getLogger(PalindromeService.class);

    /**
     * The List of messages
     */
    protected LinkedList<String> messages = new LinkedList<String>();;

    /**
     * Max amount of characters accepted as a message. This is to avoid
     * buffer overflows
     */
    protected final int MAX_CHARACTER_LENGTH = 100;

    /**
     * A predefined message queue size. The message list will not
     * exceed this amount.
     */
    protected final int messageQueueSize = 50;

    public PalindromeService() {
    }

    /**
     * Creates a message by checking if it is a palinrome first
     * @param message A string representing the message to be added
     * @throws PalindromeException If the message is empty, too long or not a palindrome
     */
    public void createMessage(String message) throws PalindromeException {
        if (message == null || message.isEmpty()) {
            throw new PalindromeException(PalindromeExceptionType.INCORRECT_FORMAT, "Message was not provided");
        }

        if (message.length() > MAX_CHARACTER_LENGTH) {
            throw new PalindromeException(PalindromeExceptionType.INCORRECT_FORMAT, "The message is too long. Try using a shorter message");
        }

        if (isPalindrome(message)) {
            addMessageToQueue(message);
        } else {
            throw new PalindromeException(PalindromeExceptionType.INCORRECT_FORMAT, String.format("The message was not accepted because [%s] is not a palindrome.\n", message));
        }
    }

    /**
     * Returns a numbered queue of messages
     * @return A String version of the queue of messages
     */
    public String getMessages() {

        if (messages == null || messages.isEmpty()) {
            return "The message queue is empty.\n";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Messages:\n");

        for (int i=0; i<messages.size(); i++) {
            sb.append((i+1) + ". " + messages.get(i) + "\n");
        }

        return sb.toString();
    }

    /**
     * Clears the queue of messages
     */
    public void clearMessages() {
        if (messages.isEmpty()) {
            LOG.info("The message queue is empty.\n");
        } else {
            LOG.info("Clearing the message queue.");
            messages.clear();
        }
    }

    /**
     * Replaces an existing message with a new message. If the message
     * does not exist, this method will not try to add it.
     * @param oldMessage The old message
     * @param newMessage The new message
     * @throws PalindromeException
     */
    public void updateMessage(String oldMessage, String newMessage) throws PalindromeException {

        if (newMessage == null || newMessage.isEmpty()) {
            throw new PalindromeException(PalindromeExceptionType.INCORRECT_FORMAT, "Message was not provided");
        }

        int index = messages.indexOf(oldMessage);

        if (index == -1) {
            LOG.error("Message does not exist yet. Please create it.");
        }

        if (newMessage.length() > MAX_CHARACTER_LENGTH) {
            throw new PalindromeException(PalindromeExceptionType.INCORRECT_FORMAT, "The message is too long. Try using a shorter message");
        }

        if (isPalindrome(newMessage)) {
            LOG.info(String.format("Replacing %s with %s in the message queue.", oldMessage, newMessage));
            messages.set(index, newMessage);
        } else {
            throw new PalindromeException(PalindromeExceptionType.NOT_PALINDROME, String.format("Message [%s] is not a palindrome.", newMessage));
        }
    }

    /**
     * Removes a message from the queue.
     * @param message The message to be removed
     * @throws PalindromeException If the message does not exist
     */
    public void deleteMessage(String message) throws PalindromeException {
        LOG.info(String.format("Removing [%s] from messages.", message));
        
        if (!messages.contains(message)) {
        	throw new PalindromeException(PalindromeExceptionType.NOT_FOUND, "Message does not exist.");
        }
        
        messages.remove(message);
    }

    /**
     * Helper method to add the message to the queue
     * @param msg The new message
     * @throws PalindromeException If the message queue size is too large or it if the message queue
     * already contains this element.
     */
    private void addMessageToQueue(String msg) throws PalindromeException {
        if (messages.contains(msg)) {
            throw new PalindromeException(PalindromeExceptionType.DUPLICATE_MESSAGE, "Message queue already contains this value.");
        }

        if (messages.size() >= messageQueueSize) {
            String removedMessage = messages.removeFirst();
            LOG.info(String.format("Message size is too large. Removing first item [%s] from messages.", removedMessage));
        }
        LOG.info(String.format("Adding [%s] to messages.", msg));
        messages.add(msg);

    }

    /**
     * Checks if the given string is a palindrome. Empty
     * strings will be considered a palindrome.
     * @param msg The message that is being verified
     * @return true if the message is a palindrome, false otherwise
     * @throws PalindromeException If there was no message given.
     */
    private boolean isPalindrome(String msg) throws PalindromeException {

        if (msg == null) {
            throw new PalindromeException(PalindromeExceptionType.INCORRECT_FORMAT, "Message is empty.");
        }

        if (msg.isEmpty()) {
            return true;
        }

        String lowercaseMsg = msg.toLowerCase().replaceAll("\\s+", "");
        int length = lowercaseMsg.length();

        for (int i=0; i<length/2; i++) {
            if (lowercaseMsg.charAt(i) != lowercaseMsg.charAt(length -i - 1)) {
                return false;
            }
        }
        return true;
    }

}
