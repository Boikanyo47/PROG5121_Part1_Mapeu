/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package programming1a_part1;
import java.util.Scanner;
/**
 *
 * @author Student
 */
public class MainApp {

    public static void main(String[] args) {
        Login login = new Login();
        Scanner sc = new Scanner(System.in);
        
        System.out.println("=== Registration ===\n");
        
        // Get first name
        System.out.print("Enter first name: ");
        String firstName = sc.nextLine();
        
        // Get last name
        System.out.print("Enter last name: ");
        String lastName = sc.nextLine();
        
        // Get username - keep asking until correct
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
        
        // Get password - keep asking until correct
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
        
        // Get cell phone - keep asking until correct
        String cellPhone = "";
        boolean validCell = false;
        while (!validCell) {
            System.out.print("Enter cell phone number (e.g., +27838968976): ");
            cellPhone = sc.nextLine();
            
            if (login.checkCellPhoneNumber(cellPhone)) {
                System.out.println("Cell phone number successfully added.");
                validCell = true;
            } else {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            }
        }
        
        // Register the user
        String regMsg = login.registerUser(username, password, firstName, lastName, cellPhone);
        System.out.println("\n" + regMsg);
        
        // Login section - CHECK USERNAME FIRST
        System.out.println("\n=== Login ===");
        
        boolean loggedIn = false;
        while (!loggedIn) {
            // Step 1: Ask for username
            System.out.print("Enter username: ");
            String loginUsername = sc.nextLine();
            
            // Step 2: Check if username exists
            if (!login.checkUsernameExists(loginUsername)) {
                System.out.println("Username not found. Please check your username.");
                System.out.println("Please try again.\n");
                continue; // Go back to start of loop - ask for username again
            }
            
            // Step 3: Username is correct - now ask for password
            System.out.print("Enter password: ");
            String loginPassword = sc.nextLine();
            
            // Step 4: Check if password matches
            if (login.loginUser(loginUsername, loginPassword)) {
                // Login successful - display welcome message
                System.out.println("\n" + login.getWelcomeMessage());
                System.out.println("Welcome to QuickChat! You are now logged in.");
                loggedIn = true;
            } else {
                // Password is incorrect
                System.out.println("Incorrect password. Please try again.");
                System.out.println("Please try again.\n");
            }
        }
        
        sc.close();
    }
}