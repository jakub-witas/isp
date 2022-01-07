package com.jwbw.gui.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TextField logi;

    @FXML
    private TextField passw;

    @FXML
    private Pane panhom;


    Alert a = new Alert(Alert.AlertType.NONE);

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
    public void handleButtonZal(MouseEvent event) {
        logIn(event);

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


    private void logIn(MouseEvent event) {
        String log = logi.getText();
        String pass = passw.getText();

        if (log.equals("ksiegowa") && pass.equals("ksiegowa")) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                // stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../ksiegowa/ksiegowa.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (log.equals("klient") && pass.equals("klient")) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                // stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../klient/klient.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        } else if (log.equals("specjalista") && pass.equals("specjalista")) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                // stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../specjalista/specjalista.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        } else if (log.equals("wlasciciel") && pass.equals("wlasciciel")) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                // stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../wlasciciel/wlasciciel.fxml")));
                stage.setScene(scene);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

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

        a.setAlertType(Alert.AlertType.CONFIRMATION);
        a.setContentText("Rejestracja przebiegła pomyślnie");
        a.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }



}
