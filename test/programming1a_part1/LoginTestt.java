/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package programming1a_part1;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Student
 */
//All 13 unit tests for Login class - all passing
public class LoginTestt {

    // Helper method to register a user with valid data
    private Login registerValidUser() {
        Login login = new Login();
        String regMsg = login.registerUser("kyl_1", "Ch&sec@ke99!", "Naledi", "Mokoena", "+27838968976");
        assertEquals("User registered successfully.", regMsg);
        return login;
    }

    // ---- Test checkUserName() ----
    @Test
    public void testCheckUserNameCorrect() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testCheckUserNameIncorrect() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!"));
        assertFalse(login.checkUserName("kyle"));
        assertFalse(login.checkUserName("kyle_123"));
    }

    // ---- Test checkPasswordComplexity() ----
    @Test
    public void testCheckPasswordComplexityValid() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&sec@ke99!"));
    }

    @Test
    public void testCheckPasswordComplexityInvalid() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
        assertFalse(login.checkPasswordComplexity("Passw0rd"));
        assertFalse(login.checkPasswordComplexity("P@ssw0"));
    }

    // ---- Test checkCellPhoneNumber() ----
    @Test
    public void testCheckCellPhoneValid() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCheckCellPhoneInvalid() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("08966553"));
        assertFalse(login.checkCellPhoneNumber("+123"));
        assertFalse(login.checkCellPhoneNumber("+27abc"));
    }

    // ---- Test registerUser() messages ----
    @Test
    public void testRegisterUserUsernameInvalid() {
        Login login = new Login();
        String msg = login.registerUser("kyle!!!!!!", "Ch&sec@ke99!", "Naledi", "Mokoena", "+27838968976");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", msg);
    }

    @Test
    public void testRegisterUserPasswordInvalid() {
        Login login = new Login();
        String msg = login.registerUser("kyl_1", "password", "Naledi", "Mokoena", "+27838968976");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", msg);
    }

    @Test
    public void testRegisterUserCellInvalid() {
        Login login = new Login();
        String msg = login.registerUser("kyl_1", "Ch&sec@ke99!", "Naledi", "Mokoena", "08966553");
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", msg);
    }

    // ---- Test loginUser() ----
    @Test
    public void testLoginSuccessful() {
        Login login = registerValidUser();
        assertTrue(login.loginUser("kyl_1", "Ch&sec@ke99!"));
    }

    @Test
    public void testLoginFailedWrongUsername() {
        Login login = registerValidUser();
        assertFalse(login.loginUser("wrong", "Ch&sec@ke99!"));
    }

    @Test
    public void testLoginFailedWrongPassword() {
        Login login = registerValidUser();
        assertFalse(login.loginUser("kyl_1", "wrong"));
    }

    // ---- Test returnLoginStatus() messages ----
    @Test
    public void testLoginStatusSuccess() {
        Login login = registerValidUser();
        String status = login.returnLoginStatus("kyl_1", "Ch&sec@ke99!");
        assertEquals("Welcome Naledi Mokoena, it is great to see you again.", status);
    }

    @Test
    public void testLoginStatusFailure() {
        Login login = registerValidUser();
        String status = login.returnLoginStatus("kyl_1", "wrong");
        assertEquals("Username or password incorrect, please try again.", status);
    }
}

    