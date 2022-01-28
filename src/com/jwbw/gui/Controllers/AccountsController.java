package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;

public class AccountsController {

    @FXML
    TableColumn<User, Integer> accounts_id;

    @FXML
    TableColumn<User, String> accounts_username, accounts_name, accounts_surname, accounts_phone, accounts_mail, accounts_pesel;

    @FXML
    TableView<User> accountsTable;

    @FXML
    private Button deleteButton;

    @FXML
    public void initialize() throws SQLException {
        getAccountsList();
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Usuń konto użytkownika");
        deleteButton.setTooltip(tooltip);
    }

    private void getAccountsList() throws SQLException {
        Object response = Proxy.getAccountsClients();

        if(response instanceof List) {
            List<User> accountsList = (List<User>) response;

            for(User accounList: accountsList) {
                accounts_id.setCellValueFactory(new PropertyValueFactory<>("id"));
                accounts_username.setCellValueFactory(new PropertyValueFactory<>("username"));
                accounts_name.setCellValueFactory(new PropertyValueFactory<>("name"));
                accounts_surname.setCellValueFactory(new PropertyValueFactory<>("surname"));
                accounts_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
                accounts_mail.setCellValueFactory(new PropertyValueFactory<>("mail"));
                accounts_pesel.setCellValueFactory(new PropertyValueFactory<>("pesel"));

                accountsTable.getItems().add(accounList);
            }
        }
    }

}
