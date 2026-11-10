/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 * MessageManager holds the arrays used by the QuickChat final PoE and
 * provides searching, deleting, reporting and JSON-reading features.
 * @author Boikanyo
 */
package programming1a_part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageManager {

    // Required arrays
    private ArrayList<String> sentMessages = new ArrayList<>();
    private ArrayList<String> disregardedMessages = new ArrayList<>();
    private ArrayList<String> storedMessages = new ArrayList<>();
    private ArrayList<String> messageHashes = new ArrayList<>();
    private ArrayList<String> messageIDs = new ArrayList<>();

    // Parallel arrays (same index = same message)
    private ArrayList<String> recipients = new ArrayList<>();
    private ArrayList<String> messageTexts = new ArrayList<>();
    private ArrayList<String> statuses = new ArrayList<>(); // Sent, Stored, Disregarded

    /**
     * Adds a message to all the parallel arrays and to the array that
     * matches its status.
     */
    public void addMessage(String id, String hash, String recipient, String text, String status) {
        messageIDs.add(id);
        messageHashes.add(hash);
        recipients.add(recipient);
        messageTexts.add(text);
        statuses.add(status);

        if (status.equals("Sent")) {
            sentMessages.add(text);
        } else if (status.equals("Stored")) {
            storedMessages.add(text);
        } else if (status.equals("Disregarded")) {
            disregardedMessages.add(text);
        }
    }

    /**
     * Populates the arrays with the five test messages from the brief.
     * Used by the unit tests only.
     */
    public void populateTestData() {
        String[][] data = {
            {"1000000001", "+27834557896", "Did you get the cake?", "Sent"},
            {"1000000002", "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Stored"},
            {"1000000003", "+27834484567", "Yohoooo, I am at your gate.", "Disregarded"},
            {"0838884567", "0838884567", "It is dinner time !", "Sent"},
            {"1000000005", "+27838884567", "Ok, I am leaving without you.", "Stored"}
        };
        Message helper = new Message();
        for (int i = 0; i < data.length; i++) {
            String hash = helper.createMessageHash(data[i][0], i, data[i][2]);
            addMessage(data[i][0], hash, data[i][1], data[i][2], data[i][3]);
        }
    }

    /**
     * Returns the sender and recipient of every stored message.
     */
    public String displaySenderAndRecipient(String sender) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < statuses.size(); i++) {
            if (statuses.get(i).equals("Stored")) {
                sb.append("Sender: ").append(sender)
                  .append(" | Recipient: ").append(recipients.get(i)).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Finds the longest message in the stored messages array.
     */
    public String getLongestStoredMessage() {
        String longest = "";
        for (String msg : storedMessages) {
            if (msg.length() > longest.length()) {
                longest = msg;
            }
        }
        return longest;
    }

    /**
     * Searches by message ID and returns the recipient and message.
     */
    public String searchByMessageID(String id) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                return "Recipient: " + recipients.get(i) + " | Message: " + messageTexts.get(i);
            }
        }
        return "Message ID not found.";
    }

    /**
     * Returns every sent or stored message for a recipient.
     */
    public ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> results = new ArrayList<>();
        for (int i = 0; i < recipients.size(); i++) {
            boolean sentOrStored = statuses.get(i).equals("Sent") || statuses.get(i).equals("Stored");
            if (recipients.get(i).equals(recipient) && sentOrStored) {
                results.add(messageTexts.get(i));
            }
        }
        return results;
    }

    /**
     * Deletes a message using its hash and removes it from every array.
     */
    public String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String text = messageTexts.get(i);
                String status = statuses.get(i);

                if (status.equals("Sent")) {
                    sentMessages.remove(text);
                } else if (status.equals("Stored")) {
                    storedMessages.remove(text);
                } else if (status.equals("Disregarded")) {
                    disregardedMessages.remove(text);
                }

                messageIDs.remove(i);
                messageHashes.remove(i);
                recipients.remove(i);
                messageTexts.remove(i);
                statuses.remove(i);

                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    /**
     * Builds a report of all messages with the given status
     * (Sent, Stored or Disregarded).
     */
    public String generateReport(String status) {
        StringBuilder sb = new StringBuilder();
        sb.append("===== ").append(status).append(" Messages Report =====\n");
        int count = 0;
        for (int i = 0; i < statuses.size(); i++) {
            if (statuses.get(i).equals(status)) {
                count++;
                sb.append("\nMessage ID: ").append(messageIDs.get(i)).append("\n");
                sb.append("Message Hash: ").append(messageHashes.get(i)).append("\n");
                sb.append("Recipient: ").append(recipients.get(i)).append("\n");
                sb.append("Message: ").append(messageTexts.get(i)).append("\n");
            }
        }
        if (count == 0) {
            sb.append("\nNo messages found.\n");
        }
        return sb.toString();
    }

    /**
     * Reads the stored messages JSON file into the arrays. Messages already
     * in the arrays (same message ID) are skipped.
     * References: Java regex Pattern docs
     * (https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html)
     * and the JSON format (https://www.json.org/json-en.html)
     */
    public void loadStoredMessagesFromFile() {
        Path path = Paths.get(Message.getStorageFile());
        if (!Files.exists(path)) {
            return;
        }
        try {
            String content = new String(Files.readAllBytes(path));
            String regex = "\"messageID\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*"
                    + "\"messageHash\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*"
                    + "\"recipient\"\\s*:\\s*\"([^\"]*)\"\\s*,\\s*"
                    + "\"message\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"";
            Matcher matcher = Pattern.compile(regex).matcher(content);

            while (matcher.find()) {
                String id = matcher.group(1);
                if (!messageIDs.contains(id)) {
                    String text = matcher.group(4).replace("\\\"", "\"").replace("\\\\", "\\");
                    addMessage(id, matcher.group(2), matcher.group(3), text, "Stored");
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read stored messages: " + e.getMessage());
        }
    }

    /**
     * Rewrites the stored messages JSON file so it matches the stored
     * messages in the arrays (used after a stored message is deleted).
     */
    public void saveStoredMessagesToFile() {
        StringBuilder sb = new StringBuilder("[\n");
        boolean first = true;
        for (int i = 0; i < statuses.size(); i++) {
            if (statuses.get(i).equals("Stored")) {
                if (!first) {
                    sb.append(",\n");
                }
                first = false;
                sb.append("  {\n")
                  .append("    \"messageID\": \"").append(escapeJson(messageIDs.get(i))).append("\",\n")
                  .append("    \"messageHash\": \"").append(escapeJson(messageHashes.get(i))).append("\",\n")
                  .append("    \"recipient\": \"").append(escapeJson(recipients.get(i))).append("\",\n")
                  .append("    \"message\": \"").append(escapeJson(messageTexts.get(i))).append("\"\n")
                  .append("  }");
            }
        }
        sb.append("\n]");
        try {
            Files.write(Paths.get(Message.getStorageFile()), sb.toString().getBytes());
        } catch (IOException e) {
            System.out.println("Could not update stored messages file: " + e.getMessage());
        }
    }

    // Escapes quotes/backslashes so the message text doesn't break the JSON
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    // Getters used by the menu and unit tests
    public ArrayList<String> getSentMessages() { return sentMessages; }
    public ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public ArrayList<String> getStoredMessages() { return storedMessages; }
    public ArrayList<String> getMessageHashes() { return messageHashes; }
    public ArrayList<String> getMessageIDs() { return messageIDs; }
}