package org.hbrs.se2.project.hellocar.test.testControl;

import org.hbrs.se2.project.hellocar.control.AusschreibungControl;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import java.util.Arrays;

public class AusschreibungControlTest {

    private AusschreibungControl ausschreibungControl;

    @BeforeEach
    public void setup() {
        ausschreibungControl = new AusschreibungControl();

    }

    @Test
    public void testGetAllAusschreibungen() throws DatabaseException {
        List<String> ausschreibungen = ausschreibungControl.getAllAusschreibungenByCompany(217);
        Assertions.assertNotNull(ausschreibungen);
    }

    @Test
    public void testDeleteAusschreibung() {
        Assertions.assertDoesNotThrow(() -> ausschreibungControl.deleteAusschreibung(217));
    }


    @Test
    public void testGetAllAusschreibungenReturnsCorrectFormat() {
        List<String> expected = Arrays.asList("11", "Software Engineer", "478", "Netzwerkadministrator (m/w/d)", "479", "Digital Marketing Specialist (m/w/d)");
        Assertions.assertDoesNotThrow(() -> {
            List<String> actual = ausschreibungControl.getAllAusschreibungenByCompany(79);
            Assertions.assertIterableEquals(expected, actual);
        });
    }
}

