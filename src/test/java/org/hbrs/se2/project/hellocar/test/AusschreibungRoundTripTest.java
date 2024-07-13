package org.hbrs.se2.project.hellocar.test;

import org.hbrs.se2.project.hellocar.util.builder.AusschreibungDTOBuilder;
import org.hbrs.se2.project.hellocar.control.AusschreibungControl;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.AusschreibungDAO;
import org.hbrs.se2.project.hellocar.dao.SkillDAO;
import org.hbrs.se2.project.hellocar.dtos.AusschreibungDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOForInsert;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AusschreibungRoundTripTest {

    private AusschreibungControl ausschreibungControl;
    private AusschreibungDTO ausschreibungDTO;
    private AusschreibungDAO ausschreibungDAO;
    private SkillDAO skillDAO;

    @BeforeEach
    void setUp() {
        //Instanziierung
        ausschreibungControl = new AusschreibungControl();
        ausschreibungDAO = new AusschreibungDAO();
        ausschreibungDTO = new AusschreibungDTOForInsert();
        skillDAO = new SkillDAO();
        AusschreibungDTOBuilder builder = new AusschreibungDTOBuilder();


        //Erstellung eines AusschreibungDTO's
        ausschreibungDTO = builder.createAusschreibung()
                .withBeschaeftigung("6")
                .withCompany("145")
                .withBeschreibung("SAP Aushilfe")
                .withStartDatum(new Date(2025, Calendar.APRIL,1))
                .withEndDatum(new Date(2026, Calendar.APRIL,1))
                .withVerguetung(1000)
                .withTitel("test2")
                .withSkills(List.of("4", "5", "6", "7"))
                .build();
    }

    @Test
    void testRoundTripAusschreibung() throws DatabaseException {
        // Abspeicherung der AusschreibungDTO's in AusschreibungDAO
        ausschreibungDAO.saveAusschreibung(ausschreibungDTO);

        // Abgespeicherte DTO im DAO finden
        AusschreibungDTOImpl ausschreibungDTOAfterSave = ausschreibungControl.getAusschreibungByID(ausschreibungDAO.getNextPossibleID());
        // not-null Werte vergleichen
        assertEquals(ausschreibungDTO.getCompany(), ausschreibungDTOAfterSave.getCompany());
        assertEquals(ausschreibungDTO.getBeschaeftigung(), ausschreibungDTOAfterSave.getBeschaeftigung());

        // auf Identität vergleichen
        assertNotSame(ausschreibungDTO, ausschreibungDTOAfterSave);

        // DTO updaten mit neuen Werten
        ausschreibungDTOAfterSave.setCompany("225");
        ausschreibungDTOAfterSave.setBeschaeftigung("2");
        ausschreibungDTOAfterSave.setBeschreibung("testText1");
        ausschreibungDTOAfterSave.setStartDatum(new Date());
        ausschreibungDTOAfterSave.setEndDatum(new Date());
        ausschreibungDTOAfterSave.setSkills(List.of("1", "2", "3"));
        ausschreibungDTOAfterSave.setTitel("testText2");
        ausschreibungDTOAfterSave.setVerguetung(1500);


        // Update im DAO abspeichern
        ausschreibungDAO.updateAusschreibung(ausschreibungDTOAfterSave);
        assertTrue(ausschreibungDAO.doesAusschreibungExist(ausschreibungDTOAfterSave.getTitel(), Integer.parseInt(ausschreibungDTOAfterSave.getCompany())));

        // das DTO mit neuen Werten finden
        AusschreibungDTOImpl ausschreibungDTOAfterUpdate = ausschreibungControl.getAusschreibungByID(ausschreibungDTOAfterSave.getId());


        // not-null Werte vergleichen
        assertNotEquals(ausschreibungDTO.getCompany(), ausschreibungDTOAfterUpdate.getCompany());
        assertNotEquals(ausschreibungDTO.getBeschaeftigung(), ausschreibungDTOAfterUpdate.getBeschaeftigung());

        // Identität prüfen
        assertNotSame(ausschreibungDTOAfterSave, ausschreibungDTOAfterUpdate);

        // Mini RoundTrip für Skills
        assertEquals(List.of(skillDAO.getBezeichnung(1),skillDAO.getBezeichnung(2),skillDAO.getBezeichnung(3)), ausschreibungDTOAfterUpdate.getSkills());
        skillDAO.deleteSkillsOfAusschreibung(ausschreibungDTOAfterUpdate.getId());
        List<String> list = new ArrayList<>();
        assertEquals(skillDAO.getSkillsOfAusschreibung(ausschreibungDTOAfterUpdate.getId()), list);

        // DTO aus DAO löschen + Testen, ob gelöscht ist
        ausschreibungControl.deleteAusschreibung(ausschreibungDTOAfterUpdate.getId());
        assertFalse(ausschreibungDAO.doesAusschreibungExist(ausschreibungDTOAfterUpdate.getTitel(), ausschreibungDTOAfterUpdate.getId()));

    }

}
