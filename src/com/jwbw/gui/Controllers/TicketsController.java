package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.isp.Naprawa_serwisowa;
import com.jwbw.isp.Utrzymanie_sieci;
import com.jwbw.isp.Wpis;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

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
    TableColumn<Utrzymanie_sieci, Wpis> networkTabLastEntry;
    @FXML
    TableView<Utrzymanie_sieci> networkTicket;
    @FXML
    TableColumn<Naprawa_serwisowa, Integer> hardwareTabId;
    @FXML
    TableColumn<Naprawa_serwisowa, Timestamp> hardwareTabCreationDate, hardwareTabDueToDate;
    @FXML
    TableColumn<Naprawa_serwisowa, Wpis> hardwareTabLastEntry;
    @FXML
    TableView<Naprawa_serwisowa> hardwareTicket;

    public static List<Utrzymanie_sieci> networkTicketList = new ArrayList<>();
    public static List<Naprawa_serwisowa> hardwareTicketList = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        getNetworkTicketList();
        getHardwareTicketList();
    }

    private void getNetworkTicketList() throws SQLException {
        Object response = Main.Database.getNetworkTicketList();

        if(response instanceof List) {
            networkTicketList = (List<Utrzymanie_sieci>) response;

            for(Utrzymanie_sieci NetworkList: networkTicketList) {
                networkTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
                networkTabCreationDate.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                networkTabDueToDate.setCellValueFactory(new PropertyValueFactory<>("data_wykonania"));
                networkTabLastEntry.setCellValueFactory(new PropertyValueFactory<>("lastEntry"));
                networkTicket.getItems().add(NetworkList);

            }
        }
    }

    private void getHardwareTicketList() throws SQLException {
        Object response = Main.Database.getHardwareTicketList();

        if(response instanceof List) {
            hardwareTicketList = (List<Naprawa_serwisowa>) response;

            for(Naprawa_serwisowa HardwareList: hardwareTicketList) {
                hardwareTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
                hardwareTabCreationDate.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                hardwareTabDueToDate.setCellValueFactory(new PropertyValueFactory<>("data_wykonania"));
                hardwareTabLastEntry.setCellValueFactory(new PropertyValueFactory<>("lastEntry"));
                hardwareTicket.getItems().add(HardwareList);

            }
        }
    }
}
