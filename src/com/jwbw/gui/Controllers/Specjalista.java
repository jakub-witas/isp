package com.jwbw.gui.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Specjalista {

    @FXML
    private Pane paneSpecjalista;

    public void handleButtonWyl(MouseEvent mouseEvent) {
        try {
            Node node = (Node) mouseEvent.getSource();
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

    public void setScreen(Pane pane) {
        paneSpecjalista.getChildren().clear();
        paneSpecjalista.getChildren().add(pane);
    }

    public void handleButtonTickets(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../specjalista/tickets.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonNaprawa(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../specjalista/naprawa.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonKonta(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../specjalista/kontaklientow.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }
}
