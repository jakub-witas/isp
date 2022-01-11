package com.jwbw.gui.Controllers;

import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Klient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;


public class ProfileController {

    @FXML
    private Label id_person, name, surname, mail, phone, pesel, id_card, city, street, code, home_number;

    @FXML
    private Button editData;

    @FXML
    private void initialize() {
       loadData();
    }

    private void loadData() {
        this.id_person.setText(((Klient)InterfaceMain.loggedUser).getId().toString());
        this.pesel.setText(((Klient)InterfaceMain.loggedUser).getPesel());
        this.id_card.setText(((Klient) InterfaceMain.loggedUser).getId_card());
        this.mail.setText(((Klient) InterfaceMain.loggedUser).getMail());
        this.name.setText(((Klient) InterfaceMain.loggedUser).getName());
        this.surname.setText(((Klient) InterfaceMain.loggedUser).getSurname());
        this.phone.setText(((Klient) InterfaceMain.loggedUser).getPhone());
        this.city.setText(((Klient) InterfaceMain.loggedUser).getCity());
        this.street.setText(((Klient) InterfaceMain.loggedUser).getStreet());
        this.code.setText(((Klient) InterfaceMain.loggedUser).getCode());
        this.home_number.setText(((Klient) InterfaceMain.loggedUser).getHome_number());
    }

    public void handleButtonEditAdres() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../klient/edycjaadres.fxml"));
            stage.setTitle("Edycja adresu");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.editData.getScene().getWindow());
            stage.setOnCloseRequest(event -> loadData());
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonEditContract() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Edycja danych");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../klient/edycjaumowa.fxml"))));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonEditcontact(ActionEvent Event) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../klient/edycjakontaktowe.fxml"));
            stage.setTitle("Edycja danych");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(this.editData.getScene().getWindow());
            stage.setOnCloseRequest(event -> loadData());
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
