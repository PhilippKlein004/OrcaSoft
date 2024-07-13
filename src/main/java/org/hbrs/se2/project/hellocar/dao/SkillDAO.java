package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SkillDAO extends srsbsbDAO {

    /**
     * Gibt eine String-Liste der aktuell verfügbaren Skills zurück.
     *
     * @return Liste der verfügbaren Skills.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getAll() throws DatabaseException {
        return super.getAll( "orcasoft.skill" );
    }

    /**
     * Gibt die Bezeichnung eines Skills anhand seines
     * Primärschlüssels wieder.
     *
     * @param idBranche ID_com des Skills.
     * @return Bezeichnung der Skills.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public String getBezeichnung( int idBranche ) throws DatabaseException {
        return super.getBezeichnung( "skill", idBranche );
    }

    /**
     * Gibt eine Liste zurück, die die Skills,
     * eines Studenten enthält.
     *
     * @param idStudent ID_com des Studenten.
     * @return ResultSet.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getSkillsOfStudent( int idStudent ) throws DatabaseException {
        ResultSet queryData = executeSelectionQuery( getPreparedStatement("SELECT bezeichnung FROM orcasoft.skill s, orcasoft.student_to_skill sk WHERE s.ID_Skill = sk.FK_Skill AND FK_Student = ?; "), new Object[]{idStudent} );
        return extractSkillsFromResultSet( queryData );
    }

    /**
     * Gibt eine Liste zurück, die die Skills,
     * einer Ausschreibung enthält.
     *
     * @param idAusschreibung ID_com des Studenten.
     * @return ResultSet.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getSkillsOfAusschreibung( int idAusschreibung ) throws DatabaseException {
        ResultSet queryData =executeSelectionQuery( getPreparedStatement("SELECT bezeichnung FROM orcasoft.skill s, orcasoft.ausschreibung_to_skill sk WHERE s.ID_Skill = sk.FK_Skill AND FK_Ausschreibung = ?; "), new Object[]{idAusschreibung} );
        return extractSkillsFromResultSet( queryData );
    }

    /**
     *
     * @param queryData Daten aus einer Datenbankabfrage.
     * @return String-Liste mit den Ergebnissen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private List<String> extractSkillsFromResultSet( ResultSet queryData ) throws DatabaseException {
        List<String> result = new ArrayList<>();

        try {
            while ( queryData.next() ) {
                result.add( queryData.getString( 1 ) );
            }
        } catch ( SQLException e1 ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }

        return result;

    }

    /**
     * Aktualisiert die Skills eines Studenten.
     *
     * @param idStudent ID_com des Studenten.
     * @param skillIDs IDs der ausgewählten Skills.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateSkillsOfStudent( int idStudent, List<String> skillIDs ) throws DatabaseException {
        String query = "DELETE FROM orcasoft.student_to_skill WHERE fk_student = ?; ";
        List<Object> idsForPs  = new ArrayList<>();
        idsForPs.add( idStudent );

        for ( String skillID : skillIDs ) {
            query = query.concat("INSERT INTO orcasoft.student_to_skill (fk_student, fk_skill) VALUES (?, ?); ");
            idsForPs.add(idStudent);
            idsForPs.add(Integer.parseInt(skillID));
        }

        executeInsertUpdateDeleteQuery( getPreparedStatement(query), idsForPs.toArray() );
    }

    /**
     * Aktualisiert die Skills einer Ausschreibung.
     *
     * @param idAusschreibung ID_com der Ausschreibung.
     * @param skillIDs IDs der ausgewählten Skills.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateSkillsOfAusschreibung( int idAusschreibung, List<String> skillIDs ) throws DatabaseException {
        String query = "DELETE FROM orcasoft.ausschreibung_to_skill WHERE fk_ausschreibung = ?; ";
        List<Object> idsForPs = new ArrayList<>();
        idsForPs.add( idAusschreibung );

        for ( String skillID : skillIDs ) {
            query = query.concat("INSERT INTO orcasoft.ausschreibung_to_skill (fk_ausschreibung, fk_skill) VALUES (?, ?); ");
            idsForPs.add(idAusschreibung);
            idsForPs.add(Integer.parseInt(skillID));
        }

        executeInsertUpdateDeleteQuery( getPreparedStatement(query), idsForPs.toArray() );
    }

    /**
     *
     * @param idAusschreibung ID_com der Ausschreibung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteSkillsOfAusschreibung( int idAusschreibung ) throws DatabaseException {
        final String query = "DELETE FROM orcasoft.ausschreibung_to_skill WHERE fk_ausschreibung = ?";
        executeInsertUpdateDeleteQuery( getPreparedStatement(query), new Object[]{idAusschreibung} );
    }

    /**
     * Löscht die Skills bzw. die Verknüpfung zu dem Studenten aus der DB.
     *
     * @param idStudent ID des Studenten.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteSkillsOfStudent( int idStudent ) throws DatabaseException {
        executeInsertUpdateDeleteQuery( getPreparedStatement( "DELETE FROM orcasoft.student_to_skill WHERE fk_student = ?"), new Object[]{idStudent} );
    }

}
