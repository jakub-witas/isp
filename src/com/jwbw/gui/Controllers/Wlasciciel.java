package com.jwbw.gui.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Wlasciciel {

    @FXML
    private Pane paneWlasciciel;


    public void setScreen(Pane pane) {
        paneWlasciciel.getChildren().clear();
        paneWlasciciel.getChildren().add(pane);
    }

    public void handleButtonWyl(MouseEvent Event) {
        try {
            Node node = (Node) Event.getSource();
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

    public void handleButtonDodajogloszenie(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../wlasciciel/dodajogloszenie.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonUstawcennik(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../wlasciciel/cennik.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

}
