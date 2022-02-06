package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Proxy;
import com.jwbw.gui.Controllers.AccountsController;
import com.jwbw.isp.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;

public class EditClientController {

    @FXML
    private TextField editedLogin, editedName, editedSurname, editedMail, editedPhone;

    @FXML
    private TextField editedCity, editedStreet, editedCode, editedNumber;

    public static User userEdit = null;

    @FXML
    public void initialize() throws SQLException {
        userEdit = AccountsController.detailedUser;
        loadData();
    }

    private void loadData(){
       this.editedSurname.setText(userEdit.getSurname());
       this.editedLogin.setText(userEdit.getUsername());
       this.editedName.setText(userEdit.getName());
       this.editedMail.setText(userEdit.getMail());
       this.editedPhone.setText(userEdit.getPhone());
       this.editedCity.setText(userEdit.getCity());
       this.editedStreet.setText(userEdit.getStreet());
       this.editedCode.setText(userEdit.getCode());
       this.editedNumber.setText(userEdit.getHome_number());
    }

    public void onConfirmButtonClick() throws SQLException {
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
        if (!Proxy.updateAccountClient(userEdit)) {
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
    private void updateClientData() throws SQLException {
        userEdit.setName(editedName.getText());
        userEdit.setUsername(editedLogin.getText());
        userEdit.setSurname(editedSurname.getText());
        userEdit.setPhone(editedPhone.getText());
        userEdit.setMail(editedMail.getText());
        userEdit.setHome_number(editedNumber.getText());
        userEdit.setCity(editedCity.getText());
        userEdit.setCode(editedCode.getText());
        userEdit.setStreet(editedStreet.getText());
        Proxy.updateAccountClient(userEdit);
    }

    @FXML
    private boolean areAllFilled() {
        return editedMail.getText() != null && editedName.getText() != null && editedSurname.getText() != null
                && editedCity.getText() != null && editedPhone.getText() != null && editedLogin.getText() != null
                && editedStreet.getText() != null && editedCode.getText() != null && editedNumber.getText() != null;
    }

    @FXML
    private boolean ifContainsSings(CharSequence sign) {
        return editedMail.getText().contains(sign) || editedName.getText().contains(sign) || editedSurname.getText().contains(sign)
                || editedCity.getText().contains(sign) || editedPhone.getText().contains(sign) || editedLogin.getText().contains(sign)
                || editedCode.getText().contains(sign) || editedNumber.getText().contains(sign);
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
