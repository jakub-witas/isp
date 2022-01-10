package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;

public class Klient {

    @FXML
    private Pane paneklient;

    @FXML
    private ToggleGroup tg1;

    @FXML
    private ToggleGroup tg2;

    @FXML
    private ToggleGroup tg3;




    //wylogowanie
    public void handleButtonWyl(MouseEvent mouseEvent) {
        try {
            Main.connection.userLoggedOff(InterfaceMain.loggedUser);
            InterfaceMain.loggedUser = null;
            Node node = (Node) mouseEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../login/login.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void setScreen(Pane pane) {
        paneklient.getChildren().clear();
        paneklient.getChildren().add(pane);
    }

    public void zglosUsterke() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/usterka.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void utworzUmowe() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/tworzenieumowy.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonProfil() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/profil.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonEditcontact(){
        try {
            Stage stage = new Stage();
            stage.setTitle("Edycja danych");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../klient/edycjakontaktowe.fxml"))));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void handleButtonEditAdres() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Edycja danych");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../klient/edycjaadres.fxml"))));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonEditContract() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Edycja danych");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../klient/edycjaumowa.fxml"))));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonZgloszenia() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/zgloszenia.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonNotifications() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/powiadomienia.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }
}
