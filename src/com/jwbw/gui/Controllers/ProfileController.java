package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.User;
import com.jwbw.isp.Umowa_usluga;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


public class ProfileController {

    @FXML
    private Label id_person, name, surname, mail, phone, pesel, id_card, city, street, code, home_number,
            nr_umowy, data_zawarcia, data_zakonczenia, kwota_mc, status, type, author;

    @FXML
    private Button editData;

    @FXML
    private void initialize() throws SQLException {
       loadData();
    }

    private void loadData() throws SQLException {
        List<Umowa_usluga> lista =  Main.Database.getServiceContracts();
        this.id_person.setText(((User)InterfaceMain.loggedUser).getId().toString());
        this.pesel.setText(((User)InterfaceMain.loggedUser).getPesel());
        this.id_card.setText(((User) InterfaceMain.loggedUser).getId_card());
        this.mail.setText(((User) InterfaceMain.loggedUser).getMail());
        this.name.setText(((User) InterfaceMain.loggedUser).getName());
        this.surname.setText(((User) InterfaceMain.loggedUser).getSurname());
        this.phone.setText(((User) InterfaceMain.loggedUser).getPhone());
        this.city.setText(((User) InterfaceMain.loggedUser).getCity());
        this.street.setText(((User) InterfaceMain.loggedUser).getStreet());
        this.code.setText(((User) InterfaceMain.loggedUser).getCode());
        this.home_number.setText(((User) InterfaceMain.loggedUser).getHome_number());
        if(!lista.isEmpty()) {
            this.nr_umowy.setText(lista.get(0).getNr_dokumentu());
            this.data_zawarcia.setText(lista.get(0).getData_utworzenia().toString());
            this.data_zakonczenia.setText(lista.get(0).getData_wygasniecia().toString());
            //this.kwota_mc.setText(list);
            //TODO: dodać kwote i funkcje ją wyliczającą lub do zmiennej, lub wyjebać. Dodać też tabele?  z ofertą wybraną na umowie
            if (lista.get(0).getData_wygasniecia().before(Date.valueOf(LocalDate.now()))) {
                this.status.setText("Zakończona");
            } else {
                this.status.setText("Aktywna");
            }

            this.type.setText("Umowa na usługę");
            this.author.setText(lista.get(0).getAutor());
        }
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
            stage.setOnCloseRequest(event -> {
                try {
                    loadData();
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

    public void handleButtonEditContract() {
        try {
            Stage stage = new Stage();
            stage.setTitle("Edycja danych");
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../klient/newDevice.fxml"))));
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
            stage.setOnCloseRequest(event -> {
                try {
                    loadData();
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
