/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * Unit tests for the MessageManager class - arrays, longest message,
 * searching, deleting, report and reading the JSON file.
 * A temporary JSON file is used so the real storedMessages.json is never changed.
 * @author Boikanyo
 */
package programming1a_part1;

import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageManagerTest {

    private static final String TEST_FILE = "testStoredMessages.json";
    private MessageManager manager;

    @Before
    public void setUp() {
        Message.setStorageFile(TEST_FILE);
        new File(TEST_FILE).delete();
        manager = new MessageManager();
        manager.populateTestData();
    }

    @After
    public void tearDown() {
        new File(TEST_FILE).delete();
        Message.setStorageFile("storedMessages.json");
    }

    @Test
    public void testSentMessagesArrayPopulated() {
        assertEquals(2, manager.getSentMessages().size());
        assertEquals("Did you get the cake?", manager.getSentMessages().get(0));
        assertEquals("It is dinner time !", manager.getSentMessages().get(1));
    }

    @Test
    public void testDisregardedAndStoredArraysPopulated() {
        assertEquals(1, manager.getDisregardedMessages().size());
        assertEquals(2, manager.getStoredMessages().size());
        assertEquals(5, manager.getMessageIDs().size());
        assertEquals(5, manager.getMessageHashes().size());
    }

    @Test
    public void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                manager.getLongestStoredMessage());
    }

    @Test
    public void testSearchByMessageID() {
        String result = manager.searchByMessageID("0838884567");
        assertTrue(result.contains("It is dinner time !"));
        assertTrue(result.contains("0838884567"));
    }

    @Test
    public void testSearchByMessageIDNotFound() {
        assertEquals("Message ID not found.", manager.searchByMessageID("9999999999"));
    }

    @Test
    public void testSearchByRecipient() {
        ArrayList<String> results = manager.searchByRecipient("+27838884567");
        assertEquals(2, results.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", results.get(0));
        assertEquals("Ok, I am leaving without you.", results.get(1));
    }

    @Test
    public void testDeleteByHash() {
        String hash = manager.getMessageHashes().get(1); // Test message 2
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                manager.deleteByHash(hash));
        assertEquals(1, manager.getStoredMessages().size());
    }

    @Test
    public void testDeleteByHashNotFound() {
        assertEquals("Message hash not found.", manager.deleteByHash("00:0:NOTHING"));
    }

    @Test
    public void testDisplayReport() {
        String report = manager.generateReport("Sent");
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("+27834557896"));
        assertTrue(report.contains(manager.getMessageHashes().get(0)));
        assertTrue(report.contains("It is dinner time !"));
    }

    @Test
    public void testLoadStoredMessagesFromJson() {
        // Store two messages in the temporary JSON file, then read them back into an array
        new Message("+27838884567", "Ok, I am leaving without you.", 0).sentMessage(3);
        new Message("+27834557896", "Did you get the cake?", 1).sentMessage(3);

        MessageManager loader = new MessageManager();
        loader.loadStoredMessagesFromFile();

        assertEquals(2, loader.getStoredMessages().size());
        assertEquals("Ok, I am leaving without you.", loader.getStoredMessages().get(0));
        assertEquals("Did you get the cake?", loader.getStoredMessages().get(1));
    }
}