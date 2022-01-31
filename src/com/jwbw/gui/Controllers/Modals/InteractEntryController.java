package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Proxy;
import com.jwbw.isp.Role;
import com.jwbw.isp.User;
import com.jwbw.isp.Wpis;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class InteractEntryController {
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> typeCombo;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    public void initialize() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Wpis");
        stringList.add("Powiadomienie");
       typeCombo.setItems(FXCollections.observableList(stringList));
    }

    public void setData(Wpis wpis) {
        if (wpis != null) {
            descriptionArea.setText(wpis.getOpis());
            typeCombo.setDisable(true);
        } else if (Proxy.loggedUser.getRole().equals(Role.CLIENT)) {
            typeCombo.setDisable(true);
            typeCombo.getSelectionModel().select(0);
        }
    }
    public void onSendButton() throws SQLException {
        if(descriptionArea.getText().length() >= 200) {
            alert.setTitle("Limit znaków");
            alert.setContentText("Przekroczony wyznaczony limit znaków(200).");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        if (HardwareEditController.interactedEntry != null) {
            HardwareEditController.interactedEntry.setOpis(descriptionArea.getText());
        } else {
            if(typeCombo.getValue() == null) {
                alert.setHeaderText(null);
                alert.setTitle("Brak typu");
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setContentText("Nie zaznaczono typu wiadomości.");
                alert.showAndWait();
                return;
            }

            if (typeCombo.getValue().equals("Wpis")) {
                var wpis = new Wpis(Proxy.loggedUser, descriptionArea.getText());
                HardwareEditController.naprawaSerwisowa.getWpisy().add(wpis);
                Proxy.addNewEntry(wpis.getId(), HardwareEditController.naprawaSerwisowa.getId());
            } else {
                var wpis = new Wpis(Proxy.loggedUser, HardwareEditController.naprawaSerwisowa.getWlasciciel(), descriptionArea.getText(), false);
                HardwareEditController.naprawaSerwisowa.getWpisy().add(wpis);
                Proxy.addNewEntry(wpis.getId(), HardwareEditController.naprawaSerwisowa.getId());
            }
        }
        alert.setHeaderText(null);
        alert.setTitle("Powodzenie");
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText("Operacja przebiegła pomyślnie.");
        alert.showAndWait();
        closeWindow();
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) descriptionArea.getScene().getWindow();
        window.fireEvent(
                new WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );

    }
}
