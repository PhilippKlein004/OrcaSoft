package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import org.hbrs.se2.project.hellocar.control.RegistrationControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.control.exception.PasswordException;
import org.hbrs.se2.project.hellocar.control.factories.UserFactory;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@PageTitle("OrcaSoft - Registrierung")

@CssImport(value = "./styles/views/registration-view.css")

@Route(value = Globals.Pages.REGISTER)
public class RegistrationView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    // Autowired → wird automatisch instanziiert. Die Klasse muss
    // dafür die Annotation @Component beinhalten!

    @Autowired
    private RegistrationControl registrationControl;

    @Autowired
    SessionController sessionController;

    // Liste z.B. aller Studiengänge, aus der sich ein registrierender
    // Student eins aussuchen bzw. wählen soll.

    private HorizontalLayout layoutRow = new HorizontalLayout();
    private VerticalLayout layoutColumn1 = new VerticalLayout();
    private VerticalLayout layoutColumn2 = new VerticalLayout();
    private Button buttonSecondary = new Button();
    private ProgressBar progressBar = new ProgressBar();
    private H1 h1 = new H1();
    private RadioButtonGroup radioGroup = new RadioButtonGroup();
    private ComboBox wahlBox = new ComboBox();
    private List<String> wahlBoxContent = new ArrayList<>();
    private TextField bezeichnung = new TextField();
    private EmailField email = new EmailField();
    private PasswordField passwort = new PasswordField();
    private PasswordField passwortCheck = new PasswordField();
    private Paragraph registrationDialog = new Paragraph();
    private Button registrierenKnopf = new Button();
    private Image successIcon = new Image("images/success.svg", "Status icon");
    private Image failIcon = new Image("images/failure.svg", "Status icon");
    private Image icon = new Image("images/logo5.jpg", "logo");

    public RegistrationView() {

        // ####### REGISTRATION VIEW
        getContent().addClassName("registration-view");

        // ####### REGISTRATION VIEW - Row
        layoutRow.addClassName("registration-view_row");

        // ####### REGISTRATION VIEW - Column 1
        layoutColumn1.addClassName("registration-view_row_col-1");

        // ####### REGISTRATION VIEW - Column 2
        layoutColumn2.addClassName("registration-view_row_col-2");

        // ####### REGISTRATION VIEW - OrcaSoft Logo
        icon.addClassName("register-view_logo");

        // ####### REGISTRATION VIEW - Button ("Zurück")
        buttonSecondary.addClassName("registration-view_button-back");
        buttonSecondary.setText("< Zurück");

        // ####### REGISTRATION VIEW - Progress Bar
        progressBar.addClassName("registration-view_progress-bar");
        progressBar.setValue(0.01);

        // ####### REGISTRATION VIEW - Heading
        h1.setText("Willkommen bei OrcaSoft!");

        // ####### REGISTRATION VIEW - Radio Group
        radioGroup.addClassName("registration-view_radio-group");
        radioGroup.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        radioGroup.setLabel("Was sind Sie?");
        radioGroup.setItems("Student", "Unternehmen");

        // ####### REGISTRATION VIEW - Registrierung Informationen
        registrationDialog.addClassName("registration-view_registration-dialog");
        registrationDialog.setVisible(false);
        bezeichnung.addClassName("registration-view_name-field");
        bezeichnung.setLabel("Wie heißen Sie?");
        bezeichnung.setErrorMessage("Name existiert bereits!");
        bezeichnung.setVisible(false);
        email.addClassName("registration-view_email-field");
        email.setLabel("E-Mail");
        email.setErrorMessage("Bitte E-Mail überprüfen!");
        email.setVisible(false);
        passwort.addClassName("registration-view_passwort-field");
        passwort.setLabel("Passwort");
        passwort.setVisible(false);
        passwortCheck.addClassName("registration-view_passwort-check-field");
        passwortCheck.setLabel("Passwort überprüfen");
        passwortCheck.setErrorMessage("Passwörter stimmen nicht überein!");
        passwortCheck.setVisible(false);
        wahlBox.addClassName("registration-view_options-field");
        wahlBox.setInvalid(true);
        wahlBox.setVisible(false);

        // ####### REGISTRATION VIEW - Button ("Registrieren")
        registrierenKnopf.addClassName("registration-view_button-register");
        registrierenKnopf.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registrierenKnopf.setEnabled(false);
        registrierenKnopf.setVisible(false);
        registrierenKnopf.setText("Registrieren");

        // ####### REGISTRATION VIEW - Icon (Success & Fail)
        successIcon.addClassName("icon-success");
        successIcon.setVisible(false);
        failIcon.addClassName("icon-fail");
        failIcon.setVisible(false);

        // ####### LOGIN VIEW - Inhaltselemente dem Layout hinzufügen
        getContent().add(buttonSecondary);
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn1);
        layoutRow.add(layoutColumn2);
        layoutColumn1.add(progressBar);
        layoutColumn1.add(h1);
        layoutColumn1.add(radioGroup);
        layoutColumn1.add(successIcon);
        layoutColumn1.add(failIcon);
        layoutColumn1.add(registrationDialog);
        layoutColumn1.add(bezeichnung);
        layoutColumn1.add(email);
        layoutColumn1.add(passwort);
        layoutColumn1.add(passwortCheck);
        layoutColumn1.add(wahlBox);
        layoutColumn1.add(registrierenKnopf);
        layoutColumn2.add(icon);

        // Ab hier folgen und werden die EventListener bitte erzeugt.

        email.setValueChangeMode(ValueChangeMode.EAGER);

        email.addValidationStatusChangeListener(e -> {

            try {

                email.setErrorMessage("Bitte E-Mail überprüfen!");
                // SQL-Abfrage erst losschicken, wenn E-Mail-Format korrekt ist!
                if ( !email.isInvalid() ) registrationControl.doesMailExist( email.getValue() );

            } catch ( DatabaseException ex ) {

                email.setInvalid( true );
                email.setErrorMessage("E-Mail wird bereits verwendet!");

            }

            refreshRegisterButton();

        });

        bezeichnung.setValueChangeMode(ValueChangeMode.EAGER);

        bezeichnung.addValueChangeListener(e -> checkBezeichnung() );

        bezeichnung.addValidationStatusChangeListener(e -> checkBezeichnung() );

        passwort.setValueChangeMode(ValueChangeMode.EAGER);

        passwort.addValidationStatusChangeListener(e -> checkPasswort() );

        passwort.addValueChangeListener(e -> checkPasswort() );

        passwortCheck.setValueChangeMode(ValueChangeMode.EAGER);

        passwortCheck.addValidationStatusChangeListener(e -> arePasswordSame() );

        passwortCheck.addValueChangeListener(e -> arePasswordSame() );

        wahlBox.addValidationStatusChangeListener(e -> {
            wahlBox.setInvalid( wahlBox.getValue() == null );
            refreshRegisterButton();
        });

        buttonSecondary.addClickListener(e -> sessionController.redirectToPage(Globals.Pages.MAIN_VIEW) );

        // Eigentliche Registrierung des Benutzers

        registrierenKnopf.addClickListener(e -> {

            try {

                if ( radioGroup.getValue().equals("Student") ) {

                    registrationControl.registerStudentUser( UserFactory.createStudentDTOImplRegistration( email.getValue(), passwort.getValue(), String.valueOf(getWahlBoxSelectedIndex()) ));

                } else {

                    registrationControl.registerCompanyUser( UserFactory.createCompanyDTOImplRegistration( email.getValue(), passwort.getValue(), bezeichnung.getValue(), String.valueOf(getWahlBoxSelectedIndex()) ));

                }

                successIcon.setVisible(true);
                registrationDialog.setText("Ihr Konto wurde erfolgreich angelegt! Sie können nun zum Log in zurückkehren und sich anmelden.");

            } catch ( DatabaseException ex ) {

                failIcon.setVisible(true);
                registrationDialog.setText("Es ist ein Fehler aufgetreten! Bitte versuchen Sie es nach einer Weile erneut.");
                System.err.println("[Registration] Die Registrierung wurde abgebrochen! (" + ex.getMessage() + ")");

            } finally {

                progressBar.setValue(1);
                bezeichnung.setVisible(false);
                email.setVisible(false);
                passwort.setVisible(false);
                passwortCheck.setVisible(false);
                wahlBox.setVisible(false);
                registrierenKnopf.setVisible(false);
                registrationDialog.setVisible(true);

            }

        });

        radioGroup.addValueChangeListener(e -> {

            radioGroup.setVisible(false);
            email.setVisible(true);
            passwort.setVisible(true);
            passwortCheck.setVisible(true);
            registrierenKnopf.setVisible(true);
            wahlBox.setVisible(true);

            if ( e.getValue().equals("Student") ) {
                wahlBox.setLabel("Studiengang wählen");
                setComboBoxSampleData(wahlBox, true);
            } else {
                wahlBox.setLabel("Branche wählen");
                setComboBoxSampleData(wahlBox, false);
                bezeichnung.setVisible(true);
            }
            progressBar.setValue(0.5);

        });

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
     * Befüllt die Wahl-Boxen mit den aktuell verfügbaren Studiengängen
     * oder Branchen, je nachdem wer sich registriert.
     *
     * @param comboBox die Wahl-Box die geändert wird.
     * @param isStudent wird ein Student registriert?
     *                  Ja → Studiengänge anzeigen.
     *                  Nein → Branchen anzeigen.
     */

    private void setComboBoxSampleData( ComboBox comboBox, Boolean isStudent ) {
        List<SampleItem> listOptions = new ArrayList<>();
        List<String> studiengaenge = null;

        try {
            wahlBoxContent = isStudent ? registrationControl.getAllStudiengaenge() : registrationControl.getAllBranche();
            studiengaenge = wahlBoxContent;
        } catch ( DatabaseException e ) {
            Notification.show( e.getMessage() );
        }

        if ( studiengaenge == null ) return;
        for ( int i = 0 ; i < studiengaenge.size() ; i++ ) {
            listOptions.add(new SampleItem((i+1), studiengaenge.get(i), null));
        }

        comboBox.setItems(listOptions);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());

    }

    /**
     * Gibt den aktuell eingeloggten Benutz zurück.
     *
     * @return aktuell eingeloggten Benutzer.
     */

    private UserDTO getCurrentUser() {
        return sessionController.getCurrentUser();
    }

    /**
     * Aktualisiert die Verfügbarkeit des 'Registrieren'-Knopfes.
     */

    private void refreshRegisterButton() {
        if ( radioGroup.getValue().equals("Student") ) {
            registrierenKnopf.setEnabled( !wahlBox.isInvalid() && !email.isInvalid() && !passwort.isEmpty() && !passwort.isInvalid() && !passwortCheck.isEmpty() && !passwortCheck.isInvalid() );
        } else {
            registrierenKnopf.setEnabled( !wahlBox.isInvalid() && !email.isInvalid() && !passwort.isEmpty() && !passwort.isInvalid() && !bezeichnung.getValue().isEmpty() && !passwortCheck.isEmpty() && !passwortCheck.isInvalid() );
        }
    }

    /**
     * Gibt den Index der Wahl-Box zurück, den man ausgewählt hat.
     * Dieser dient auch direkt als Fremdschlüssel für die Datenbank.
     *
     * @return Fremdschlüssel für den Studiengang oder Branche.
     */

    private int getWahlBoxSelectedIndex() {

        // Da die Indexe der Arrays und die Primärschlüssel gleich sind, können
        // wir diesen hier übergeben. Somit ist kein weiterer Datentyp notwendig (2in1).

        return ((SampleItem) wahlBox.getValue()).value;
    }

    /**
     * Überprüft, ob ein Unternehmensname bereits in der Datenbank steht.
     */

    private void checkBezeichnung() {

        try {
            registrationControl.doesCompanyNameExist( bezeichnung.getValue() );
            bezeichnung.setInvalid(false);
        } catch ( DatabaseException ex ) {
            bezeichnung.setInvalid(true);
        }

        refreshRegisterButton();

    }

    /**
     * Überprüft die Semantik des Passworts.
     */

    private void checkPasswort() {

        try {
            registrationControl.checkPassword( passwort.getValue() );
            passwort.setInvalid(false);
        } catch ( PasswordException ex ) {
            passwort.setErrorMessage( ex.getMessage() );
            passwort.setInvalid(true);
        }

        refreshRegisterButton();
    }

    /**
     * Überprüft, ob die Passwörter aus den Eingabefeldern gleich sind.
     */

    private void arePasswordSame() {

        try {
            registrationControl.doPasswordsMatch( passwort.getValue(), passwortCheck.getValue() );
            passwort.setInvalid(false);
        } catch ( PasswordException ex ) {
            passwortCheck.setInvalid(true);
        }

        refreshRegisterButton();
    }

    /**
     * Wenn der Nutzer schon angemeldet ist, wird er auf die Suchseite
     * weitergeleitet. Erst wenn die Session geschlossen wurde, hat der
     * Nutzer wieder Zugriff auf die Registrierung.
     *
     * @param beforeEnterEvent ausgelöste Eventinstanz.
     */

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if ( null != getCurrentUser() ) sessionController.redirectToPage(Globals.Pages.SEARCH);
    }

}
