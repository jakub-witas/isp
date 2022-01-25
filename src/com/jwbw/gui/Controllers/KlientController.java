package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.gui.InterfaceMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class KlientController {

    @FXML
    private Pane paneklient;

    @FXML
    private ToggleGroup tg1;

    @FXML
    private ToggleGroup tg2;

    @FXML
    private ToggleGroup tg3;



    @FXML
    private void initialize() {}

    //wylogowanie
    public void handleButtonWyl(MouseEvent mouseEvent) {
        try {
            //Main.connection.userLoggedOff(InterfaceMain.loggedUser);
            Proxy.loggedUser = null;
            Node node = (Node) mouseEvent.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../login/login.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    public void setScreen(Pane pane) {
        this.paneklient.getChildren().clear();
        this.paneklient.getChildren().add(pane);
    }

    public void zglosUsterke() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/usterka.fxml"));
        Pane pane = new Pane();
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void utworzUmowe() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/NewSrvwiceContractForm.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    @FXML
    public void handleButtonProfil() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/profil.fxml"));
        Pane pane = new Pane();
        try {
            pane = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonZgloszenia() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/zgloszenia.fxml"));
        Pane pane = new Pane();
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
