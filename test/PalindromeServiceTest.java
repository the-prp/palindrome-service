import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.palindrome.PalindromeService;

public class PalindromeServiceTest extends PalindromeService {
    
    /**
     * Tests message capacity
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

            Assert.assertEquals(messageQueueSize, messages);
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
            assertTrue(messages.size() == 0);
        }
    }
}
