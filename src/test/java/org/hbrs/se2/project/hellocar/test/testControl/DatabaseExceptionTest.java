package org.hbrs.se2.project.hellocar.test.testControl;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DatabaseExceptionTest {

    @Test
    public void testDatabaseExceptionConstructor() {
        String message = "Test message";
        DatabaseException exception = new DatabaseException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testDatabaseExceptionConstants() {
        assertEquals("Der Nutzer konnte nicht gefunden werden!", DatabaseException.USER_NOT_FOUND);
        assertEquals("Nutzername existiert bereits!", DatabaseException.USERNAME_ALREADY_EXISTS);
        assertEquals("Mail wird bereits verwendet!", DatabaseException.MAIL_ALREADY_EXISTS);
        assertEquals("Verbindung zur Datenbank ist nicht möglich!", DatabaseException.NO_CONNECTION);
        assertEquals("Es gab einen Fehler im SQL-Code! ", DatabaseException.SQL_ERROR);}
}
