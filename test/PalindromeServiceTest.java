import org.junit.Assert;
import org.junit.Test;

import com.palindrome.PalindromeService;

public class PalindromeServiceTest extends PalindromeService {
    
    
    @Test
    public void basicTest() {
        
        String validPalindrome = "racecar";
        String invalidPalindrome = "Not a palindrome";
        
        System.out.println("Trying to add an invalid palindrome to the queue.");
        
        try {
            createMessage(invalidPalindrome);
        } catch (Exception e) {
            Assert.assertFalse(messages.size() > 0);
        }
        
        System.out.println("Trying to add a valid palindrome to the queue.");
        
        try {
            createMessage(validPalindrome);
        } catch (Exception e) {
            Assert.assertTrue(messages.contains(validPalindrome));
        }
        
        System.out.println("Trying to add an empty string to the queue.");
        
        try {
            createMessage("");
        } catch (Exception e) {
            Assert.assertFalse(messages.contains(""));
        }
        
        System.out.println("Trying to add null to the queue.");
        
        try {
            createMessage(null);
        } catch (Exception e) {
            Assert.assertFalse(messages.contains(null));
        }
        
        System.out.println("Printing out current queue:\n" + getMessages() + "\n\n");
        
        // replace the palindrome to test updates 
        System.out.println(String.format("Trying to replace [%s] with [%s] in the queue.", validPalindrome, invalidPalindrome));
        
        try {
            updateMessage(validPalindrome, invalidPalindrome);
        } catch (Exception e) {
            Assert.assertTrue(messages.contains(validPalindrome));
            Assert.assertFalse(messages.contains(invalidPalindrome));
            System.out.println("Message was not replaced due to it being an invalid palindrome.");
        }

        System.out.println("Printing out current queue:\n" + getMessages() + "\n\n");
        
        String validPalindromeReplacement = "kayak";
        
        try {
            updateMessage(validPalindrome, validPalindromeReplacement);
        } catch (Exception e) {
            Assert.assertFalse(messages.contains(validPalindrome));
            Assert.assertTrue(messages.contains(validPalindromeReplacement));
            System.out.println("Message was not replaced due to it being an invalid palindrome.");
        }
        
        System.out.println("Printing out current queue:\n" + getMessages() + "\n\n");
        
        // remove the palindrome to test deletes
        System.out.println(String.format("Removing %s from the queue", validPalindromeReplacement));
        
        deleteMessage(validPalindromeReplacement);
        
        System.out.println("Printing out current queue:\n" + getMessages() + "\n\n");
        
        Assert.assertFalse(messages.contains(validPalindromeReplacement));
        
        // re add the palindrome for the final clear test
        try {
            createMessage(validPalindrome);
        } catch (Exception e) {
            Assert.fail();
        }
        
        Assert.assertTrue(messages.contains(validPalindrome));

        System.out.println("Printing out current queue:\n" + getMessages() + "\n\n");

        
        System.out.println("Clearing the message queue.");
        
        clearMessages();
        
        Assert.assertTrue(messages.size() == 0);
        
        System.out.println("Printing out current queue:\n" + getMessages() + "\n\n");
    }
    
    /**
     * Tests message capacity. Generates a larger than queue size
     * amount of messages to add to prove the rotation of the queue works
     */
    @Test
    public void capacityTest() {
        
        int doubleCapacity = messageQueueSize * 2;

        try {        
            for (int i=0; i<doubleCapacity; i++) {
                StringBuilder sb = new StringBuilder(String.valueOf(i));
                
                if (i > 9) {
                    sb.reverse();
                }

                String message = i + "racecar" + sb.toString();            

                System.out.println(String.format("Trying to add message: %s", message));
                createMessage(message);
            }

            System.out.println(getMessages());

            Assert.assertEquals(messageQueueSize, messages.size());
        } catch (Exception e) {
            Assert.fail("Failure in capacity test during message creation.");
        }
    }
    
    
    /**
     * Generates a large string that will try to be
     * added to the palindrome messages
     */
    @Test
    public void testOverflow() {
        StringBuilder sb = new StringBuilder();
        
        for (int i=0; i<MAX_CHARACTER_LENGTH*4; i++) {
            sb.append("a");
        }
        
        String longString = sb.toString();
        
        try {
            System.out.println("Trying to overflow the input...");
            createMessage(longString);
        } catch (Exception e) {
            System.out.println("Message was not accepted as expected");
            Assert.assertTrue(messages.size() == 0);
        }
    }
}
