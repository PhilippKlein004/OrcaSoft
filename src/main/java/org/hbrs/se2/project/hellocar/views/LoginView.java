package org.hbrs.se2.project.hellocar.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import org.hbrs.se2.project.hellocar.control.LoginControl;
import org.hbrs.se2.project.hellocar.control.SessionController;
import org.hbrs.se2.project.hellocar.control.exception.DatabaseException;
import org.hbrs.se2.project.hellocar.dtos.UserDTO;
import org.hbrs.se2.project.hellocar.util.Globals;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;

@PageTitle("OrcaSoft - Log in")

@CssImport(value = "./styles/views/login-view.css")
@Route(value = Globals.Pages.MAIN_VIEW)
@RouteAlias(value = Globals.Pages.LOGIN_VIEW)
@Uses(Icon.class)
public class LoginView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    @Autowired
    SessionController sessionController;

    @Autowired
    LoginControl loginControl;

    private HorizontalLayout layoutRow   = new HorizontalLayout();
    private VerticalLayout layoutColumn1 = new VerticalLayout();
    private VerticalLayout layoutColumn2 = new VerticalLayout();
    private TextField usernameField = new TextField();
    private PasswordField passwordField = new PasswordField();
    private Button loginButton = new Button();
    private Image icon = new Image("images/logo5.jpg", "HelloCar logo");
    private H1 h1 = new H1();
    private Hr hr = new Hr();
    private Paragraph textMedium = new Paragraph();
    private RouterLink routerLink = new RouterLink();

    public LoginView() {

        // ####### LOGIN VIEW
        getContent().addClassName("login-view");

        // ####### LOGIN VIEW - Row
        layoutRow.addClassName("login-view_row");

        // ####### LOGIN VIEW - Column 1
        layoutColumn1.addClassName("login-view_row_col-1");

        // ####### LOGIN VIEW - Column 2
        layoutColumn2.addClassName("login-view_row_col-2");

        // ####### LOGIN VIEW - OrcaSoft Logo
        icon.addClassName("login-view_logo");
        icon.getElement().setAttribute("logo", "lumo:user");

        // ####### LOGIN VIEW - Heading
        h1.setText("Willkommen zurück!");

        // ####### LOGIN VIEW - Login Username Field
        usernameField.addClassName("login-view_username-field");
        usernameField.setLabel("E-Mail *");
        usernameField.setErrorMessage("Bitte überprüfen die E-Mail nochmal!");
        usernameField.addKeyPressListener(Key.ENTER, e -> passwordField.focus() );

        // ####### LOGIN VIEW - Login Password Field
        passwordField.addClassName("login-view_password-field");
        passwordField.setLabel("Passwort *");
        passwordField.setErrorMessage("Bitte überprüfen das Passwort nochmal!");
        passwordField.addKeyPressListener(Key.ENTER, e -> loginButton.focus() );

        // ####### LOGIN VIEW - Login Button
        loginButton.addClassName("login-view_login-button");
        loginButton.setText("Log in");
        loginButton.addClickListener(e -> authenticate( usernameField.getValue(), passwordField.getValue() ) );

        // ####### LOGIN VIEW - Login Text wenn noch kein Profil vorhanden ist
        textMedium.addClassName("login-view_row_col-2_text");
        textMedium.setText("Noch kein Profil angelegt?");

        // ####### LOGIN VIEW - Router Link
        routerLink.setRoute(RegistrationView.class);
        routerLink.setText("Registrieren");

        // ####### LOGIN VIEW - Inhaltselemente dem Layout hinzufügen
        getContent().add(layoutRow);
        layoutRow.add(layoutColumn1);
        layoutRow.add(layoutColumn2);
        layoutColumn1.add(icon);
        layoutColumn2.add(h1);
        layoutColumn2.add(usernameField);
        layoutColumn2.add(passwordField);
        layoutColumn2.add(loginButton);
        layoutColumn2.add(hr);
        layoutColumn2.add(textMedium);
        layoutColumn2.add(routerLink);
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
     * Authentifiziert den Nutzer anhand der übergebenen
     * E-Mail und dem Passwort.
     *
     * @param username E-Mail des Nutzers.
     * @param password Passwort des Nutzers.
     */

    private void authenticate( String username, String password ) {

        loginButton.setEnabled( false );

        boolean isAuthenticated = false;

        try {

            isAuthenticated = loginControl.authenticate( username, password );

        } catch ( DatabaseException dex ) {

            Notification.show( dex.getMessage() );

        } catch ( SQLException sqlex ) {

            Notification.show("SQL-Fehler!");
            System.out.println(sqlex.getMessage());

        }

        usernameField.setInvalid( !isAuthenticated );
        passwordField.setInvalid( !isAuthenticated );
        loginButton.setEnabled( true );

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
        if ( null != getCurrentUser() ) {
            beforeEnterEvent.rerouteTo(Globals.Pages.SEARCH);
        }
    }
}