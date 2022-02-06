package com.jwbw.gui.Controllers.Panes;

import com.jwbw.Main;
import com.jwbw.Proxy;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HardwarePaneController {
    @FXML
    Button sendButton, newDeviceButton;
    @FXML
    TextArea descArea;
    @FXML
    ComboBox<String> deviceCombo;

    private List<Urzadzenie> urzadzenieList = Proxy.getDevices();

    Alert alert = new Alert(Alert.AlertType.WARNING);

    public HardwarePaneController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        fillData();
    }

    private void fillData() {
        List<String> devList = new ArrayList<>();
        for(Urzadzenie deviceList: urzadzenieList) {
            if(!(deviceList instanceof Urzadzenie_sieciowe) && !(deviceList instanceof Czesc_komputerowa))
            devList.add(deviceList.getProducent() + " " + deviceList.getNazwa());
        }
        ObservableList<String> observableList = FXCollections.observableList(devList);
        deviceCombo.setItems(observableList);
    }

    public void onSendButton() throws SQLException {
        if(deviceCombo.getValue() == null || deviceCombo.getValue().isEmpty()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Niepowodzenie");
            alert.setContentText("Nie wybrano urządzenia do naprawy.");
            alert.showAndWait();
            return;
        } else if(!Proxy.checkConnection()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Błąd połączenia");
            alert.setContentText("Brak połączenia z bazą danych.");
            alert.showAndWait();
            return;
        } else {
            Naprawa_serwisowa.utworzZlecenieFormularz(urzadzenieList.get(deviceCombo.getItems().indexOf(deviceCombo.getValue())),
                    null, null,null, Proxy.loggedUser, descArea.getText());
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Zgłoszenie zostało pomyślnie dodane");
            alert.setTitle("Powodzenie");
            alert.showAndWait();
            descArea.clear();
            deviceCombo.valueProperty().set(null);
        }
    }

    public void onAddNewDevice() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../../klient/newDevice.fxml"));
            stage.setTitle("Dodawanie urządzenia");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.deviceCombo.getScene().getWindow());
            stage.setOnCloseRequest(event -> {
                try {
                    urzadzenieList = Proxy.getDevices();
                    deviceCombo.getItems().clear();
                    fillData();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
