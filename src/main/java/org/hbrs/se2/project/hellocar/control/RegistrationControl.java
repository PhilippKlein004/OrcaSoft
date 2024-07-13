package org.hbrs.se2.project.hellocar.control;

import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.exception.PasswordException;
import org.hbrs.se2.project.hellocar.dao.BrancheDAO;
import org.hbrs.se2.project.hellocar.dao.StudiengangDAO;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegistrationControl extends UserControl {

	@Autowired
	UserDAO userDAO = new UserDAO();

    /**
     * Registriert einen Studenten in der Datenbank.
     * Diese Methode speichert die übergebenen Studenteninformationen in der Datenbank.
     * Bei Fehlern während des Speichervorgangs wird eine DatabaseException ausgelöst.
     *
     * @param dto Student-Objekt mit den zu speichernden Daten
     * @throws DatabaseException Datenbankfehler
     */

	public void registerStudentUser( StudentDTO dto ) throws DatabaseException {
		try {
			userDAO.saveStudentUser( dto );
		} catch ( DatabaseException e ) {
			DatabaseException exp = new DatabaseException(e.getMessage());
			throw exp;
		}
	}

    /**
     * Registriert ein Unternehmen in der Datenbank.
     * Ähnlich wie `registerStudentUser`, speichert diese Methode Unternehmensdaten.
     * Bei Datenbankfehlern wird eine spezifische Exception geworfen.
     *
     * @param dto Unternehmens-Objekt mit den zu speichernden Daten
     * @throws DatabaseException Datenbankfehler
     */

	public void registerCompanyUser( CompanyDTO dto ) throws DatabaseException {
		try {
			userDAO.saveCompany( dto );
		} catch ( DatabaseException e ) {
			DatabaseException exp = new DatabaseException(e.getMessage());
			throw exp;
		}

	}

    /**
     * Überprüft das Passwort auf bestimmte Sicherheitskriterien.
     * Diese Methode stellt sicher, dass das Passwort eine Mindestlänge hat, Zahlen, Buchstaben und Sonderzeichen enthält.
     * Bei Nichterfüllung wird eine PasswordException mit einer spezifischen Fehlermeldung ausgelöst.
     *
     * @param password übergebenes Passwort.
     * @throws PasswordException Fehler beim Passwort.
     */

	public void checkPassword( String password ) throws PasswordException {

		if ( password.length() < 6 ) throw new PasswordException( "Passwort ist zu kurz! (" + password.length() + "/6)" );
		if ( password.length() > 32 ) throw new PasswordException( "Passwort ist zu lang!" );

		Pattern letter = Pattern.compile("\\p{L}\\p{M}*+");
		Pattern digit = Pattern.compile("\\d");
		Pattern special = Pattern.compile ("[!@#$%&*]");

		Matcher hasLetter = letter.matcher(password);
		Matcher hasDigit = digit.matcher(password);
		Matcher hasSpecial = special.matcher(password);

		if ( !hasLetter.find() ) throw new PasswordException( "Passwort muss ein Buchstaben enthalten!" );
		if ( !hasDigit.find() ) throw new PasswordException( "Passwort muss eine Zahl enthalten!" );
		if ( !hasSpecial.find() ) throw new PasswordException( "Passwort muss min. ein Sonderzeichen (!@#$%&*) enthalten!" );

	}

    /**
     * Überprüft, ob zwei Passwörter identisch sind.
     * Diese Methode wirft eine PasswordException, wenn die Passwörter nicht übereinstimmen.
     *
     * @param password1 Das erste Passwort zur Überprüfung.
     * @param password2 Das zweite Passwort zur Überprüfung.
     * @throws PasswordException Wird ausgelöst, wenn die Passwörter nicht übereinstimmen.
     */

	public void doPasswordsMatch( String password1, String password2 ) throws PasswordException {
		if ( !password1.equals( password2 ) ) throw new PasswordException();
	}

    /**
     * Ruft eine Liste aller verfügbaren Studiengänge aus der Datenbank ab.
     * Diese Methode kommuniziert mit der Datenbank, um eine Liste aller Studiengänge zu erhalten.
     * Bei einem Fehler im Datenbankzugriff wird eine DatabaseException ausgelöst.
     *
     * @return Eine Liste der Namen aller Studiengänge.
     * @throws DatabaseException Wird ausgelöst bei Fehlern im Datenbankzugriff.
     */

	public List<String> getAllStudiengaenge() throws DatabaseException {

		try {
			return new StudiengangDAO().getAll();
		} catch ( DatabaseException e ) {
			throw new DatabaseException( e.getMessage() );
		}

	}

    /**
     * Ruft eine Liste aller Branchen aus der Datenbank ab.
     * Diese Methode interagiert mit der Datenbank, um eine Liste aller Branchen zu erhalten.
     * Bei einem Fehler im Datenbankzugriff wird eine DatabaseException ausgelöst.
     *
     * @return Eine Liste der Namen aller Branchen.
     * @throws DatabaseException Wird ausgelöst bei Fehlern im Datenbankzugriff.
     */

	public List<String> getAllBranche() throws DatabaseException {

		try {
			return new BrancheDAO().getAll();
		} catch ( DatabaseException e ) {
			throw new DatabaseException( e.getMessage() );
		}

	}

}
