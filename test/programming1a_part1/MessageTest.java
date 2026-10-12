/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package programming1a_part1;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        Message m = new Message();
        assertEquals("Message ready to send.", m.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageLengthFailure() {
        Message m = new Message();
        String longMessage = "a".repeat(260); // 260 chars = 10 over the 250 limit
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                m.checkMessageLength(longMessage));
    }

    @Test
    public void testRecipientCellSuccess() {
        Message m = new Message();
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientCellFailure() {
        Message m = new Message();
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                m.checkRecipientCell("08575975889"));
    }

    @Test
    public void testMessageHashCorrect() {
        Message m = new Message();
        String hash = m.createMessageHash("0012345678", 0, "Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", hash);
    }

    @Test
    public void testMessageIDGenerated() {
        Message m = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertTrue(m.getMessageIDStatus().startsWith("Message ID generated: "));
        assertTrue(m.checkMessageID(m.getMessageID()));
    }

    @Test
    public void testMessageSentOptionSend() {
        Message m = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("Message successfully sent.", m.sentMessage(1));
    }

    @Test
    public void testMessageSentOptionDisregard() {
        Message m = new Message("+27838884567", "Hi Keegan, did you receive the payment?", 1);
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
    }

    @Test
    public void testMessageSentOptionStore() {
        Message m = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight?", 0);
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }
}
