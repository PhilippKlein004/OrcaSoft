package org.hbrs.se2.project.hellocar.test.testControl;

import org.hbrs.se2.project.hellocar.control.exception.SearchException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SearchExceptionTest {

    @Test
    public void testSearchExceptionInstantiation() {
        // Test, ob eine SearchException mit einer Nachricht korrekt instanziiert werden kann.
        String message = "Test-Nachricht";
        SearchException exception = new SearchException(message);
        assertNotNull(exception);
    }

    @Test
    public void testSearchExceptionMessage() {
        // Test, ob die Nachricht korrekt gesetzt wird.
        String message = "Test-Nachricht";
        SearchException exception = new SearchException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void testNoSearchResultConstant() {
        // Test, ob die NO_SEARCH_RESULT Konstante den erwarteten Wert hat.
        assertEquals("Es gab keine Treffer bei der Suche!", SearchException.NO_SEARCH_RESULT);
    }
}
