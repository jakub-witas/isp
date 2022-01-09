package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Klient {

    @FXML
    private Pane paneklient;

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

    public void handleButtonUmowy() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/umowy.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
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

}
