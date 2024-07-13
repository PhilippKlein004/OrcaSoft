package org.hbrs.se2.project.hellocar.test.testControl;

import org.hbrs.se2.project.hellocar.control.RegistrationControl;
import static org.junit.jupiter.api.Assertions.*;

import org.hbrs.se2.project.hellocar.control.exception.PasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RegistrationControlTest {
    static RegistrationControl registrationControl;

    @BeforeEach
    void setUpClass()  {
        registrationControl = new RegistrationControl();
    }

    @Test
    void checkPasswordToShort() {
        PasswordException exception = assertThrows(PasswordException.class, () -> registrationControl.checkPassword(String.valueOf(123)));

        assertEquals("Passwort ist zu kurz! (3/6)", exception.getMessage());
    }

    @Test
    void checkPasswordToLong() {
        PasswordException exception = assertThrows(PasswordException.class, () -> registrationControl.checkPassword("1243124124124124124124124124124124124124124124"));

        assertEquals("Passwort ist zu lang!", exception.getMessage());
    }
    @Test
    void checkPasswordNoNumber() {
        PasswordException exception = assertThrows(PasswordException.class, () -> registrationControl.checkPassword("asdasdasdasd"));

        assertEquals("Passwort muss eine Zahl enthalten!", exception.getMessage());
    }

    @Test
    void checkPasswordNoLetter() {
        PasswordException exception = assertThrows(PasswordException.class, () -> registrationControl.checkPassword("123123123123"));

        assertEquals("Passwort muss ein Buchstaben enthalten!", exception.getMessage());
    }

    @Test
    void checkPasswordNoSpecialCharacter() {
        PasswordException exception = assertThrows(PasswordException.class, () -> registrationControl.checkPassword("1231231a"));

        assertEquals("Passwort muss min. ein Sonderzeichen (!@#$%&*) enthalten!", exception.getMessage());
    }

    @Test
    void checkPasswordCorrect() {
        assertDoesNotThrow(() -> registrationControl.checkPassword("1231231a!"));
    }

    @Test
    void doPasswordsMatchCorrect() {
        assertDoesNotThrow(() -> registrationControl.doPasswordsMatch("1231231a!", "1231231a!"));
    }

    @Test
    void doPasswordsMatchIncorrect() {
        assertThrows(PasswordException.class, () -> registrationControl.doPasswordsMatch("1231231a!", "1231231a"));

    }

}