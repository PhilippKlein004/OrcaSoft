package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

// Klasse die Standard-Operationen zentralisiert

@Component
public abstract class UserControl {

    @Autowired
    private UserDAO userDAO;

    /**
     * Überprüft, ob eine übergebene Mailadresse bereits in der
     * Datenbank existiert.
     *
     * @param mailAddress zu überprüfende Mailadresse.
     * @throws DatabaseException Mail existiert bereits? Fehler im SQL-Code? Verbindung zur Datenbank?
     */

    public void doesMailExist( String mailAddress ) throws DatabaseException {

        try {
            if ( userDAO.doesUserExist( mailAddress ) ) throw new DatabaseException( DatabaseException.MAIL_ALREADY_EXISTS );
        } catch ( DatabaseException e ) {
            throw new DatabaseException( "Es konnte keine Verbindung zur Datenbank aufgebaut werden!" );
        } catch ( SQLException e ) {
            throw new DatabaseException( "Es gibt einen Fehler im SQL Code!" );
        }

    }

    /**
     * Überprüft, ob ein übergebener Unternehmensname bereits existiert.
     *
     * @param companyName zu überprüfender Unternehmensname.
     * @throws DatabaseException Mail existiert bereits? Fehler im SQL-Code? Verbindung zur Datenbank?
     */

    public void doesCompanyNameExist( String companyName ) throws DatabaseException {

        try {
            if ( userDAO.doesCompanyNameExist( companyName ) ) throw new DatabaseException( DatabaseException.USERNAME_ALREADY_EXISTS );
        } catch ( DatabaseException e ) {
            throw new DatabaseException( "Es konnte keine Verbindung zur Datenbank aufgebaut werden!" );
        } catch ( SQLException e ) {
            throw new DatabaseException( "Es gibt einen Fehler im SQL Code!" );
        }

    }

}
