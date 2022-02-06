package com.jwbw.gui.Controllers.Modals;

import com.jwbw.Proxy;
import com.jwbw.gui.Controllers.TicketsController;
import com.jwbw.isp.*;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

public class NetworkEditController {

    public static Wpis interactedEntry;
    @FXML
    private Button addEntryButton, editEntryButton, removeEntryButton, closeIssueButton, upgradeLevelButton;

    @FXML
    private Label deviceName, deviceProducer, deviceId, levelLabel, levelLabelLabel, deviceSn, contractLabel, transferLabel, ipLabel;

    @FXML
    private ListView<String> featuresListView;

    @FXML
    private TableView<Wpis> entryTable;

    @FXML
    private TableColumn<Wpis, String> entryId, entryDate, entryType, entryAuthor, entryDescription;

    public static Utrzymanie_sieci naprawaSieci = null;

    Alert alert = new Alert(Alert.AlertType.WARNING);

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

        featuresListView.getSelectionModel().setSelectionMode(null);

        naprawaSieci = TicketsController.detailedNaprawaSieci;
        entryTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        this.levelLabel.setText(String.valueOf(naprawaSieci.getPoziom()));
        loadEntriesTable();
        loadData();
        loadListViewData();

        if(Proxy.loggedUser.getRole() == Role.CLIENT) {
            disableButtonsForClient();
        }

        if(naprawaSieci.getData_wykonania()!= null) {
            disableAllButtons(true);
        }
    }

    private void disableButtonsForClient() {
        closeIssueButton.setDisable(true);
        upgradeLevelButton.setDisable(true);
        closeIssueButton.setVisible(false);
        upgradeLevelButton.setVisible(false);
        levelLabel.setVisible(false);
        levelLabelLabel.setVisible(false);
    }

    private void loadButtonTooltips() {
        addEntryButton.setTooltip(new Tooltip("Dodaj wpis"));
        editEntryButton.setTooltip(new Tooltip("Edytuj wpis"));
        removeEntryButton.setTooltip(new Tooltip("Usuń wpis"));
    }

    private void disableAllButtons(boolean value) {
        addEntryButton.setDisable(value);
        editEntryButton.setDisable(value);
        removeEntryButton.setDisable(value);
        upgradeLevelButton.setDisable(value);
        closeIssueButton.setText("Otwórz naprawę");
    }

    private void loadData() {
        Urzadzenie_sieciowe urzadzenieSieciowe = Proxy.getNetworkDevice(naprawaSieci.getNr_umowy());
        deviceName.setText(urzadzenieSieciowe.getNazwa());
        deviceId.setText(String.valueOf(urzadzenieSieciowe.getId()));
        deviceProducer.setText(urzadzenieSieciowe.getProducent());
        deviceSn.setText(urzadzenieSieciowe.getSn());
        contractLabel.setText(urzadzenieSieciowe.getNrUmowy());
        ipLabel.setText(urzadzenieSieciowe.getIp_address());
        for(Dokument dokument: naprawaSieci.getKlient().getDokumenty()) {
            if (dokument instanceof Umowa_usluga && dokument.getNr_dokumentu().equals(urzadzenieSieciowe.getNrUmowy())) {
                for(Object obj: ((Umowa_usluga) dokument).getOferta()) {
                    if (obj instanceof Pakiet_internetu) {
                        transferLabel.setText(((Pakiet_internetu) obj).getDownload() + "/" + ((Pakiet_internetu) obj).getUpload() + " Mb/s");
                    }
                }
            }
        }
        transferLabel.setAlignment(Pos.CENTER_RIGHT);
    }

    private void loadListViewData() {
        for(Dokument dokument: naprawaSieci.getKlient().getDokumenty()) {
            if(dokument instanceof Umowa_usluga) {
                if(dokument.getNr_dokumentu().equals(naprawaSieci.getNr_umowy())) {
                    for(Object obj: ((Umowa_usluga) dokument).getOferta()) {
                        if (obj instanceof Pakiet_internetu&& ((Pakiet_internetu) obj).getAdditionalFeaturesList()!=null) {
                            for(InternetFeatures feature: ((Pakiet_internetu) obj).getAdditionalFeaturesList()) {
                                featuresListView.getItems().add(feature.getName());
                            }
                        }
                        if (obj instanceof Telewizja && ((Telewizja) obj).getAdditionalFeaturesList()!=null) {
                            for(TelevisionFeatures feature: ((Telewizja) obj).getAdditionalFeaturesList()) {
                                featuresListView.getItems().add(feature.getName());
                            }
                        }
                        if (obj instanceof GSM&& ((GSM) obj).getAdditionalFeaturesList()!=null) {
                            for(TelephoneFeatures feature: ((GSM) obj).getAdditionalFeaturesList()) {
                                featuresListView.getItems().add(feature.getName());
                            }
                        }
                    }
                }
            }
        }
    }

    public void loadEntriesTable() {
        if(naprawaSieci.getWpisy() != null) {
            for(Wpis wpis: naprawaSieci.getWpisy()) {
                if(wpis.getOdbiorca() != null) {
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
                } else if (!Objects.equals(entryTable.getSelectionModel().getSelectedItem().getAutor().getId(), Proxy.loggedUser.getId())) {
                    alert.setAlertType(Alert.AlertType.WARNING);
                    alert.setTitle("Błąd edycji");
                    alert.setHeaderText(null);
                    alert.setContentText("Wpis został utworzony przez kogoś innego - nie możesz go edytować.");
                    alert.showAndWait();
                    return;
                }
                interactedEntry = entryTable.getSelectionModel().getSelectedItem();

                controller.setData(interactedEntry, naprawaSieci);
                stage.setTitle("Edycja wpisu");
            } else {
                stage.setTitle("Dodanie wpisu");
                controller.setData(null, naprawaSieci);
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
            alert.setAlertType(Alert.AlertType.WARNING);
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
            naprawaSieci.getWpisy().remove(entryTable.getSelectionModel().getSelectedItem());
            Proxy.removeEntry(naprawaSieci);
            entryTable.getItems().clear();
            loadEntriesTable();
            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Powodzenie");
            alert.setContentText("Pomyślnie usunięto wpis");
            alert.showAndWait();
        }
    }

    public void onCloseIssueButton() {
        if(naprawaSieci.getData_wykonania() == null) {
            naprawaSieci.setData_wykonania(Timestamp.valueOf(LocalDateTime.now()));
            if (Proxy.closeIssueTicket(naprawaSieci)) {
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
                naprawaSieci.setData_wykonania(null);
            }
        } else {
            Timestamp closeDate = naprawaSieci.getData_wykonania();
            naprawaSieci.setData_wykonania(null);
            if (Proxy.closeIssueTicket(naprawaSieci)) {
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
                naprawaSieci.setData_wykonania(closeDate);
            }
        }
    }

    public void onChangeLevelButton() {
        if(naprawaSieci.getPoziom() >= 3) {
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.setTitle("Niepowodzenie");
            alert.setContentText("Zlecenie znajduje się już na najwyższym dostępnym poziomie.");
            alert.showAndWait();
        } else {

            naprawaSieci.setPoziom(naprawaSieci.getPoziom() + 1);
            if (Proxy.upgradeIssueLevel(naprawaSieci)) {
                alert.setAlertType(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setTitle("Powodzenie");
                alert.setContentText("Zlecenie zostało pomyślnie przekazane do specjalistów z wyższym poziomem umiejętności." +
                        "Nastąpi zamnięcie okna.");
                alert.showAndWait();
                Proxy.naprawySieci.remove(naprawaSieci);
                closeWindow();
            } else {
                alert.setAlertType(Alert.AlertType.WARNING);
                alert.setHeaderText(null);
                alert.setTitle("Niepowodzenie");
                alert.setContentText("Coś poszło nie tak.");
                alert.showAndWait();
                naprawaSieci.setPoziom(naprawaSieci.getPoziom()-1);
            }
        }
    }

    @FXML
    private void closeWindow() {

        Stage window = (Stage) addEntryButton.getScene().getWindow();
        window.fireEvent(
                new javafx.stage.WindowEvent(
                        window,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );

    }
}
