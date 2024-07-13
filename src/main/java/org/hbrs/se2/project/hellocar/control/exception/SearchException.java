package org.hbrs.se2.project.hellocar.control.exception;

public class SearchException extends Exception {

    public static final String NO_SEARCH_RESULT = "Es gab keine Treffer bei der Suche!";

    public SearchException(String message) {
        super(message);
    }

}
