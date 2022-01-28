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

public class KsiegowaController {

    @FXML
    private Pane paneKsiegowa;

    @FXML
    private Button profileButton, invoicesButton, employeeButton, clientsButton;

    @FXML
    private void initialize() {
        profileButton.setOnMouseEntered(e -> profileButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        profileButton.setOnMouseExited(e -> profileButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        invoicesButton.setOnMouseEntered(e -> invoicesButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        invoicesButton.setOnMouseExited(e -> invoicesButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        employeeButton.setOnMouseEntered(e -> employeeButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        employeeButton.setOnMouseExited(e -> employeeButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));

        clientsButton.setOnMouseEntered(e -> clientsButton.setStyle("-fx-border-color: green;-fx-background-color: #99CCFF"));
        clientsButton.setOnMouseExited(e -> clientsButton.setStyle("-fx-border-color:  #99CCFF; -fx-background-color: #99CCFF"));
    }

    public void setScreen(Pane pane) {
        paneKsiegowa.getChildren().clear();
        paneKsiegowa.getChildren().add(pane);
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

    public void handleButtonFaktury() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../ksiegowa/faktury.fxml"));
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

    public void handleButtonEmployee() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../ksiegowa/pracownicy.fxml"));
        Pane pane = new Pane();
        try {
            pane = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }

    public void handleButtonClients() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("../ksiegowa/klienci.fxml"));
        Pane pane = new Pane();
        try {
            pane = loader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
        setScreen(pane);
    }
}
