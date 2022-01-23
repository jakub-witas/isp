package com.jwbw.gui.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicketMakerController {
    @FXML
    Pane formPane, mainPane;

    ComboBox<String> comboBox;
    private List<String> list = new ArrayList<>();
    public ObservableList<String> observableList = FXCollections.observableList(list);

    @FXML
    public void initialize() {
        list.add("Naprawa sprzętu");
        list.add("Naprawa sieci");
        comboBox = new ComboBox<>(observableList);
        comboBox.setOnAction(this::showSelectedPane);
        comboBox.setPrefWidth(150);
        comboBox.setLayoutX(710);
        comboBox.setLayoutY(126);
        mainPane.getChildren().add(comboBox);
    }

    @FXML
    public void showSelectedPane(Event event) {
        FXMLLoader loader;
        if(comboBox.getValue().equals("Naprawa sieci")) {
             loader = new FXMLLoader(this.getClass().getResource("../klient/Panes/NetworkPane.fxml"));
        } else {
            loader = new FXMLLoader(this.getClass().getResource("../klient/Panes/HardwarePane.fxml"));
        }
        Pane pane = new Pane();
        try {
            pane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.formPane.getChildren().clear();
        this.formPane.getChildren().add(pane);
    }
}