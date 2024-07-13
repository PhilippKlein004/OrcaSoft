package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class BewerbungDAO extends SQLExecutor {

    /**
     * Speichert eine Bewerbung in die Datenbank und verknüpft diese
     * direkt über die Tabelle 'bewerben' mit dem Studenten & Ausschreibung.
     *
     * @param bewerbung Instanz der Bewerbung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void saveBewerbung( final BewerbungDTO bewerbung ) throws DatabaseException {
        final String query =    "INSERT INTO orcasoft.bewerbung (datum, inhalt, status, motivationsschreiben, lebenslauf) VALUES (?,?,?,?,?); "
                                + "INSERT INTO orcasoft.bewerben VALUES (?,?, (SELECT MAX(ID_Bewerbung) FROM orcasoft.bewerbung) );";
        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), new Object[] { bewerbung } );
    }

    /**
     * Löscht eine Bewerbung aus der Datenbank.
     *
     * @param idBewerbung ID der Bewerbung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteBewerbung( int idBewerbung ) throws DatabaseException {
        final String query = "DELETE FROM orcasoft.bewerben WHERE FK_Bewerbung = ?; DELETE FROM orcasoft.bewerbung WHERE ID_Bewerbung = ?; ";
        executeInsertUpdateDeleteQuery( getPreparedStatement(query), new Object[]{idBewerbung, idBewerbung} );
    }

    /**
     * Gibt einen Wahrheitswert zurück, ob ein Student sich bereits auf
     * die Ausschreibung beworben hat. Dies wird durch die Tabelle 'bewerben' getan.
     *
     * @param idStudent ID des Studenten.
     * @param idAusschreibung ID der Ausschreibung.
     * @return Hat sich der Student bereits beworben?
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Query.
     */

    public boolean hasStudentAlreadyApplied( int idStudent, int idAusschreibung ) throws DatabaseException, SQLException {
        final String query = "SELECT * FROM orcasoft.bewerben WHERE FK_Student = ? AND FK_Ausschreibung = ?;";
        return executeSelectionQuery( getPreparedStatement( query ), new Object[] {idStudent, idAusschreibung } ).next();
    }

    /**
     * Gibt die IDs aller Bewerbungen zu einer bestimmten Ausschreibung zurück.
     *
     * @param idAusschreibung ID der Ausschreibung.
     * @return Liste an IDs.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<Integer> getIDsOfBewerbungenLinkedToAusschreibung( int idAusschreibung ) throws DatabaseException {
        final String query = "SELECT DISTINCT ID_Bewerbung FROM orcasoft.bewerbung WHERE ID_Bewerbung IN ( SELECT FK_Bewerbung FROM orcasoft.bewerben WHERE FK_Ausschreibung = ? );";
        ResultSet data = executeSelectionQuery( getPreparedStatement( query ), new Object[] {idAusschreibung} );
        return getIDsListFromResultSet( data );
    }

    /**
     * Gibt die IDs aller Bewerbungen zu einem bestimmten Studenten zurück.
     *
     * @param idStudent ID der Ausschreibung.
     * @return Liste an IDs.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private List<Integer> getIDsOfBewerbungenLinkedToStudent( int idStudent ) throws DatabaseException {
        final String query = "SELECT DISTINCT ID_Bewerbung FROM orcasoft.bewerbung WHERE ID_Bewerbung IN ( SELECT FK_Bewerbung FROM orcasoft.bewerben WHERE FK_Student = ? );";
        ResultSet data = executeSelectionQuery( getPreparedStatement( query ), new Object[] {idStudent} );
        return getIDsListFromResultSet( data );
    }

    /**
     *
     * @param data ResultSet mit IDs.
     * @return Liste mit den IDs.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private List<Integer> getIDsListFromResultSet( ResultSet data ) throws DatabaseException {

        List<Integer> ids  = new ArrayList<>();

        try {
            while ( data.next() ) ids .add( data.getInt("ID_Bewerbung") );
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR + e.getMessage() );
        }

        return ids ;

    }

    /**
     * Diese Methode löscht alle Bewerbungen einer angegebenen Ausschreibung.
     *
     * @param idAusschreibung ID der Ausschreibung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteBewerbungenOfAusschreibung( int idAusschreibung ) throws DatabaseException {
        List<Integer> bewerbungenIDs = getIDsOfBewerbungenLinkedToAusschreibung( idAusschreibung );
        for ( Integer id : bewerbungenIDs ) deleteBewerbung( id );
    }

    /**
     * Diese Methode löscht alle Bewerbungen eines angegebenen Studenten.
     *
     * @param idStudent ID der Ausschreibung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteAllBewerbungenOfStudent( int idStudent ) throws DatabaseException {
        List<Integer> bewerbungenIDs = getIDsOfBewerbungenLinkedToStudent( idStudent );
        for ( Integer id : bewerbungenIDs ) deleteBewerbung( id );
    }

    /**
     * Gibt alle Bewerbungen eines Studenten in Form
     * eines ResultSet zurück.
     *
     * @param idStudent ID des Studenten.
     * @return ResultSet mit den Daten.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getAllBewerbungenOfStudent( int idStudent ) throws DatabaseException {
        final String query =    "SELECT bew.ID_Bewerbung AS ID_Bewerbung, bew.datum AS datum, aus.titel as titel, bew.status AS status " +
                                "FROM orcasoft.bewerbung bew, orcasoft.bewerben bw, orcasoft.ausschreibung aus " +
                                "WHERE bew.ID_Bewerbung = bw.FK_Bewerbung " +
                                "AND bw.FK_Student = ? " +
                                "AND bw.FK_Ausschreibung = aus.ID_Ausschreibung;";
        return executeSelectionQuery( getPreparedStatement( query ), new Object[]{ idStudent } );
    }

    /**
     * Diese Methode gibt ein ResultSet wieder, welches
     * die Bewerbung aus der Datenbank mit der übergebenen ID
     * enthält, falls diese gefunden wurde.
     *
     * @param idBewerbung ID der Bewerbung.
     * @return ResultSet mit der Bewerbung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getBewerbungByID( int idBewerbung ) throws DatabaseException {
        final String query = "SELECT * FROM orcasoft.bewerbung WHERE ID_Bewerbung = ?";
        return executeSelectionQuery( getPreparedStatement(query), new Object[]{idBewerbung} );
    }

    /**
     * Aktualisiert den Status einer Bewerbung in der Datenbank.
     *
     * @param idBewerbung ID der Bewerbung.
     * @param newStatus Neuer Status der Bewerbung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateStatusOfBewerbung( int idBewerbung, int newStatus ) throws DatabaseException {
        final String query = "UPDATE orcasoft.bewerbung SET status = ? WHERE ID_Bewerbung = ?";
        executeInsertUpdateDeleteQuery( getPreparedStatement(query), new Object[]{newStatus, idBewerbung} );
    }

    /**
     * Überprüft, ob sich auf eine Ausschreibung beworben wurde bzw.
     * min. eine Bewerbung vorliegt.
     *
     * @param idAusschreibung ID der Ausschreibung.
     * @return ResultSet mit den IDs der Bewerbungen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getBewerbungenFromAusschreibung( int idAusschreibung ) throws DatabaseException {
        final String query =
                "SELECT b.datum AS datum, bew.FK_Bewerbung AS ID_Bewerbung, bew.FK_Student AS ID_Student, u.profile_picture AS profilepic, s.vorname AS vorname, s.nachname AS nachname " +
                        "FROM orcasoft.bewerben bew " +
                        "JOIN orcasoft.bewerbung b ON bew.FK_Bewerbung = b.ID_Bewerbung " +
                        "JOIN orcasoft.student s ON bew.FK_Student = s.ID_Student " +
                        "JOIN orcasoft.user u ON bew.FK_Student = u.ID_User " +
                        "WHERE bew.FK_Ausschreibung = ?";
        return executeSelectionQuery( getPreparedStatement( query ), new Object[]{ idAusschreibung } );
    }


    public int getNextPossibleID() throws DatabaseException {
        final String query = "SELECT MAX(ID_Bewerbung) FROM orcasoft.bewerbung;";
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement(query), null );
        try {
            if ( queryResult.next() ) return queryResult.getInt(1);
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }
        return -1;
    }
}
