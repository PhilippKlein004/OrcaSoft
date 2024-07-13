package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxBase;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import org.hbrs.se2.project.hellocar.control.AusschreibungControl;
import org.hbrs.se2.project.hellocar.control.BewerbungControl;
import org.hbrs.se2.project.hellocar.control.ProfileControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dao.*;
import org.hbrs.se2.project.hellocar.dtos.CompanyDTO;
import org.hbrs.se2.project.hellocar.dtos.StudentDTO;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.dtos.impl.CompanyDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.StudentDTOImpl;
import org.hbrs.se2.project.hellocar.dtos.impl.UserDTOImpl;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/* @CssImport(value = "./styles/views/profile/profile-view.css") */

@CssImport(value = "./styles/views/profile-view.css")
@CssImport(value = "./styles/views/input-fields.css")
@PageTitle("Dein Profil - OrcaSoft")
@Route(value = Globals.Pages.PROFIL, layout = AppView.class)
@RouteAlias(value = Globals.Pages.PROFILE, layout = AppView.class)
public class ProfileView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    private StudiengangDAO studiengangDAO;

    @Autowired
    private BrancheDAO brancheDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private ProfileControl profileControl;

    // Muss instanziiert werden, da sonst NullPointerException!
    // @Autowired-Annotation funktioniert hier nicht ...
    private SessionController sessionController = new SessionController();
    private SkillDAO skillDAO = new SkillDAO();
    private SpracheDAO spracheDAO = new SpracheDAO();
    private BewerbungControl bewerbungControl = new BewerbungControl();

    private HorizontalLayout layoutRow = new HorizontalLayout();
    private VerticalLayout layoutColumn1 = new VerticalLayout();
    private VerticalLayout layoutColumn1sub = new VerticalLayout();
    private VerticalLayout layoutColumn2 = new VerticalLayout();
    private VerticalLayout layoutColumn3 = new VerticalLayout();
    private FormLayout profileForm1 = new FormLayout();
    private FormLayout profileForm2 = new FormLayout();
    private Avatar avatar = new Avatar();
    private Image icon = new Image("images/logo5.jpg", "logo");
    private H2 h2 = new H2();
    private H4 avatarMail = new H4();
    private H4 h42 = new H4();
    private Paragraph uploadStatus = new Paragraph("Lade dein Profilbild hoch (1MB .PNG)");
    private Hr hr = new Hr();
    private Hr hr2 = new Hr();
    private Hr hr4 = new Hr();
    private MultiSelectListBox avatarItems = new MultiSelectListBox();
    private TextField nameField = new TextField();
    private TextField vornameField = new TextField();
    private TextField matrikelnummerWebsiteField = new TextField();
    private EmailField emailField = new EmailField();
    private NumberField telefonnummerField = new NumberField();
    private TextField strasseField = new TextField();
    private NumberField hausnummerField = new NumberField();
    private NumberField plzField = new NumberField();
    private TextField ortField = new TextField();
    private DatePicker datePicker = new DatePicker();
    private TextField IBANField = new TextField("IBAN");
    private ComboBox studiengangBrancheComboBox = new ComboBox();
    private MultiSelectComboBox skillComboBox = new MultiSelectComboBox();
    private MultiSelectComboBox sprachenComboBox = new MultiSelectComboBox();
    private TextArea ausbildungOrBioMultilineText = new TextArea();
    private TextArea berufserfahrungMultilineText = new TextArea();
    private Button speichernButton = new Button();
    private Button abbrechenButton = new Button("Abbrechen");
    private Button deletePBButton = new Button("Profilbild löschen");
    private Button uploadImageButton = new Button();
    private Button uploadLebenslaufButton = new Button();
    private Button lebenslaufLoeschenButton = new Button("Lebenslauf löschen");
    private Button deleteAccountButton = new Button();
    private Notification notification = new Notification();
    private boolean fieldsAreActive = false;
    private boolean isStudentProfile = Globals.IS_STUDENT_USER;
    public static boolean refreshWhileSaving;

    // ====*====*====*====*==== UPLOAD BEREICH ====*====*====*====*====

    private FileBuffer fileBuffer = new FileBuffer();

    private Paragraph lebenslaufStatus = new Paragraph("Lade deinen Lebenslauf hoch (1MB .PDF)");

    private Hr hr3 = new Hr();

    private Upload uploadImage = new Upload(fileBuffer);
    private Upload uploadLebenslauf = new Upload(fileBuffer);

    private Anchor lebenslaufDownload = null;


    public static String refreshMessage = "";

    // Wird benötigt, um die Indexe richtig platzieren zu können. Sonst wird beim
    // Abspeichern der Index 0 übergeben, wenn in dem Feld nichts verändert wird.
    private List<String> studiengaengeBranche;

    private UserDTO currentUser = sessionController.getCurrentUser();

    private AusschreibungControl ausschreibungControl;

    public ProfileView(AusschreibungControl ausschreibungControl) {

        this.ausschreibungControl = ausschreibungControl;

        // ####### PROFILE VIEW
        getContent().addClassName("profile-view");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setFlexGrow(1.0, layoutRow);
        /* getContent().setWidth("100%"); */

        // ####### PROFILE VIEW - Row
        layoutRow.addClassName("profile-view_row");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.getStyle().set("flex-grow", "1");
        layoutRow.setFlexGrow(1.0, layoutColumn1);
        layoutRow.setFlexGrow(1.0, layoutColumn2);
        layoutRow.setFlexGrow(1.0, layoutColumn3);

        // ####### PROFILE VIEW - Column 1
        layoutColumn1.addClassName("profile-view_row_col-1");
        layoutColumn1.addClassName(Padding.XLARGE);
        layoutColumn1.getStyle().set("flex-grow", "1");
        layoutColumn1.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn1.setAlignItems(Alignment.CENTER);
        layoutColumn1.setFlexGrow(1.0, layoutColumn1sub);
        layoutColumn1.setAlignSelf(FlexComponent.Alignment.CENTER, avatar);

        // ####### PROFILE VIEW - Column 1 Sub
        layoutColumn1sub.addClassName("profile-view_row_col-1-sub");
        layoutColumn1sub.getStyle().set("flex-grow", "1");
        layoutColumn1sub.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn1sub.setAlignItems(Alignment.CENTER);

        // ####### PROFILE VIEW - Column 2
        layoutColumn2.addClassName("profile-view_row_col-2");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, speichernButton, abbrechenButton);
        layoutColumn2.setHeightFull();
        layoutColumn2.setWidth("100%");

        // ####### PROFILE VIEW - Column 3
        layoutColumn3.addClassName("profile-view_row_col-3");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setHeightFull();
        layoutColumn3.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutColumn3.setAlignItems(Alignment.CENTER);

        // ####### PROFILE VIEW - Headings
        h2.setText("Profileinstellungen");

        // ####### PROFILE VIEW - OrcaSoft Logo
        icon.addClassName("profile-view_logo");

        // ####### PROFILE VIEW - Profil Avatar (Unternehmen / Student)
        avatar.addClassName("profile-view_profile_image");
        avatar.setName( "Max Mustermann" );
        avatarMail.setText("orca@soft.de");
        avatarMail.setWidth("max-content");

        // ####### PROFILE VIEW - Profil Avatar Liste (offene Stellenanzeigen / Bewerbungen)
        avatarItems.addClassName("profile-view_avatar-items");
        setAvatarItemsSampleData(avatarItems);

        // ####### PROFILE VIEW - Profil - Forms
        profileForm1.addClassName("profile-view_profile_form-1");
        profileForm2.addClassName("profile-view_profile_form-2"); // Bisher noch unbekanntest Element ?!

        // ####### PROFILE VIEW - Profil Informationen (Unternehmen / Student)
        nameField.setLabel("Name");
        nameField.setErrorMessage("Dieser Name wird bereits verwendet!");
        nameField.setWidth("min-content");

        vornameField.setLabel("Vorname");
        vornameField.setWidth("min-content");

        emailField.setLabel("E-Mail");
        emailField.setRequired(true);
        emailField.setPlaceholder("example@mail.com");
        emailField.setWidth("min-content");

        telefonnummerField.setLabel("Telefonnummer");
        telefonnummerField.setPlaceholder("+49 12345 67890");
        telefonnummerField.setErrorMessage("Eine Telefonnummer besteht nur aus Zahlen!");
        telefonnummerField.setWidth("100%");

        matrikelnummerWebsiteField.setLabel("Matrikelnummer");
        matrikelnummerWebsiteField.setRequired(true);
        matrikelnummerWebsiteField.setWidth("100%");

        datePicker.setLabel("Geburtsdatum");
        datePicker.setWidth("100%");

        strasseField.setPlaceholder("Musterstrasse");
        strasseField.setLabel("Straße");
        strasseField.setWidth("min-content");

        hausnummerField.setPlaceholder("1");
        hausnummerField.setLabel("Hausnummer");
        hausnummerField.setErrorMessage("Eine Hausnummer besteht nur aus Zahlen!");
        hausnummerField.setWidth("min-content");

        plzField.setPlaceholder("12345");
        plzField.setLabel("PLZ");
        plzField.setErrorMessage("Eine PLZ besteht nur aus Zahlen!");
        plzField.setWidth("min-content");

        ortField.setPlaceholder("Musterstadt");
        ortField.setLabel("Ort");
        ortField.setWidth("min-content");

        setSelectionBoxes(skillComboBox, true);
        setSelectionBoxes(sprachenComboBox, false);

        ausbildungOrBioMultilineText.setLabel("Ausbildung");
        ausbildungOrBioMultilineText.getStyle().set("flex-grow", "1");
        ausbildungOrBioMultilineText.setWidth("100%");

        berufserfahrungMultilineText.setLabel("Berufserfahrung");
        berufserfahrungMultilineText.setWidth("100%");

        studiengangBrancheComboBox.setLabel("Studiengang");
        studiengangBrancheComboBox.setWidth("100%");

        skillComboBox.setPlaceholder("Welche Skills besitzt du?");
        skillComboBox.setLabel("Skills");
        skillComboBox.setWidth("100%");

        sprachenComboBox.setPlaceholder("Welche Sprachen sprichst du?");
        sprachenComboBox.setLabel("Sprachen");
        sprachenComboBox.setWidth("100%");

        // ####### PROFILE VIEW - Button (Speichern / Abbrechen - Profil Informationen)
        speichernButton.addClassName("profile-view_button-senden");
        speichernButton.setText("Bearbeiten");
        speichernButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        abbrechenButton.addClassName("profile-view_button-abbrechen");
        abbrechenButton.setVisible(false);

        // ####### PROFILE VIEW - Button (Hochladen / Löschen - Profilbild)
        // ToDo Tomcat erlaubt nur ≈ 1 MB an upload -> für PDF ändern!
        uploadImage.setMaxFileSize(1024 * 1024); // 1 MB
        uploadImage.setMaxFiles(1);
        uploadImage.setAcceptedFileTypes(".png");
        uploadImage.setDropAllowed(false);
        uploadImage.setUploadButton( uploadImageButton );
        uploadImage.setVisible(false);
        uploadImageButton.setText( currentUser.getProfilePicture() == null ? "Profilbild hochladen" : "Profilbild aktualisieren");

        deletePBButton.addClassName("profile-view_button-delete");

        lebenslaufLoeschenButton.getStyle().set("background", "indianred");
        lebenslaufLoeschenButton.getStyle().set("color", "white");
        lebenslaufLoeschenButton.getStyle().set("cursor", "pointer");

        // ####### PROFILE VIEW - Button (Hochladen / Löschen - Lebenslauf)

        uploadLebenslauf.setMaxFileSize(1024 * 1024); // 1 MB
        uploadLebenslauf.setMaxFiles(1);
        uploadLebenslauf.setAcceptedFileTypes(".pdf");
        uploadLebenslauf.setDropAllowed(false);
        uploadLebenslauf.setUploadButton( uploadLebenslaufButton );
        uploadLebenslauf.setVisible(false);

        uploadLebenslaufButton.getStyle().set("cursor", "pointer");

        // ####### PROFILE VIEW - Inhaltselemente dem Layout hinzufügen
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn1);
        layoutRow.add(layoutColumn2);
        layoutRow.add(layoutColumn3);

        h42.getStyle().set("text-align", "center");
        layoutColumn1sub.add(h42);
        layoutColumn1sub.add(avatarItems);

        layoutColumn2.add(h2);
        layoutColumn2.add(hr4);
        layoutColumn2.add(profileForm1);
        layoutColumn2.add(profileForm2);
        layoutColumn2.add(ausbildungOrBioMultilineText);
        layoutColumn2.add(berufserfahrungMultilineText);
        layoutColumn2.add(speichernButton);
        layoutColumn2.add(abbrechenButton);

        layoutColumn3.add(icon);

        layoutColumn1.add(avatar);
        layoutColumn1.add(avatarMail);

        deleteAccountButton.setText("Konto löschen");
        deleteAccountButton.getStyle().set("color", "white");
        deleteAccountButton.getStyle().set("cursor", "pointer");
        deleteAccountButton.getStyle().set("background", "indianred");

        layoutColumn1.add(deleteAccountButton);
        layoutColumn1.add(hr);

        lebenslaufDownload = getLebenslaufDownload();
        if ( lebenslaufDownload != null ) {
            lebenslaufDownload.setVisible(true);
            layoutColumn1.add(lebenslaufDownload);
            layoutColumn1.add(hr3);
        }
        else hr3.setVisible(false);

        layoutColumn1.add(uploadStatus);
        layoutColumn1.add(uploadImage);
        layoutColumn1.add(deletePBButton);
        layoutColumn1.add(hr2);
        layoutColumn1.add(lebenslaufStatus);
        layoutColumn1.add(uploadLebenslauf);
        layoutColumn1.add(lebenslaufLoeschenButton);
        layoutColumn1.add(layoutColumn1sub);

        profileForm1.add(nameField);
        profileForm1.add(vornameField);
        profileForm1.add(emailField);
        profileForm1.add(matrikelnummerWebsiteField);
        profileForm1.add(datePicker);
        profileForm1.add(studiengangBrancheComboBox);
        profileForm1.add(telefonnummerField);
        profileForm1.add(strasseField);
        profileForm1.add(hausnummerField);
        profileForm1.add(plzField);
        profileForm1.add(ortField);
        profileForm1.add(IBANField);

        profileForm2.add(skillComboBox);
        profileForm2.add(sprachenComboBox);

        // EventListener bitte hier einfügen

        nameField.setValueChangeMode(ValueChangeMode.EAGER);

        nameField.addValueChangeListener(e -> { if(fieldsAreActive) checkCompanyNameField(nameField.getValue()); } );

        nameField.addValidationStatusChangeListener(e -> { if(fieldsAreActive) checkCompanyNameField(nameField.getValue()); } );

        emailField.setValueChangeMode(ValueChangeMode.EAGER);

        emailField.addValueChangeListener(e -> { if(fieldsAreActive) checkEMailField(emailField.getValue()); } );

        emailField.addValidationStatusChangeListener(e -> { if(fieldsAreActive) checkEMailField(emailField.getValue()); } );

        matrikelnummerWebsiteField.setValueChangeMode(ValueChangeMode.EAGER);

        matrikelnummerWebsiteField.addValueChangeListener(e -> { if(fieldsAreActive && isStudentProfile) checkMatrikelnummerField(matrikelnummerWebsiteField.getValue()); } );

        matrikelnummerWebsiteField.addValidationStatusChangeListener(e -> { if(fieldsAreActive && isStudentProfile) checkMatrikelnummerField(matrikelnummerWebsiteField.getValue()); } );

        hausnummerField.setValueChangeMode(ValueChangeMode.EAGER);

        hausnummerField.addValueChangeListener(e -> { if(fieldsAreActive) refreshSpeicherButton(); } );

        hausnummerField.addValidationStatusChangeListener(e -> { if(fieldsAreActive) refreshSpeicherButton(); } );

        plzField.setValueChangeMode(ValueChangeMode.EAGER);

        plzField.addValueChangeListener(e -> { if(fieldsAreActive) refreshSpeicherButton(); } );

        plzField.addValidationStatusChangeListener(e -> { if(fieldsAreActive) refreshSpeicherButton(); } );

        telefonnummerField.setValueChangeMode(ValueChangeMode.EAGER);

        telefonnummerField.addValueChangeListener(e -> { if(fieldsAreActive) refreshSpeicherButton(); } );

        telefonnummerField.addValidationStatusChangeListener(e -> { if(fieldsAreActive) refreshSpeicherButton(); } );

        datePicker.addValueChangeListener(e -> { if(fieldsAreActive && !isStudentProfile) refreshSpeicherButton(); } );

        speichernButton.addClickListener(e -> {

            speichernButton.setEnabled(false);

            if ( fieldsAreActive ) {
                lockInputFields();
                refreshSpeicherButton();
                abbrechenButton.setVisible(false);
                updateCurrentUser();
                speichernButton.setText("Bearbeiten");
                refreshWhileSaving = true;
                refreshMessage = "Deine Änderungen wurden übernommen.";
                sessionController.refreshPage();
            } else {
                unlockInputFields();
                refreshSpeicherButton();
                abbrechenButton.setVisible(true);
                speichernButton.setText("Speichern");
            }

            fieldsAreActive = !fieldsAreActive;

        });

        abbrechenButton.addClickListener(e -> sessionController.refreshPage() );

        avatarItems.addSelectionListener(e -> {

            if ( !e.getValue().isEmpty() ) {
                try {
                    // e.getValue().toArray() ein Array aus den selektieren Werten generieren.
                    if ( isStudentProfile ) {
                        BewerbungView.viewType = 2;
                        BewerbungView.bewerbungDTO = bewerbungControl.getBewerbungByID( ((BewerbungControl.BewerbungFromStudent) e.getValue().toArray()[0]).id(), currentUser.getId() );
                        BewerbungView.name = ((BewerbungControl.BewerbungFromStudent) e.getValue().toArray()[0]).titel();
                        sessionController.redirectToPage( Globals.Pages.BEWERBUNG );
                    } else {
                        StellenangebotView.ausschreibungDTO = ausschreibungControl.getAusschreibungByID( ((AusschreibungControl.Ausstellung) e.getValue().toArray()[0]).ID() );
                        sessionController.redirectToPage( Globals.Pages.STELLENANGEBOT );
                    }
                } catch ( DatabaseException ex ) {
                    notification = Notification.show(ex.getMessage());
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
            }

        });

        uploadImage.addSucceededListener(event -> {
            // Dateierweiterung ermitteln
            String file = fileBuffer.getFileName();
            String fileExtension = file.substring(file.lastIndexOf("."));
            System.out.println("fileExtension: " + fileExtension);

            File profilePicture = fileBuffer.getFileData().getFile();
            uploadFiles( profilePicture, fileExtension, uploadStatus, "profile picture" );
            profilePicture.deleteOnExit();
        });

        uploadLebenslauf.addSucceededListener(event -> {
            File lebenslaufFile = fileBuffer.getFileData().getFile();
            uploadFiles( lebenslaufFile, null, lebenslaufStatus, "lebenslauf" );
            lebenslaufFile.deleteOnExit();
        });

        uploadImage.addFileRejectedListener(event -> setErrorTextOnUpload( uploadStatus, event.getErrorMessage() ) );

        uploadLebenslauf.addFileRejectedListener(event -> setErrorTextOnUpload( lebenslaufStatus, event.getErrorMessage() ) );

        deletePBButton.addClickListener(e -> deleteFiles( uploadStatus, "profile picture" ));

        lebenslaufLoeschenButton.addClickListener(e -> deleteFiles( lebenslaufStatus, "lebenslauf" ));

        deleteAccountButton.addClickListener(e -> getLoeschenDialog( currentUser.getId() ).open());

    }

    private void deleteFiles( Paragraph statusText, String fileType ) {

        System.out.println("[Löschen] Datei wird gelöscht...");

        try {

            switch ( fileType ) {

                case "profile picture":
                    ((UserDTOImpl) currentUser).setProfilePicture(null);
                    userDAO.deleteProfilePicture( currentUser.getId() );
                    uploadImageButton.setText("Profilbild hochladen");
                    deletePBButton.setVisible(false);
                    avatar.setImageResource(null);
                    break;
                case "lebenslauf":
                    ((StudentDTOImpl) currentUser).setLebenslauf(null);
                    userDAO.deleteLebenslauf( currentUser.getId() );
                    lebenslaufDownload.setVisible(false);
                    hr3.setVisible(false);
                    uploadLebenslaufButton.setText("Lebenslauf hochladen");
                    lebenslaufLoeschenButton.setVisible(false);
                    break;
                default:
                    throw new IllegalArgumentException("[Löschen] Unbekannten Datentyp angegeben!");

            }

            statusText.getStyle().set("color", "green");
            statusText.setText("Löschen war erfolgreich!");
            System.out.println("[Löschen] Löschen war erfolgreich!\n");

        } catch ( DatabaseException e ) {
            System.out.println("[Löschen] ERROR : " + e.getMessage());
        } finally {
            abbrechenButton.setVisible(false);
        }

    }

    /**
     * Erstellt eine Instanz von einem Dialog, der eine
     * Bestätigung des Nutzers zum Löschen des Kontos fordert.
     *
     * @return Referenz auf die Dialog-Instanz.
     */

    private Dialog getLoeschenDialog( int ID_User ) {

        Dialog deleteDialog = new Dialog("Konto löschen");
        Paragraph text = new Paragraph("Möchten Sie wirklich Ihr Konto löschen? Dies kann nicht mehr rückgängig gemacht werden.");

        Button loeschenButton = new Button("Konto Löschen");
        Button abbrechenButton = new Button("Abbrechen");

        loeschenButton.getStyle().set("background", "indianred");
        loeschenButton.getStyle().set("color", "white");
        loeschenButton.getStyle().set("cursor", "pointer");
        abbrechenButton.getStyle().set("cursor", "pointer");

        loeschenButton.addClickListener(e -> {
           try {
               text.setText("Dein Konto wird gelöscht...");
               userDAO.deleteUser( ID_User );
           } catch ( DatabaseException ex ) {
               notification = Notification.show("Dein Konto konnte nicht gelöscht werden! " + ex.getMessage());
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
     * Diese Methode zentralisiert den Upload von den
     * Profilbildern oder der Lebensläufe und kann
     * beliebig erweitert werden.
     *
     * @param file Datei, die hochgeladen wird.
     * @param statusText Statustext, der in der View zu sehen ist.
     * @param uploadType Was für eine Datei wird hochgeladen?
     */

    private void uploadFiles( File file, String fileExtension, Paragraph statusText, String uploadType ) {

        try {

            System.out.println("[Upload] Upload wird vorbereitet...");
            final byte[] newFileData = Files.readAllBytes( Paths.get( file.getAbsolutePath()) );
            StreamResource fileResource;
            System.out.println("[Upload] Datei '" + file.getName() + "' wird hochgeladen...");

            switch ( uploadType ) {

                case "profile picture":
                    fileResource = new StreamResource("ProfilePicture" + fileExtension, () -> new ByteArrayInputStream(newFileData));
                    userDAO.updateProfilePicture( currentUser.getId(), file );
                    ((UserDTOImpl) currentUser).setProfilePicture( fileResource );
                    avatar.setImageResource( currentUser.getProfilePicture() );
                    break;

                case "lebenslauf":
                    fileResource = new StreamResource( "Lebenslauf.pdf", () -> new ByteArrayInputStream( newFileData ));
                    userDAO.updateLebenslauf( currentUser.getId(), file );
                    ((StudentDTOImpl) currentUser).setLebenslauf( fileResource );
                    break;
                default:
                    throw new IllegalArgumentException("[Upload] Unbekannten Datentyp angegeben!");

            }

            statusText.getStyle().set("color", "green");
            statusText.setText("Upload war erfolgreich!");
            System.out.println("[Upload] Upload war erfolgreich!\n");

        } catch ( Exception e ) {
            System.out.println("[Upload] ERROR : " + e.getMessage());
            statusText.getStyle().set("color", "red");
            statusText.setText("Upload ist schiefgelaufen! Bitte versuche es später nochmal.\n");
        } finally {
            abbrechenButton.setVisible(false);
        }

    }

    /**
     * Je nach Upload und Fehler wird der jeweilige Statustext
     * geändert bzw. aktualisiert.
     *
     * @param statusText Instanz des Paragrafen in der View.
     * @param errorMessage Error-Nachricht.
     */

    private void setErrorTextOnUpload( Paragraph statusText, String errorMessage ) {

        System.out.println("[Upload] Fehler beim Upload! ( " + errorMessage + " )");
        statusText.getStyle().set("color", "red");
        switch ( errorMessage ) {
            case "Incorrect File Type.":
                statusText.setText("Bitte achte auf den Typ der Datei!");
                break;
            case "File is Too Big.":
                statusText.setText("Die Datei ist zu groß: 1 MB!");
                break;
            default:
                statusText.setText("Unbekannter Fehler!");
                break;
        }
    }

    /**
     * Fügt die AvatarItems in die MultiSelectListBox ein.
     * @param multiSelectListBox Referenz auf die MultiSelectListBox.
     */

    private void setAvatarItemsSampleData( MultiSelectListBox multiSelectListBox ) {
        try {
            if ( isStudentProfile ) {
                bewerbungControl.bewerbungenListeBefuellen( multiSelectListBox, currentUser );
            } else {
                ausschreibungControl.ausschreibungslisteBefuellen( multiSelectListBox, currentUser );
            }
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
        if ( isStudentProfile ) speichernButton.setEnabled( !emailField.isInvalid() && !matrikelnummerWebsiteField.isInvalid() && !plzField.isInvalid() && !hausnummerField.isInvalid() && !datePicker.isInvalid() );
        else speichernButton.setEnabled( !emailField.isInvalid() && !plzField.isInvalid() && !hausnummerField.isInvalid() && !nameField.isEmpty() && !nameField.isInvalid() );
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

        if ( null == currentUser ) {
            beforeEnterEvent.rerouteTo(Globals.Pages.LOGIN_VIEW);
            return;
        }

        setUp();

        if ( refreshWhileSaving ) {
            Notification notification = Notification.show(refreshMessage);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setPosition(Notification.Position.TOP_CENTER);
            refreshWhileSaving = false;
        }

    }

    /**
     * Baut die View auf.
     */

    private void setUp() {

        avatar.setImageResource( currentUser.getProfilePicture() );
        uploadImageButton.getStyle().set("cursor", "pointer");
        avatarMail.setText( currentUser.getEmail() );
        System.out.println("[Profil] Nutzer hat sein Profil aufgerufen.");
        System.out.println("[Profil] Nutzerdaten werden ermittelt...");

        if ( isStudentProfile ) showStudentFields();
        else {
            hr3.setVisible(false);
            showCompanyFields();
        }

        System.out.println("[Profil] Nutzerdaten wurden ermittelt und view wird dargestellt.\n");

    }

    /**
     * Aktualisiert den aktuell angemeldeten Nutzer.
     */

    private void updateCurrentUser() {

        if ( isStudentProfile ) {

            System.out.println("[Update] StudentDTO wird aktualisiert...");

            try {
                updateStudentUser();
                System.out.println("[Update] StudentDTO wurde erfolgreich aktualisiert!\n");
            } catch ( DatabaseException e ) {
                System.out.println("[Update] Nutzer konnte nicht aktualisiert werden!\n");
            }

        } else {

            System.out.println("[Update] CompanyDTO wird aktualisiert...");

            try {
                updateCompanyUser();
                System.out.println("[Update] CompanyDTO wurde erfolgreich aktualisiert!\n");
            } catch ( DatabaseException e ) {
                System.out.println("[Update] Nutzer konnte nicht aktualisiert werden!\n");
            }
        }
    }

    /**
     * Aktualisiert den aktuell angemeldeten Nutzer (Student).
     *
     * @throws DatabaseException Fehler mit der Datenbank.
     */

    private void updateStudentUser() throws DatabaseException {

        StudentDTOImpl studentDTO = (StudentDTOImpl) currentUser;

        studentDTO.setFirstName( vornameField.getValue() );
        studentDTO.setLastName( nameField.getValue() );
        studentDTO.setEmail( emailField.getValue() );
        studentDTO.setMatrikelnummer( Integer.parseInt( matrikelnummerWebsiteField.getValue() ) );
        studentDTO.setHausnummer( hausnummerField.getValue().intValue() );
        studentDTO.setPlz( plzField.getValue().intValue() );
        studentDTO.setDateOfBirth( java.sql.Date.valueOf(datePicker.getValue()) );

        // Hier wird der Index bzw. Schlüsselwert des Studiengangs übergeben
        studentDTO.setStudiengang( String.valueOf(((SampleItem) studiengangBrancheComboBox.getValue()).value) );

        studentDTO.setSprachen( getMultiSelectBoxIDs( sprachenComboBox ) );
        studentDTO.setSkills( getMultiSelectBoxIDs( skillComboBox ) );

        studentDTO.setStrasse( strasseField.getValue() );
        studentDTO.setOrt( ortField.getValue() );
        studentDTO.setAusbildung( ausbildungOrBioMultilineText.getValue() );
        studentDTO.setBerufserfahrung( berufserfahrungMultilineText.getValue() );

        userDAO.updateUser( studentDTO );

        // Hier werden wieder die labels als Studiengänge, Skills und Sprache gesetzt (lesbar)
        studentDTO.setSprachen( getMultiSelectBoxLabel( sprachenComboBox ) );
        studentDTO.setSkills( getMultiSelectBoxLabel( skillComboBox ) );
        studentDTO.setStudiengang( ((SampleItem) studiengangBrancheComboBox.getValue()).label );
        sessionController.setCurrentUser( studentDTO );

    }

    /**
     * Aktualisiert den aktuell angemeldeten Nutzer (Unternehmen).
     *
     * @throws DatabaseException Fehler mit der Datenbank.
     */

    private void updateCompanyUser() throws DatabaseException {

        CompanyDTOImpl companyDTO = (CompanyDTOImpl) currentUser;

        companyDTO.setEmail( emailField.getValue() );
        companyDTO.setStrasse( strasseField.getValue() );
        companyDTO.setOrt( ortField.getValue() );
        companyDTO.setPlz( plzField.getValue().intValue() );
        companyDTO.setHausnummer( hausnummerField.getValue().intValue() );

        companyDTO.setBranche( String.valueOf(((SampleItem) studiengangBrancheComboBox.getValue()).value) );
        companyDTO.setBezeichnung( nameField.getValue() );
        companyDTO.setBio( ausbildungOrBioMultilineText.getValue() );
        companyDTO.setPhone( String.valueOf(telefonnummerField.getValue()) );
        companyDTO.setWebsite( matrikelnummerWebsiteField.getValue() );
        companyDTO.setIBAN( IBANField.getValue() );

        userDAO.updateUser( companyDTO );

        companyDTO.setBranche( ((SampleItem) studiengangBrancheComboBox.getValue()).label );
        sessionController.setCurrentUser( companyDTO );

    }

    /**
     * Gibt eine Liste an IDs zurück, die in der Liste
     * ausgewählt wurden. Diese werden für das Update gebraucht.
     *
     * @param box Selektionsbox
     * @return Liste an IDs
     */

    private List<String> getMultiSelectBoxIDs( MultiSelectComboBox box ) {

        List<String> skillsSpracheIDs = new ArrayList<>();

        for ( Object o : box.getValue().toArray() ) skillsSpracheIDs.add( String.valueOf(((SampleItem) o).value) );

        return skillsSpracheIDs;

    }

    /**
     * Gibt eine Liste an Label zurück, die in der Liste
     * ausgewählt wurden. Dies wird benötigt, um den angemeldeten
     * Nutzer aktualisieren (label sind lesbar & eindeutig).
     *
     *  @param box Selektionsbox
     *  @return Liste an IDs
     */

    private List<String> getMultiSelectBoxLabel( MultiSelectComboBox box ) {

        List<String> skillsSprachePure = new ArrayList<>();

        for ( Object o : box.getValue().toArray() ) skillsSprachePure.add( String.valueOf(((SampleItem) o).label) );

        return skillsSprachePure;

    }

    /**
     * Blendet die Felder, die keine Attribute von
     * Unternehmen zeigen aus bzw. ein.
     */

    private void showStudentFields() {
        telefonnummerField.setVisible(false);
        IBANField.setVisible(false);
        studiengangBrancheComboBox.setVisible(true);
        nameField.setPlaceholder("Mustermann");
        vornameField.setPlaceholder("Max");
        matrikelnummerWebsiteField.setPlaceholder("0123456");
        studiengangBrancheComboBox.setPlaceholder("Bitte wähle einen Studiengang aus");
        ausbildungOrBioMultilineText.setPlaceholder("Schreibe über deine Ausbildung...");
        berufserfahrungMultilineText.setPlaceholder("Schreibe über deine Berufserfahrung...");
        fillStudentFieldsFromDTO();
    }

    /**
     * Blendet die Felder, die keine Attribute von
     * Unternehmen zeigen aus.
     */

    private void showCompanyFields() {
        vornameField.setVisible(false);
        matrikelnummerWebsiteField.setLabel("Website (URL)");
        datePicker.setVisible(false);
        studiengangBrancheComboBox.setLabel("Branche");
        skillComboBox.setVisible(false);
        sprachenComboBox.setVisible(false);
        berufserfahrungMultilineText.setVisible(false);
        ausbildungOrBioMultilineText.setLabel("Bio");
        matrikelnummerWebsiteField.setPlaceholder("https://www.example.com");
        studiengangBrancheComboBox.setPlaceholder("Bitte wähle ein Branche aus");
        ausbildungOrBioMultilineText.setPlaceholder("Schreibe über dich...");
        fillCompanyFieldsFromDTO();
    }

    /**
     * Diese Methode fügt die Attribute, die sowohl Student als
     * auch Unternehmer haben in die Felder ein.
     */

    private void fillInDefaultAttributes() {

        emailField.setValue( currentUser.getEmail() );
        strasseField.setValue( currentUser.getStrasse() == null ? "" : currentUser.getStrasse());
        hausnummerField.setValue( Double.valueOf(String.valueOf(currentUser.getHausnummer())) );
        plzField.setValue( Double.valueOf(String.valueOf(currentUser.getPLZ())) );
        ortField.setValue( currentUser.getOrt() == null ? "" : currentUser.getOrt() );

    }

    /**
     * Füllt die Felder des Unternehmens mit den Daten aus dem CompanyDTO.
     */

    private void fillCompanyFieldsFromDTO() {

        h42.setText("Aktuelle Ausschreibungen");
        setSelectionBoxes(studiengangBrancheComboBox, false );
        fillInDefaultAttributes();

        CompanyDTO currentUser = (CompanyDTO) this.currentUser;

        avatar.setName( currentUser.getBezeichnung() );
        nameField.setValue( currentUser.getBezeichnung() );
        IBANField.setValue( currentUser.getIBAN() == null ? "" : currentUser.getIBAN() );
        ausbildungOrBioMultilineText.setValue( currentUser.getBio() == null ? "" : currentUser.getBio() );
        matrikelnummerWebsiteField.setValue( currentUser.getWebsite() == null ? "" : currentUser.getWebsite() );
        telefonnummerField.setValue( Double.valueOf(String.valueOf(currentUser.getPhone() == null ? 0 : currentUser.getPhone() )) );

        studiengangBrancheComboBox.setValue( new SampleItem( studiengaengeBranche.indexOf(currentUser.getBranche())+1, currentUser.getBranche(), null ) );

    }

    /**
     * Füllt die Felder des Studenten mit den Daten aus dem StudentDTO.
     */

    private void fillStudentFieldsFromDTO() {

        h42.setText("Aktuelle Bewerbungen");
        setSelectionBoxes(studiengangBrancheComboBox, false );
        fillInDefaultAttributes();

        StudentDTOImpl currentUser = (StudentDTOImpl) this.currentUser;

        avatar.setName( currentUser.getFirstName() + " " + currentUser.getLastName() );
        nameField.setValue( currentUser.getLastName() == null ? "" : currentUser.getLastName());
        vornameField.setValue( currentUser.getFirstName() == null ? "" : currentUser.getFirstName());

        if ( currentUser.getMatrikelnummer() == 0 ) {
            matrikelnummerWebsiteField.setErrorMessage("Du musst deine Matrikelnummer angeben! (1x möglich ansonsten Support kontaktieren)");
            matrikelnummerWebsiteField.setInvalid(true);
        } else {
            matrikelnummerWebsiteField.setValue( String.valueOf(currentUser.getMatrikelnummer()));
        }

        datePicker.setValue(currentUser.getDateOfBirth() == null ? LocalDate.now() : currentUser.getDateOfBirthAsLocalDate() );
        studiengangBrancheComboBox.setValue( new SampleItem( studiengaengeBranche.indexOf(currentUser.getStudiengang())+1, currentUser.getStudiengang(), null) );
        ausbildungOrBioMultilineText.setValue( currentUser.getAusbildung() == null ? "" : currentUser.getAusbildung()  );
        berufserfahrungMultilineText.setValue( currentUser.getBerufserfahrung() == null ? "" : currentUser.getBerufserfahrung() );
        fillSpracheAndSkill();

    }

    /**
     * Überträgt die Sprachen und Skills eines Nutzers in die
     * View (Markieren, was der Nutzer hat).
     */

    private void fillSpracheAndSkill() {

        List<SampleItem> skills = new ArrayList<>();
        List<SampleItem> sprachen = new ArrayList<>();

        List<String> studentSprachen = ((StudentDTO) currentUser).getSprachen();
        List<String> studentSkills = ((StudentDTO) currentUser).getSkills();

        try {

            List<String> totalSprachen = spracheDAO.getAll();
            List<String> totalSkills = skillDAO.getAll();

            for ( String sprache : studentSprachen ) sprachen.add( new SampleItem( ( totalSprachen.indexOf(sprache)+1 ), sprache, null ) );

            for ( String skill : studentSkills ) skills.add( new SampleItem( ( totalSkills.indexOf(skill)+1 ), skill, null ) );

        } catch ( DatabaseException e ) {
            System.err.println("[Initialisierung] Skills/Sprachen können nicht geladen werden!\n");
            return;
        }

        skillComboBox.setValue( skills );
        sprachenComboBox.setValue( sprachen );

    }

    /**
     * Sperrt alle Eingabefelder in der View.
     */

    private void lockInputFields() {
        nameField.setReadOnly(true);
        vornameField.setReadOnly(true);
        emailField.setReadOnly(true);
        matrikelnummerWebsiteField.setReadOnly(true);
        datePicker.setReadOnly(true);
        telefonnummerField.setReadOnly(true);
        strasseField.setReadOnly(true);
        hausnummerField.setReadOnly(true);
        plzField.setReadOnly(true);
        ortField.setReadOnly(true);
        skillComboBox.setReadOnly(true);
        sprachenComboBox.setReadOnly(true);
        ausbildungOrBioMultilineText.setReadOnly(true);
        berufserfahrungMultilineText.setReadOnly(true);
        studiengangBrancheComboBox.setReadOnly(true);
        IBANField.setReadOnly(true);
        uploadImage.setVisible(false);
        uploadStatus.setVisible(false);
        deletePBButton.setVisible(false);
        uploadLebenslauf.setVisible(false);
        lebenslaufStatus.setVisible(false);
        lebenslaufLoeschenButton.setVisible(false);
        deleteAccountButton.setVisible(false);
        hr2.setVisible(false);
    }

    /**
     * Gibt einen Download-Link zurück, wo man den
     * Lebenslauf herunterladen kann.
     *
     * @return View-Component (Link/Anchor)
     */

    private Anchor getLebenslaufDownload() {
        if ( isStudentProfile && ((StudentDTO) currentUser).getLebenslauf() != null ) {
            Anchor lebenslaufDownloadLink = new Anchor( ((StudentDTO) currentUser).getLebenslauf(), "Lebenslauf herunterladen" );
            lebenslaufDownloadLink.getElement().setAttribute("download", true);
            return lebenslaufDownloadLink;
        }
        return null;
    }

    /**
     * Entsperrt alle Eingabefelder in der View.
     */

    private void unlockInputFields() {
        nameField.setReadOnly(false);
        vornameField.setReadOnly(false);
        emailField.setReadOnly(false);

        if ( isStudentProfile ) {

            boolean doesStudentHaveLebenslauf = ((StudentDTO) currentUser).getLebenslauf() != null;

            uploadLebenslauf.setVisible(true);
            uploadLebenslaufButton.setText( !doesStudentHaveLebenslauf ? "Lebenslauf hochladen" : "Lebenslauf aktualisieren" );
            lebenslaufStatus.setVisible(true);
            lebenslaufLoeschenButton.setVisible( doesStudentHaveLebenslauf );

            // Wenn keine Matrikelnummer eingeben → immer aktiv, ansonsten wird das Feld gesperrt.
            if ( matrikelnummerWebsiteField.isEmpty() ) matrikelnummerWebsiteField.setReadOnly(false);

        } else {
            matrikelnummerWebsiteField.setReadOnly(false);
        }

        datePicker.setReadOnly(false);
        telefonnummerField.setReadOnly(false);
        strasseField.setReadOnly(false);
        hausnummerField.setReadOnly(false);
        plzField.setReadOnly(false);
        ortField.setReadOnly(false);
        skillComboBox.setReadOnly(false);
        sprachenComboBox.setReadOnly(false);
        ausbildungOrBioMultilineText.setReadOnly(false);
        berufserfahrungMultilineText.setReadOnly(false);
        studiengangBrancheComboBox.setReadOnly(false);
        IBANField.setReadOnly(false);
        uploadImage.setVisible(true);
        uploadStatus.setVisible(true);
        if ( currentUser.getProfilePicture() != null ) deletePBButton.setVisible(true);
        deleteAccountButton.setVisible(true);
        hr2.setVisible(true);
    }

    /**
     * Überprüft, ob der Name eines Unternehmens
     * in der Datenbank vorhanden ist.
     *
     * @param nameInput Name des Unternehmens.
     */

    private void checkCompanyNameField( String nameInput ) {
        if ( !isStudentProfile ) {
            try {
                if ( !nameInput.equals( ((CompanyDTO) currentUser).getBezeichnung() ) ) {
                    profileControl.doesCompanyNameExist( nameInput );
                    nameField.setInvalid(false);
                }
            } catch ( DatabaseException ex ) {
                nameField.setInvalid(true);
            } finally {
                refreshSpeicherButton();
            }
        }
    }

    /**
     * Überprüft, ob die E-Mail eines Nutzers
     * bereits in der Datenbank existiert.
     *
     * @param mailInput E-Mail des Nutzers.
     */

    private void checkEMailField( String mailInput ) {
        try {
            emailField.setErrorMessage("Bitte E-Mail überprüfen!");
            if ( !mailInput.equals( currentUser.getEmail() ) && !emailField.isInvalid() ) {
                profileControl.doesMailExist( mailInput );
                emailField.setInvalid(false);
            }
        } catch ( DatabaseException ex ) {
            emailField.setErrorMessage("Diese E-Mail wird bereits verwendet!");
            emailField.setInvalid(true);
        } finally {
            refreshSpeicherButton();
        }
    }

    /**
     * Überprüft die Matrikelnummer semantisch und
     * in der Datenbank.
     *
     * @param matrikelnummer übergebene Matrikelnummer.
     */

    public void checkMatrikelnummerField( String matrikelnummer ) {

        try {
            if ( matrikelnummer.length() < 7 ) {
                matrikelnummerWebsiteField.setErrorMessage("Die Matrikelnummer ist zu kurz! (" + matrikelnummer.length() + "/7)");
                matrikelnummerWebsiteField.setInvalid(true);
            } else if ( userDAO.doesMatrikelnummerExist( Integer.parseInt( matrikelnummer ) ) ) {
                matrikelnummerWebsiteField.setErrorMessage("Die Matrikelnummer existiert bereits!");
                matrikelnummerWebsiteField.setInvalid(true);
            } else {
                matrikelnummerWebsiteField.setInvalid(false);
            }

        } catch ( NumberFormatException e ) {
            matrikelnummerWebsiteField.setErrorMessage("Eine Matrikelnummer besteht nur aus Zahlen!");
            matrikelnummerWebsiteField.setInvalid(true);
        } catch ( DatabaseException | SQLException e ) {
            Notification.show("Es gab ein Problem mit der Datenbank! (" + e.getMessage() + ")" );
        } finally {
            refreshSpeicherButton();
        }

    }

    /**
     * Sind die Optionen in den Wahl-Boxen, die der
     * Nutzer auswählen kann.
     *
     * @param value Wert der Option.
     * @param label Label der Option. Ist sichtbar.
     * @param disabled ist diese Option wählbar?
     */

    record SampleItem(int value, String label, Boolean disabled) {
    }

    /**
     * Füllt die Selektion-Boxen mit Inhalt.
     *
     * @param comboBoxBase Referenz auf die Selektion-Box
     * @param isSkill Handelt es sich dabei, um die Skill-Box?
     */

    // Obertyp von MultiSelectComboBox & ComboBox -> ComboBoxBase
    public void setSelectionBoxes( ComboBoxBase comboBoxBase, boolean isSkill ) {

        List<SampleItem> sampleItems = new ArrayList<>();
        List<String> queryData = null;

        try {

            if ( comboBoxBase instanceof ComboBox ) {

                // Hier brauchen wir die Variable 'studiengaengeBranche' um
                // die selektieren Werte, als Indexe zu erhalten.

                if ( isStudentProfile ) {
                    studiengaengeBranche = studiengangDAO.getAll();
                } else {
                    studiengaengeBranche = brancheDAO.getAll();
                }
                queryData = studiengaengeBranche;

            } else {

                if ( isSkill ) {
                    queryData = skillDAO.getAll();
                } else {
                    queryData = spracheDAO.getAll();
                }

            }

        } catch ( DatabaseException e ) {
            Notification.show( e.getMessage() );
        }

        if ( queryData == null ) return;
        for ( int i = 0 ; i < queryData.size() ; i++ ) {
            sampleItems.add(new SampleItem((i+1), queryData.get(i), null));
        }

        comboBoxBase.setItems(sampleItems);
        comboBoxBase.setItemLabelGenerator(item -> ((SampleItem) item).label());

    }

}
