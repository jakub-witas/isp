package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AccountsController {

    @FXML
    TableColumn<User, Integer> accounts_id;

    @FXML
    TableColumn<User, String> accounts_username, accounts_name, accounts_surname, accounts_phone, accounts_mail, accounts_pesel;

    @FXML
    TableColumn<User,String> accounts_city, accounts_street, accounts_code, accounts_number;

    @FXML
    TableView<User> accountsTable;

    @FXML
    private Button deleteButton, editButton;

    @FXML
    public void initialize() throws SQLException {
        getAccountsList();
        final Tooltip tooltip = new Tooltip();
        tooltip.setText("Usuń konto użytkownika");
        deleteButton.setTooltip(tooltip);
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("-fx-border-color: red"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("-fx-border-color: #99CCFF; -fx-background-color: #99CCFF"));
        final Tooltip tool = new Tooltip();
        tool.setText("Edytuj konto użytkownika");
        editButton.setTooltip(tool);
        editButton.setOnMouseEntered(e -> editButton.setStyle("-fx-border-color: red"));
        editButton.setOnMouseExited(e -> editButton.setStyle("-fx-border-color: #99CCFF; -fx-background-color: #99CCFF"));
        accounts_username.setStyle("-fx-alignment: CENTER;");
        accounts_name.setStyle("-fx-alignment: CENTER;");
        accounts_surname.setStyle("-fx-alignment: CENTER;");
        accounts_username.setStyle("-fx-alignment: CENTER;");
        accounts_phone.setStyle("-fx-alignment: CENTER;");
        accounts_mail.setStyle("-fx-alignment: CENTER;");
        accounts_pesel.setStyle("-fx-alignment: CENTER;");
        accounts_city.setStyle("-fx-alignment: CENTER;");
        accounts_street.setStyle("-fx-alignment: CENTER;");
        accounts_code.setStyle("-fx-alignment: CENTER;");
        accounts_number.setStyle("-fx-alignment: CENTER;");
        accounts_id.setStyle("-fx-alignment: CENTER;");
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
                accounts_city.setCellValueFactory(new PropertyValueFactory<>("city"));
                accounts_street.setCellValueFactory(new PropertyValueFactory<>("street"));
                accounts_code.setCellValueFactory(new PropertyValueFactory<>("code"));
                accounts_number.setCellValueFactory(new PropertyValueFactory<>("home_number"));

                accountsTable.getItems().add(accounList);
            }
        }
    }

    public void handleButtonDeleteClient() throws SQLException{
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Powiadomienie");
        if(accountsTable.getSelectionModel().getSelectedItem() != null) {
            accountsTable.getSelectionModel().getSelectedItem().setId(accountsTable.getSelectionModel().getSelectedItem().getId());
            Proxy.setDeleteAccountStatus(accountsTable.getSelectionModel().getSelectedItem().getId());
            accountsTable.refresh();
        }else{
            alert.setContentText("Wybierz klienta, którego chcesz usunąć");
            alert.showAndWait();
        }
    }


    public void handleButtonEditClient() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setTitle("Powiadomienie");

        if(accountsTable.getSelectionModel().getSelectedItem() != null) {
               TablePosition pos = accountsTable.getSelectionModel().getSelectedCells().get(0);
               int row= pos.getRow();
               TableColumn col = pos.getTableColumn();
               String data = (String) col.getCellObservableValue(row).getValue();
               System.out.println(data);



            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("../specjalista/editClient.fxml"));
                stage.setTitle("Edycja konta klienta");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(this.editButton.getScene().getWindow());
                stage.setOnCloseRequest(event -> {
                    //  try {
                    //    getAccountsList();
                    //  } catch (SQLException throwables) {
                    //     throwables.printStackTrace();
                    //  }
                });
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            alert.setContentText("Wybierz klienta, którego chcesz edytować");
            alert.showAndWait();
        }
        /*

        */
    }
}