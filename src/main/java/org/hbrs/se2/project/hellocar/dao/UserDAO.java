package org.hbrs.se2.project.hellocar.dao;

import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.control.AusschreibungControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.factories.UserFactory;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class UserDAO extends SQLExecutor {

    @Autowired
    private StudiengangDAO studiengangDAO = new StudiengangDAO();

    @Autowired
    private BrancheDAO brancheDAO = new BrancheDAO();

    @Autowired
    private SessionController sessionController = new SessionController();
    @Autowired
    private SkillDAO skillDAO = new SkillDAO();
    @Autowired
    private SpracheDAO spracheDAO = new SpracheDAO();

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);


    private final BewerbungDAO bewerbungDAO = new BewerbungDAO();
    private final AusschreibungDAO ausschreibungDAO = new AusschreibungDAO();

    @Autowired
    private AusschreibungControl ausschreibungControl;

    public UserDAO(StudiengangDAO studiengangDAO, BrancheDAO brancheDAO, SkillDAO skillDAO, SpracheDAO spracheDAO) {
        this.studiengangDAO = studiengangDAO;
        this.brancheDAO = brancheDAO;
        this.skillDAO = skillDAO;
        this.spracheDAO = spracheDAO;
    }

    public UserDAO() {
    }

    /**
     * Gibt eine userDTO-Instanz zurück, wenn ein Nutzer gefunden wurde, der
     * die gleiche E-Mail und Passwort besitzt.
     *
     * @param email E-Mail des Nutzers.
     * @param password Passwort des Nutzers.
     * @return UserDTO (Obertyp).
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public UserDTO findUserByUseridAndPassword( String email, String password ) throws DatabaseException, SQLException {

        final String query = " SELECT * "
                        + " FROM orcasoft.user u "
                        + " WHERE u.email = ?"
                        + " AND u.password = ?";


        return extractUserDTOFromResultSet( executeSelectionQuery( getPreparedStatement(query), new Object[] {email, password} ) );


    }

    /**
     * Diese Methode gibt den Lebenslauf eines Studenten in Form
     * eines ResultSet zurück, welches von den Methoden entsprechen
     * interpretiert wird.
     *
     * @param idstudent ID des Studenten.
     * @return ResultSet mit dem Lebenslauf.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public ResultSet getLebenslaufOfStudent( int idstudent ) throws DatabaseException {
        final String query = "SELECT lebenslauf FROM orcasoft.student WHERE ID_Student = ?";
        return executeSelectionQuery( getPreparedStatement(query), new Object[] {idstudent} );
    }

    /**
     * Gibt den Namen des Unternehmens zurück, welches
     * die übergebene ID_com besitzt.
     *
     * @param ID_Company ID_com des Unternehmens.
     * @return Name des Unternehmens.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public String getCompanyNameByID( int ID_Company ) throws DatabaseException {
        final String query = "SELECT bezeichnung FROM orcasoft.company WHERE id_company = ?";
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement(query), new Object[]{ID_Company} );
        try {
            if ( queryResult.next() ) return queryResult.getString("bezeichnung");
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }
        return "Unknown Company";
    }

    /**
     * Ermittelt, ob die übergebene Mail-Adresse bereits
     * in der Datenbank existiert.
     *
     * @param email E-Mail des Nutzers.
     * @return Wahrheitswert (boolean)
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public boolean doesUserExist( String email ) throws DatabaseException, SQLException {
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement("SELECT * FROM orcasoft.user WHERE email = ?; "), new Object[]{email} );
        return queryResult.next();
    }

    /**
     * Ermittelt, ob ein übergebener Unternehmensname bereits
     * in der Datenbank existiert.
     *
     * @param companyName Name des Unternehmens.
     * @return Wahrheitswert (boolean)
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public boolean doesCompanyNameExist( String companyName ) throws DatabaseException, SQLException {
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement("SELECT * FROM orcasoft.company WHERE bezeichnung = ?; "), new Object[]{companyName} );
        return queryResult.next();
    }

    /**
     * Ermittelt, ob eine übergebene Matrikelnummer bereits
     * in der Datenbank existiert.
     *
     * @param matrikelnummer Matrikelnummer des Studenten.
     * @return Wahrheitswert (boolean)
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public boolean doesMatrikelnummerExist( int matrikelnummer ) throws DatabaseException, SQLException {
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement("SELECT * FROM orcasoft.student WHERE matrikelnummer = ?; "), new Object[]{matrikelnummer} );
        return queryResult.next();
    }

    /**
     * Überprüft, ob die übergebene ID_com eines Nutzers einem Studenten
     * oder einem Unternehmen gehört.
     *
     * @param id ID_com des Nutzers.
     * @return Wahrheitswert (boolean)
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public boolean isUserStudent( int id ) throws DatabaseException, SQLException {
        ResultSet queryResult = executeSelectionQuery( getPreparedStatement("SELECT ID_Student FROM orcasoft.student WHERE ID_Student = ?; "), new Object[]{id} );
        return queryResult.next();
    }

    /**
     * Speichert den Studenten in die Datenbank ein.
     *
     * @param user Student der gespeichert werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void saveStudentUser( StudentDTO user ) throws DatabaseException {
        logger.info("[Registration] Studenten in die Datenbank einspeichern...");

        final String query = "INSERT INTO orcasoft.user (email, password) VALUES (?, ?); INSERT INTO orcasoft.student (id_student, fk_studiengang) VALUES ((SELECT MAX(ID_User) FROM orcasoft.user), ?);";

        List<Object> psPlaceholders = new ArrayList<>();
        psPlaceholders.add( user.getEmail() );
        psPlaceholders.add( user.getPassword() );
        psPlaceholders.add( Integer.parseInt(user.getStudiengang()) );

        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), psPlaceholders.toArray() );

        logger.info("[Registration] SQL-Skript erfolgreich ausgeführt!");
        logger.info("[Registration] Student wurde erfolgreich eingespeichert!");

    }

    /**
     * Speichert das Unternehmen in die Datenbank ein.
     *
     * @param user Unternehmen das gespeichert werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void saveCompany( CompanyDTO user ) throws DatabaseException {
        logger.info("[Registration] Student wurde erfolgreich eingespeichert!");

        final String query = "INSERT INTO orcasoft.user (email, password) VALUES (?, ?); INSERT INTO orcasoft.company (id_company, fk_branche, bezeichnung) VALUES ((SELECT MAX(ID_User) FROM orcasoft.user), ?, ?);";

        List<Object> psPlaceholders = new ArrayList<>();
        psPlaceholders.add( user.getEmail() );
        psPlaceholders.add( user.getPassword() );
        psPlaceholders.add( Integer.parseInt(user.getBranche()) );
        psPlaceholders.add( user.getBezeichnung() );

        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), psPlaceholders.toArray() );

        logger.info("[Registration] SQL-Skript erfolgreich ausgeführt!");
        logger.info("[Registration] Student wurde erfolgreich eingespeichert!");

    }

    /**
     * Aktualisiert einen normalen Nutzer.
     *
     * @param user userDTO bzw. Nutzer der aktualisiert werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateUser( UserDTO user ) throws DatabaseException {
        final String query = "UPDATE orcasoft.user SET email = ?, strasse = ?, ort = ?, plz = ?, haus_nr = ? WHERE ID_User = ?; ";
        if ( user instanceof StudentDTO ) {
            updateStudentUser( (StudentDTO) user );
        } else if ( user instanceof CompanyDTO ) {
            updateCompanyUser( (CompanyDTO) user );
        }
        List<Object> psPlaceholders = new ArrayList<>();
        psPlaceholders.add( user.getEmail() );
        psPlaceholders.add( user.getStrasse() );
        psPlaceholders.add( user.getOrt() );
        psPlaceholders.add( user.getPLZ() );
        psPlaceholders.add( user.getHausnummer() );
        psPlaceholders.add( user.getId() );

        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), psPlaceholders .toArray() );


    }

    /**
     * Lädt das übergebene Profilbild in die Datenbank hoch.
     *
     * @param userID ID_com des Nutzers.
     * @param file Profilbild-Datei (PNG)
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateProfilePicture( int userID, File file ) throws DatabaseException {
        executeInsertUpdateDeleteQuery( getPreparedStatement("UPDATE orcasoft.user SET profile_picture = ? WHERE ID_User = ?"), new Object[] {file, userID} );
    }

    /**
     * Löscht das Profilbild eines Benutzers aus der
     * Datenbank.
     *
     * @param userID ID_com des Benutzers.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteProfilePicture( int userID ) throws DatabaseException {
        executeInsertUpdateDeleteQuery( getPreparedStatement("UPDATE orcasoft.user SET profile_picture = NULL WHERE ID_User = ?"), new Object[] {userID} );
    }

    /**
     * Lädt den übergebenen Lebenslauf in die Datenbank hoch.
     *
     * @param studentID ID_com des Nutzers.
     * @param file Profilbild-Datei (PNG)
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void updateLebenslauf( int studentID, File file ) throws DatabaseException {
        executeInsertUpdateDeleteQuery( getPreparedStatement("UPDATE orcasoft.student SET lebenslauf = ? WHERE ID_Student = ?"), new Object[] {file, studentID} );
    }

    /**
     * Löscht den Lebenslauf eines Benutzers aus der
     * Datenbank.
     *
     * @param studentID ID_com des Benutzers.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void deleteLebenslauf( int studentID ) throws DatabaseException {
        executeInsertUpdateDeleteQuery( getPreparedStatement("UPDATE orcasoft.student SET lebenslauf = NULL WHERE ID_Student = ?"), new Object[] {studentID} );
    }

    /**
     * Aktualisiert einen Student.
     *
     * @param studentDTO studentDTO bzw. Nutzer der aktualisiert werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private void updateStudentUser( StudentDTO studentDTO ) throws DatabaseException {
        final String query = "UPDATE orcasoft.student SET vorname = ?, nachname = ?, matrikelnummer = ?, date_of_birth = ? , ausbildung = ?, berufserfahrung = ?, fk_studiengang = ? WHERE ID_Student = ?;";
        List<Object> psPlaceholders  = new ArrayList<>();
        psPlaceholders .add( studentDTO.getFirstName() );
        psPlaceholders .add( studentDTO.getLastName() );
        psPlaceholders .add( studentDTO.getMatrikelnummer() );
        psPlaceholders .add( studentDTO.getDateOfBirth() );
        psPlaceholders .add( studentDTO.getAusbildung() );
        psPlaceholders .add( studentDTO.getBerufserfahrung() );
        psPlaceholders .add( Integer.parseInt(studentDTO.getStudiengang()) );
        psPlaceholders .add( studentDTO.getId() );

        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), psPlaceholders .toArray() );

        skillDAO.updateSkillsOfStudent( studentDTO.getId(), studentDTO.getSkills() );
        spracheDAO.updateSprachenOfStudent( studentDTO.getId(), studentDTO.getSprachen() );
    }

    /**
     * Aktualisiert eine Company.
     *
     * @param companyDTO companyDTO bzw. Nutzer der aktualisiert werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private void updateCompanyUser( CompanyDTO companyDTO ) throws DatabaseException {
        final String query = "UPDATE orcasoft.company SET bio = ?, phone = ?, website = ?, iban = ?, bezeichnung = ?, fk_branche = ? WHERE ID_Company = ?; ";
        List<Object> PSPlaceholders = new ArrayList<>();
        PSPlaceholders.add( companyDTO.getBio() );
        PSPlaceholders.add( companyDTO.getPhone() );
        PSPlaceholders.add( companyDTO.getWebsite() );
        PSPlaceholders.add( companyDTO.getIBAN() );
        PSPlaceholders.add( companyDTO.getBezeichnung() );
        PSPlaceholders.add( Integer.parseInt(companyDTO.getBranche()) );
        PSPlaceholders.add( companyDTO.getId() );

        executeInsertUpdateDeleteQuery( getPreparedStatement( query ), PSPlaceholders.toArray() );
    }
    /**
     * Löscht einen Nutzer mit allen Attributen
     *
     * @param userID Nutzer der gelöscht werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */
    
    public void deleteUser( int userID ) throws DatabaseException {

        try {

            logger.info("[Löschung] Nutzer wird gelöscht...");

            if ( isUserStudent( userID ) ) {

                deleteStudentUser( userID );

            } else {

                deleteCompanyUser( userID );
            }

            executeInsertUpdateDeleteQuery( getPreparedStatement( "DELETE FROM orcasoft.user WHERE id_user = ?" ), new Object[]{userID} );
            logger.info("[Löschung] Nutzer wurde erfolgreich gelöscht.\n");

        } catch ( SQLException e ) {
            logger.error("[Löschung] Löschen ist fehlgeschlagen! (SQL)", e);
            throw new DatabaseException( DatabaseException.SQL_ERROR + e.getMessage() );
        } finally {
            sessionController.logOutUser();
        }

    }

    /**
     * Löscht einen Student mit allen Attributen
     *
     * @param studentID Student der gelöscht werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private void deleteStudentUser( int studentID ) throws DatabaseException {

        spracheDAO.deleteSprachenOfStudent( studentID );
        skillDAO.deleteSkillsOfStudent( studentID );
        bewerbungDAO.deleteAllBewerbungenOfStudent( studentID );
        executeInsertUpdateDeleteQuery( getPreparedStatement("DELETE FROM orcasoft.student WHERE id_student = ?"), new Object[]{studentID} );

    }

    /**
     * Löscht eine Company mit allen Attributen
     *
     * @param companyID Company die gelöscht werden soll.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private void deleteCompanyUser( int companyID ) throws DatabaseException {

        ausschreibungDAO.deleteAllByCompany( companyID );
        executeInsertUpdateDeleteQuery( getPreparedStatement("DELETE FROM orcasoft.company WHERE id_company = ?"), new Object[]{companyID} );

    }

    /**
     * Erstellt eine 'leichte' userDTO-Instanz aus dem ResultSet.
     *
     * @param set ResultSet (Ergebnis einer SQL-Selektion)
     * @return userDTO-Instanz
     * @throws SQLException Fehler im SQL-Code.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public UserDTO extractUserDTOFromResultSet( ResultSet set ) throws SQLException, DatabaseException {

        UserDTO user = null;

        try {

            if ( set.next() ) {

                // Attribute direkt auslesen, da das set später leer ist!
                int id = set.getInt("ID_User");
                String email = set.getString("email");
                String strasse = set.getString("strasse");
                String ort = set.getString("ort");
                int plz = set.getInt("plz");
                int hausNr = set.getInt("haus_nr");
                final byte[] photoBytes = set.getBytes( "profile_picture" );

                // Benutzer via Factory erstellen
                if ( photoBytes == null ) {
                    user = UserFactory.createDefaultUserDTO( id, email, strasse, ort, plz, hausNr, null );
                } else {
                    user = UserFactory.createDefaultUserDTO( id, email, strasse, ort, plz, hausNr, new StreamResource( "ProfilePicture.png", () -> new ByteArrayInputStream( photoBytes )) );
                }

            } else {
                throw new DatabaseException( DatabaseException.USER_NOT_FOUND );
            }

        } finally {
            set.close();
        }

        return user;

    }

    /**
     * Erstellt ein StudentDTO mit Einbeziehung eines übergebenen userDTOs und seiner ID_com.
     * VORAUSSETZUNG: es muss vorher bekannt sein, dass das userDTO zu einem Studenten gehört.
     * Man sollte es vorher mit der Methode isUserStudent() überprüfen.
     *
     * @param user grobe Oberdaten des Studenten.
     * @param userID ID_com des Studenten.
     * @return Student-Instanz
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public StudentDTOImpl fuseUserDTOWithExtractedStudentData( UserDTO user, int userID ) throws DatabaseException, SQLException {

        StudentDTOImpl studentDTO = new StudentDTOImpl();

        ResultSet studentData = executeSelectionQuery( getPreparedStatement("SELECT * FROM orcasoft.student WHERE ID_Student = ?"), new Object[]{userID} );

        if ( studentData.next() ) {

            final byte[] lebenslaufDaten = studentData.getBytes("lebenslauf");

            studentDTO = UserFactory.createStudentDTO(
                    userID,
                    user.getEmail(),
                    user.getStrasse(),
                    user.getOrt(),
                    user.getPLZ(),
                    studentData.getString( "vorname" ),
                    studentData.getString( "nachname" ),
                    studentData.getInt("matrikelnummer"),
                    studentData.getDate("date_of_birth"),
                    studentData.getString( "ausbildung" ),
                    studentData.getString( "berufserfahrung" ),
                    studiengangDAO.getBezeichnung( Integer.parseInt( studentData.getString( "fk_studiengang" ) ) ),
                    user.getHausnummer(),
                    skillDAO.getSkillsOfStudent( userID ),
                    spracheDAO.getSprachenOfStudent( userID ),
                    user.getProfilePicture(),
                    lebenslaufDaten == null ? null : new StreamResource("lebenslauf.pdf", () -> new ByteArrayInputStream( lebenslaufDaten ))
            );

        }

        return studentDTO;
    }

    /**
     * Erstellt ein CompanyDTO mit Einbeziehung eines übergebenen userDTOs und seiner ID_com.
     * VORAUSSETZUNG: es muss vorher bekannt sein, dass das userDTO zu einem Unternehmen gehört.
     * Man sollte es vorher mit isUserStudent() überprüfen.
     *
     * @param user grobe Oberdaten des Unternehmens.
     * @param userID ID_com des Unternehmens.
     * @return Unternehmens-Instanz
     * @throws DatabaseException Fehler in der Datenbank.
     * @throws SQLException Fehler im SQL-Code.
     */

    public CompanyDTOImpl fuseUserDTOWithExtractedCompanyData( UserDTO user, int userID ) throws DatabaseException, SQLException {

        CompanyDTOImpl companyDTO = new CompanyDTOImpl();

        ResultSet companyData = executeSelectionQuery( getPreparedStatement("SELECT * FROM orcasoft.company WHERE ID_Company = ?; "), new Object[]{userID} );

        if ( companyData.next() ) {

            companyDTO = UserFactory.createCompanyDTO(
                    userID,
                    user.getEmail(),
                    user.getStrasse(),
                    user.getOrt(),
                    user.getPLZ(),
                    brancheDAO.getBezeichnung( Integer.parseInt( companyData.getString( "fk_branche" ) ) ),
                    companyData.getString( "bezeichnung" ),
                    companyData.getString("bio"),
                    companyData.getString("phone"),
                    companyData.getString( "website" ),
                    companyData.getString( "iban" ),
                    user.getHausnummer(),
                    user.getProfilePicture()
            );

        }

        return companyDTO;
    }

}
