package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Main;
import com.jwbw.Proxy;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Naprawa_serwisowa;
import com.jwbw.isp.Urzadzenie;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;


public class NewDeviceController {
    @FXML
    TextField snField, producentField, modelField;

    private Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    public void onAddButton() throws SQLException {
        if (checkForData()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Niepowodzenie");
            alert.setContentText("Wypełnij wszystkie pola informacjami.");
            alert.showAndWait();
            return;
        } else if (!Proxy.checkConnection()) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setTitle("Błąd połączenia");
            alert.setContentText("Brak połączenia z bazą danych.");
            alert.showAndWait();
            return;
        } else {
            Urzadzenie urzadzenie = new Urzadzenie(modelField.getText(), producentField.getText(), snField.getText());
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Zgłoszenie zostało pomyślnie dodane");
            alert.setTitle("Powodzenie");
            alert.showAndWait();
            closeWindow();
        }
    }

    private boolean checkForData() {
        return modelField.getText().isEmpty() || producentField.getText().isEmpty() || snField.getText().isEmpty();
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) producentField.getScene().getWindow();
        window.fireEvent(
                new WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
    }
}
