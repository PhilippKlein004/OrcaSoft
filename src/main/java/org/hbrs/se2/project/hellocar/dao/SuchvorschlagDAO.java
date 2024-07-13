package org.hbrs.se2.project.hellocar.dao;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;

import java.sql.ResultSet;

public class SuchvorschlagDAO extends SQLExecutor {

    /**
     * Gibt zu einem Suchbegriff den/die passenden Suchvorschläge
     * aus der Tabelle (DB) zurück. Je nachdem, wie viele Treffer
     * es in der Tabelle gibt (max. 6).
     *
     * @param searchQuery Übergebener Inhalt aus der Suchleiste.
     * @return ResultSet mit den passenden Suchvorschlägen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getSuchvorschlaegeThatAreSimilarTo( String searchQuery ) throws DatabaseException {
        final String query = "SELECT DISTINCT bezeichnung FROM orcasoft.suchvorschlag WHERE bezeichnung ILIKE ? LIMIT 6";
        return executeSelectionQuery( getPreparedStatement(query), new Object[]{ "%" + searchQuery + "%" } );
    }

}
