package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import org.hbrs.se2.project.hellocar.control.AusschreibungControl;
import org.hbrs.se2.project.hellocar.control.BewerbungControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.factories.AusschreibungFactory;
import org.hbrs.se2.project.hellocar.dao.*;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.util.ArrayList;
import java.util.List;



/* @CssImport(value = "./styles/views/stellenangebot/stellenangebot-view.css") */

@CssImport(value = "./styles/views/stellenangebot-view.css")
@CssImport(value = "./styles/views/input-fields.css")
@PageTitle("Neues Stellenangebot - OrcaSoft")
@Route(value = Globals.Pages.STELLENANGEBOT, layout = AppView.class)
@RouteAlias(value = Globals.Pages.STELLENANGEBOT, layout = AppView.class)
public class StellenangebotView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    public static AusschreibungDTOImpl ausschreibungDTO;
    private static boolean refreshOnSave = false;
    private static String refreshMessage = "";

    private final SkillDAO skillDAO;
    private final BeschaeftigungDAO beschaeftigungDAO;
    private final AusschreibungDAO ausschreibungDAO;
    private final AusschreibungControl ausschreibungControl = new AusschreibungControl();
    private final BewerbungControl bewerbungControl = new BewerbungControl();

    // Muss instanziiert werden, da sonst NullPointerException!
    // @Autowired-Annotation funktioniert hier nicht ...
    private SessionController sessionController = new SessionController();

    private List<String> beschaeftigungen;
    private List<String> skills;
    private boolean fieldsAreActive = false;
    private UserDTO currentUser = sessionController.getCurrentUser();

    private HorizontalLayout layoutRow = new HorizontalLayout();
    private VerticalLayout layoutColumn1 = new VerticalLayout();
    private VerticalLayout layoutColumn1sub = new VerticalLayout();
    private VerticalLayout layoutColumn2 = new VerticalLayout();
    private VerticalLayout layoutColumn3 = new VerticalLayout();

    private FormLayout formLayout2Col = new FormLayout();

    private Avatar avatar = new Avatar();

    private Image icon = new Image("images/logo5.jpg", "logo");

    private H2 h2 = new H2();
    private H4 h42 = new H4("Aktuelle Ausschreibungen");
    private H4 avatarMail = new H4();
    private Hr hr = new Hr();

    private MultiSelectListBox avatarItems = new MultiSelectListBox();
    private TextField titelField = new TextField();
    private ComboBox ausschreibungBeschaeftigung = new ComboBox();
    private TextArea beschreibungField = new TextArea();
    private DatePicker zeitr_startField = new DatePicker();
    private DatePicker zeitr_endeField = new DatePicker();
    private NumberField verguetungField = new NumberField();
    private Button speichernButton = new Button();
    private Button abbrechenButton = new Button("Abbrechen");
    private Button loeschenButton = new Button("Löschen");

    private MultiSelectComboBox skillComboBox = new MultiSelectComboBox("Benötigte Skills");
    private Notification notification = new Notification();

    private H4 bewerbungListHeader = new H4("Aktuelle Bewerbungen auf diese Ausschreibung");
    private MultiSelectListBox bewerbungenList = new MultiSelectListBox();

    public StellenangebotView(SkillDAO skillDAO, BeschaeftigungDAO beschaeftigungDAO, AusschreibungDAO ausschreibungDAO) {

        this.skillDAO = skillDAO;
        this.beschaeftigungDAO = beschaeftigungDAO;
        this.ausschreibungDAO = ausschreibungDAO;

        // Laden der aktuell verfügbaren Beschäftigungen und Skills
        try {
            beschaeftigungen = beschaeftigungDAO.getAll();
            skills = skillDAO.getAll();
        } catch ( DatabaseException e ) {
            notification = Notification.show("Ein Fehler ist aufgetreten! Bitte versuche es erneut...");
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }

        // ####### STELLENANGEBOT VIEW
        getContent().addClassName("stellenangebot-view");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setFlexGrow(1.0, layoutRow);
        /* getContent().setWidth("100%"); */

        // ####### STELLENANGEBOT VIEW - Row
        layoutRow.addClassName("stellenangebot-view_row");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setFlexGrow(1.0, layoutColumn1);
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutRow.setFlexGrow(1.0, layoutColumn3);
        layoutRow.setWidthFull();
        layoutRow.setWidth("100%");

        // ####### STELLENANGEBOT VIEW - Column 1
        layoutColumn1.addClassName("stellenangebot-view_row_col-1");
        layoutColumn1.addClassName(Padding.XLARGE);
        layoutColumn1.getStyle().set("flex-grow", "1");
        layoutColumn1.setFlexGrow(1.0, layoutColumn1sub);
        layoutColumn1.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn1.setAlignItems(Alignment.CENTER);
        layoutColumn1.setAlignSelf(FlexComponent.Alignment.CENTER, avatar);
        layoutColumn1.setHeightFull();
        layoutColumn1.setWidth("557px");
        layoutColumn1.setMinWidth("300px");
        layoutColumn1.setMaxWidth("350px");

        // ####### STELLENANGEBOT VIEW - Column 1 Sub
        layoutColumn1sub.addClassName("stellenangebot-view_row_col-1-sub");
        layoutColumn1sub.getStyle().set("flex-grow", "1");
        layoutColumn1sub.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn1sub.setAlignItems(Alignment.CENTER);
        layoutColumn1sub.setWidthFull();
        layoutColumn1sub.setWidth("min-content");

        // ####### STELLENANGEBOT VIEW - Column 2
        layoutColumn2.addClassName("stellenangebot-view_row_col-2");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, speichernButton, abbrechenButton);
        layoutColumn2.setHeightFull();
        layoutColumn2.setWidth("100%");

        // ####### STELLENANGEBOT VIEW - Column 3
        layoutColumn3.addClassName("stellenangebot-view_row_col-3");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn3.setAlignItems(Alignment.CENTER);
        layoutColumn3.setHeightFull();
        layoutColumn3.setWidth("545px");
        layoutColumn3.setMinWidth("300px");
        layoutColumn3.setMaxWidth("350px");

        // ####### STELLENANGEBOT VIEW - OrcaSoft Logo
        icon.getStyle().set("width", "24px");
        icon.getStyle().set("min-width", "300px");
        icon.getStyle().set("height", "300px");

        // ####### STELLENANGEBOT VIEW - Avatar ()
        avatar.setName( "Max Mustermann" );
        avatar.setWidth("36px");
        avatar.setMinWidth("120px");
        avatar.setMaxWidth("240px");
        avatar.setHeight("36px");
        avatar.setMinHeight("120px");
        avatar.setMaxHeight("240px");

        avatarMail.setText("orca@soft.de");
        avatarMail.setWidth("max-content");

        avatarItems.setWidth("100%");
        setAvatarItemsSampleData(avatarItems);

        // ####### STELLENANGEBOT VIEW - Headings
        h2.setText("Neues Stellenangebot");
        /* h2.setWidth("max-content"); */
        /* h42.setWidth("max-content"); */

        formLayout2Col.setWidth("100%");

        titelField.setLabel("Titel");

        titelField.setPlaceholder("Werkstudent");
        titelField.setRequired(true);
        titelField.setWidth("100%");

        skillComboBox.setErrorMessage("Bitte weisen Sie der Ausschreibung Skills zu.");
        skillComboBox.setRequired(true);
        skillComboBox.setWidth("100%");

        beschreibungField.setLabel("Beschreibung");
        beschreibungField.setErrorMessage("Bitte geben Sie eine kurze Beschreibung an.");
        beschreibungField.setPlaceholder("Beschreibung Ihrer Ausschreibung...");
        beschreibungField.setPlaceholder("Beschäftigung wählen...");
        beschreibungField.setRequired(true);
        beschreibungField.getStyle().set("flex-grow", "1");
        beschreibungField.setWidth("100%");

        ausschreibungBeschaeftigung.setLabel("Beschäftigung");
        ausschreibungBeschaeftigung.setRequired(true);
        ausschreibungBeschaeftigung.setWidth("min-content");

        verguetungField.setPlaceholder("450 €");
        verguetungField.setLabel("Vergütung");
        verguetungField.setErrorMessage("Bitte geben Sie einen gültigen Betrag ein.");
        verguetungField.setRequired(true);
        verguetungField.setWidth("min-content");

        zeitr_startField.setLabel("Startdatum");
        zeitr_startField.setRequired(true);
        zeitr_startField.setWidth("min-content");

        zeitr_endeField.setLabel("Enddatum");
        zeitr_endeField.setRequired(true);
        zeitr_endeField.setWidth("min-content");

        ausschreibungBeschaeftigung.setItems( beschaeftigungen );
        skillComboBox.setItems( skills );

        // ####### BEWERBUNG VIEW - Buttons (Speichern / Abbrechen / Löschen)
        speichernButton.addClassName("stellenangebot-view_button-speichern");
        speichernButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        /* speichernButton.setWidth("min-content"); */
        /* speichernButton.getStyle().set("cursor", "pointer"); */

        abbrechenButton.addClassName("stellenangebot-view_button-abbrechen");
        abbrechenButton.setVisible(false);
        /* abbrechenButton.setWidth("min-content"); */
        /* abbrechenButton.getStyle().set("cursor", "pointer"); */

        loeschenButton.addClassName("stellenangebot-view_button-loeschen");
        loeschenButton.setVisible(false);
        /* loeschenButton.getStyle().set("cursor", "pointer"); */
        /* loeschenButton.getStyle().set("background", "indianred"); */
        /* loeschenButton.getStyle().set("color", "white"); */

        // ####### STELLENANGEBOT VIEW - Inhaltselemente dem Layout hinzufügen
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn1);
        layoutRow.add(layoutColumn2);
        layoutRow.add(layoutColumn3);

        layoutColumn1.add(avatar);
        layoutColumn1.add(avatarMail);
        layoutColumn1.add(hr);
        layoutColumn1.add(layoutColumn1sub);
        layoutColumn1.add(loeschenButton);

        layoutColumn1sub.add(h42);
        layoutColumn1sub.add(avatarItems);

        layoutColumn2.add(h2);
        layoutColumn2.add(titelField);
        layoutColumn2.add(beschreibungField);
        layoutColumn2.add(skillComboBox);
        layoutColumn2.add(formLayout2Col);
        layoutColumn2.add(speichernButton);
        layoutColumn2.add(abbrechenButton);

        formLayout2Col.add(ausschreibungBeschaeftigung);
        formLayout2Col.add(verguetungField);
        formLayout2Col.add(zeitr_startField);
        formLayout2Col.add(zeitr_endeField);

        layoutColumn3.add(icon);

        bewerbungListHeader.setWidth("100%");
        bewerbungenList.setWidth("100%");
        bewerbungListHeader.setVisible(false);
        bewerbungenList.setVisible(false);


        layoutColumn3.add(bewerbungListHeader);
        layoutColumn3.add(bewerbungenList);

        // EventListener bitte hier einfügen

        titelField.setValueChangeMode(ValueChangeMode.EAGER);

        titelField.addValueChangeListener(e -> checkTitleField( titelField.getValue() ));

        titelField.addValidationStatusChangeListener(e -> checkTitleField( titelField.getValue() ));

        beschreibungField.setValueChangeMode(ValueChangeMode.EAGER);

        beschreibungField.addValueChangeListener(e -> refreshSpeicherButton());

        zeitr_endeField.addValueChangeListener(e -> checkDates());

        zeitr_startField.addValueChangeListener(e -> checkDates());

        zeitr_endeField.addValidationStatusChangeListener(e -> checkDates());

        zeitr_startField.addValidationStatusChangeListener(e -> checkDates());

        beschreibungField.addValueChangeListener(e -> checkDates());

        ausschreibungBeschaeftigung.addValueChangeListener(e -> refreshSpeicherButton());

        verguetungField.addValueChangeListener(e -> refreshSpeicherButton());

        skillComboBox.addValueChangeListener(e -> refreshSpeicherButton());

        loeschenButton.addClickListener(e -> showDeleteDialog().open());

        speichernButton.addClickListener(e -> {

            speichernButton.setEnabled(false);

            if ( fieldsAreActive ) {

                lockInputFields();
                refreshSpeicherButton();
                abbrechenButton.setVisible(false);
                loeschenButton.setVisible( false );

                try {

                    if ( ausschreibungDTO == null ) {

                        System.out.println("[Ausschreibung] Ausschreibung wird angelegt...");
                        ausschreibungDAO.saveAusschreibung( AusschreibungFactory.createAusschreibungDTOImplForInsert(
                                titelField.getValue(),
                                beschreibungField.getValue(),
                                String.valueOf(currentUser.getId()),
                                getSelectedIndexesOfSkills(),
                                getSelectedIndexOfBeschaeftigung(),
                                verguetungField.getValue().intValue(),
                                java.sql.Date.valueOf( zeitr_startField.getValue() ),
                                java.sql.Date.valueOf( zeitr_endeField.getValue() )
                        ));

                        System.out.println("[Ausschreibung] Ausschreibung wurde erfolgreich angelegt!\n");
                        refreshMessage = "Die Ausschreibung wurde erfolgreich gespeichert!";

                    } else {

                        System.out.println("[Ausschreibung] Ausschreibung wird aktualisiert...");
                        updateAusschreibungDTO();
                        ausschreibungDAO.updateAusschreibung( ausschreibungDTO );
                        refreshMessage = "Die Ausschreibung wurde erfolgreich aktualisiert!";
                        System.out.println("[Ausschreibung] Ausschreibung wurde erfolgreich aktualisiert!\n");

                    }

                    refreshOnSave = true;
                    ausschreibungDTO = null;
                    sessionController.refreshPage();

                } catch ( DatabaseException ex ) {
                    notification = Notification.show("Die Ausschreibung konnte nicht gespeichert werden! " + ex.getMessage());
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }

                speichernButton.setText( ausschreibungDTO == null ? "Neues Stellenangebot erstellen" : "Bearbeiten");
            } else {
                unlockInputFields();
                refreshSpeicherButton();
                abbrechenButton.setVisible( true );
                loeschenButton.setVisible( ausschreibungDTO != null );
                speichernButton.setText( ausschreibungDTO == null ? "Speichern" : "Aktualisieren");
            }

            fieldsAreActive = !fieldsAreActive;

        });

        abbrechenButton.addClickListener(e -> sessionController.refreshPage() );

        avatarItems.addSelectionListener(e -> {
            if ( e.getValue().isEmpty() ) return;

            try {
                ausschreibungDTO = ausschreibungControl.getAusschreibungByID( ((AusschreibungControl.Ausstellung) e.getValue().toArray()[0]).ID() );
                sessionController.refreshPage();
            } catch ( DatabaseException ex ) {
                notification = Notification.show(ex.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }

        });

        bewerbungenList.addSelectionListener(e -> {
            if ( e.getValue().isEmpty() ) return;

            BewerbungView.viewType = 3;
            BewerbungControl.BewerbungForCompany bewerbung = (BewerbungControl.BewerbungForCompany) e.getValue().toArray()[0];
            try {
                BewerbungView.bewerbungDTO = bewerbungControl.getBewerbungByID( bewerbung.ID_Bewerbung(), bewerbung.ID_Student() );
                BewerbungView.name = bewerbung.vorname() + " " + bewerbung.nachname();
                sessionController.redirectToPage( Globals.Pages.BEWERBUNG );
            } catch ( DatabaseException ex ) {
                notification = Notification.show("Die Bewerbungen konnten leider nicht geladen werden! " + ex.getMessage());
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }

        });

    }

    /**
     * Aktualisiert das statische AusschreibungDTO
     * mit den Werten aus den Eingabefeldern.
     */

    private void updateAusschreibungDTO() {
        ausschreibungDTO.setTitel( titelField.getValue() );
        ausschreibungDTO.setBeschreibung( beschreibungField.getValue() );
        ausschreibungDTO.setSkills( getSelectedIndexesOfSkills() );
        ausschreibungDTO.setBeschaeftigung( getSelectedIndexOfBeschaeftigung() );
        ausschreibungDTO.setVerguetung( verguetungField.getValue().intValue() );
        ausschreibungDTO.setStartDatum( java.sql.Date.valueOf( zeitr_startField.getValue()) );
        ausschreibungDTO.setEndDatum( java.sql.Date.valueOf( zeitr_endeField.getValue()) );
    }

    /**
     * Überprüft, ob der Titel der Ausschreibung schon einmal von dem Unternehmen
     * verwendet wurde.
     */

    private void checkTitleField( String value ) {
        try {
            if ( ausschreibungDTO != null && value.equals(ausschreibungDTO.getTitel()) ) return;
            if ( ausschreibungDAO.doesAusschreibungExist( value, currentUser.getId()) ) {
                titelField.setInvalid( true );
                titelField.setErrorMessage("Sie haben bereits eine Ausschreibung dazu erstellt!");
            } else {
                titelField.setErrorMessage("Bitte geben Sie einen Titel ein.");
            }
            refreshSpeicherButton();
        } catch ( DatabaseException ex ) {
            notification = Notification.show(ex.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }
    }

    /**
     * Überprüft, nach jeder Eingabe, ob das Enddatum < Startdatum ist.
     */

    private void checkDates() {
        if ( zeitr_startField.getValue() != null && zeitr_endeField.getValue() != null ) {
            boolean datesAreInvalid = zeitr_endeField.getValue().isBefore(zeitr_startField.getValue());
            zeitr_endeField.setErrorMessage("Das Enddatum kann nicht vor dem Startdatum liegen! Bitte Enddatum korrigieren.");
            zeitr_startField.setInvalid( datesAreInvalid );
            zeitr_endeField.setInvalid( datesAreInvalid );
        } else {
            zeitr_startField.setErrorMessage("Bitte Startdatum eingeben!");
            zeitr_endeField.setErrorMessage("Bitte Enddatum eingeben!");
        }
        refreshSpeicherButton();
    }

    /**
     * Gibt den selektierten Index in der Beschäftigung-Box zurück.
     *
     * @return Liste der IDs.
     */

    private String getSelectedIndexOfBeschaeftigung() {
        return String.valueOf( beschaeftigungen.indexOf( (String) ausschreibungBeschaeftigung.getValue())+1 );
    }

    /**
     * Gibt die selektierten Indexe in der Skills-Box zurück.
     *
     * @return Liste der IDs.
     */

    private List<String> getSelectedIndexesOfSkills() {

        List<String> selectedIndexes = new ArrayList<>();
        for ( Object skill : skillComboBox.getValue() ) selectedIndexes.add( String.valueOf( skills.indexOf( (String) skill )+1 ) );
        return selectedIndexes;

    }

    /**
     * Fügt die AvatarItems in die MultiSelectListBox ein.
     * @param multiSelectListBox Referenz auf die MultiSelectListBox.
     */

    private void setAvatarItemsSampleData( MultiSelectListBox multiSelectListBox ) {
        try {
            ausschreibungControl.ausschreibungslisteBefuellen( multiSelectListBox, currentUser );
        } catch ( DatabaseException ex ) {
            notification = Notification.show(ex.getMessage());
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.setPosition(Notification.Position.TOP_CENTER);
        }
    }

    /**
     * Wenn alle wichtigen Felder gültig sind, wird
     * der 'Speichern'-Knopf freigeschaltet.
     */

    private void refreshSpeicherButton() {
        speichernButton.setEnabled( !titelField.isInvalid() &&
                                    !titelField.isEmpty() &&
                                    !beschreibungField.isEmpty() &&
                                    !beschreibungField.isInvalid() &&
                                    !verguetungField.isInvalid() &&
                                    !verguetungField.isEmpty() &&
                                    !ausschreibungBeschaeftigung.isEmpty() &&
                                    !ausschreibungBeschaeftigung.isInvalid() &&
                                    !zeitr_startField.isInvalid() &&
                                    !zeitr_startField.isEmpty() &&
                                    !zeitr_endeField.isInvalid() &&
                                    !zeitr_endeField.isEmpty() &&
                                    !skillComboBox.isInvalid() );
    }

    /**
     * Überprüft vor dem Laden der View, ob der
     * Nutzer vorhanden bzw. angemeldet ist. Falls
     * nicht, wird er zum Login weitergeleitet.
     *
     * @param beforeEnterEvent Event.
     */

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {

        lockInputFields();

        if ( null == currentUser || Globals.IS_STUDENT_USER ) {
            beforeEnterEvent.rerouteTo( Globals.Pages.SEARCH );
            return;
        }

        if ( refreshOnSave ) {
            notification = Notification.show(refreshMessage);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_CENTER);
            refreshOnSave = false;
        }

        avatar.setImageResource( currentUser.getProfilePicture() );
        setUp();

        if ( ausschreibungDTO != null ) {
            h2.setText("Ihr Stellenangebot");
            speichernButton.setText("Bearbeiten");
            setBewerbungValues();
            checkForAndSetBewerbung();
        } else {
            speichernButton.setText("Neues Stellenangebot erstellen");
        }

    }

    /**
     * Baut die View auf.
     */

    private void setUp() {

        avatarMail.setText( currentUser.getEmail() );
        System.out.println("[Ausschreibung] Nutzer hat Stellenangebot aufgerufen.\n");

        if ( Globals.IS_STUDENT_USER ) {
            sessionController.redirectToPage(Globals.Pages.SEARCH);
            System.out.println("[Ausschreibung] Nutzer ist Student und wurde auf die Startseite zurückgeleitet.\n");
        }

    }

    /**
     * Zeit das Löschen-Dialogfenster an.
     *
     * @return Referenz auf die Dialoginstanz
     */

    private Dialog showDeleteDialog() {

        Dialog deleteDialog = new Dialog("Ausschreibung löschen");
        Paragraph text = new Paragraph("Sind Sie sich wirklich sicher, dass Sie diese Ausschreibung löschen möchten?");
        Button loeschenButton = new Button("Löschen");
        Button abbrechenButton = new Button("Abbrechen");

        loeschenButton.getStyle().set("background", "indianred");
        loeschenButton.getStyle().set("color", "white");
        loeschenButton.getStyle().set("cursor", "pointer");
        abbrechenButton.getStyle().set("cursor", "pointer");

        loeschenButton.addClickListener(e -> {
            System.out.println("[Ausschreibung] Ausschreibung wird gelöscht...");
            deleteDialog.close();
            try {
                ausschreibungControl.deleteAusschreibung( ausschreibungDTO.getId() );
                ausschreibungDTO = null;
                refreshOnSave = true;
                refreshMessage = "Die Ausschreibung wurde erfolgreich gelöscht!";
                System.out.println("[Ausschreibung] Ausschreibung wurde erfolgreich gelöscht!\n");
                sessionController.refreshPage();
            } catch ( DatabaseException ex ) {
                System.out.println("[Ausschreibung] Die Ausschreibung konnte nicht gelöscht werden!\n");
                notification = Notification.show("Die Ausschreibung konnte nicht gelöscht werden!");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }

        });

        abbrechenButton.addClickListener(e -> deleteDialog.close());

        deleteDialog.setCloseOnOutsideClick(false);
        deleteDialog.add(text);
        deleteDialog.getFooter().add(abbrechenButton);
        deleteDialog.getFooter().add(loeschenButton);
        return deleteDialog;

    }

    /**
     * Setzt aus der statischen Ausschreibungsinstanz
     * die Werte in die Eingabefelder ein.
     */

    private void setBewerbungValues() {
        titelField.setValue( ausschreibungDTO.getTitel() );
        beschreibungField.setValue( ausschreibungDTO.getBeschreibung() );
        skillComboBox.setValue( ausschreibungDTO.getSkills() );

        String beschaeftigung = "Vollzeit";

        try {
            beschaeftigung = beschaeftigungDAO.getBezeichnung( Integer.parseInt( ausschreibungDTO.getBeschaeftigung() ) );
        } catch ( DatabaseException ignored ) {}

        ausschreibungBeschaeftigung.setValue( beschaeftigung );
        verguetungField.setValue( ausschreibungDTO.getVerguetung().doubleValue() );
        zeitr_startField.setValue( ausschreibungDTO.getStartDatumLocal() );
        zeitr_endeField.setValue( ausschreibungDTO.getEndDatumLocal() );
    }

    /**
     * Überprüft, ob Bewerbungen für diese Ausschreibung vorhanden ist.
     * Falls ja, dann wird die Liste dargestellt.
     */

    private void checkForAndSetBewerbung() {
        try {
            List<BewerbungControl.BewerbungForCompany> bewerbungen = bewerbungControl.getAllBewerbungenOfAusschreibung( ausschreibungDTO.getId() );
            if ( !bewerbungen.isEmpty() ) {
                ausschreibungControl.bewerbungListeBefuellen( bewerbungenList, bewerbungen );
                bewerbungListHeader.setVisible(true);
                bewerbungenList.setVisible(true);
            }
        } catch ( DatabaseException e ) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Sperrt alle Eingabefelder in der View.
     */

    private void lockInputFields() {
        titelField.setReadOnly(true);
        beschreibungField.setReadOnly(true);
        ausschreibungBeschaeftigung.setReadOnly(true);
        verguetungField.setReadOnly(true);
        zeitr_startField.setReadOnly(true);
        zeitr_endeField.setReadOnly(true);
        skillComboBox.setReadOnly(true);
    }

    /**
     * Entsperrt alle Eingabefelder in der View.
     */

    private void unlockInputFields() {
        titelField.setReadOnly(false);
        beschreibungField.setReadOnly(false);
        ausschreibungBeschaeftigung.setReadOnly(false);
        verguetungField.setReadOnly(false);
        zeitr_startField.setReadOnly(false);
        zeitr_endeField.setReadOnly(false);
        skillComboBox.setReadOnly(false);
    }

}
