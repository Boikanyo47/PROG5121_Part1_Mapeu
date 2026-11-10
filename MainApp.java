/**
 * Console entry point for the QuickChat registration, login, messaging
 * and stored-messages features.
 * @author Boikanyo
 */
package programming1a_part1;
import java.util.ArrayList;
import java.util.Scanner;

public class MainApp {

    private static MessageManager manager = new MessageManager();

    public static void main(String[] args) {
        Login login = new Login();
        Scanner sc = new Scanner(System.in);

        System.out.println("=== Registration ===\n");

        System.out.print("Enter first name: ");
        String firstName = sc.nextLine();

        System.out.print("Enter last name: ");
        String lastName = sc.nextLine();

        String username = "";
        boolean validUsername = false;
        while (!validUsername) {
            System.out.print("Enter username: ");
            username = sc.nextLine();
            if (login.checkUserName(username)) {
                System.out.println("Username successfully captured.");
                validUsername = true;
            } else {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            }
        }

        String password = "";
        boolean validPassword = false;
        while (!validPassword) {
            System.out.print("Enter password: ");
            password = sc.nextLine();
            if (login.checkPasswordComplexity(password)) {
                System.out.println("Password successfully captured.");
                validPassword = true;
            } else {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            }
        }

        String cellPhone = "";
        boolean validCell = false;
        while (!validCell) {
            System.out.print("Enter cell phone number (e.g., +27838968976): ");
            cellPhone = sc.nextLine();
            if (login.checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell number successfully captured.");
                validCell = true;
            } else {
                System.out.println("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.");
            }
        }

        String regMsg = login.registerUser(username, password, firstName, lastName, cellPhone);
        System.out.println("\n" + regMsg);

        System.out.println("\n=== Login ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter username: ");
            String loginUsername = sc.nextLine();

            if (!login.checkUsernameExists(loginUsername)) {
                System.out.println("Username or password incorrect, Please try again.");
                continue;
            }

            System.out.print("Enter password: ");
            String loginPassword = sc.nextLine();

            if (login.loginUser(loginUsername, loginPassword)) {
                System.out.println("\n" + login.getWelcomeMessage());
                loggedIn = true;
            } else {
                System.out.println("Username or password incorrect, Please try again.");
            }
        }

        // ===== QuickChat Messaging =====
        // Read any previously stored messages from the JSON file into the arrays
        manager.loadStoredMessagesFromFile();

        System.out.println("\nWelcome to QuickChat.");

        boolean running = true;
        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    sendMessagesFlow(sc);
                    break;
                case 2:
                    System.out.println(manager.generateReport("Sent"));
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                case 4:
                    storedMessagesMenu(sc, cellPhone);
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, 3 or 4.");
            }
        }

        sc.close();
    }

    private static void sendMessagesFlow(Scanner sc) {
        System.out.print("How many messages do you want to enter? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number, returning to menu.");
            return;
        }

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

            Message tempMsg = new Message();

            String recipient;
            while (true) {
                System.out.print("Enter recipient cell number: ");
                recipient = sc.nextLine();
                String result = tempMsg.checkRecipientCell(recipient);
                System.out.println(result);
                if (result.equals("Cell phone number successfully captured.")) {
                    break;
                }
            }

            String messageText;
            while (true) {
                System.out.print("Enter your message: ");
                messageText = sc.nextLine();
                String result = tempMsg.checkMessageLength(messageText);
                System.out.println(result);
                if (result.equals("Message ready to send.")) {
                    break;
                }
            }

            Message message = new Message(recipient, messageText, i);
            System.out.println(message.getMessageIDStatus());

            // Keep asking until the user picks 1, 2 or 3
            int action = 0;
            while (action < 1 || action > 3) {
                System.out.println("1) Send Message  2) Disregard Message  3) Store Message");
                System.out.print("Choose an option: ");
                try {
                    action = Integer.parseInt(sc.nextLine().trim());
                } catch (NumberFormatException e) {
                    action = 0;
                }
                if (action < 1 || action > 3) {
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
                }
            }
            System.out.println(message.sentMessage(action));

            // Save the message into the arrays (sent / stored / disregarded)
            if (message.getStatus() != null) {
                manager.addMessage(message.getMessageID(), message.getMessageHash(),
                        message.getRecipient(), message.getMessageText(), message.getStatus());
            }

            System.out.println("\nMessage ID: " + message.getMessageID());
            System.out.println("Message Hash: " + message.getMessageHash());
            System.out.println("Recipient: " + message.getRecipient());
            System.out.println("Message: " + message.getMessageText());
        }

        System.out.println("\nTotal messages sent so far: " + Message.returnTotalMessages());
    }

    private static void storedMessagesMenu(Scanner sc, String senderCell) {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n=== Stored Messages ===");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search messages for a particular recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display report of all stored messages");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            switch (choice) {
                case 1:
                    String list = manager.displaySenderAndRecipient(senderCell);
                    System.out.println(list.isEmpty() ? "No stored messages." : list);
                    break;
                case 2:
                    String longest = manager.getLongestStoredMessage();
                    System.out.println(longest.isEmpty() ? "No stored messages." : "Longest stored message: " + longest);
                    break;
                case 3:
                    System.out.print("Enter message ID: ");
                    System.out.println(manager.searchByMessageID(sc.nextLine().trim()));
                    break;
                case 4:
                    System.out.print("Enter recipient cell number: ");
                    ArrayList<String> found = manager.searchByRecipient(sc.nextLine().trim());
                    if (found.isEmpty()) {
                        System.out.println("No messages found for that recipient.");
                    } else {
                        for (String m : found) {
                            System.out.println(m);
                        }
                    }
                    break;
                case 5:
                    System.out.print("Enter message hash: ");
                    String result = manager.deleteByHash(sc.nextLine().trim());
                    System.out.println(result);
                    if (result.endsWith("successfully deleted.")) {
                        manager.saveStoredMessagesToFile();
                    }
                    break;
                case 6:
                    System.out.println(manager.generateReport("Stored"));
                    break;
                case 7:
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option, please choose 1 to 7.");
            }
        }
    }
}