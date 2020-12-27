package com.palindrome.exception;

/**
 * Exception thrown when the message is not found in the queue
 *
 */
@SuppressWarnings("serial")
public class PalindromeException extends Exception {
	
	PalindromeExceptionType type;

	public PalindromeException(PalindromeExceptionType type, String message) {
		super(message);
		this.type = type;
	}
	
	public PalindromeExceptionType getType() {
		return type;
	}

}
