/**
 * Message class handling creation, validation, sending, storing and
 * reporting of QuickChat messages.
 * @author Boikanyo
 */
package programming1a_part1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Message {

    // Keeps track of all messages that have been SENT during this run
    private static ArrayList<String> sentMessagesLog = new ArrayList<>();
    private static ArrayList<String> storedMessagesJson = new ArrayList<>();
    private static int totalMessagesSent = 0;

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private int messageNumber;
    private String status; // "Sent", "Disregarded", "Stored"

    public Message(String recipient, String messageText, int messageNumber) {
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash(this.messageID, this.messageNumber, this.messageText);
    }

    // Empty constructor - handy for unit testing individual methods
    public Message() {
    }

    /**
     * Generates a random 10-digit message ID.
     */
    public static String generateMessageID() {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(rand.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * Ensures the message ID is not more than ten characters long.
     */
    public boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    /**
     * Returns a status message confirming the generated Message ID.
     */
    public String getMessageIDStatus() {
        return "Message ID generated: " + this.messageID;
    }

    /**
     * Validates the recipient cell number.
     * Reuses the exact logic from Login.checkCellPhoneNumber().
     */
    public String checkRecipientCell(String cellNumber) {
        Login login = new Login();
        if (login.checkCellPhoneNumber(cellNumber)) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    /**
     * Validates message length (max 250 characters).
     */
    public String checkMessageLength(String message) {
        if (message.length() > 250) {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        } else {
            return "Message ready to send.";
        }
    }

    /**
     * Creates the Message Hash: first two digits of the ID, the message
     * number, and the first+last words of the message (stripped of
     * punctuation), all in capitals. E.g. 00:0:HITONIGHT
     */
    public String createMessageHash(String id, int msgNumber, String message) {
        String idPrefix = id.substring(0, 2);
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        return idPrefix + ":" + msgNumber + ":" + firstWord + lastWord;
    }

    /**
     * Handles the user's choice of what to do with a completed message.
     * 1 = Send, 2 = Disregard, 3 = Store
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                this.status = "Sent";
                totalMessagesSent++;
                sentMessagesLog.add(this.messageID + " | " + this.messageHash + " | "
                        + this.recipient + " | " + this.messageText);
                return "Message successfully sent.";
            case 2:
                this.status = "Disregarded";
                return "Press 0 to delete the message.";
            case 3:
                this.status = "Stored";
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    /**
     * Returns all the messages that have been SENT during this run,
     * formatted one per line.
     */
    public static String printMessages() {
        StringBuilder sb = new StringBuilder();
        for (String m : sentMessagesLog) {
            sb.append(m).append("\n");
        }
        return sb.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /**
     * Builds a JSON object (as plain text) for this message and writes
     * all stored messages to storedMessages.json.
     * Manual JSON construction - no external library required.
     * Reference for JSON formatting: https://www.json.org/json-en.html
     */
    private void storeMessage() {
        String jsonEntry = "  {\n"
                + "    \"messageID\": \"" + escapeJson(this.messageID) + "\",\n"
                + "    \"messageHash\": \"" + escapeJson(this.messageHash) + "\",\n"
                + "    \"recipient\": \"" + escapeJson(this.recipient) + "\",\n"
                + "    \"message\": \"" + escapeJson(this.messageText) + "\"\n"
                + "  }";

        storedMessagesJson.add(jsonEntry);

        try (FileWriter writer = new FileWriter("storedMessages.json")) {
            StringBuilder fileContent = new StringBuilder();
            fileContent.append("[\n");
            for (int i = 0; i < storedMessagesJson.size(); i++) {
                fileContent.append(storedMessagesJson.get(i));
                if (i < storedMessagesJson.size() - 1) {
                    fileContent.append(",");
                }
                fileContent.append("\n");
            }
            fileContent.append("]");
            writer.write(fileContent.toString());
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // Escapes quotes/backslashes so the message text doesn't break the JSON
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Getters
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getStatus() { return status; }
}