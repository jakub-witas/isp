package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Pracownik;
import com.jwbw.isp.Role;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.beans.EventHandler;
import java.io.DataOutputStream;
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
            //stage.setMaximized(true);
            // stage.close();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../home/hom.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

private void logIn() throws SQLException {
        if(Main.connection == null || Main.connection.databaseHandler == null) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Błąd połączenia");
            alert.setContentText("Brak połączenia z bazą danych.");
            alert.showAndWait();
            return;
        }
    String log = logi.getText();
    String pass = passw.getText();

    if (Main.connection.databaseHandler.authenticateUser(log, pass)) {
        Object user = Main.connection.databaseHandler.fetchUserData(log, pass);
        InterfaceMain.loggedUser = user;
        Role rola;

        if (user.getClass() == Pracownik.class) {
            rola = ((Pracownik)user).getRole();
        } else {
            rola = Role.CLIENT;
        }
        assert rola != null;
        try {
            Stage stage = (Stage) this.passw.getScene().getWindow();
            Scene scene;
            switch(rola) {
                case OWNER -> scene = new Scene(FXMLLoader.load(getClass().getResource("../wlasciciel/wlasciciel.fxml")));
                case CLIENT -> scene = new Scene(FXMLLoader.load(getClass().getResource("../klient/klient.fxml")));
                case ACCOUNTANT -> scene = new Scene(FXMLLoader.load(getClass().getResource("../ksiegowa/ksiegowa.fxml")));
                case SPECIALIST -> scene = new Scene(FXMLLoader.load(getClass().getResource("../specjalista/specjalista.fxml")));
                //TODO: dorobić pracownika biurowego ffs.
                default -> throw new IllegalStateException("Unexpected value: " + rola);
            }

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

    public void handleButtonZarejestruj(MouseEvent mouseEvent) {

        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Rejestracja przebiegła pomyślnie");
        alert.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



}
