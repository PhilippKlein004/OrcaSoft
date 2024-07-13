package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dtos.AusschreibungDTO;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AusschreibungDAO extends SQLExecutor {

    private final SkillDAO skillDAO = new SkillDAO();

    /**
     * Gibt einen Wahrheitswert zurück, ob eine
     * Ausschreibung mit diesem Titel von dem
     * Unternehmen
     *
     * @param title Titel der Ausschreibung.
     * @param idCompany ID_com des Unternehmens.
     * @return Wahrheitswert über die Existenz.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public boolean doesAusschreibungExist( String title, int idCompany ) throws DatabaseException {
        final String query = "SELECT * FROM orcasoft.ausschreibung WHERE FK_Company = ? AND titel = ?";
        try {
            return executeSelectionQuery( getPreparedStatement(query), new Object[] {idCompany, title} ).next();
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }
    }

    /**
     * Speichert eine Ausschreibung in der Datenbank ab.
     *
     * @param ausschreibungDTO Ausschreibungsinstanz.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void saveAusschreibung( AusschreibungDTO ausschreibungDTO )  throws DatabaseException {
        String query = "INSERT INTO orcasoft.ausschreibung (fk_beschaeftigung, fk_company, beschreibung, zeitr_start, zeitr_ende, verguetung, titel) VALUES (?, ?, ?, ?, ?, ?, ?);";
        for ( String skill : ausschreibungDTO.getSkills() ) query = query.concat(" INSERT INTO orcasoft.ausschreibung_to_skill VALUES ((SELECT MAX(ID_Ausschreibung) FROM orcasoft.ausschreibung), ?);");
        executeInsertUpdateDeleteQuery( getPreparedStatement(query), new Object[] {ausschreibungDTO} );
    }

    /**
     * Diese Methode gibt die nächstmögliche ID in der Tabelle wieder, die, falls
     * eine neue Ausschreibung erstellt wird, belegt werden würde.
     *
     * @return verfügbare ID in der DB.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public int getNextPossibleID() throws DatabaseException {
        final String query = "SELECT MAX(ID_Ausschreibung) FROM orcasoft.ausschreibung;";
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement(query), null );
        try {
            if ( queryResult.next() ) return queryResult.getInt(1);
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }
        return -1;
    }

    /**
     * Gibt alle Ausschreibungen eines Unternehmens in
     * Form einer Menge zurück.
     *
     * @param idCompany ID_com des Unternehmens.
     * @return ResultSet mit den Ausschreibungen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getAllAusschreibungenByCompany( int idCompany ) throws DatabaseException {
        final String query = "SELECT DISTINCT id_ausschreibung, titel FROM orcasoft.ausschreibung WHERE fk_company = ?";
        return executeSelectionQuery( getPreparedStatement(query), new Object[]{idCompany} );
    }

    /**
     * Gibt alle IDs der Ausschreibung wieder, die zu einem gegebenen Unternehmen gehören.
     *
     * @param idCompany ID des Unternehmens.
     * @return Liste der IDs.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<Integer> getAusschreibungIDsByCompany( int idCompany ) throws DatabaseException {

        ResultSet data = executeSelectionQuery( getPreparedStatement("SELECT ID_Ausschreibung FROM orcasoft.ausschreibung WHERE FK_Company = ?"), new Object[]{idCompany} );
        List<Integer> IDs = new ArrayList<>();

        try {
            while ( data.next() ) IDs.add( data.getInt("ID_Ausschreibung") );
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR + e.getMessage() );
        }

        return IDs;

    }

    /**
     * Gibt eine Ausschreibung in Form eines ResultSet
     * zurück, gemäß ihrer ID_com.
     *
     * @param idAusschreibung ID_com der Ausschreibung.
     * @return ResultSet, das die Ausschreibung enthält.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getAusschreibungByID( int idAusschreibung ) throws DatabaseException {
        final String query = "SELECT * FROM orcasoft.ausschreibung WHERE id_ausschreibung = ?";
        return executeSelectionQuery( getPreparedStatement(query), new Object[] {idAusschreibung} );
    }

    /**
     * Aktualisiert eine Ausschreibung in der Datenbank.
     *
     * @param ausschreibungDTO Ausschreibungsinstanz.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateAusschreibung( AusschreibungDTO ausschreibungDTO ) throws DatabaseException {
        skillDAO.updateSkillsOfAusschreibung( ausschreibungDTO.getId(), ausschreibungDTO.getSkills() );
        final String query = "UPDATE orcasoft.ausschreibung SET fk_beschaeftigung = ?, fk_company = ?, beschreibung = ?, zeitr_start = ?, zeitr_ende = ?, verguetung = ?, titel = ? WHERE id_ausschreibung = ?";
        executeInsertUpdateDeleteQuery( getPreparedStatement(query), new Object[] {
                                                                                Integer.parseInt(ausschreibungDTO.getBeschaeftigung()),
                                                                                Integer.parseInt(ausschreibungDTO.getCompany()),
                                                                                ausschreibungDTO.getBeschreibung(),
                                                                                ausschreibungDTO.getStartDatum(),
                                                                                ausschreibungDTO.getEndDatum(),
                                                                                ausschreibungDTO.getVerguetung(),
                                                                                ausschreibungDTO.getTitel(),
                                                                                ausschreibungDTO.getId()
        });
    }

    /**
     * Löscht eine Ausschreibung in der Datenbank.
     *
     * @param ID_Ausschreibung ID_com der Ausschreibung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteAusschreibung( int ID_Ausschreibung ) throws DatabaseException {
        final String query = "DELETE FROM orcasoft.ausschreibung WHERE id_ausschreibung = ?; ";
        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), new Object[]{ID_Ausschreibung} );
    }

    /**
     * Löscht alle Ausschreibungen von einem Unternehmen aus der DB.
     *
     * @param ID_Company ID des Unternehmens.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteAllByCompany( int ID_Company ) throws DatabaseException {
        final String query = "SELECT orcasoft.delete_all_ausschreibungen_by_company(?);";
        executeSelectionQuery( getPreparedStatement( query ), new Object[]{ID_Company} );
    }

    /**
     * Gibt alle Ausschreibungen wieder, die mit dem
     * übergebenen Titel in der Datenbank gefunden wurden.
     *
     * @param title Titel der Ausschreibung (nicht case-sensitive!)
     * @return ResultSet mit den Ausschreibungen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getAusschreibungenByTitle( String title ) throws DatabaseException {
        // record AusstellungSearch(int ID_com, String titel, String companyName, StreamResource companyLogo)
        final String query =
                        "SELECT asr.id_ausschreibung AS id_aus, com.bezeichnung AS companyName, us.profile_picture AS companyLogo, com.ID_Company AS id_com, asr.titel AS titel " +
                        "FROM orcasoft.company com " +
                        "JOIN orcasoft.User us ON us.ID_User = com.ID_Company " +
                        "JOIN orcasoft.ausschreibung asr ON asr.FK_Company = com.ID_Company " +
                        "WHERE ? ILIKE '%' || com.bezeichnung || '%'" +
                        "OR asr.titel ILIKE ? ";
        final String ausschreibungTitle = "%" + title + "%";
        return executeSelectionQuery( getPreparedStatement( query ), new Object[]{ausschreibungTitle, ausschreibungTitle} );
    }

}
