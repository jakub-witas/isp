package com.jwbw.gui.Controllers;

import com.jwbw.Proxy;
import com.jwbw.isp.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
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
    List<Pakiet_internetu> internetList = Proxy.getInternetPackets();
    List<Telewizja> telewizjaList = Proxy.getTVpackets();
    List<GSM> gsmList = Proxy.getGSMpackets();
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
        loadAdditionalInternetFeatures();
        loadAdditionalTelephoneFeatures();
        loadAdditionalTelevisionFeatures();
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
        for(Pakiet_internetu netList: internetList.subList(0, internetList.size()/2)) {
            stringList.add((int)netList.getDownload() + "/" + (int)netList.getUpload() + "Mb/s - " + netList.getCena() + "zł/mc");
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        comboInternet.setItems(observableList);
    }

    private void loadTvPackets() throws SQLException {
        List<String> stringList = new ArrayList<>();
        for(Telewizja tvList: telewizjaList.subList(0, telewizjaList.size()/2)) {
            stringList.add(tvList.getIlosc_kanalow() + " kanałów - " + tvList.getCena() + "zł/mc");
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        comboTV.setItems(observableList);
    }

    private void loadGsmPackets() throws SQLException {
        List<String> stringList = new ArrayList<>();
        for(GSM gList: gsmList.subList(0, gsmList.size()/2)) {
            stringList.add(gList.getStandard() + " - " + gList.getCena() + "zł/mc");
        }
        ObservableList<String> observableList = FXCollections.observableList(stringList);
        comboGSM.setItems(observableList);
    }

    private void loadAdditionalInternetFeatures() {
        for(InternetFeatures featureList: InternetFeatures.values()) {
                    internetAdditionalList.add(featureList.getName() + " - " + featureList.getPrice() + "zł");
        }
        AdditionalInternetListView.setItems(FXCollections.observableList(internetAdditionalList));
    }

    private void loadAdditionalTelevisionFeatures() {
        for(TelevisionFeatures featureList: TelevisionFeatures.values()) {
            tvAdditionalList.add(featureList.getName() + " - " + featureList.getPrice() + "zł");
        }
        AdditionalTVListView.setItems(FXCollections.observableList(tvAdditionalList));
    }

    private void loadAdditionalTelephoneFeatures() {
        for(TelephoneFeatures featureList: TelephoneFeatures.values()) {
            gsmAdditionalList.add(featureList.getName() + " - " + featureList.getPrice() + "zł");
        }
        AdditionalGSMListView.setItems(FXCollections.observableList(gsmAdditionalList));
    }

    @FXML
    public void onSelectPacket() {
        double cena = (float) 0;
        if (comboGSM.getValue() != null)  {
            cena += gsmList.get(comboGSM.getItems().indexOf(comboGSM.getValue())).getCena();
            if(selectedGSMItems != null) {
                for (String string : selectedGSMItems) {
                    cena += TelephoneFeatures.getFeature(string.substring(0, string.indexOf("-") - 1)).getPrice();
                }
            }
        }
        if (comboInternet.getValue() != null) {
            cena += internetList.get(comboInternet.getItems().indexOf(comboInternet.getValue())).getCena();
            if(selectedInternetItems != null)
                for(String string: selectedInternetItems) {
                    cena += InternetFeatures.getFeature(string.substring(0, string.indexOf("-")-1)).getPrice();
                }
        }
        if (comboTV.getValue() != null) {
            cena += telewizjaList.get(comboTV.getItems().indexOf(comboTV.getValue())).getCena();
            if(selectedTVItems != null)
                for(String string: selectedTVItems) {
                    cena += TelevisionFeatures.getFeature(string.substring(0, string.indexOf("-")-1)).getPrice();
                }
        }
        if(comboTV.getValue() != null && comboInternet.getValue() != null && comboGSM != null) cena *= 0.90;
        else if ((comboGSM.getValue() != null && comboInternet.getValue() != null) ||
                (comboGSM.getValue() != null && comboTV.getValue() != null) ||
                (comboTV.getValue() != null && comboInternet.getValue() != null))
                    cena *= 0.95;
        priceCount.setText(cena + " zł");
    }

    public void onCreateContract() throws SQLException {
        if (!priceCount.getText().substring(0, priceCount.getText().indexOf("z")-1).equals("0")) {
            var umowa = new Umowa_usluga();
            List<Object> lista = new ArrayList<>();
            String uslugi = "";
            if (comboInternet.getValue() != null) {
                String dl = comboInternet.getValue().substring(0, comboInternet.getValue().indexOf("/"));
                String features = "";
                if (selectedInternetItems != null) {
                    for (String string : selectedInternetItems) {
                        features += InternetFeatures.getFeature(string.substring(0, string.indexOf("-") - 1)).getValue() + ",";
                    }
                }
                uslugi += Proxy.getInternetPacketId(Float.parseFloat(dl), features) + ",";
                lista.add(Proxy.getInternetPacket(Float.parseFloat(dl), features));
            } else {
                uslugi += "0,";
            }

            if (comboTV.getValue() != null) {
                int kanały = Integer.parseInt(comboTV.getValue().substring(0, comboTV.getValue().indexOf("k") - 1));
                String features = "";
                if (selectedTVItems != null) {
                    for (String string : selectedTVItems) {
                        features += TelevisionFeatures.getFeature(string.substring(0, string.indexOf("-") - 1)).getValue() + ",";
                    }
                }
                uslugi += Proxy.getTvPacketId(kanały, features) + ",";
                lista.add(Proxy.getTvPacket(kanały, features));
            } else {
                uslugi += "0,";
            }

            if (comboGSM.getValue() != null) {
                String standard = comboGSM.getValue().substring(0, comboGSM.getValue().indexOf("-") - 1);
                String features = "";
                if (selectedGSMItems != null) {
                    for (String string : selectedGSMItems) {
                        features += TelephoneFeatures.getFeature(string.substring(0, string.indexOf("-") - 1)).getValue() + ",";
                    }
                }
                uslugi += Proxy.getGsmPacketId(standard, features) + ",";
                lista.add(Proxy.getGsmPacket(standard, features));
            } else {
                uslugi += "0";
            }

            umowa.setAutor((String) null);
            umowa.setNabywca(Proxy.loggedUser);
            umowa.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now().with(TemporalAdjusters.firstDayOfNextMonth())));
            umowa.setData_wygasniecia(Timestamp.valueOf(LocalDateTime.now().with(TemporalAdjusters.lastDayOfMonth()).plusYears(2)));
            umowa.setOferta(lista);
            umowa.setId(Proxy.sendServiceContractFromFormGetId(umowa, uslugi));

            alert.setAlertType(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle("Powodzenie");
            alert.setContentText("Umowa została poprawnie przekazana do zatwierdzenia.");
            alert.showAndWait();

        } else {
            alert.setTitle("Błąd");
            alert.setContentText("Brak usług do utworzenia umowy.");
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        }

}
