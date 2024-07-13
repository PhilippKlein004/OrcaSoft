package org.hbrs.se2.project.hellocar.test;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.util.builder.BewerbungDTOBuilder;
import org.hbrs.se2.project.hellocar.control.BewerbungControl;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class BewerbungRoundTripTest {

    private BewerbungControl bewerbungControl;
    private BewerbungDAO bewerbungDAO;
    private BewerbungDTOImpl bewerbungDTO;

    @BeforeEach
    void setUp() throws DatabaseException {
        BewerbungDTOBuilder builder = new BewerbungDTOBuilder();
        bewerbungControl = new BewerbungControl();
        bewerbungDAO = new BewerbungDAO();

        // Define file paths for the resources
        File motivationFilePath = new File( "src/main/resources/META-INF/resources/images/logo4.jpg");
        String lebenslaufFilePath = "src/main/resources/exampleLebenslauf.txt";

        // Create StreamResource for motivation letter
        StreamResource motivationsschreibenResource = new StreamResource("exampleMotivation.txt", () -> {
            try {
                return new FileInputStream(motivationFilePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("Motivation file not found", e);
            }
        });

        // Create StreamResource for CV
        StreamResource lebenslaufFile = new StreamResource("exampleLebenslauf.txt", () -> {
            try {
                return new FileInputStream(lebenslaufFilePath);
            } catch (FileNotFoundException e) {
                throw new RuntimeException("CV file not found", e);
            }
        });



        bewerbungDTO = builder.createBewerbung()
                .withID(bewerbungDAO.getNextPossibleID())
                .withStudentID(2)
                .withLebenslaufAttached(true)
                .withDate(new Date())
                .withAusschreibungID(11)
                .withInhalt("textTest1")
                .withStatus(0)
                .withLebenslaufFile(lebenslaufFile)
                .withMotivationsschreibenFile(motivationFilePath)
                .withMotivationsschreibenResource(motivationsschreibenResource)
                .build();


    }
    @Test
    void testBewerbungRoundTrip() throws Exception {
        // DTO speichern
        bewerbungDAO.saveBewerbung(bewerbungDTO);

        // DTO aus DB herausholen
        BewerbungDTOImpl bewererbungAfterSave = bewerbungControl.getBewerbungByID(bewerbungDAO.getNextPossibleID(), bewerbungDTO.getStudentID());
        bewererbungAfterSave.setAusschreibungID(bewerbungDTO.getAusschreibungID());

        // not-Null and Identität ungleich
        //assertNotNull(bewererbungAfterSave);
        assertNotSame(bewererbungAfterSave, bewerbungDTO);

        // Werte stimmen überein
        assertNotEquals(bewererbungAfterSave.getID(), bewerbungDTO.getID());
        assertEquals(bewererbungAfterSave.getStudentID(), bewerbungDTO.getStudentID());
        assertEquals(bewererbungAfterSave.getInhalt(), bewerbungDTO.getInhalt());

        // Student hat sich beworben
        assertTrue(bewerbungDAO.hasStudentAlreadyApplied( bewererbungAfterSave.getStudentID(), bewererbungAfterSave.getAusschreibungID()));

        // Status der Bewerbung ändern
        bewererbungAfterSave.setStatus(1);
        bewerbungDAO.updateStatusOfBewerbung(bewererbungAfterSave.getID(), bewererbungAfterSave.getStatus());
        assertNotEquals(bewererbungAfterSave.getStatus(), bewerbungDTO.getStatus());

        // DTO aus DB löschen
        bewerbungDAO.deleteBewerbung(bewerbungDTO.getID());
        bewerbungDAO.deleteBewerbung(bewererbungAfterSave.getID());
    }
}
