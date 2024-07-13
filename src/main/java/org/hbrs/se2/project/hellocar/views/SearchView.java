package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.hbrs.se2.project.hellocar.control.AusschreibungControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.exception.SearchException;
import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.util.List;

@CssImport(value = "./styles/views/search-view.css")

@PageTitle("Suche nach Ausschreibung")
@Route(value = Globals.Pages.SEARCH, layout = AppView.class)
@RouteAlias(value = Globals.Pages.SEARCH, layout = AppView.class)
public class SearchView extends Composite<VerticalLayout> {

    private final AusschreibungControl ausschreibungControl;
    private final SessionController sessionController;
    private final BewerbungDAO bewerbungDAO;
    // Layout Elemente
    private HorizontalLayout layoutRow = new HorizontalLayout();
    private VerticalLayout columnLeft = new VerticalLayout();
    private VerticalLayout columnMiddle = new VerticalLayout();
    private VerticalLayout columnRight = new VerticalLayout();

    private VerticalLayout layoutColumn = new VerticalLayout();
    /*
    private VerticalLayout columnLeft = new VerticalLayout();   // -> kann entfernt werden - dient nur als Seitenabstand
    private VerticalLayout columnRight = new VerticalLayout();  // -> kann entfernt werden - dient nur als Seitenabstand
    */
    private H3 h3 = new H3();
    private H4 h4 = new H4();
    private TextField suchBegriffField = new TextField();
    private ListBox suchErgebnisListe = new ListBox();
    private ListBox suchvorschlaege = new ListBox();
    private Image notfoundImage = new Image("images/no_results.png", "Not found");
    private Image orcaSoftSchriftzug = new Image("images/OrcaSoftSchriftzug.png", "OrcaSoft Schriftzug");
    private Notification notification = new Notification();

    public SearchView(AusschreibungControl ausschreibungControl, SessionController sessionController, BewerbungDAO bewerbungDAO) {
        // Layout Einstellungen

        // ####### SEARCH VIEW
        getContent().addClassName("search-view");
        getContent().setFlexGrow(1.0, layoutRow);
        getContent().getStyle().set("flex-grow", "1");
        /* getContent().setWidth("100%"); */

        // ####### SEARCH VIEW - Row
        layoutRow.addClassName("search-view_row");
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        /* layoutRow.setWidthFull(); */
        /* layoutRow.setWidth("100%"); */

        // ####### SEARCH VIEW - Column 1 kann entfernt werden - dient nur als Seitenabstand
        /* columnLeft.addClassName("search-view_row_col-1"); */
        /* columnLeft.setHeightFull(); */
        /* columnLeft.getStyle().set("flex-grow", "1"); */
        /* columnLeft.setMinWidth("300px"); */
        /* columnLeft.setMaxWidth("350px"); */
        /* columnLeft.getStyle().set("flex-grow", "1"); */

        // ####### SEARCH VIEW - Column 2
        layoutColumn.addClassName("search-view_row_col-2");
        layoutColumn.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn.setAlignItems(Alignment.CENTER);
        layoutColumn.addClassName(Gap.XLARGE);
        layoutColumn.getStyle().set("flex-grow", "1");
        /* layoutColumn.setWidth("100%"); */
        /* layoutColumn.setHeightFull(); */
        /* layoutColumn.setHeightFull(); */

        // ####### SEARCH VIEW - Column 3 kann entfernt werden - dient nur als Seitenabstand
        /* columnRight.addClassName("search-view_row_col-3"); */
        /* columnRight.setHeightFull(); */
        /* columnRight.getStyle().set("flex-grow", "1"); */
        /* columnRight.setMinWidth("300px"); */
        /* columnRight.setMaxWidth("350px"); */
        /* columnRight.getStyle().set("flex-grow", "1"); */

        // ####### PROFILE VIEW - Headings
        h3.setText("Dein Sprungbrett zur Karrierewelt!");
        h4.setText("");
        /* h3.setWidth("max-content"); */
        /* h4.setWidth("max-content"); */

        // ####### PROFILE VIEW - Suchfeld Elemente
        orcaSoftSchriftzug.addClassName("search-view_orcasoftheading");

        // ####### PROFILE VIEW - Suchfeld Elemente
        notfoundImage.setWidth("200px");
        notfoundImage.setHeight("200px");

        suchvorschlaege.setVisible(false);
        suchvorschlaege.getStyle().set("border-left", "1px solid var(--lumo-contrast-10pct)");
        suchvorschlaege.getStyle().set("border-right", "1px solid var(--lumo-contrast-10pct)");
        suchvorschlaege.getStyle().set("border-bottom", "1px solid var(--lumo-contrast-10pct)");
        suchvorschlaege.getStyle().set("border-radius", "0 0 14px 14px");
        suchvorschlaege.getStyle().set("margin-top", "-20px");
        suchvorschlaege.getStyle().set("cursor", "pointer");
        suchBegriffField.setPlaceholder("Suchbegriff");
        suchBegriffField.setWidth("100%");
        suchErgebnisListe.setWidth("100%");
        suchvorschlaege.setWidth("99%");
        notfoundImage.setVisible(false);

        // ####### SEARCH VIEW - Inhaltselemente dem Layout hinzufügen
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn);
        layoutRow.setFlexGrow(1.0, layoutColumn);
        layoutColumn.add(orcaSoftSchriftzug);
        layoutColumn.add(h3);
        layoutColumn.add(suchBegriffField);
        layoutColumn.add(suchvorschlaege);
        layoutColumn.add(h4);
        layoutColumn.add(suchErgebnisListe);
        layoutColumn.add(notfoundImage);
        /* layoutRow.add(columnLeft); */
        /* layoutRow.add(columnRight); */
        /* layoutRow.setFlexGrow(1.0, columnLeft); */
        /* layoutRow.setFlexGrow(1.0, columnRight); */

        /*
         * Listener
         */

        suchBegriffField.setValueChangeMode(ValueChangeMode.EAGER);

        suchBegriffField.addKeyPressListener(Key.ENTER, e -> setSuchergebnisse());

        suchBegriffField.addValueChangeListener(e -> setSuchvorschlaege( e.getValue() ));

        suchvorschlaege.addValueChangeListener(e -> {
            if ( e.getValue() != null ) suchBegriffField.setValue( (String) e.getValue() );
            suchvorschlaege.setVisible(false);
            suchBegriffField.focus();
        });

        suchErgebnisListe.addValueChangeListener(e -> {
            if ( e.getValue() != null ) showPopup( (AusschreibungControl.AusstellungSearch) e.getValue());
        });

        this.ausschreibungControl = ausschreibungControl;
        this.sessionController = sessionController;
        this.bewerbungDAO = bewerbungDAO;
    }

    /**
     * Diese Methode erhält das AusschreibungDTO und erstellt zur Übersicht
     * ein Popup Window mit den nötigsten Informationen
     *
     * @return ausschreibungDetails
     */

    private Dialog showPopup(AusschreibungControl.AusstellungSearch ausschreibung) {

        AusschreibungDTOImpl ausschreibungDTO = null;

        // Dialog Struktur

        Dialog ausschreibungDetails = new Dialog();
        VerticalLayout popupColumn = new VerticalLayout();
        HorizontalLayout popupHeaderRow = new HorizontalLayout();
        HorizontalLayout popupButtonRow = new HorizontalLayout();
        Avatar companyLogo = new Avatar(ausschreibung.companyName());
        if ( ausschreibung.companyLogo() != null ) companyLogo.setImageResource( new StreamResource( "ProfilePicture.png", () -> new ByteArrayInputStream( ausschreibung.companyLogo() )) );

        try {
            ausschreibungDTO = ausschreibungControl.getAusschreibungByID( ausschreibung.ID() );
        } catch ( DatabaseException e ) {
            ausschreibungDetails.close();
            notification = Notification.show(e.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
            return null;
        }

        // Dialog Bedienelemente

        FormLayout formLayout2Col = new FormLayout();

        Div firmaDiv = new Div();
        Div bezeichnungDiv = new Div();
        Div startDatumDiv = new Div();
        Div endDatumDiv = new Div();
        Div anstellungDiv = new Div();
        Div gehaltDiv = new Div();
        Div beschreibungDiv = new Div();

        H5 firmaHeading = new H5();
        H5 bezeichnungHeading = new H5();
        H5 startDatumHeading = new H5();
        H5 endDatumHeading = new H5();
        H5 anstellungHeading = new H5();
        H5 gehaltHeading = new H5();
        H5 beschreibungHeading = new H5();

        Paragraph firmaText = new Paragraph();
        Paragraph bezeichnungText = new Paragraph();
        Paragraph startDatumText = new Paragraph();
        Paragraph endDatumText = new Paragraph();
        Paragraph anstellungText = new Paragraph();
        Paragraph gehaltText = new Paragraph();
        Paragraph beschreibungText = new Paragraph();

        Button zurueck = new Button();
        Button bewerben = new Button();

        Avatar avatar = new Avatar();

        // Dialog Einstellungen

        companyLogo.setWidth("70px");
        companyLogo.setHeight("70px");

        zurueck.getStyle().set("cursor", "pointer");
        bewerben.getStyle().set("cursor", "pointer");

        ausschreibungDetails.setWidth("80%");
        ausschreibungDetails.setHeight("80%");
        zurueck.setText("Schließen");
        zurueck.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        bewerben.setText("Bewerben");
        bewerben.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        ausschreibungDetails.setHeaderTitle(ausschreibung.companyName() + " - " + ausschreibung.titel());
        ausschreibungDetails.getHeader().add(companyLogo);

        ausschreibungDetails.isCloseOnOutsideClick();
        ausschreibungDetails.open();

        popupColumn.setJustifyContentMode(JustifyContentMode.CENTER);
        popupColumn.setAlignItems(Alignment.CENTER);

        popupButtonRow.setJustifyContentMode(JustifyContentMode.END);
        popupButtonRow.setAlignItems(Alignment.CENTER);

        popupHeaderRow.setJustifyContentMode(JustifyContentMode.START);
        popupHeaderRow.setAlignItems(Alignment.CENTER);

        // Dialog Aufbau

        formLayout2Col.setWidth("100%");

        firmaHeading.setText("Firma");
        firmaHeading.setWidth("max-content");

        bezeichnungHeading.setText("Bezeichnung");
        bezeichnungHeading.setWidth("max-content");

        startDatumHeading.setText("Startdatum");
        startDatumHeading.setWidth("max-content");

        endDatumHeading.setText("Enddatum");
        endDatumHeading.setWidth("max-content");

        anstellungHeading.setText("Anstellung");
        anstellungHeading.setWidth("max-content");

        gehaltHeading.setText("Gehalt");
        gehaltHeading.setWidth("max-content");

        beschreibungHeading.setText("Beschreibung");
        beschreibungHeading.setWidth("max-content");

        firmaText.setText(ausschreibung.companyName());
        firmaText.setWidth("100%");
        firmaText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        bezeichnungText.setText(ausschreibung.titel());
        bezeichnungText.setWidth("100%");
        bezeichnungText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        startDatumText.setText(ausschreibungDTO.getStartDatum().toString());
        startDatumText.setWidth("100%");
        startDatumText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        endDatumText.setText(ausschreibungDTO.getEndDatum().toString());
        endDatumText.setWidth("100%");
        endDatumText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        anstellungText.setText(ausschreibungDTO.getBeschaeftigung());
        anstellungText.setWidth("100%");
        anstellungText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        gehaltText.setText(ausschreibungDTO.getVerguetung() + " €");
        gehaltText.setWidth("100%");
        gehaltText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        beschreibungText.setText(ausschreibungDTO.getBeschreibung());
        beschreibungText.setWidth("100%");
        beschreibungText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        firmaDiv.add(firmaHeading);
        firmaDiv.add(firmaText);

        bezeichnungDiv.add(bezeichnungHeading);
        bezeichnungDiv.add(bezeichnungText);

        startDatumDiv.add(startDatumHeading);
        startDatumDiv.add(startDatumText);

        endDatumDiv.add(endDatumHeading);
        endDatumDiv.add(endDatumText);

        anstellungDiv.add(anstellungHeading);
        anstellungDiv.add(anstellungText);

        gehaltDiv.add(gehaltHeading);
        gehaltDiv.add(gehaltText);

        beschreibungDiv.add(beschreibungHeading);
        beschreibungDiv.add(beschreibungText);

        formLayout2Col.add(firmaDiv);
        formLayout2Col.add(bezeichnungDiv);
        formLayout2Col.add(startDatumDiv);
        formLayout2Col.add(endDatumDiv);
        formLayout2Col.add(anstellungDiv);
        formLayout2Col.add(gehaltDiv);

        popupButtonRow.add(bewerben);
        popupButtonRow.add(zurueck);

        popupColumn.add(new Hr());
        popupColumn.add(formLayout2Col);
        popupColumn.add(new Hr());
        popupColumn.add(beschreibungDiv);

        ausschreibungDetails.getHeader().add(popupHeaderRow);
        ausschreibungDetails.getFooter().add(popupButtonRow);
        ausschreibungDetails.add(popupColumn);

        if ( Globals.IS_STUDENT_USER ) bewerben.setEnabled( checkSkills( ausschreibungDTO.getSkills(), ((StudentDTO) sessionController.getCurrentUser()).getSkills() )
                                                            && checkIfAlreadyApplied( ausschreibungDTO.getId() ));
        else bewerben.setVisible(false);

        // Listener zum Schließen

        zurueck.addClickListener(ze -> {
            ausschreibungDetails.close();
        });

        bewerben.addClickListener(ze -> {
            try {
                BewerbungView.ausschreibungDTO  = ausschreibungControl.getAusschreibungByID( ausschreibung.ID() );
                BewerbungView.ausschreibungDTO.setCompanyLogo( new StreamResource( "ProfilePicture.png", () -> new ByteArrayInputStream( ausschreibung.companyLogo() )) );
                BewerbungView.viewType = 1;
                sessionController.redirectToPage(Globals.Pages.BEWERBUNG);
            } catch (DatabaseException e) {
            }
            ausschreibungDetails.close();
        });

        return ausschreibungDetails;
    }

    // Ausgelagerte Methode um die Suchergebnisse zu ermitteln
    private void setSuchergebnisse() {
        try {
            suchvorschlaege.setVisible(false);
            h4.setText( "Suchergebnisse zu '" + suchBegriffField.getValue() + "' (" +  ausschreibungControl.setSearchResults( suchErgebnisListe, suchBegriffField.getValue()) + "):" );
            suchErgebnisListe.setVisible(true);
            notfoundImage.setVisible(false);
        } catch ( SearchException ex ) {
            h4.setText( "Huch. Leider konnten wir nichts zu '" + suchBegriffField.getValue() + "' finden..." );
            suchErgebnisListe.setVisible(false);
            notfoundImage.setVisible(true);
        }
    }

    // Ausgelagerte Methode um die Suchvorschläge zu ermitteln
    private void setSuchvorschlaege( String value ) {
        try {
            suchvorschlaege.setVisible(true);
            suchvorschlaege.setItems( ausschreibungControl.getSearchExamples( value ) );
        } catch ( DatabaseException ex ) {
            suchvorschlaege.setVisible(false);
            System.err.println(ex.getMessage());
        }
    }

    // Überprüft, ob ein Student, die benötigten Skills für eine Bewerbung besitzt
    private boolean checkSkills( List<String> ausschreibungSkills, List<String> studentSkills  ) {

        for ( String skill : ausschreibungSkills ) if ( !studentSkills.contains(skill) ) {
            notification = Notification.show("Du scheinst nicht die nötigen Skills für diese Ausschreibung zu besitzen! (" + skill + ")" );
            notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.setDuration(10000);
            return false;
        }
        return true;

    }

    private boolean checkIfAlreadyApplied( int ID_Ausschreibung ) {
        try {
            return !bewerbungDAO.hasStudentAlreadyApplied( sessionController.getCurrentUser().getId(), ID_Ausschreibung );
        } catch ( SQLException | DatabaseException ex ) {
            notification = Notification.show("Ein Fehler ist aufgetreten! Eine Bewerbung ist momentan nicht möglich." );
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
            notification.setDuration(10000);
        }
        return true;
    }

}
