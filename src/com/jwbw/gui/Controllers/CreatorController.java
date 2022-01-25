package com.jwbw.gui.Controllers;

import com.jwbw.Main;
import com.jwbw.isp.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreatorController {
    @FXML
    Label priceCount;
    @FXML
    Button create_contract;
    @FXML
    ComboBox<String> comboInternet, comboTV, comboGSM, comboAdditionalInternet, comboAdditionalGSM, comboAdditionalTV;
    @FXML
    ListView<String> AdditionalInternetListView, AdditionalTVListView, AdditionalGSMListView;

    ObservableList<String> selectedInternetItems, selectedTVItems, selectedGSMItems;
    List<Pakiet_internetu> internetList = Main.Database.getInternetPackets();
    List<Telewizja> telewizjaList = Main.Database.getTVpackets();
    List<GSM> gsmList = Main.Database.getGSMpackets();
    List<String> internetAdditionalList = new ArrayList<>();
    List<String> tvAdditionalList = new ArrayList<>();
    List<String> gsmAdditionalList = new ArrayList<>();
    Alert alert = new Alert(Alert.AlertType.WARNING);


    public CreatorController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {
        loadInternetPackets();
        loadTvPackets();
        loadGsmPackets();
        loadAdditionalFeatures();
        AdditionalInternetListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        AdditionalTVListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        AdditionalGSMListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        AdditionalInternetListView.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    selectedInternetItems = AdditionalInternetListView.getSelectionModel().getSelectedItems();
                    onSelectPacket();
                });
        AdditionalTVListView.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    selectedTVItems = AdditionalTVListView.getSelectionModel().getSelectedItems();
                    onSelectPacket();
                });
        AdditionalGSMListView.getSelectionModel().selectedItemProperty()
                .addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
                    selectedGSMItems = AdditionalGSMListView.getSelectionModel().getSelectedItems();
                    onSelectPacket();
                });
    }

    private void loadInternetPackets() throws SQLException {
        List<String> stringList = new ArrayList<>();
        for(Pakiet_internetu netList: internetList) {
            stringList.add((int)netList.getDownload() + "/" + (int)netList.getUpload() + "Mb/s - " + netList.getCena() + "zł/mc");
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        comboInternet.setItems(observableList);
    }

    private void loadTvPackets() throws SQLException {
        List<String> stringList = new ArrayList<>();
        for(Telewizja tvList: telewizjaList) {
            stringList.add(tvList.getIlosc_kanalow() + " kanałów - " + tvList.getCena() + "zł/mc");
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        comboTV.setItems(observableList);
    }

    private void loadGsmPackets() throws SQLException {
        List<String> stringList = new ArrayList<>();
        for(GSM gList: gsmList) {
            stringList.add(gList.getStandard() + " - " + gList.getCena() + "zł/mc");
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        comboGSM.setItems(observableList);
    }

    private void loadAdditionalFeatures() {
        for(AdditionalFeatures featureList: AdditionalFeatures.values()) {
            switch (featureList.getPacket()) {
                case 1 -> {
                    internetAdditionalList.add(featureList.getName() + " - " + featureList.getPrice() + "zł");
                }
                case 2 -> {
                    tvAdditionalList.add(featureList.getName() + " - " + featureList.getPrice() + "zł");
                }
                case 3 -> {
                    gsmAdditionalList.add(featureList.getName() + " - " + featureList.getPrice() + "zł");
                }
                default ->  {}
            }
        }
        AdditionalGSMListView.setItems(FXCollections.observableList(gsmAdditionalList));
        AdditionalInternetListView.setItems(FXCollections.observableList(internetAdditionalList));
        AdditionalTVListView.setItems(FXCollections.observableList(tvAdditionalList));
    }

    @FXML
    public void onSelectPacket() {
        Float cena = (float) 0;
        if (comboGSM.getValue() != null)  {
            cena += gsmList.get(comboGSM.getItems().indexOf(comboGSM.getValue())).getCena();
            if(selectedGSMItems != null) {
                for (String string : selectedGSMItems) {
                    cena += AdditionalFeatures.getFeature(string.substring(0, string.indexOf("-") - 1)).getPrice();
                }
            }
        }
        if (comboInternet.getValue() != null) {
            cena += internetList.get(comboInternet.getItems().indexOf(comboInternet.getValue())).getCena();
            if(selectedInternetItems != null)
                for(String string: selectedInternetItems) {
                    cena += AdditionalFeatures.getFeature(string.substring(0, string.indexOf("-")-1)).getPrice();
                }
        }
        if (comboTV.getValue() != null) {
            cena += telewizjaList.get(comboTV.getItems().indexOf(comboTV.getValue())).getCena();
            if(selectedTVItems != null)
                for(String string: selectedTVItems) {
                    cena += AdditionalFeatures.getFeature(string.substring(0, string.indexOf("-")-1)).getPrice();
                }
        }

        priceCount.setText(cena + " zł");
    }

    public void onCreateContract() {
        if(comboInternet.getValue() != null) {
            comboInternet.getValue();
        }
    }
}
