package org.hbrs.se2.project.hellocar.control.exception;

public class DatabaseException extends Exception {

    public static final String USER_NOT_FOUND = "Der Nutzer konnte nicht gefunden werden!";
    public static final String USERNAME_ALREADY_EXISTS = "Nutzername existiert bereits!";
    public static final String MAIL_ALREADY_EXISTS = "Mail wird bereits verwendet!";
    public static final String NO_CONNECTION = "Verbindung zur Datenbank ist nicht möglich!";
    public static final String SQL_ERROR = "Es gab einen Fehler im SQL-Code! ";
    public static final String NO_SEARCH_RESULTS = "Es wurden keine Ergebnisse zum Suchbegriff gefunden!";

    public DatabaseException( String msg ) {
        super(msg);
    }
}
