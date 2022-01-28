package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.Urzadzenie;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class AccountsController {

    @FXML
    TableColumn<Urzadzenie, Integer> accounts_id;

    @FXML
    TableColumn<Urzadzenie, String> accounts_username;

    @FXML
    TableColumn<Urzadzenie, String> accounts_name;

    @FXML
    TableColumn<Urzadzenie, String> accounts_surname;

    @FXML
    TableColumn<Urzadzenie, String> accounts_phone;

    @FXML
    TableColumn<Urzadzenie, String> accounts_mail;

    @FXML
    TableView<Urzadzenie> accountsTable;

    //public static List<Urzadzenie> accountsList = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        getAccountsList();
    }

    private void getAccountsList() throws SQLException {
        Object response = Proxy.getDev();

        if(response instanceof List) {
            List<Urzadzenie> accountsList = (List<Urzadzenie>) response;

            for(Urzadzenie accounList: accountsList) {
                System.out.println("duap");
                accounts_id.setCellValueFactory(new PropertyValueFactory<>("id"));
                accounts_username.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
               accounts_name.setCellValueFactory(new PropertyValueFactory<>("producent"));
                accounts_surname.setCellValueFactory(new PropertyValueFactory<>("sn"));
                accounts_phone.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
                accounts_mail.setCellValueFactory(new PropertyValueFactory<>("producent"));

                accountsTable.getItems().add(accounList);
            }
        }
    }

}
