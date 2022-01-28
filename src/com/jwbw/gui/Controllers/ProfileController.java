package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
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


public class ProfileController{

    @FXML
    private Label id_person, name, surname, mail, phone, pesel, id_card, city, street, code, home_number,
            nr_umowy, data_zawarcia, data_zakonczenia, kwota_mc, status, type, author,name_device,producer,id_sn;

    @FXML
    private Button editData;

    @FXML
    private void initialize() throws SQLException {
       loadData();
    }

    private void loadData() throws SQLException {
        List<Object> lista =  Proxy.getServiceContracts();
        this.id_person.setText(Proxy.loggedUser.getId().toString());
        this.pesel.setText(Proxy.loggedUser.getPesel());
        this.id_card.setText(Proxy.loggedUser.getId_card());
        this.mail.setText(Proxy.loggedUser.getMail());
        this.name.setText(Proxy.loggedUser.getName());
        this.surname.setText(Proxy.loggedUser.getSurname());
        this.phone.setText(Proxy.loggedUser.getPhone());
        this.city.setText(Proxy.loggedUser.getCity());
        this.street.setText(Proxy.loggedUser.getStreet());
        this.code.setText(Proxy.loggedUser.getCode());
        this.home_number.setText(Proxy.loggedUser.getHome_number());
        if(!lista.isEmpty()) {
            for(Object dokument: lista) {
                if(dokument instanceof Umowa_usluga) {
                    this.nr_umowy.setText(((Umowa_usluga)lista.get(0)).getNr_dokumentu());
                    this.data_zawarcia.setText(((Umowa_usluga)lista.get(0)).getData_utworzenia().toString());
                    this.data_zakonczenia.setText(((Umowa_usluga)lista.get(0)).getData_wygasniecia().toString());
                    //this.kwota_mc.setText(list);
                    //TODO: dodać kwote i funkcje ją wyliczającą lub do zmiennej, lub wyjebać. Dodać też tabele?  z ofertą wybraną na umowie
                    if (((Umowa_usluga)lista.get(0)).getData_wygasniecia().before(Date.valueOf(LocalDate.now()))) {
                        this.status.setText("Zakończona");
                    } else {
                        this.status.setText("Aktywna");
                    }

                    this.type.setText("Umowa na usługę");
                    this.author.setText(((Umowa_usluga)lista.get(0)).getAutor());
                }
            }
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
