package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.Klient;
import com.jwbw.isp.Utrzymanie_sieci;
import com.jwbw.isp.Wpis;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.security.Timestamp;
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

    public static List<Utrzymanie_sieci> networkTicketList = new ArrayList<>();

    private void getNetworkTicketList()  {
        Object response = Main.Database.getNetworkTicketList();

        if(response instanceof List) {
            networkTicketList = (List<Utrzymanie_sieci>) response;

            for(Utrzymanie_sieci NetworkList: networkTicketList) {

                networkTabId.setCellValueFactory(new PropertyValueFactory<>("id"));
                networkTabCreationDate.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
                networkTabDueToDate.setCellValueFactory(new PropertyValueFactory<>("dueToDate"));
                networkTabLastEntry.setCellValueFactory(new PropertyValueFactory<>("lastEntry"));
                networkTicket.getItems().add(NetworkList);

            }
        }
    }
}
