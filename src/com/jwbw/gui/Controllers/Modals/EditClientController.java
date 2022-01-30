package com.jwbw.gui.Controllers.Modals;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class EditClientController {

    @FXML
    private TextField editedLogin, editedPassword, editedName, editedSurname, editedMail, editedPhone;

    @FXML
    private TextField editedCity, editedStreet, editedCode, editedNumber;

    @FXML
    public void initialize() {
        loadData();
    }

    private void loadData() {
     //   this.editedLogin.setText(Proxy.accountList);
    }

    public void onConfirmButtonClick() {
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
      //  if (!Proxy.updateUserContractData()) {
        //    alert.setContentText("Aktualizacja danych nie powiodła się.");
      //      alert.showAndWait();
     //   } else {
      //      alert.setAlertType(Alert.AlertType.INFORMATION);
      //      alert.setContentText("Aktualizacja danych powiodła się.");
      //      alert.showAndWait();
      //      closeWindow();
     //   }
    }

    @FXML
    private void updateClientData() {

    }

    @FXML
    private boolean areAllFilled() {
        return editedMail.getText() != null && editedName.getText() != null && editedSurname.getText() != null
                && editedCity.getText() != null && editedPhone.getText() != null && editedLogin.getText() != null
                && editedPassword.getText() != null && editedStreet.getText() != null && editedCode.getText() != null
                && editedNumber.getText() != null;
    }

    @FXML
    private boolean ifContainsSings(CharSequence sign) {
        return editedMail.getText().contains(sign) || editedName.getText().contains(sign) || editedSurname.getText().contains(sign)
                || editedCity.getText().contains(sign) || editedPhone.getText().contains(sign) || editedLogin.getText().contains(sign)
                || editedPassword.getText().contains(sign) || editedStreet.getText().contains(sign) || editedCode.getText().contains(sign)
                || editedNumber.getText().contains(sign);
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
