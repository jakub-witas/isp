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
import java.util.List;


public class InteractEntryController {
    @FXML
    private TextArea descriptionArea;
    @FXML
    private ComboBox<String> typeCombo;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    //private int klientId;

    public void initialize() {
       typeCombo.setItems(FXCollections.observableList(List.of("Wpis", "Powiadomienie")));
    }

    public void setData(Wpis wpis) {
        if (wpis != null) {
            descriptionArea.setText(wpis.getOpis());
            typeCombo.setVisible(false);
        } else if (Proxy.loggedUser.getRole().equals(Role.CLIENT)) {
            typeCombo.setVisible(false);
        }
//        else {
//            this.klientId = klientId;
//        }
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
            if (typeCombo.getValue().equals("Wpis")) {
                var wpis = new Wpis(Proxy.loggedUser, descriptionArea.getText());
                HardwareEditController.naprawaSerwisowa.getWpisy().add(wpis);
                Proxy.addNewEntry(wpis.getId(), HardwareEditController.naprawaSerwisowa.getId());
            } else {
                var user = new User();
                //user.setId(klientId);
                user.setId(3);
                var wpis = new Wpis(Proxy.loggedUser, user, descriptionArea.getText(), false);
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
