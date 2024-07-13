package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// srsbsb = Studiengang, Rolle, Sprache, Branche, Skill, Beschäftigung

@Component
public abstract class srsbsbDAO extends SQLExecutor {

    Statement statement = null;

    /**
     * Gibt alle Objekte (Bezeichnungen) zurück. Da sich die Tabellen: Studiengang, Rolle, Sprache, Branche, Skill und Beschäftigung
     * diese Eigenschaften teilen, sind diese in dieser Oberklasse zusammengefasst.
     *
     * @param table In welcher Tabelle soll gesucht werden?
     * @return String-Liste an Objekten.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getAll( final String table ) throws DatabaseException {

        ResultSet set = null;
        List<String> rolls = new ArrayList<>();

        try {

            set = executeSelectionQuery( getPreparedStatement("SELECT bezeichnung FROM " + table), null );

            while ( set.next() ) {
                rolls.add( set.getString( 1 ) );
            }

            return rolls;

        } catch ( SQLException ex ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        } catch ( NullPointerException ex ) {
            throw  new DatabaseException( DatabaseException.NO_CONNECTION );
        }

    }

    /**
     * Gibt die Bezeichnung aus einer gegebenen Tabelle wieder mit dem
     * benötigten Fremdschlüssel.
     *
     * @param table der Name der Tabelle (orcasoft.user).
     * @param ForeignKey Fremdschlüssel der Bezeichnung.
     * @return gibt die Bezeichnung zurück.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public String getBezeichnung( final String table, final int ForeignKey ) throws DatabaseException {

        ResultSet set = null;

        try {

            set = executeSelectionQuery( getPreparedStatement( "SELECT bezeichnung FROM orcasoft." + table + " WHERE ID_" + table + " = ?;"), new Object[]{ForeignKey} );

            if ( set.next() ) return set.getString( "bezeichnung" );

            return "NULL";

        } catch ( SQLException ex ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        } catch ( NullPointerException ex ) {
            throw new DatabaseException( DatabaseException.NO_CONNECTION );
        }

    }

}
