package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Proxy;
import com.jwbw.isp.Czesc_komputerowa;
import com.jwbw.isp.Naprawa_serwisowa;
import com.jwbw.isp.Zamowienie;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreateOrderController {

    private Naprawa_serwisowa naprawaSerwisowa;
    @FXML
    private TableView<Czesc_komputerowa> partsTable;

    @FXML
    private TableColumn<Czesc_komputerowa, String> partsName, partsProducer, partsPrice, partsPurpose, partsConnector, partsSn;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    public void initialize() throws SQLException {
        loadPartsTable();
        partsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    private void loadPartsTable() throws SQLException {
        List<Czesc_komputerowa> partsList = Proxy.getUnownedPartsList();

        if(partsList.get(0) != null) {
            for(Czesc_komputerowa czesc: partsList) {
                partsName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
                partsProducer.setCellValueFactory(new PropertyValueFactory<>("producent"));
                partsSn.setCellValueFactory(new PropertyValueFactory<>("sn"));
                partsConnector.setCellValueFactory(new PropertyValueFactory<>("port"));
                partsPrice.setCellValueFactory(new PropertyValueFactory<>("koszt"));
                partsPurpose.setCellValueFactory(new PropertyValueFactory<>("przeznaczenie"));
                partsTable.getItems().add(czesc);
            }
        }
    }

    public void loadData(Naprawa_serwisowa naprawa) {
        naprawaSerwisowa = naprawa;
    }

    public void onAddZamowienieButton() throws SQLException {
        if(partsTable.getSelectionModel().getSelectedItem() == null) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setContentText("Nie zaznaczono żadnych części.");
            alert.setTitle("Brak zaznaczenia");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            var zamowienie = new Zamowienie();
            List<Czesc_komputerowa> partsList = new ArrayList<>(partsTable.getSelectionModel().getSelectedItems());
            for(Czesc_komputerowa czesc: partsList) {
                czesc.setWlasciciel(naprawaSerwisowa.getWlasciciel());
                Proxy.addPartsOwner(czesc);
            }
            zamowienie.setCzesci(partsList);
            zamowienie.calculatePrice();
            zamowienie.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));
            zamowienie.setData_wygasniecia(zamowienie.getData_utworzenia());
            zamowienie.setId(Proxy.sendZamowienieGetId(zamowienie));
            if(naprawaSerwisowa.getZamowienie() == null) {
                List<Zamowienie> list = new ArrayList<>();
                list.add(zamowienie);
                naprawaSerwisowa.setZamowienie(list);
            } else {
                naprawaSerwisowa.getZamowienie().add(zamowienie);
            }
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setContentText("Zamówienie zostało pomyślnie dodane.");
            alert.setTitle("Powodzenie");
            alert.setHeaderText(null);
            alert.showAndWait();
            closeWindow();
        }
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) partsTable.getScene().getWindow();
        window.fireEvent(
                new WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );

    }
}
