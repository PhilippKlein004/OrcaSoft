package org.hbrs.se2.project.hellocar.control;

import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.server.StreamResource;
import org.hbrs.se2.project.hellocar.util.AvatarItem;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.exception.SearchException;
import org.hbrs.se2.project.hellocar.control.factories.AusschreibungFactory;
import org.hbrs.se2.project.hellocar.dao.*;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class AusschreibungControl {

    // Record-Klasse für die Listen links in der Profile- und Ausschreibung-View
    public record Ausstellung(int ID, String name) {
    }

    public record AusstellungSearch(int ID_com, String titel, String companyName, byte[] companyLogo, int ID) {
    }

    private final UserDAO userDAO = new UserDAO();
    private final SkillDAO skillDAO = new SkillDAO();
    private final BewerbungDAO bewerbungDAO = new BewerbungDAO();
    private final BeschaeftigungDAO beschaeftigungDAO = new BeschaeftigungDAO();
    private final SuchvorschlagDAO suchvorschlagDAO = new SuchvorschlagDAO();
    private final AusschreibungDAO ausschreibungDAO = new AusschreibungDAO();

    /**
     * Gibt eine List aller Ausschreibungen von einem
     * Unternehmen (ID_com) für die Liste
     * links 'Aktuelle Ausschreibungen' zurück.
     *
     * @param Company_ID ID_com des Unternehmens.
     * @return Tupel an Ausschreibungen (ID_Ausschreibung, Titel).
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public List<String> getAllAusschreibungenByCompany(int Company_ID ) throws DatabaseException {
        ResultSet queryResult;
        List<String> ausschreibungen = new ArrayList<>();

        try {

            queryResult = ausschreibungDAO.getAllAusschreibungenByCompany( Company_ID );
            while ( queryResult.next() ) {
                ausschreibungen.add( queryResult.getString("id_ausschreibung") );
                ausschreibungen.add( queryResult.getString("titel") );
            }

        } catch ( SQLException | DatabaseException e ) {
            throw new DatabaseException( e.getMessage() );
        }

        return ausschreibungen;
    }

    /**
     * Diese Methode gibt mögliche Suchbeispiele auf Grundlage
     * der vorhandenen Ausschreibungen und ihre Titel wieder.
     *
     * @param titel Titel der Ausschreibungen.
     * @return
     */

    public List<String> getSearchExamples( String titel ) throws DatabaseException {
        ResultSet queryResult = suchvorschlagDAO.getSuchvorschlaegeThatAreSimilarTo( titel );
        List<String> searchExamples = new ArrayList<>();

        try {
            // queryResult.next() schiebt den Zeiger auf die nächste Zeile
            while ( queryResult.next() ) searchExamples.add( queryResult.getString("bezeichnung") );
        } catch ( SQLException e ) {
            throw new DatabaseException( e.getMessage() );
        }

        return searchExamples;
    }

    /**
     * Löscht eine Ausschreibung aus der DB.
     * DIESE METHODE ZUM TESTEN VERWENDEN!
     *
     * @param Ausschreibung_ID ID_com der Ausschreibung.
     */

    public void deleteAusschreibung( int Ausschreibung_ID ) throws DatabaseException {
        skillDAO.deleteSkillsOfAusschreibung( Ausschreibung_ID ); // ---
        bewerbungDAO.deleteBewerbungenOfAusschreibung( Ausschreibung_ID ); // ---
        ausschreibungDAO.deleteAusschreibung( Ausschreibung_ID );
    }

    /**
     * Gibt ein Ausschreibungsobjekt für eine
     * gegebene ID_com zurück.
     *
     * @param Ausschreibung_ID ID_com der Ausschreibung.
     * @return Ausschreibungsobjekt
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public AusschreibungDTOImpl getAusschreibungByID( int Ausschreibung_ID ) throws DatabaseException {
        ResultSet queryResult = ausschreibungDAO.getAusschreibungByID( Ausschreibung_ID );
        AusschreibungDTOImpl ausschreibungDTO = new AusschreibungDTOImpl();

        try {

            if ( queryResult != null && queryResult.next() ) {

                ausschreibungDTO = AusschreibungFactory.createAusschreibungDTOImpl(
                        queryResult.getString("titel"),
                        queryResult.getString("beschreibung"),
                        queryResult.getString("fk_company"),
                        skillDAO.getSkillsOfAusschreibung( Ausschreibung_ID ),
                        queryResult.getString("fk_beschaeftigung"),
                        queryResult.getInt("verguetung"),
                        queryResult.getDate("zeitr_start"),
                        queryResult.getDate("zeitr_ende"),
                        queryResult.getInt("id_ausschreibung")
                );

            }

        } catch ( SQLException e ) {
            throw new DatabaseException( DatabaseException.SQL_ERROR );
        }

        return ausschreibungDTO;
    }

    /**
     * Fügt die Suchergebnisse in die übergebene Liste ein.
     *
     * @param selectionBox Liste in der View.
     * @param searchQuery Suchbegriff.
     * @return Anzahl der Suchergebnisse.
     */

    public int setSearchResults( ListBox selectionBox, String searchQuery ) throws SearchException {

        List<AusstellungSearch> data;

        try {
            data = extractSearchResultsFromResultSet( ausschreibungDAO.getAusschreibungenByTitle( searchQuery ) );
        } catch ( DatabaseException | SQLException e ) {
            System.out.println(e.getMessage());
            throw new SearchException( e.getMessage() );
        }

        if ( data.isEmpty() ) throw new SearchException( SearchException.NO_SEARCH_RESULT );

        selectionBox.setItems(data);
        selectionBox.setRenderer(new ComponentRenderer<>( result -> {
            HorizontalLayout row = new HorizontalLayout();
            row.setAlignItems(FlexComponent.Alignment.CENTER);

            Avatar avatar = new Avatar();
            avatar.setName( ((AusstellungSearch) result).companyName );

            if ( ((AusstellungSearch) result).companyLogo != null ) {
                StreamResource resource = new StreamResource( "ProfilePicture.png", () -> new ByteArrayInputStream( ((AusstellungSearch) result).companyLogo ));
                avatar.setImageResource( resource );
            }

            Span title = new Span( ((AusstellungSearch) result).titel );
            Span companyName = new Span( ((AusstellungSearch) result).companyName );
            companyName.getStyle()
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-size", "var(--lumo-font-size-s)");

            VerticalLayout column = new VerticalLayout(title, companyName);
            column.setPadding(false);
            column.setSpacing(false);

            row.add(avatar, column);
            row.getStyle().set("line-height", "var(--lumo-line-height-m)");
            row.getStyle().set("cursor", "pointer");
            return row;
        }));

        return data.size();

    }

    public List<AusstellungSearch> extractSearchResultsFromResultSet( ResultSet searchResults ) throws SQLException {

        List<AusstellungSearch> results = new ArrayList<>();

        while ( searchResults.next() ) {
            results.add( new AusstellungSearch(     searchResults.getInt("id_com"),
                                                    searchResults.getString("titel"),
                                                    searchResults.getString("companyName"),
                                                    searchResults.getBytes("companyLogo"),
                                                    searchResults.getInt("id_aus")
            ));
        }

        return results;
    }

    /**
     * Fügt die AvatarItems in die MultiSelectListBox ein.
     * @param selectionBox Referenz auf die MultiSelectListBox.
     */

    // Obertyp von MultiSelectBox und ListBox -> ListBoxBase
    public void ausschreibungslisteBefuellen( MultiSelectListBox selectionBox, UserDTO currentUser ) throws DatabaseException {

        List<String> ausschreibungen;
        List<Ausstellung> data = new ArrayList<>();

        ausschreibungen = getAllAusschreibungenByCompany( currentUser.getId() );
        // Ergebnisse sind in Tupel gespeichert (ID, Name), deswegen auch ++i
        for ( int i = 0 ; i < ausschreibungen.size() ; ++i ) data.add( new Ausstellung( Integer.parseInt( ausschreibungen.get( i++ )), ausschreibungen.get(i) ) );

        selectionBox.setItems(data);
        selectionBox.setRenderer(new ComponentRenderer(item -> {
            AvatarItem avatarItem = new AvatarItem();
            avatarItem.setHeading(((Ausstellung) item).name);
            avatarItem.setDescription( ((CompanyDTOImpl) currentUser).getBezeichnung() );
            avatarItem.setAvatar(new Avatar(((Ausstellung) item).name));
            return avatarItem;
        }));

    }

    /**
     * Befüllt die Liste mit den Bewerbungen auf eine Ausschreibung
     * mit den Daten.
     *
     * @param selectionBox Referenz auf die Liste.
     * @param bewerbungen Liste mit den Bewerbungen.
     * @throws DatabaseException Fehler in der Datenbank.
     */

    public void bewerbungListeBefuellen( MultiSelectListBox selectionBox, List<BewerbungControl.BewerbungForCompany> bewerbungen ) throws DatabaseException {

        selectionBox.setItems( bewerbungen );
        selectionBox.setRenderer(new ComponentRenderer(item -> {

            BewerbungControl.BewerbungForCompany bewerbung = (BewerbungControl.BewerbungForCompany) item;
            String studentName = bewerbung.vorname() + " " + bewerbung.nachname();

            AvatarItem avatarItem = new AvatarItem();
            avatarItem.setHeading( studentName );
            avatarItem.setDescription( bewerbung.datum().toString() );

            Avatar avatar = new Avatar();

            if ( bewerbung.profilePicture() == null ) avatar.setName( studentName );
            else avatar.setImageResource( bewerbung.profilePicture() );

            avatarItem.setAvatar(avatar);
            return avatarItem;
        }));

    }

}
