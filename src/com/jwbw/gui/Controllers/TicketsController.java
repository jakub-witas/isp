package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.Naprawa_serwisowa;
import com.jwbw.isp.Utrzymanie_sieci;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TicketsController {

    @FXML
    TableColumn<Utrzymanie_sieci, Integer> networkTabId;
    @FXML
    TableColumn<Utrzymanie_sieci, Timestamp> networkTabCreationDate, networkTabDueToDate;
    @FXML
    TableColumn<Utrzymanie_sieci, String> networkTabLastEntry;
    @FXML
    TableView<Utrzymanie_sieci> networkTicket;
    @FXML
    TableColumn<Naprawa_serwisowa, Integer> hardwareTabId;
    @FXML
    TableColumn<Naprawa_serwisowa, Timestamp> hardwareTabCreationDate, hardwareTabDueToDate;
    @FXML
    TableColumn<Naprawa_serwisowa, String> hardwareTabLastEntry;
    @FXML
    TableView<Naprawa_serwisowa> hardwareTicket;

    @FXML
    private Button tooltipButton1;

    @FXML
    private Button tooltipButton2;

    public static List<Utrzymanie_sieci> networkTicketList = new ArrayList<>();
    public static List<Naprawa_serwisowa> hardwareTicketList = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        getNetworkTicketList();
        getHardwareTicketList();

        final Tooltip t1 = new Tooltip();
        t1.setText("Pokaż szczegóły");
        tooltipButton1.setTooltip(t1);
        tooltipButton1.setOnMouseEntered(e -> tooltipButton1.setStyle("-fx-background-color: green"));
        tooltipButton1.setOnMouseExited(e -> tooltipButton1.setStyle("-fx-background-color: white"));

        final Tooltip t2 = new Tooltip();
        t2.setText("Pokaż szczegóły");
        tooltipButton2.setTooltip(t2);
        tooltipButton2.setOnMouseEntered(e -> tooltipButton2.setStyle("-fx-background-color: green"));
        tooltipButton2.setOnMouseExited(e -> tooltipButton2.setStyle("-fx-background-color: white"));
    }

    private void getNetworkTicketList() throws SQLException {
        Object response = Proxy.getNetworkTicketList();

        if(response instanceof List) {
            networkTicketList = (List<Utrzymanie_sieci>) response;

            for(Utrzymanie_sieci NetworkList: networkTicketList) {
                networkTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
                networkTabCreationDate.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                networkTabDueToDate.setCellValueFactory(new PropertyValueFactory<>("data_wykonania"));
                networkTabLastEntry.setCellValueFactory(cellData ->
                        new ReadOnlyStringWrapper(cellData.getValue().getWpisy().get(cellData.getValue().getWpisy().size()-1).getOpis()));

                networkTicket.getItems().add(NetworkList);

            }
        }
    }

    private void getHardwareTicketList() throws SQLException {
        Object response = Proxy.getHardwareTicketList();

        if(response instanceof List) {
            hardwareTicketList = (List<Naprawa_serwisowa>) response;

            for(Naprawa_serwisowa HardwareList: hardwareTicketList) {
                hardwareTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
                hardwareTabCreationDate.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                hardwareTabDueToDate.setCellValueFactory(new PropertyValueFactory<>("data_wykonania"));
                hardwareTabLastEntry.setCellValueFactory(cellData ->
                        new ReadOnlyStringWrapper(cellData.getValue().getWpisy().get(cellData.getValue().getWpisy().size()-1).getOpis()));

                hardwareTicket.getItems().add(HardwareList);

            }
        }
    }

    public void handleButtonEditNetwork(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../klient/editNetwork.fxml"));
            stage.setTitle("Naprawy sieciowe");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleButtonEditHardware(MouseEvent mouseEvent) {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("../klient/editHardware.fxml"));
            stage.setTitle("Naprawy sprzętowe");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
