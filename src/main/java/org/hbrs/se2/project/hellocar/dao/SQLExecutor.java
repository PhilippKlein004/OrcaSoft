package org.hbrs.se2.project.hellocar.dao;


import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOForInsert;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;
import org.hbrs.se2.project.hellocar.db.JDBCConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SQLExecutor {
    private static final Logger logger = LoggerFactory.getLogger(SQLExecutor.class);


    /**
     * Führt INSERT, UPDATE oder DELETE-Operationen in der Datenbank aus.
     *
     * @param query PreparedStatement mit der SQL-Query.
     * @param values Werte, die für die Platzhalter in der PS eingesetzt werden sollen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void executeInsertUpdateDeleteQuery( PreparedStatement query, Object[] values ) throws DatabaseException {

        try {

            openDatabaseConnection();
            fillPreparedStatement( query, values ).executeUpdate();

        } catch ( SQLException ex ) {
            logger.error(ex.getMessage());
            throw  new DatabaseException( DatabaseException.SQL_ERROR );
        } catch ( NullPointerException ex ) {
            throw new DatabaseException( DatabaseException.NO_CONNECTION );
        } catch ( ClassCastException ex ) {
            logger.error(ex.getMessage());
        } finally {
            closeDatabaseConnection();
        }

    }

    /**
     * Führt SELECT-Operationen in der Datenbank aus.
     *
     * @param query PreparedStatement mit der SQL-Query.
     * @param values Werte, die für die Platzhalter in der PS eingesetzt werden sollen.
     * @return ResultSet (Ergebnismenge) mit den Werten/Ergebnissen aus der SELECT-Abfrage.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet executeSelectionQuery( PreparedStatement query, Object[] values ) throws DatabaseException {

        try {

            openDatabaseConnection();
            return fillPreparedStatement( query, values ).executeQuery();

        } catch ( SQLException ex ) {
            logger.error("Error occurred: {}", ex.getMessage());
            throw  new DatabaseException( DatabaseException.SQL_ERROR );
        } catch ( NullPointerException ex ) {
            throw new DatabaseException( DatabaseException.NO_CONNECTION );
        } catch ( ClassCastException ex ) {
            logger.error(ex.getMessage());
        } finally {
            closeDatabaseConnection();
        }

        return null;

    }

    /**
     * Befüllt ein PreparedStatement mit den übergebenen Daten in Form eines Arrays.
     *
     * @param query PreparedStatement, welches befüllt werden soll.
     * @param values Werte für die Platzhalter (?)
     * @return befülltes PreparedStatement, dass zur Ausführung bereitsteht.
     */

    private PreparedStatement fillPreparedStatement( PreparedStatement query, Object[] values) {

        try {

            if ( values == null ) return query;

            for ( int index = 0 ; index < values.length ; ++index ) {

                if ( values[index] instanceof String ) query.setString( index + 1, (String) values[index] );

                else if ( values[index] instanceof Integer ) query.setInt( index + 1, (Integer) values[index] );

                else if ( values[index] instanceof File ) {
                    File file = (File) values[index];
                    query.setBinaryStream( index + 1, new FileInputStream(file), (int) file.length() );
                }

                else if ( values[index] instanceof Double ) query.setDouble( index + 1, (Double) values[index] );

                else if ( values[index] instanceof java.util.Date ) query.setDate( index + 1, new java.sql.Date( (((java.util.Date) values[index]).getTime())) );

                // Sonderfälle wegen INSERT

                else if ( values[index] instanceof AusschreibungDTOForInsert ) {
                    insertAusschreibungDTOForInsertIntoSQLQuery( query, (AusschreibungDTOForInsert) values[index] );
                    break;
                }

                if (  values[index] instanceof BewerbungDTOImpl ) {
                    insertBewerbungDTOIntoSQLQueryForInsert( query, (BewerbungDTOImpl) values[index] );
                    break;
                }

            }
        } catch (SQLException ex) {
            logger.error("[SQLExecutor] Fehler beim Setzen der Attribute! " + ex.getMessage());
        } catch (FileNotFoundException ex) {
            logger.error("[SQLExecutor] Datei konnte nicht gefunden werden! " + ex.getMessage());
        }
        return query;
    }
    /**
     * Fügt eine BewerbungDTO in die SQL Query ein.
     *
     * @param query SQL-Query.
     * @param bewerbungDTO Instanz der Bewerbung.
     */

    private void insertBewerbungDTOIntoSQLQueryForInsert( PreparedStatement query, BewerbungDTOImpl bewerbungDTO ) throws SQLException, FileNotFoundException  {

        // "INSERT INTO orcasoft.bewerbung (datum, inhalt, status, motivationsschreiben, lebenslauf) VALUES (?,?,?,?,?); "
        query.setDate( 1, new java.sql.Date( bewerbungDTO.getDate().getTime() ) );
        query.setString( 2, bewerbungDTO.getInhalt() );
        query.setInt( 3, bewerbungDTO.getStatus() );
        query.setBinaryStream( 4, new FileInputStream(bewerbungDTO.getMotivationsschreibenFile()), (int) bewerbungDTO.getMotivationsschreibenFile().length() );
        query.setBoolean( 5, bewerbungDTO.isLebenslaufAttached() );

        // "INSERT INTO orcasoft.bewerben VALUES (?,?, (SELECT MAX(ID_Bewerbung) FROM orcasoft.bewerbung) );"
        query.setInt( 6, bewerbungDTO.getStudentID() );
        query.setInt( 7, bewerbungDTO.getAusschreibungID() );

    }

    /**
     * Fügt ein AusschreibungDTO welches explizit zum INSERT in
     * die Datenbank vorgesehen ist, in die SQL Query ein.
     *
     * @param query SQL-Query.
     * @param ausschreibung Instanz der Ausschreibung.
     */

    private void insertAusschreibungDTOForInsertIntoSQLQuery( PreparedStatement query, AusschreibungDTOForInsert ausschreibung ) throws SQLException {

        query.setInt(1, Integer.parseInt( ausschreibung.getBeschaeftigung() ));
        query.setInt(2, Integer.parseInt( ausschreibung.getCompany() ));
        query.setString(3, ausschreibung.getBeschreibung());
        query.setDate(4, new java.sql.Date( ((ausschreibung.getStartDatum()).getTime())));
        query.setDate(5, new java.sql.Date( ((ausschreibung.getEndDatum()).getTime())));
        query.setInt(6, ausschreibung.getVerguetung());
        query.setString(7, ausschreibung.getTitel());

        List<String> skills = ausschreibung.getSkills();
        int querySkillIndex = 8;

        for ( String skill : skills ) query.setInt( querySkillIndex++, Integer.parseInt(skill) );

    }

    /**
     * Öffnet die JDBC-Verbindung zur Postgressql-Datenbank.
     *
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private void openDatabaseConnection() throws DatabaseException {
        JDBCConnection.getInstance().getStatement();
    }

    /**
     * Schließt die JDBC-Verbindung zur Postgressql-Datenbank.
     *
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private void closeDatabaseConnection() throws DatabaseException {
        JDBCConnection.getInstance().closeConnection();
    }

    /**
     * Gibt zu einer gegebenen SQL-Query ein
     * Prepared-Statement zurück.
     *
     * @param query normales SQL-Query.
     * @return Prepared SQL-Query.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public PreparedStatement getPreparedStatement( final String query ) throws DatabaseException {
        return JDBCConnection.getInstance().getPreparedStatement( query );
    }

}
