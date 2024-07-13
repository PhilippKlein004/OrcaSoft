package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import org.hbrs.se2.project.hellocar.dao.BeschaeftigungDAO;
import org.hbrs.se2.project.hellocar.dao.UserDAO;
import org.hbrs.se2.project.hellocar.util.AvatarItem;
import org.hbrs.se2.project.hellocar.control.BewerbungControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.factories.BewerbungFactory;
import org.hbrs.se2.project.hellocar.dao.BewerbungDAO;
import org.hbrs.se2.project.hellocar.dtos.BewerbungDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.AusschreibungDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CssImport(value = "./styles/views/bewerbung-view.css")
@CssImport(value = "./styles/views/input-fields.css")
@PageTitle("Neue Bewerbung - OrcaSoft")
@Route(value = Globals.Pages.BEWERBUNG, layout = AppView.class)
@RouteAlias(value = Globals.Pages.BEWERBEN, layout = AppView.class)
public class BewerbungView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    record bewerbungStatus( String inhalt, int id ) {

        @Override
        public String toString() {
            return inhalt;
        }

    }

    public static AusschreibungDTOImpl ausschreibungDTO = null;
    public static String name = null;
    public static BewerbungDTO bewerbungDTO;
    public static byte viewType = 0;

    private final List<bewerbungStatus> statusList = new ArrayList<>();
    private final SessionController sessionController = new SessionController();

    private final UserDTO currentUser = sessionController.getCurrentUser();
    private final UserDAO userDAO = new UserDAO();
    private final BewerbungDAO bewerbungDAO = new BewerbungDAO();
    private final BewerbungControl bewerbungControl = new BewerbungControl();
    private final BeschaeftigungDAO beschaeftigungDAO = new BeschaeftigungDAO();

    private HorizontalLayout layoutRow = new HorizontalLayout();
    private VerticalLayout layoutColumn1 = new VerticalLayout();
    private VerticalLayout layoutColum1sub = new VerticalLayout();
    private VerticalLayout layoutColumn2 = new VerticalLayout();
    private VerticalLayout layoutColumn3 = new VerticalLayout();

    private Avatar avatar = new Avatar();

    private H4 h42 = new H4("Deine Bewerbung für");
    private H2 h2 = new H2();
    private ComboBox statusSelect = new ComboBox();
    Hr hr2 = new Hr();
    H5 h5 = new H5();

    private Hr hr = new Hr();

    private TextField textTitel = new TextField();
    private TextField textFirma = new TextField();
    private TextField textBeschaeftigung = new TextField();
    private TextArea textBeschreibung = new TextArea();
    private MultiSelectListBox avatarItems = new MultiSelectListBox();

    FormLayout formLayoutCheckbox = new FormLayout();
    Checkbox checkboxLebenslauf = new Checkbox();
    Checkbox checkboxMotivationsschreiben = new Checkbox();

    private FileBuffer buffer = new FileBuffer();
    private Upload uploadMotivationsschreiben = new Upload(buffer);

    private TextArea anschreibenField = new TextArea();
    private Button absendenButton = new Button();
    private Button abbrechenButton = new Button("Abbrechen");
    private Button uploadMotivationsschreibenButton = new Button("Motivationsschreiben hochladen");
    private Paragraph lebenslaufNotice = new Paragraph("Lebenslauf ist nicht vorhanden!");
    private Paragraph uploadNotice = new Paragraph("Upload ist fehlgeschlagen! Bitte überprüfe die Dateigröße.");
    private Image icon = new Image("images/logo5.jpg", "logo");
    private Notification notification = new Notification();

    public BewerbungView() {

        // ####### BEWERBUNG VIEW
        getContent().addClassName("bewerbung-view");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setFlexGrow(1.0, layoutRow);

        // ####### BEWERBUNG VIEW - Row
        layoutRow.addClassName("bewerbung-view_row");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setFlexGrow(1.0, layoutColumn1);
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutRow.setFlexGrow(1.0, layoutColumn3);

        // ####### BEWERBUNG VIEW - Column 1
        layoutColumn1.addClassName("bewerbung-view_row_col-1");
        layoutColumn1.addClassName(Padding.XLARGE);
        layoutColumn1.getStyle().set("flex-grow", "1");
        layoutColumn1.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn1.setAlignItems(Alignment.CENTER);
        layoutColumn1.setAlignSelf(FlexComponent.Alignment.CENTER, avatar);

        // ####### BEWERBUNG VIEW - Column 1 Sub
        layoutColum1sub.addClassName("bewerbung-view_row_col-1-sub");
        layoutColumn1.setFlexGrow(1.0, layoutColum1sub);
        layoutColum1sub.getStyle().set("flex-grow", "1");
        layoutColum1sub.setJustifyContentMode(JustifyContentMode.START);
        layoutColum1sub.setAlignItems(Alignment.CENTER);

        // ####### BEWERBUNG VIEW - Column 2
        layoutColumn2.addClassName("bewerbung-view_row_col-2");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, absendenButton, abbrechenButton);
        layoutColumn2.setHeightFull();

        uploadMotivationsschreibenButton.getStyle().set("cursor", "pointer");

        uploadMotivationsschreiben.setMaxFileSize(1024 * 1024); // 1 MB
        uploadMotivationsschreiben.setMaxFiles(1);
        uploadMotivationsschreiben.setAcceptedFileTypes(".pdf");
        uploadMotivationsschreiben.setDropAllowed(false);
        uploadMotivationsschreiben.setUploadButton(uploadMotivationsschreibenButton);

        // ####### BEWERBUNG VIEW - Column 3
        layoutColumn3.addClassName("bewerbung-view_row_col-3");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn3.setAlignItems(Alignment.CENTER);
        layoutColumn3.setHeightFull();

        // ####### BEWERBUNG VIEW - OrcaSoft Logo
        icon.addClassName("bewerbung-view_logo");

        // ####### BEWERBUNG VIEW - Headings
        h2.setText("Bewerbung aufgeben");
        h5.setText("Dateienanhang");

        // ####### BEWERBUNG VIEW - Avatar (Profilbild Unternehmen)
        avatar.addClassName("bewerbung-view_avatar");

        // ####### BEWERBUNG VIEW - Bewerbung Inhalt
        anschreibenField.addClassName("bewerbung-view_anschreiben-field");
        anschreibenField.setLabel("Anschreiben (max. 512 Zeichen)");
        anschreibenField.setErrorMessage("Bitte geben Sie Ihr Anschreiben an.");
        anschreibenField.setPlaceholder("Sehr geehrte...");
        anschreibenField.setRequired(true);
        anschreibenField.getStyle().set("flex-grow", "1");

        // ####### BEWERBUNG VIEW - Bewerbung Layout ()
        formLayoutCheckbox.setWidth("100%");

        checkboxLebenslauf.addClassName("bewerbung-view_checkbox-lebenslauf");
        checkboxLebenslauf.getStyle().set("cursor", "pointer");
        checkboxLebenslauf.setLabel("Lebenslauf");
        checkboxLebenslauf.setWidth("min-content");

        checkboxMotivationsschreiben.addClassName("bewerbung-view_checkbox-motivationsschreiben");
        checkboxMotivationsschreiben.setLabel("Motivationsschreiben");
        checkboxMotivationsschreiben.setWidth("min-content");

        textTitel.setReadOnly(true);
        textTitel.setLabel("Titel");

        textFirma.setReadOnly(true);
        textFirma.setLabel("Firma");

        textBeschaeftigung.setReadOnly(true);
        textBeschaeftigung.setLabel("Beschäftigung");

        textBeschreibung.setReadOnly(true);
        textBeschreibung.setLabel("Beschreibung");

        lebenslaufNotice.getStyle().set("color", "#f0c33c");
        lebenslaufNotice.setVisible(false);

        statusSelect.setLabel("Status der Bewerbung ändern");
        statusSelect.setWidth("100%");

        statusList.add( new bewerbungStatus( "In Bearbeitung", 0 ) );
        statusList.add( new bewerbungStatus( "Bewerbung abgelehnt", 1 ) );
        statusList.add( new bewerbungStatus( "Bewerbung angenommen", 2 ) );

        statusSelect.setItems(statusList);

        String beschaeftigung = "Unbekannt";
        String companyName = "Unbekannt";

        if ( ausschreibungDTO != null ) {

            try {
                beschaeftigung = beschaeftigungDAO.getBezeichnung( Integer.parseInt( (ausschreibungDTO.getBeschaeftigung() ) ));
                companyName = userDAO.getCompanyNameByID( Integer.parseInt( ausschreibungDTO.getCompany() ) );
            } catch ( DatabaseException ignored) {}

            textTitel.setValue(ausschreibungDTO.getTitel());
            textFirma.setValue(companyName);
            textBeschaeftigung.setValue( beschaeftigung );
            textBeschreibung.setValue(ausschreibungDTO.getBeschreibung());
        }

        uploadNotice.getStyle().set("color", "red");
        uploadNotice.setVisible(false);

        formLayoutCheckbox.add(checkboxLebenslauf);
        formLayoutCheckbox.add(lebenslaufNotice);
        formLayoutCheckbox.add(uploadNotice);
        formLayoutCheckbox.add(uploadMotivationsschreiben);

        // ####### BEWERBUNG VIEW - Buttons (Absenden / Abbrechen Bewerbung)
        absendenButton.addClassName("bewerbung-view_button-senden");
        absendenButton.setText("Absenden");
        absendenButton.setEnabled(false);
        absendenButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        abbrechenButton.addClassName("bewerbung-view_button-abbrechen");
        abbrechenButton.setText("Abbrechen");
        abbrechenButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        // ####### BEWERBUNG VIEW - Inhaltselemente dem Layout hinzufügen
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn1);
        layoutRow.add(layoutColumn2);
        layoutRow.add(layoutColumn3);
        layoutColumn1.add(avatar);
        layoutColumn1.add(hr);
        layoutColumn1.add(layoutColum1sub);
        layoutColum1sub.add(h42);
        layoutColum1sub.add(textTitel);
        layoutColum1sub.add(textFirma);
        layoutColum1sub.add(textBeschaeftigung);
        layoutColum1sub.add(textBeschreibung);
        layoutColum1sub.add(avatarItems);
        layoutColumn2.add(h2);
        layoutColumn2.add(anschreibenField);
        layoutColumn2.add(hr2);
        layoutColumn2.add(h5);
        layoutColumn2.add(formLayoutCheckbox);
        layoutColumn2.add(absendenButton);
        layoutColumn2.add(abbrechenButton);
        layoutColumn3.add(icon);

        // EventListener bitte hier einfügen

        anschreibenField.setValueChangeMode(ValueChangeMode.EAGER);

        anschreibenField.addValueChangeListener(e -> refreshAbsendenButton());

        absendenButton.addClickListener(e -> {

            LocalDate local = LocalDate.now();
            Date currentDate = new Date( local.getYear()-1900, local.getMonthValue()-1, local.getDayOfMonth() );

            try {

                System.out.println("[Bewerbung] Bewerbung wird verschickt...");

                bewerbungDAO.saveBewerbung( BewerbungFactory.createBewerbungForInsert(
                        currentDate,
                        anschreibenField.getValue(),
                        0,
                        buffer.getFileData() == null ? null : buffer.getFileData().getFile(),
                        checkboxLebenslauf.getValue(),
                        currentUser.getId(),
                        ausschreibungDTO.getId()
                ));

                System.out.println("[Bewerbung] Bewerbung wurde erfolgreich verschickt!\n");
                showSuccessDialog();

            } catch ( DatabaseException ex ) {
                showFailureDialog();
                System.out.println("[Bewerbung] Bewerbung konnte nicht verschickt werden! ( " + ex.getMessage() + " )\n");
            }

        });

        uploadMotivationsschreiben.addSucceededListener(e -> {
            System.out.println("Datei wurde erkannt!");
            uploadNotice.setVisible(false);
            refreshAbsendenButton();
        });

        uploadMotivationsschreiben.addFileRejectedListener(e -> {
            System.out.println("Datei wurde abgelehnt: " + e.getErrorMessage());
            uploadNotice.setVisible(true);
        });

        abbrechenButton.addClickListener(e -> showAbbrechenDialog().open());
    }

    /**
     * Befüllt die ComboBox, wo man den Status auswählen kann mit Daten.
     */

    private void styleAndActivateStatusComboBoxData() {

        statusSelect.setRenderer(new ComponentRenderer(item -> {

            AvatarItem avatarItem = new AvatarItem();
            avatarItem.setHeading( ((bewerbungStatus) item).inhalt );

            Avatar avatar = bewerbungControl.getBewerbungAvatar( ((bewerbungStatus) item).id );
            avatar.setWidth("40px");
            avatar.setHeight("40px");

            avatarItem.setAvatar(avatar);
            return avatarItem;
        }));

        for ( bewerbungStatus b : statusList ) if ( b.id == bewerbungDTO.getStatus() ) statusSelect.setValue( b );
        addListenerToStatusComboBox();
        layoutColumn2.add(statusSelect);

    }

    /**
     * Ausgelagerte Methode, die der ComboBox für den Status
     * einen Listener hinzufügt. Dies ist deshalb notwendig, da dieser
     * sonst auslöst, wenn der bereits aktive Wert in diese Liste
     * eingefügt wurde (for-Schleife vor Methodenaufruf).
     */

    private void addListenerToStatusComboBox() {

        statusSelect.addValueChangeListener(e -> {
            statusSelect.blur();
            try {
                System.out.println( "[Bewerbung] Status wird aktualisiert..." );
                bewerbungDAO.updateStatusOfBewerbung( bewerbungDTO.getID(), ((bewerbungStatus) e.getValue()).id );
                notification = Notification.show("Der Status wurde aktualisiert!");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
                System.out.println( "[Bewerbung] Status wurde erfolgreich aktualisiert!\n" );
            } catch ( DatabaseException ex ) {
                System.out.println( "[Bewerbung] Status konnte nicht aktualisiert werden!\n" );
                notification = Notification.show("Der Status konnte nicht aktualisiert werden! Eventuell ist diese Bewerbung nicht mehr verfügbar.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
            }
        });

    }

    /**
     * Blendet alle Felder für die Ausschreibungen aus.
     */

    private void hideAusschreibungFields() {
        avatar.setVisible(false);
        hr.setVisible(false);
        layoutColum1sub.setVisible(false);
    }

    /**
     * Versteckt alle Eingabefelder, Buttons, Uploads usw.
     */

    private void hideInputField() {
        anschreibenField.setVisible(false);
        lebenslaufNotice.setVisible(false);
        hr2.setVisible(false);
        h5.setVisible(false);
        checkboxLebenslauf.setVisible(false);
        uploadMotivationsschreiben.setVisible(false);
        absendenButton.setVisible(false);
        abbrechenButton.setVisible(false);
    }

    /**
     * Zeigt den Failure-Dialog an, falls das Absenden funktioniert ist.
     */

    private void showSuccessDialog() {
        hideInputField();
        Image successIcon = new Image("images/success.svg", "Success!");
        successIcon.setWidth("100px");
        successIcon.setHeight("100px");
        layoutColumn2.add(successIcon);
        layoutColumn2.add(new Paragraph("Deine Bewerbung wurde erfolgreich abgeschickt!"));
    }

    /**
     * Zeigt den Failure-Dialog an, falls das Absenden fehlgeschlagen ist.
     */

    private void showFailureDialog() {
        hideInputField();
        Image failureIcon = new Image("images/failure.svg", "Failure!");
        failureIcon.setWidth("100px");
        failureIcon.setHeight("100px");
        layoutColumn2.add(failureIcon);
        layoutColumn2.add(failureIcon);
        layoutColumn2.add(new Paragraph("Deine Bewerbung konnte nicht abgeschickt werden!"));
    }


    /**
     * Wenn alle wichtigen Felder gültig sind, wird
     * der 'Absenden'-Knopf freigeschaltet.
     */

    private void refreshAbsendenButton() {
        absendenButton.setEnabled(!anschreibenField.isEmpty() && !anschreibenField.isInvalid() && buffer.getFileData() != null );
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

        switch ( viewType ) {
            case 1:
                // Student füllt Bewerbung aus
                checkForLebenslaufAndCompanyLogo();
                break;
            case 2:
                // Student sieht sich seine Bewerbung erneut an
                fillInFieldsWithBewerbungData();
                hideAusschreibungFields();
                addDeleteButton();
                break;
            case 3:
                // Unternehmen sieht sich die Bewerbung an
                fillInFieldsWithBewerbungData();
                hideAusschreibungFields();
                styleAndActivateStatusComboBoxData();
                break;
            default:
                beforeEnterEvent.rerouteTo( Globals.Pages.SEARCH );
                break;
        }
        viewType = 0;

    }

    /**
     * Aktualisiert das Eingabefeld bzw. sperrt die Lebenslauf-Checkbox, falls
     * der Student kein Lebenslauf besitzt und der Avatar bekommt entweder das Logo
     * des Unternehmens oder die Initialen dessen Namens.
     */

    private void checkForLebenslaufAndCompanyLogo() {
        boolean lebenslaufNotExisting = ( ((StudentDTO) currentUser).getLebenslauf() == null );
        checkboxLebenslauf.setEnabled( !lebenslaufNotExisting );
        lebenslaufNotice.setVisible( lebenslaufNotExisting );

        if ( ausschreibungDTO != null ) {
            if ( ausschreibungDTO.getCompanyLogo() == null ) avatar.setName( ausschreibungDTO.getCompany() );
            else avatar.setImageResource( ausschreibungDTO.getCompanyLogo() );
        }
    }

    /**
     * Fügt hinzu und aktiviert den 'Löschen'-Knopf in der View.
     */

    private void addDeleteButton() {

        Button deleteButton = new Button("Bewerbung löschen");
        deleteButton.getStyle().set("background", "indianred");
        deleteButton.getStyle().set("cursor", "pointer");
        deleteButton.getStyle().set("color", "white");

        deleteButton.addClickListener(e -> {
           getLoeschenDialog().open();
        });

        layoutColumn2.add(deleteButton);

    }

    /**
     * Erstellt eine Instanz von einem Dialog, der eine
     * Bestätigung des Studenten zum Löschen der Bewerbung fordert.
     *
     * @return Referenz auf die Dialog-Instanz.
     */

    private Dialog getLoeschenDialog() {

        Dialog deleteDialog = new Dialog("Bewerbung löschen");
        Paragraph text = new Paragraph("Sind Sie sich wirklich sicher, dass Sie diese Bewerbung löschen möchten? Dies kann nicht mehr rückgängig gemacht werden.");

        Button loeschenButton = new Button("Bewerbung Löschen");
        Button abbrechenButton = new Button("Abbrechen");

        loeschenButton.getStyle().set("background", "indianred");
        loeschenButton.getStyle().set("color", "white");
        loeschenButton.getStyle().set("cursor", "pointer");
        abbrechenButton.getStyle().set("cursor", "pointer");

        loeschenButton.addClickListener(e -> {
            try {
                bewerbungDAO.deleteBewerbung( bewerbungDTO.getID() );
                deleteDialog.close();
                ProfileView.refreshMessage = "Deine Bewerbung wurde erfolgreich gelöscht!";
                ProfileView.refreshWhileSaving = true;
                sessionController.redirectToPage( Globals.Pages.PROFIL );
            } catch ( DatabaseException ex ) {
                Notification notification = Notification.show("Die Bewerbung wurde bereits gelöscht! " + ex.getMessage() );
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
     * Entfernt die 'Absenden'- und 'Abbrechen'-Knöpfe und
     * lädt die Daten in die Felder ein.
     */

    private void fillInFieldsWithBewerbungData() {
        abbrechenButton.setVisible(false);
        absendenButton.setVisible(false);
        checkboxLebenslauf.setVisible(false);
        uploadMotivationsschreiben.setVisible(false);

        h2.setText( Globals.IS_STUDENT_USER ? "Deine Bewerbung für '" + name + "'" : "Bewerbung von " + name);
        name = null;
        h5.setText("Die Bewerbung ist eingegangen am: " + bewerbungDTO.getDate().toString() + ".");
        layoutColumn2.add(new H5("Beigefügte Dateien:"));

        anschreibenField.setValue( bewerbungDTO.getInhalt() );
        anschreibenField.setReadOnly(true);

        if ( bewerbungDTO.isLebenslaufAttached() ) {
            Anchor lebenslaufDownloadLink = new Anchor( bewerbungDTO.getLebenslaufResource(), "📄 Lebenslauf herunterladen" );
            lebenslaufDownloadLink.getElement().setAttribute("download", true);
            layoutColumn2.add( lebenslaufDownloadLink );
        }

        Anchor motivationsschreibenDownloadLink = new Anchor( bewerbungDTO.getMotivationsschreibenResource(), "📄 Motivationsschreiben herunterladen" );
        motivationsschreibenDownloadLink.getElement().setAttribute("download", true);
        layoutColumn2.add( motivationsschreibenDownloadLink );
    }

    /**
     * Zeit das Abbrechen-Dialogfenster an.
     *
     * @return Referenz auf die Dialoginstanz
     */

    private Dialog showAbbrechenDialog() {

        Dialog abbrechenDialog = new Dialog("Bewerbung abbrechen");
        Paragraph text = new Paragraph("Sind Sie sich wirklich sicher, dass Sie diese Ausschreibung löschen möchten?" +
                "Ihre bisherigen Eingaben gehen dabei verloren.");
        Button loeschenButton = new Button("Bewerbung abbrechen");
        Button abbrechenButton = new Button("Fortfahren");

        loeschenButton.getStyle().set("background", "indianred");
        loeschenButton.getStyle().set("color", "white");
        loeschenButton.getStyle().set("cursor", "pointer");
        abbrechenButton.getStyle().set("cursor", "pointer");

        loeschenButton.addClickListener(e -> {
            System.out.println("[Ausschreibung] Ausschreibung wird gelöscht...");
            abbrechenDialog.close();
            System.out.println("[Ausschreibung] Ausschreibung wurde erfolgreich gelöscht!\n");
            sessionController.redirectToPage(Globals.Pages.SEARCH);
        });

        abbrechenButton.addClickListener(e -> abbrechenDialog.close());

        abbrechenDialog.setCloseOnOutsideClick(false);
        abbrechenDialog.add(text);
        abbrechenDialog.getFooter().add(abbrechenButton);
        abbrechenDialog.getFooter().add(loeschenButton);
        return abbrechenDialog;
    }
}
