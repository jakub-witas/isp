package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.Dokument;
import com.jwbw.isp.Umowa_o_prace;
import com.jwbw.isp.Umowa_usluga;
import com.jwbw.isp.Urzadzenie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class ProfileController{

    @FXML
    private Label id_person, name, surname, mail, phone, pesel, id_card, city, street, code, home_number,
            nr_umowy, data_zawarcia, data_zakonczenia, kwota_mc, status, type, author,deviceName, deviceNameLabel,
        deviceSn, deviceSnLabel, deviceProducer, deviceProducerLabel, noContractsLabel,  endDateLabel, nameLabel,
            typeLabel, statusLabel, startDateLabel, costLabel, authorLabel, noDevicesLabel;

    @FXML
    private Button editData;

    @FXML
    private Pagination ContractPagination, devicesPagination;

    @FXML
    private void initialize() throws SQLException {
       loadData();
    }

    private void loadData() throws SQLException {
        List<Dokument> lista = new ArrayList<>();

        List<Dokument> listaUsluga = Proxy.getServiceContracts(Proxy.loggedUser.getId());
        if(listaUsluga != null && listaUsluga.size() > 0) {
            if(listaUsluga.get(0) instanceof Umowa_usluga)
            lista.addAll(listaUsluga);
        }

        List<Dokument> listaPraca = Proxy.getEmploymentContracts();
        if (listaPraca != null  && listaPraca.size() > 0) {
            if(listaPraca.get(0) instanceof Umowa_o_prace)
            lista.addAll(listaPraca);
        }

        List<Urzadzenie> listDevices = Proxy.getDevices();

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
            ContractPagination.setPageCount(lista.size());
            ContractPagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
            ContractPagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer integer) {
                    return createPage(integer, lista.get(integer));
                }
            });
        } else {
            ContractPagination.setVisible(false);
            endDateLabel.setVisible(false);
            nameLabel.setVisible(false);
            typeLabel.setVisible(false);
            statusLabel.setVisible(false);
            costLabel.setVisible(false);
            startDateLabel.setVisible(false);
            authorLabel.setVisible(false);
            noContractsLabel.setVisible(true);
        }

        if(!listDevices.isEmpty()) {
            devicesPagination.setPageCount(listDevices.size());
            devicesPagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
            devicesPagination.setPageFactory(new Callback<Integer, Node>() {
                @Override
                public Node call(Integer integer) {
                    return createPageDevices(integer, listDevices.get(integer));
                }
            });
        } else {
            deviceNameLabel.setVisible(false);
            deviceProducerLabel.setVisible(false);
            deviceSnLabel.setVisible(false);
            devicesPagination.setVisible(false);
            noDevicesLabel.setVisible(true);
        }
    }

    private Node createPageDevices(int pageIndex, Urzadzenie urzadzenie) {
        VBox box = new VBox();

        this.deviceName.setText(urzadzenie.getNazwa());
        this.deviceProducer.setText(urzadzenie.getProducent());
        this.deviceSn.setText(urzadzenie.getSn());

        return box;
    }

    private Node createPage(int pageIndex, Dokument dokument) {
        VBox box = new VBox();
        if(dokument instanceof Umowa_usluga) {
            this.nr_umowy.setText(dokument.getNr_dokumentu());
            this.data_zawarcia.setText(dokument.getData_utworzenia().toString());
            this.data_zakonczenia.setText(dokument.getData_wygasniecia().toString());
            this.kwota_mc.setText(((Umowa_usluga) dokument).calculateMonthlyPayment() + " z??");
            if (dokument.getData_wygasniecia().before(Date.valueOf(LocalDate.now()))) {
                this.status.setText("Zako??czona");
            } else {
                this.status.setText("Aktywna");
            }
            this.type.setText("Umowa na us??ug??");
            this.author.setText(((Umowa_usluga) dokument).getAutor());
            this.authorLabel.setText("Autor");
            this.costLabel.setText("Miesi??czny koszt");
        } else if (dokument instanceof Umowa_o_prace) {
            this.nr_umowy.setText(dokument.getNr_dokumentu());
            this.data_zawarcia.setText(dokument.getData_utworzenia().toString());
            this.data_zakonczenia.setText(dokument.getData_wygasniecia().toString());
            this.kwota_mc.setText(((Umowa_o_prace) dokument).getWynagrodzenie() + " z??");
            if (dokument.getData_wygasniecia().before(Date.valueOf(LocalDate.now()))) {
                this.status.setText("Zako??czona");
            } else {
                this.status.setText("Aktywna");
            }
            this.type.setText("Umowa o prac??");
            this.author.setText(((Umowa_o_prace) dokument).getRola().getName());
            this.authorLabel.setText("Rola");
            this.costLabel.setText("Wynagrodzenie");
        }
        return box;
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
