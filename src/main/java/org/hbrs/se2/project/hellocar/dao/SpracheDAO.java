package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SpracheDAO extends srsbsbDAO {

    /**
     * Gibt eine String-Liste der aktuell verfügbaren Sprachen zurück.
     *
     * @return Liste der verfügbaren Sprachen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getAll() throws DatabaseException {
        return super.getAll( "orcasoft.sprache" );
    }

    /**
     * Gibt die Bezeichnung einer Sprache anhand ihres
     * Primärschlüssels wieder.
     *
     * @param idBranche ID_com der Sprache.
     * @return Bezeichnung der Sprache.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public String getBezeichnung( int idBranche ) throws DatabaseException {
        return super.getBezeichnung( "sprache", idBranche );
    }

    /**
     * Gibt ein ResultSet zurück, dass die Sprachen,
     * eines Studenten enthält.
     *
     * @param idStudent ID_com des Studenten.
     * @return ResultSet.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getSprachenOfStudent( int idStudent ) throws DatabaseException {
        ResultSet queryData = executeSelectionQuery( getPreparedStatement("SELECT bezeichnung FROM orcasoft.sprache sp, orcasoft.student_to_sprache sts WHERE sp.ID_Sprache = sts.FK_Sprache AND FK_Student = ?"), new Object[]{idStudent} );
        List<String> result = new ArrayList<>();

        try {
            while ( queryData.next() ) result.add( queryData.getString( 1 ) );
        } catch ( SQLException e1 ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }

        return result;

    }

    /**
     * Aktualisiert die Sprachen eines Studenten.
     *
     * @param idStudent ID_com des Studenten.
     * @param spracheIDs IDs der ausgewählten Sprachen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateSprachenOfStudent( int idStudent, List<String> spracheIDs ) throws DatabaseException {
        String query = "DELETE FROM orcasoft.student_to_sprache WHERE fk_student = ?; ";
        List<Object> idsForPs  = new ArrayList<>();
        idsForPs .add( idStudent );

        for ( String spracheID : spracheIDs ) {
            query = query.concat("INSERT INTO orcasoft.student_to_sprache (fk_student, fk_sprache) VALUES (?, ?); ");
            idsForPs .add( idStudent );
            idsForPs .add( Integer.parseInt( spracheID ) );
        }

        executeInsertUpdateDeleteQuery( getPreparedStatement(query), idsForPs .toArray() );
    }

    /**
     * Löscht die Sprachen bzw. die Verknüpfung zu dem Studenten aus der DB.
     *
     * @param idStudent ID des Studenten.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteSprachenOfStudent( int idStudent ) throws DatabaseException {
        executeInsertUpdateDeleteQuery( getPreparedStatement( "DELETE FROM orcasoft.student_to_sprache WHERE fk_student = ?"), new Object[]{idStudent} );
    }

}
