package org.hbrs.se2.project.hellocar.control;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.util.AvatarItem;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.factories.BewerbungFactory;
import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.BewerbungDTOImpl;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BewerbungControl {

    private final BewerbungDAO bewerbungDAO = new BewerbungDAO();
    private final UserDAO userDAO = new UserDAO();

    public record BewerbungFromStudent(int id, String titel, String datum, int status ) {}

    public record BewerbungForCompany(int ID_Bewerbung, int ID_Student, String vorname, String nachname, Date datum, StreamResource profilePicture) {}

    /**
     * Fügt die Bewerbungen in die MultiSelectListBox ein.
     * @param selectionBox Referenz auf die MultiSelectListBox.
     */

    // Obertyp von MultiSelectBox und ListBox -> ListBoxBase
    public void bewerbungenListeBefuellen( MultiSelectListBox selectionBox, UserDTO currentUser ) throws DatabaseException {

        List<BewerbungFromStudent> data = getAllBewerbungenOfStudent( currentUser.getId() );

        selectionBox.setItems(data);
        selectionBox.setRenderer(new ComponentRenderer(item -> {

            BewerbungFromStudent bewerbung = (BewerbungFromStudent) item;

            AvatarItem avatarItem = new AvatarItem();
            avatarItem.setHeading( bewerbung.titel );
            avatarItem.setDescription( bewerbung.datum );

            Avatar avatar = getBewerbungAvatar( bewerbung.status);
            avatar.setWidth("60px");
            avatar.setHeight("60px");

            avatarItem.setAvatar( avatar ) ;

            return avatarItem;
        }));

    }

    /**
     * Diese Methode gibt einen Avatar zurück, der den Status
     * der Bewerbung visuell darstellt.
     *
     * @param status Status der Bewerbung.
     * @return Avatar-Instanz (60px x 60px).
     */

    public Avatar getBewerbungAvatar(int status ) {

        Avatar avatar = new Avatar();

        switch ( status ) {
            case 0:
                avatar.setImage("images/waiting.svg");
                break;
            case 1:
                avatar.setImage("images/decline.svg");
                break;
            case 2:
                avatar.setImage("images/success.svg");
                break;
            default:
                avatar.setName("?");
                break;
        }

        return avatar;
    }

    /**
     * Diese Methode gibt alle Bewerbung eines Studenten in Form
     * einer Liste der record-Klasse 'Bewerbung' wieder.
     *
     * @param ID_Student ID des Studenten.
     * @return Liste der Bewerbungen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    private List<BewerbungFromStudent> getAllBewerbungenOfStudent(int ID_Student ) throws DatabaseException {

        ResultSet queryResult = bewerbungDAO.getAllBewerbungenOfStudent( ID_Student );
        List<BewerbungFromStudent> data = new ArrayList<>();

        try {
            while ( queryResult.next() ) data.add(  new BewerbungFromStudent(  queryResult.getInt("ID_Bewerbung"),
                                                                    queryResult.getString("titel"),
                                                                    queryResult.getDate("datum").toString(),
                                                                    queryResult.getInt("status") ));
        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR + e.getMessage() );
        }

        return data;
    }

    /**
     * Gibt eine Bewerbung-Instanz zurück, welcher in der
     * Datenbank mit ihrer ID gesucht wurde.
     *
     * @param ID_Bewerbung ID der Bewerbung.
     * @param ID_Student ID des Studenten.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public BewerbungDTOImpl getBewerbungByID(int ID_Bewerbung, int ID_Student ) throws DatabaseException {

        ResultSet bewerbungData = bewerbungDAO.getBewerbungByID( ID_Bewerbung );
        BewerbungDTOImpl bewerbungDTO = new BewerbungDTOImpl();

        try {

            if ( bewerbungData.next() ) {

                final boolean lebenslaufExist = bewerbungData.getBoolean("lebenslauf");
                final byte[] motivationsschreibenRawData = bewerbungData.getBytes("motivationsschreiben");

                StreamResource motivationsschreibenFile = new StreamResource( "Motivationsschreiben.pdf", () -> new ByteArrayInputStream( motivationsschreibenRawData ) );
                StreamResource lebenslaufFile = null;

                if ( lebenslaufExist ) {

                    ResultSet lebenslaufData = userDAO.getLebenslaufOfStudent( ID_Student );
                    if ( lebenslaufData.next() ) {
                        final byte[] lebenslaufRawData = lebenslaufData.getBytes("lebenslauf");
                        lebenslaufFile =  new StreamResource( "Lebenslauf.pdf", () -> new ByteArrayInputStream( lebenslaufRawData ) );
                    }

                }

                bewerbungDTO = BewerbungFactory.createBewerbung(    bewerbungData.getInt("ID_Bewerbung"),
                                                                    bewerbungData.getDate("datum"),
                                                                    bewerbungData.getString("inhalt"),
                                                                    bewerbungData.getInt("status"),
                                                                    motivationsschreibenFile,
                                                                    lebenslaufExist,
                                                                    lebenslaufFile,
                                                                    ID_Student);



            }

        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR + e.getMessage() );
        }

        return bewerbungDTO;
    }

    /**
     * Gibt eine Liste an Bewerbungen zurück, die auf eine bestimmte
     * Ausschreibung gestellt wurden.
     *
     * @param ID_Ausschreibung ID der Ausschreibung.
     * @return Liste an Bewerbungen für diese Ausschreibung.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<BewerbungForCompany> getAllBewerbungenOfAusschreibung( int ID_Ausschreibung ) throws DatabaseException {

        List<BewerbungForCompany> data = new ArrayList<>();
        ResultSet bewerbungenData = bewerbungDAO.getBewerbungenFromAusschreibung( ID_Ausschreibung );

        // record BewerbungForCompany(int ID_Bewerbung, int ID_Student, String vorname, String nachname, Date datum, StreamResource profilePicture) {}
        try {
            while ( bewerbungenData.next() ) {

                final byte[] profilePictureRawData = bewerbungenData.getBytes("profilepic");

                data.add( new BewerbungForCompany(
                        bewerbungenData.getInt("ID_Bewerbung"),
                        bewerbungenData.getInt("ID_Student"),
                        bewerbungenData.getString("vorname"),
                        bewerbungenData.getString("nachname"),
                        bewerbungenData.getDate("datum"),
                        profilePictureRawData == null ? null : new StreamResource( "Profilbild.png", () -> new ByteArrayInputStream( profilePictureRawData ) ) ));
                }

        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR + e.getMessage() );
        }

        return data;

    }

}
