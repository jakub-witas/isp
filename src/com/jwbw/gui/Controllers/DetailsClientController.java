package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.Dokument;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class DetailsClientController {

    @FXML
    TableColumn <Dokument, Integer> account_id;

    @FXML
    TableColumn <Dokument, String> account_name, account_pesel,account_card, account_contract, account_price;

    @FXML
    TableColumn <Dokument, Timestamp> account_datestart, account_dateend;

    @FXML
    TableView <Dokument> clientsTable;

    @FXML
    private Button displayButton;

    @FXML
    public void initialize() throws SQLException {
        getContractClient();
        final Tooltip tip = new Tooltip();
        tip.setText("Wyświetl szczegóły umowy");
        displayButton.setTooltip(tip);
        displayButton.setOnMouseEntered(e -> displayButton.setStyle("-fx-border-color: red"));
        displayButton.setOnMouseExited(e -> displayButton.setStyle("-fx-border-color: white"));
        //account_name.setStyle("-fx-alignment: CENTER;");
        //account_pesel.setStyle("-fx-alignment: CENTER;");
        //account_card.setStyle("-fx-alignment: CENTER;");
        //account_contract.setStyle("-fx-alignment: CENTER;");
        //account_price.setStyle("-fx-alignment: CENTER;");
        //account_datestart.setStyle("-fx-alignment: CENTER;");
        //account_dateend.setStyle("-fx-alignment: CENTER;");
        //account_id.setStyle("-fx-alignment: CENTER;");
    }

    private void getContractClient() throws SQLException {
        Object response = Proxy.getContractClient();

        if(response instanceof List) {
            List<Dokument> contractList = (List<Dokument>) response;

            for(Dokument contraList: contractList) {
                System.out.println("eeeeee");
                account_id.setCellValueFactory(new PropertyValueFactory<>("id"));
               // account_name.setCellValueFactory(new PropertyValueFactory<>("name"));
               // account_pesel.setCellValueFactory(new PropertyValueFactory<>("name"));
               // account_card.setCellValueFactory(new PropertyValueFactory<>("surname"));
              //  account_contract.setCellValueFactory(new PropertyValueFactory<>("phone"));
              //  account_price.setCellValueFactory(new PropertyValueFactory<>("mail"));
                account_datestart.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                account_dateend.setCellValueFactory(new PropertyValueFactory<>("data_wygasniecia"));

                clientsTable.getItems().add(contraList);
            }
        }
    }

    public void handleButtonDisplay() {

    }
}
