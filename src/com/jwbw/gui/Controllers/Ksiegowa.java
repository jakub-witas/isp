package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Ksiegowa {

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

    public void handleButtonUmowy(MouseEvent mouseEvent) {
    }

    public void handleButtonFaktury(MouseEvent mouseEvent) {
    }
}
