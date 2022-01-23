package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.isp.Utrzymanie_sieci;
import com.jwbw.isp.Wpis;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


public class NotificationController {
    @FXML
    TableColumn<Wpis, Integer> notificationId;
    @FXML
    TableColumn<Wpis, Boolean> notificationWasRead;
    @FXML
    TableColumn<Wpis, Timestamp> notificationDate;
    @FXML
    TableColumn<Wpis, String> notificationAuthor, notificationDescription;
    @FXML
    TableView<Wpis> notificationTable;

    public static List<Wpis> notificationList = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        getNotificationList();
    }

    private void getNotificationList() throws SQLException {
        Object response = Main.Database.getNotificationList();

        if(response instanceof List) {
            notificationList = (List<Wpis>) response;

            for(Wpis notifList: notificationList) {
                notificationId.setCellValueFactory(new PropertyValueFactory<>("id"));
                notificationDate.setCellValueFactory(new PropertyValueFactory<>("data_utworzenia"));
                notificationDescription.setCellValueFactory(new PropertyValueFactory<>("opis"));
                notificationWasRead.setCellValueFactory(new PropertyValueFactory<>("wasRead"));
                notificationAuthor.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNameSurname()));

                notificationTable.getItems().add(notifList);

            }
        }
    }
}
