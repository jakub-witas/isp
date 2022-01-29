package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.Proxy;
import com.jwbw.isp.Wpis;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

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
    @FXML
    private Button confirmationButton;

    public static List<Wpis> notificationList = new ArrayList<>();

    @FXML
    public void initialize() throws SQLException {
        getNotificationList();
        final Tooltip tooltipButton = new Tooltip();
        tooltipButton.setText("Oznacz jako przeczytane");
        confirmationButton.setTooltip(tooltipButton);
        confirmationButton.setOnMouseEntered(e -> confirmationButton.setStyle("-fx-background-color: green"));
        confirmationButton.setOnMouseExited(e -> confirmationButton.setStyle("-fx-background-color: white"));
        notificationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    private void getNotificationList() throws SQLException {
        Object response = Proxy.getNotificationList();

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

    public void handleButtonActionRead() throws SQLException {
        notificationTable.getSelectionModel().getSelectedItem().setWasRead(!notificationTable.getSelectionModel().getSelectedItem().isWasRead());
        notificationTable.refresh();
        System.out.println(notificationTable.getSelectionModel().getSelectedItem().getId());
        Proxy.setNotificationStatus(notificationTable.getSelectionModel().getSelectedItem().getId(),
                notificationTable.getSelectionModel().getSelectedItem().isWasRead());
    }
}
