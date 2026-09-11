/**
 * Login class handling user registration, credential validation,
 * and login authentication for the QuickChat application.
 * @author Boikanyo
 */
package programming1a_part1;

public class Login {

    // Stored user details after successful registration
    private String storedUsername;
    private String storedPassword;
    private String storedFirstName;
    private String storedLastName;
    private String storedCellPhone;
    private String lastError; // Track the last error message for login

    /**
     * Checks if username contains an underscore and is at most 5 characters.
     * @param username the username to check
     * @return true if valid, false otherwise
     */
    public boolean checkUserName(String username) {
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Validates password complexity:
     * - at least 8 characters
     * - contains a capital letter
     * - contains a number
     * - contains a special character
     * @param password the password to check
     * @return true if valid, false otherwise
     */
    public boolean checkPasswordComplexity(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Validates cell phone number using regex.
     * Requires: starts with '+', followed by digits only, total length 10-14 digits after '+'.
     * Reference: regex adapted from common international phone number patterns.
     * See: https://www.baeldung.com/java-regex-validate-phone-numbers
     * @param cellNumber the phone number to check
     * @return true if valid, false otherwise
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber.matches("^\\+[0-9]{10,14}$");
    }

    /**
     * Registers a new user. Performs all validations in order: username,
     * password, then cell phone. Stores the user's details once all three
     * checks pass.
     * @param username   entered username
     * @param password   entered password
     * @param firstName  user's first name
     * @param lastName   user's last name
     * @param cellPhone  entered phone number
     * @return appropriate status message
     */
    public String registerUser(String username, String password, String firstName, String lastName, String cellPhone) {
        // Check username format
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        // Check password complexity
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // Check cell phone format
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }

        // All validations passed - store details
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedFirstName = firstName;
        this.storedLastName = lastName;
        this.storedCellPhone = cellPhone;

        return "Username successfully captured.";
    }

    /**
     * Checks if the username exists in the system.
     * @param username the username to check
     * @return true if username exists, false otherwise
     */
    public boolean checkUsernameExists(String username) {
        if (storedUsername == null) {
            return false;
        }
        return storedUsername.equals(username);
    }

    /**
     * Verifies login credentials against stored data.
     * @param username entered username
     * @param password entered password
     * @return true if credentials match, false otherwise
     */
    public boolean loginUser(String username, String password) {
        // Check if any user is registered
        if (storedUsername == null) {
            lastError = "Username or password incorrect, please try again.";
            return false;
        }

        // Check if username exists
        if (!storedUsername.equals(username)) {
            lastError = "Username or password incorrect, please try again.";
            return false;
        }

        // Check if password matches
        if (!storedPassword.equals(password)) {
            lastError = "Username or password incorrect, please try again.";
            return false;
        }

        // Login successful - clear error
        lastError = null;
        return true;
    }

    /**
     * Returns the welcome message after successful login.
     * Format: "Welcome <first name>, <last name> it is great to see you again."
     * @return welcome message with user's first and last name
     */
    public String getWelcomeMessage() {
        return "Welcome " + storedFirstName + ", " + storedLastName + " it is great to see you again.";
    }

    /**
     * Returns the login status message.
     * @param username entered username
     * @param password entered password
     * @return welcome message on success, error message on failure
     */
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + storedFirstName + ", " + storedLastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    /**
     * Gets the last error message from login attempt.
     * @return the last error message
     */
    public String getLastError() {
        return lastError;
    }

    // Getter methods for stored user details
    public String getStoredUsername() {
        return storedUsername;
    }

    public String getStoredFirstName() {
        return storedFirstName;
    }

    public String getStoredLastName() {
        return storedLastName;
    }

    public String getStoredCellPhone() {
        return storedCellPhone;
    }
}