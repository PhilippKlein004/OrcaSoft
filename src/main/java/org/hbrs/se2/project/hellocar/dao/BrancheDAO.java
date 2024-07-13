package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BrancheDAO extends srsbsbDAO {

    /**
     * Gibt eine String-Liste der aktuell verfügbaren Branchen zurück.
     *
     * @return Liste der verfügbaren Branchen.
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws DatabaseException Fehler bei der JDBC-Verbindung.
     */

    public List<String> getAll() throws DatabaseException {
        return super.getAll( "orcasoft.branche" );
    }

    /**
     * Gibt die Bezeichnung einer Branche anhand seines
     * Primärschlüssels wieder.
     *
     * @param ID_Branche ID_com des Branche.
     * @return Bezeichnung der Branche.
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws DatabaseException Fehler bei der JDBC-Verbindung.
     */

    public String getBezeichnung( int ID_Branche ) throws DatabaseException {
        return super.getBezeichnung( "branche", ID_Branche );
    }

}
