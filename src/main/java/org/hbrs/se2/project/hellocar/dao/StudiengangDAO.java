package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudiengangDAO extends srsbsbDAO {

    /**
     * Gibt eine String-Liste der aktuell verfügbaren Studiengänge zurück.
     *
     * @return Liste der verfügbaren Studiengänge.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getAll() throws DatabaseException {
        return super.getAll( "orcasoft.studiengang" );
    }

    /**
     * Gibt die Bezeichnung eines Studiengangs anhand seines
     * Primärschlüssels wieder.
     *
     * @param idStudiengang ID_com des Studiengangs.
     * @return Bezeichnung des Studiengangs.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public String getBezeichnung( int idStudiengang ) throws DatabaseException {
        return super.getBezeichnung( "studiengang", idStudiengang );
    }

}
