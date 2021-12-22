package com.jwbw.gui.Controllers;

import com.jwbw.isp.Naprawa_serwisowa;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class InterfaceMain extends Application {

    public static ObservableList<Naprawa_serwisowa> naprawy;
    public static Object loggedUser;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(InterfaceMain.class.getResource("hello-view.fxml"));
        stage.setTitle("Hello!");
        stage.setScene(new Scene(root, 320, 240));
        stage.show();
    }

    public static void Interface(String[] args) {
                launch(args);
    }
}
