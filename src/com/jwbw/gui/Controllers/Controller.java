package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.Proxy;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.User;
import com.jwbw.isp.Pracownik;
import com.jwbw.isp.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField logi;

    @FXML
    private TextField passw;

    @FXML
    private Pane panhom;

    @FXML
    private TextField Rlogn;

    @FXML
    private PasswordField Rpassword;

    @FXML
    private TextField name;

    @FXML
    private TextField surname;

    @FXML
    private TextField mail;

    @FXML
    private TextField phone;

    @FXML
    private TextField pesel;

    @FXML
    private TextField city;

    @FXML
    private TextField street;

    @FXML
    private TextField home_number;

    @FXML
    private TextField code;

    @FXML
    private TextField id_card;


    Alert alert = new Alert(Alert.AlertType.NONE);


    // wyswietlenie okna z logowaniem i rejestracja
    public void handleButtonAction(MouseEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            //stage.setMaximized(true);
            // stage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../login/login.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    // logowanie
    public void handleButtonZal() throws SQLException {
        logIn();

    }


    // wyswietlanie oferty
    public void handleButtonOfer() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../home/oferta.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    // wyswietlanie ogloszen
    public void handleButtonOglo() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../home/ogloszenia.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void setScreen(Pane pane) {
        panhom.getChildren().clear();
        panhom.getChildren().add(pane);
    }

    // powrot na strone glowna
    public void handleButtonPowrot(MouseEvent mouseEvent) {
        try {
            Node node = (Node) mouseEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../home/hom.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    private void logIn() throws SQLException {
        if (checkDatabaseConnection()) return;

        String log = logi.getText();
        String pass = passw.getText();

    if (Proxy.authenticateUser(log, pass)) {
        User user = Proxy.fetchUserData(log, pass);
        Proxy.loggedUser = user;

        try {
            Stage stage = (Stage) this.passw.getScene().getWindow();
            Scene scene;
            switch(user.getRole()) {
                case OWNER -> scene = new Scene(FXMLLoader.load(getClass().getResource("../wlasciciel/wlasciciel.fxml")));
                case CLIENT -> scene = new Scene(FXMLLoader.load(getClass().getResource("../klient/klient.fxml")));
                case ACCOUNTANT -> scene = new Scene(FXMLLoader.load(getClass().getResource("../ksiegowa/ksiegowa.fxml")));
                case JUNIOR_SPECIALIST, MID_SPECIALIST, SENIOR_SPECIALIST ->
                        scene = new Scene(FXMLLoader.load(getClass().getResource("../specjalista/specjalista.fxml")));
                case OFFICE_WORKER -> scene = new Scene(FXMLLoader.load(getClass().getResource("../pracownik_biurowy/pracownik_biurowy.fxml")));
                default -> throw new IllegalStateException("Unexpected value: " + user.getRole());
            }
            Proxy.loggedUser = user;
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    } else {
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setTitle("Nieudane logowanie");
        alert.setContentText("Nieprawidłowe dane logowania.");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}

    public void handleButtonAplikuj(MouseEvent event) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Formularz zgłoszeniowy");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../home/stanowisko.fxml"))));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleButtonZarejestruj(MouseEvent mouseEvent) throws SQLException {
        if (checkDatabaseConnection()) return;

        if (registrationEmptyFieldCheck()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Błąd rejestracji");
            alert.setContentText("Wszystkie pola są obowiązkowe.");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else if (checkForExistingUser()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Błąd rejestracji");
            alert.setContentText("Podany login jest już zajęty.");
            alert.setHeaderText(null);
            alert.showAndWait();
        }else{
            User user =  new User();
            this.setUserInfo(user);
            user.setId(Proxy.registerNewUser(user, Rlogn.getText(), Rpassword.getText()));
            clearRegistrationForms();
           alert.setAlertType(Alert.AlertType.INFORMATION);
           alert.setTitle("Rejestracja");
           alert.setContentText("Rejestracja przebiegła pomyślnie");
           alert.setHeaderText(null);
           alert.showAndWait();
       }
    }

    @FXML
    private void clearRegistrationForms() {
        Rlogn.clear();
        Rpassword.clear();
        name.clear();
        surname.clear();
        mail.clear();
        phone.clear();
        pesel.clear();
        home_number.clear();
        street.clear();
        city.clear();
        code.clear();
        id_card.clear();
    }

    private boolean checkForExistingUser() {
        return Proxy.checkForExistingUser(Rlogn.getText());
    }

    private boolean checkDatabaseConnection() throws NullPointerException {
        if(!Proxy.checkConnection()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Błąd połączenia");
            alert.setContentText("Brak połączenia z bazą danych.");
            alert.showAndWait();
            return true;
        }
        return false;
    }

    private void setUserInfo(User user) {
        user.setName(name.getText());
        user.setSurname(surname.getText());
        user.setMail(mail.getText());
        user.setPhone(phone.getText());
        user.setPesel(pesel.getText());
        user.setHome_number(home_number.getText());
        user.setStreet(street.getText());
        user.setCity(city.getText());
        user.setCode(code.getText());
        user.setId_card(id_card.getText());
    }

    @FXML
    private boolean registrationEmptyFieldCheck() {
        return Rlogn.getText().isEmpty() || Rpassword.getText().isEmpty() || name.getText().isEmpty()
                || surname.getText().isEmpty() || mail.getText().isEmpty() || phone.getText().isEmpty()
                || pesel.getText().isEmpty() || city.getText().isEmpty() || street.getText().isEmpty()
                || home_number.getText().isEmpty() || code.getText().isEmpty() || id_card.getText().isEmpty();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Proxy.resetProxyData();
    }



}
