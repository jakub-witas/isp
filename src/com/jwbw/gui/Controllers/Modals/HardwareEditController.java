package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Proxy;
import com.jwbw.gui.Controllers.TicketsController;
import com.jwbw.isp.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class HardwareEditController {


    public static Wpis interactedEntry;
    @FXML
    private Label deviceName, deviceProducer, deviceSn, deviceId, cost, orderNumber, levelLabel, levelLabelLabel;

    @FXML
    private TableView<Wpis> entryTable;

    @FXML
    private TableColumn<Wpis, String> entryId, entryDescription, entryDate, entryAuthor, entryType;

    @FXML
    private TableView<Czesc_komputerowa> partsTable;

    @FXML
    private TableColumn<Czesc_komputerowa, String> partsName, partsProducer, partsPrice, partsPurpose, partsConnector, partsSn;

    @FXML
    private TableView<Cennik_uslug> servicesTable;

    @FXML
    private TableColumn<Cennik_uslug, String> serviceName, servicePrice;

    @FXML
    private ComboBox<Cennik_uslug> servicesCombo;

    @FXML
    private Button addEntryButton, removeEntryButton, editEntryButton, addOrderButton, removeOrderButton, addServiceButton,
            removeServiceButton, upgradeLevelButton, closeIssueButton;

    public static Naprawa_serwisowa naprawaSerwisowa = null;

    Alert alert = new Alert(Alert.AlertType.WARNING);

    @FXML
    public void initialize() {
        loadButtonTooltips();

        entryDescription.setCellFactory(tc -> {
            TableCell<Wpis, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(entryDescription.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });

        servicesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        naprawaSerwisowa = TicketsController.detailedNaprawaSprzetu;
        this.cost.setText(naprawaSerwisowa.getKoszt() + " zł");
        this.levelLabel.setText(String.valueOf(naprawaSerwisowa.getPoziom()));
        loadDeviceData();
        loadServicesTable();
        loadEntriesTable();
        loadServicesComboData();
        if(naprawaSerwisowa.getZamowienie() != null) {
            //orderNumber.setText(naprawaSerwisowa.getZamowienie().getNr_dokumentu());
            loadPartsTable();
        } else {
            orderNumber.setText("Brak");
        }

        if(Proxy.loggedUser.getRole() == Role.CLIENT) {
            disableButtonsForClient();
        }

        if(naprawaSerwisowa.getData_wykonania()!= null) {
            disableAllButtons(true);
        }
    }

    private void disableAllButtons(boolean value) {
        servicesCombo.setDisable(value);
        addServiceButton.setDisable(value);
        removeServiceButton.setDisable(value);
        addOrderButton.setDisable(value);
        removeOrderButton.setDisable(value);
        upgradeLevelButton.setDisable(value);
        addEntryButton.setDisable(value);
        editEntryButton.setDisable(value);
        removeEntryButton.setDisable(value);
        closeIssueButton.setText("Otwórz naprawę");
    }

    private void disableButtonsForClient() {
        servicesCombo.setDisable(true);
        addServiceButton.setDisable(true);
        removeServiceButton.setDisable(true);
        addOrderButton.setDisable(true);
        removeOrderButton.setDisable(true);
        upgradeLevelButton.setDisable(true);
        upgradeLevelButton.setVisible(false);
        levelLabel.setVisible(false);
        levelLabelLabel.setVisible(false);
        closeIssueButton.setDisable(true);
        closeIssueButton.setVisible(false);
    }

    public void onCloseIssueButton() {
        if(naprawaSerwisowa.getData_wykonania() == null) {
            naprawaSerwisowa.setData_wykonania(Timestamp.valueOf(LocalDateTime.now()));
            if (Proxy.closeHardwareIssueTicket(naprawaSerwisowa)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Powodzenie");
                alert.setContentText("Naprawa została pomyślnie zakończona.");
                alert.showAndWait();
                disableAllButtons(true);
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Niepowodzenie");
                alert.setContentText("Coś poszło nie tak.");
                alert.showAndWait();
                naprawaSerwisowa.setData_wykonania(null);
            }
        } else {
            Timestamp closeDate = naprawaSerwisowa.getData_wykonania();
            naprawaSerwisowa.setData_wykonania(null);
            if (Proxy.closeHardwareIssueTicket(naprawaSerwisowa)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Powodzenie");
                alert.setContentText("Naprawa została pomyślnie otworzona.");
                alert.showAndWait();
                disableAllButtons(false);
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Niepowodzenie");
                alert.setContentText("Coś poszło nie tak.");
                alert.showAndWait();
                naprawaSerwisowa.setData_wykonania(closeDate);
            }
        }
    }


    public void onChangeLevelButton() {
        if(naprawaSerwisowa.getPoziom() >= 3) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Niepowodzenie");
            alert.setContentText("Zlecenie znajduje się już na najwyższym dostępnym poziomie.");
            alert.showAndWait();
        } else {

            naprawaSerwisowa.setPoziom(naprawaSerwisowa.getPoziom() + 1);
            if (Proxy.upgradeIssueLevel(naprawaSerwisowa)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Powodzenie");
                alert.setContentText("Zlecenie zostało pomyślnie przekazane do specjalistów z wyższym poziomem umiejętności." +
                        "Nastąpi zamnięcie okna.");
                alert.showAndWait();
                Proxy.naprawySprzetu.remove(naprawaSerwisowa);
                closeWindow();
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Niepowodzenie");
                alert.setContentText("Coś poszło nie tak.");
                alert.showAndWait();
                naprawaSerwisowa.setPoziom(naprawaSerwisowa.getPoziom()-1);
            }
        }
    }

    private void loadButtonTooltips() {
        addEntryButton.setTooltip(new Tooltip("Dodaj wpis"));
        editEntryButton.setTooltip(new Tooltip("Edytuj wpis"));
        removeEntryButton.setTooltip(new Tooltip("Usuń wpis"));
        addOrderButton.setTooltip(new Tooltip("Dodaj zamówienie"));
        removeOrderButton.setTooltip(new Tooltip("Usuń zamówienie"));
    }

    public void onDeleteServiceButton() {
        naprawaSerwisowa.getWykonane_uslugi().remove(servicesTable.getSelectionModel().getSelectedItem());
        naprawaSerwisowa.calculatePrice();
        cost.setText(naprawaSerwisowa.getKoszt() + " zł");
        servicesTable.getItems().clear();
        loadServicesTable();
    }

    public void onAddOrderButton(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../klient/Panes/CreateOrder.fxml"));
            stage.setScene(new Scene(loader.load()));
            CreateOrderController controller = loader.getController();
            controller.loadData(naprawaSerwisowa);
            stage.setResizable(false);
            stage.setTitle("Nowe zamówienie");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            stage.setOnCloseRequest(windowEvent -> {
                naprawaSerwisowa.calculatePrice();
                cost.setText(naprawaSerwisowa.getKoszt() + " zł");
                Proxy.updateHardwareTicketData(naprawaSerwisowa);
                partsTable.getItems().clear();
                loadPartsTable();
            });
            stage.show();
        } catch (IOException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Niepowodzenie");
            alert.setHeaderText(null);
            alert.setContentText("Brak części do dodania.");
            alert.showAndWait();
        }
    }

    public void onInteractEntryButton(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../klient/Panes/InteractEntry.fxml"));
            stage.setScene(new Scene(loader.load()));
            InteractEntryController controller = loader.getController();
            if(event.getSource().equals(editEntryButton)) {
                if(entryTable.getSelectionModel().getSelectedItem() == null) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setTitle("Brak zaznaczenia");
                    alert.setHeaderText(null);
                    alert.setContentText("Brak wpisu do edycji");
                    alert.showAndWait();
                    return;
                } else if (entryTable.getSelectionModel().getSelectedItem().getAutor().getId() != Proxy.loggedUser.getId()) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setTitle("Błąd edycji");
                    alert.setHeaderText(null);
                    alert.setContentText("Wpis został utworzony przez kogoś innego - nie możesz go edytować.");
                    alert.showAndWait();
                    return;
                }
                interactedEntry = entryTable.getSelectionModel().getSelectedItem();

                controller.setData(interactedEntry);
                stage.setTitle("Edycja wpisu");
            } else {
                stage.setTitle("Dodanie wpisu");
                controller.setData(null);
            }

            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node)event.getSource()).getScene().getWindow());
            stage.setOnCloseRequest(windowEvent -> {
                if (interactedEntry != null) {
                    Proxy.updateEntry(interactedEntry);
                }
                interactedEntry = null;
                entryTable.getItems().clear();
                loadEntriesTable();
            });
            stage.show();
        }
        catch (IOException e) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setTitle("Niepowodzenie");
            alert.setHeaderText(null);
            alert.setContentText("Coś poszło nie tak, nie można otworzyć okna.");
            alert.showAndWait();
        }
    }

    public void onDeleteEntryButton() {
        if(entryTable.getSelectionModel().getSelectedItem() == null) {
            alert.setHeaderText(null);
            alert.setTitle("Brak zaznaczenia");
            alert.setContentText("Nie wybrano wpisu do usunięcia");
            alert.showAndWait();
        } else if (!Objects.equals(entryTable.getSelectionModel().getSelectedItem().getAutor().getId(), Proxy.loggedUser.getId())) {
            alert.setTitle("Błąd usuwania");
            alert.setContentText("Wybrany wpis został dodany przez kogoś innego. Nie możesz go usunąć.");
            alert.showAndWait();
        }
        else {
            naprawaSerwisowa.getWpisy().remove(entryTable.getSelectionModel().getSelectedItem());
            Proxy.removeEntry(naprawaSerwisowa);
            entryTable.getItems().clear();
            loadEntriesTable();
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Powodzenie");
            alert.setContentText("Pomyślnie usunięto wpis");
            alert.showAndWait();
        }
    }

    public void onAddServiceButton() {
        naprawaSerwisowa.dodajUsluge(servicesCombo.getValue());
        naprawaSerwisowa.calculatePrice();
        cost.setText(naprawaSerwisowa.getKoszt() + " zł");
        servicesTable.getItems().clear();
        loadServicesTable();
    }

    private void loadPartsTable() {
        if(naprawaSerwisowa.getZamowienie().size() > 0) {
            for(Zamowienie zamowienie: naprawaSerwisowa.getZamowienie()) {
                for (Czesc_komputerowa czesc : zamowienie.getCzesci()) {
                    partsName.setCellValueFactory(new PropertyValueFactory<>("nazwa"));
                    partsConnector.setCellValueFactory(new PropertyValueFactory<>("port"));
                    partsPrice.setCellValueFactory(new PropertyValueFactory<>("koszt"));
                    partsProducer.setCellValueFactory(new PropertyValueFactory<>("producent"));
                    partsSn.setCellValueFactory(new PropertyValueFactory<>("sn"));
                    partsPurpose.setCellValueFactory(new PropertyValueFactory<>("przeznaczenie"));
                    partsTable.getItems().add(czesc);
                }
            }
        }
    }

    public void onRemoveOrderButton() {
        if(partsTable.getSelectionModel().getSelectedItem() == null) {
            alert.setHeaderText(null);
            alert.setTitle("Brak zaznaczenia");
            alert.setContentText("Nie wybrano części do usunięcia");
            alert.showAndWait();
        } else {
            for(Zamowienie zamowienie: naprawaSerwisowa.getZamowienie()) {
                for(Czesc_komputerowa czesc: zamowienie.getCzesci()) {
                    if(czesc == partsTable.getSelectionModel().getSelectedItem()) {
                         if(!Proxy.updatePartsOwner(czesc)) {
                             alert.setHeaderText(null);
                             alert.setTitle("Nieoczekiwany błąd");
                             alert.setContentText("Coś poszło nie tak.");
                             alert.showAndWait();
                             return;
                         }
                        zamowienie.getCzesci().remove(czesc);
                         zamowienie.calculatePrice();
                        naprawaSerwisowa.calculatePrice();
                        cost.setText(naprawaSerwisowa.getKoszt() + " zł");
                        break;
                    }
                }
                if( zamowienie.getCzesci().size() == 0) {
                    if(!Proxy.removeOrderFromIssue(naprawaSerwisowa, zamowienie.getId())) {
                        alert.setHeaderText(null);
                        alert.setTitle("Nieoczekiwany błąd");
                        alert.setContentText("Coś poszło nie tak.");
                        alert.showAndWait();
                        return;
                    }
                    naprawaSerwisowa.getZamowienie().remove(zamowienie);
                    break;
                }
            }

            partsTable.getItems().clear();
            loadPartsTable();
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Powodzenie");
            alert.setContentText("Usuwanie zakończone pomyślnie.");
            alert.showAndWait();
        }
    }

    private void loadServicesComboData() {
        List<Cennik_uslug> lista = Cennik_uslug.getValuesList();
        servicesCombo.setItems(FXCollections.observableList(lista));
    }

    private void loadServicesTable() {
        if(naprawaSerwisowa.getWykonane_uslugi() != null) {
            for(Cennik_uslug usluga: naprawaSerwisowa.getWykonane_uslugi()) {
                serviceName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                        cellData.getValue().getName()
                ));
                servicePrice.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                        cellData.getValue().getPrice() + " zł"
                ));
                servicesTable.getItems().add(usluga);
            }
        }
    }

    private void loadEntriesTable() {
        if(naprawaSerwisowa.getWpisy() != null) {
            for(Wpis wpis: naprawaSerwisowa.getWpisy()) {
                if(wpis.isWasRead()) {
                    entryType.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("Powiadomienie"));
                } else {
                    entryType.setCellValueFactory(cellData -> new ReadOnlyStringWrapper("Wpis"));
                }
                entryId.setCellValueFactory(new PropertyValueFactory<>("id"));
                entryDate.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                entryDescription.setCellValueFactory(new PropertyValueFactory<>("opis"));
                entryAuthor.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
                    cellData.getValue().getNameSurname()
                ));

                entryTable.getItems().add(wpis);
            }
        }
    }

    private void loadDeviceData() {
        deviceId.setText(String.valueOf(naprawaSerwisowa.getUrzadzenie_naprawiane().getId()));
        deviceName.setText(naprawaSerwisowa.getUrzadzenie_naprawiane().getNazwa());
        deviceProducer.setText(naprawaSerwisowa.getUrzadzenie_naprawiane().getProducent());
        deviceSn.setText(naprawaSerwisowa.getUrzadzenie_naprawiane().getSn());
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) addOrderButton.getScene().getWindow();
        window.fireEvent(
                new javafx.stage.WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );

    }
}
