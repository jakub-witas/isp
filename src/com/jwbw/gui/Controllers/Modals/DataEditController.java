package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Klient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;

public class DataEditController {
    @FXML
    private TextField editedName, editedSurname, editedMail, editedPhone, editedId_card;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
        this.editedId_card.setText(((Klient) InterfaceMain.loggedUser).getId_card());
        this.editedMail.setText(((Klient) InterfaceMain.loggedUser).getMail());
        this.editedName.setText(((Klient) InterfaceMain.loggedUser).getName());
        this.editedSurname.setText(((Klient) InterfaceMain.loggedUser).getSurname());
        this.editedPhone.setText(((Klient) InterfaceMain.loggedUser).getPhone());
    }


    public void onConfirmButtonClick(ActionEvent actionEvent) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Powiadomienie");

        if (!areAllFilled()) {

            alert.setContentText("Nie wszystkie pola zostały wypełnione.");
            alert.showAndWait();

            return;
        } else if (ifContainsSings(" ")) {
            alert.setContentText("Pola nie mogą zawierać znaków spacji.");
            alert.showAndWait();

            return;
        } else if (!isNumeric(editedPhone.getText())) {
            alert.setContentText("Pole telefonu zawiera znaki nie będące cyframi.");
            alert.showAndWait();

            return;
        }

        updateClientData();
        if (!Main.Database.updateUserContractData(InterfaceMain.loggedUser)) {
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
        ((Klient)InterfaceMain.loggedUser).setPhone(editedPhone.getText());
        ((Klient)InterfaceMain.loggedUser).setName(editedName.getText());
        ((Klient)InterfaceMain.loggedUser).setMail(editedMail.getText());
        ((Klient)InterfaceMain.loggedUser).setSurname(editedSurname.getText());
        ((Klient)InterfaceMain.loggedUser).setId_card(editedId_card.getText());
    }

    @FXML
    private boolean areAllFilled() {
        return editedMail.getText() != null && editedName.getText() != null && editedSurname.getText() != null
                && editedId_card.getText() != null && editedPhone.getText() != null;
    }

    @FXML
    private boolean ifContainsSings(CharSequence sign) {
        return editedMail.getText().contains(sign) || editedName.getText().contains(sign) || editedSurname.getText().contains(sign)
                || editedId_card.getText().contains(sign) || editedPhone.getText().contains(sign);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) editedPhone.getScene().getWindow();
        window.fireEvent(
                new WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );

    }
}
