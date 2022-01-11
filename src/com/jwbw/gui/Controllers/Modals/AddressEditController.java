package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Klient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;

public class AddressEditController {
    @FXML
    private TextField editedCity, editedStreet, editedCode, editedNumber;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        this.editedCity.setText(((Klient) InterfaceMain.loggedUser).getCity());
        this.editedStreet.setText(((Klient) InterfaceMain.loggedUser).getStreet());
        this.editedCode.setText(((Klient) InterfaceMain.loggedUser).getCode());
        this.editedNumber.setText(((Klient) InterfaceMain.loggedUser).getHome_number());
    }

    public void onConfirmButtonClick(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Powiadomienie");

        if (!areAllFilled()) {

            alert.setContentText("Nie wszystkie pola zostały wypełnione.");
            alert.showAndWait();

            return;
        }

        updateClientData();
        if (!Main.connection.databaseHandler.updateUserAddressData(InterfaceMain.loggedUser)) {
            alert.setContentText("Aktualizacja danych nie powiodła się.");
            alert.showAndWait();
        } else {
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Aktualizacja danych powiodła się.");
            alert.showAndWait();
            closeWindow();
        }
    }

    @FXML
    private void updateClientData() {
        ((Klient)InterfaceMain.loggedUser).setCity(editedCity.getText());
        ((Klient)InterfaceMain.loggedUser).setStreet(editedStreet.getText());
        ((Klient)InterfaceMain.loggedUser).setHome_number(editedNumber.getText());
        ((Klient)InterfaceMain.loggedUser).setCode(editedCode.getText());
    }

    @FXML
    private boolean areAllFilled() {
        return editedCity.getText() != null && editedStreet.getText() != null && editedNumber.getText() != null
                && editedCode.getText() != null;
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) editedCode.getScene().getWindow();
        window.fireEvent(
                new WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );

    }
}
