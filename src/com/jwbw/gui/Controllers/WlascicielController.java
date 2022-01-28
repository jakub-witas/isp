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

public class WlascicielController {

    @FXML
    private Pane paneWlasciciel;

    @FXML
    private Button profileButton, advertismentButton, deleteButton, priceButton;

    @FXML
    private void initialize() {
        profileButton.setOnMouseEntered(e -> profileButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        profileButton.setOnMouseExited(e -> profileButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        advertismentButton.setOnMouseEntered(e -> advertismentButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        advertismentButton.setOnMouseExited(e -> advertismentButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        priceButton.setOnMouseEntered(e -> priceButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        priceButton.setOnMouseExited(e -> priceButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));
    }

    public void setScreen(Pane pane) {
        paneWlasciciel.getChildren().clear();
        paneWlasciciel.getChildren().add(pane);
    }

    public void handleButtonWyl(MouseEvent Event) {
        try {
            //Main.connection.userLoggedOff(InterfaceMain.loggedUser);
            Proxy.loggedUser = null;
            Node node = (Node) Event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("../login/login.fxml")));
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void handleButtonAddAdvertisment() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../wlasciciel/dodajogloszenie.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonSetPrice() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../wlasciciel/cennik.fxml"));
        Pane pane = null;
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

    public void handleButtonDelete(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../wlasciciel/usunogloszenie.fxml"));
        Pane pane = null;
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }
}
