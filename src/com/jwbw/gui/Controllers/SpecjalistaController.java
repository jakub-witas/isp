package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SpecjalistaController {

    @FXML
    private Pane paneSpecjalista;

    @FXML
    private Button profileButton, ticketsButton, devicesButton, accountsButton;

    @FXML
    private void initialize() {
        profileButton.setOnMouseEntered(e -> profileButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        profileButton.setOnMouseExited(e -> profileButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        ticketsButton.setOnMouseEntered(e -> ticketsButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        ticketsButton.setOnMouseExited(e -> ticketsButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        devicesButton.setOnMouseEntered(e -> devicesButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        devicesButton.setOnMouseExited(e -> devicesButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        accountsButton.setOnMouseEntered(e -> accountsButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        accountsButton.setOnMouseExited(e -> accountsButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));
    }

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

    public void setScreen(Pane pane) {
        paneSpecjalista.getChildren().clear();
        paneSpecjalista.getChildren().add(pane);
    }

    public void handleButtonTickets() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../specjalista/tickets.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonNaprawa() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../specjalista/naprawa.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonKonta() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../specjalista/kontaklientow.fxml"));
        Pane pane = new Pane();
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonProfile() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../klient/profil.fxml"));
        Pane pane = new Pane();
        try {
            pane = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }
}
