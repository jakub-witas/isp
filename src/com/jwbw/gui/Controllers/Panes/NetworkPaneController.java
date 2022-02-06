package com.jwbw.gui.Controllers.Panes;

import com.jwbw.Main;
import com.jwbw.Proxy;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Dokument;
import com.jwbw.isp.Utrzymanie_sieci;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NetworkPaneController {
    @FXML
    Button sendButton;
    @FXML
    TextArea descArea;
    @FXML
    ComboBox<String> contractCombo;

    Alert alert = new Alert(Alert.AlertType.WARNING);
    @FXML
    public void initialize() throws SQLException {
                List<String> docList = new ArrayList<>();
                for(Object dokumentList: Proxy.getServiceContracts(Proxy.loggedUser.getId())) {
                    docList.add(((Dokument)dokumentList).getNr_dokumentu());
                }
                ObservableList<String> observableList = FXCollections.observableList(docList);
                contractCombo.setItems(observableList);
    }

    public void onSendButton() throws SQLException {
        if(contractCombo.getValue() == null || contractCombo.getValue().isEmpty()) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setContentText("Nie wybrano umowy, której ma dotyczyć zgłoszenie");
            alert.showAndWait();
            return;
        } else if(!Proxy.checkConnection()) {
                alert.setAlertType(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setTitle("Błąd połączenia");
                alert.setContentText("Brak połączenia z bazą danych.");
                alert.showAndWait();
                return;
            } else {
            Utrzymanie_sieci utrzymanieSieci = new Utrzymanie_sieci(contractCombo.getValue(), descArea.getText());
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Zgłoszenie zostało pomyślnie dodane");
            alert.setTitle("Powodzenie");
            alert.showAndWait();
            descArea.clear();
            contractCombo.valueProperty().set(null);
        }
    }

}
