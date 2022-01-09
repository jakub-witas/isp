package com.jwbw.gui;

import com.jwbw.isp.Naprawa_serwisowa;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.io.IOException;

public class InterfaceMain extends Application {

    public static ObservableList<Naprawa_serwisowa> naprawy;
    public static Object loggedUser;


    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(InterfaceMain.class.getResource("home/hom.fxml"));
        stage.setTitle("Hello!");
        stage.setScene(new Scene(root, 1200, 700));
        stage.show();
    }

    public static void Interface(String[] args) {
                launch(args);
    }
}
