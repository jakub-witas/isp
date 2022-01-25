package com.jwbw;

import com.jwbw.isp.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class Proxy {
    static DatabaseHandler Database;
    public static ObservableList<Naprawa_serwisowa> naprawySprzetu;
    public static ObservableList<Utrzymanie_sieci> naprawySieci;
    public static ObservableList<Wpis> notificationList;
    public static List<Pakiet_internetu> internetList;
    public static List<Telewizja> telewizjaList;
    public static List<GSM> gsmList;
    public static User loggedUser;

    public static boolean updateUserContractData() throws SQLException {
       return Database.updateUserContractData(loggedUser);
    }

    public static boolean updateUserAddressData() throws SQLException {
        return Database.updateUserAddressData(loggedUser);
    }

    public static int registerNewUser(User user, String username, String password) throws SQLException {
        return Database.registerNewUser(user, username, password);
    }

    public static User fetchUserData(String username, String password) throws SQLException {
        return Database.fetchUserData(username, password);
    }

    public static boolean checkForExistingUser(String username) {
        return Database.checkForExistingUser(username);
    }

    public static boolean authenticateUser(String username, String password) throws SQLException {
        return Database.authenticateUser(username, password);
    }

    //servicesHandler
    public static List<Object> getServiceContracts() throws SQLException {
        List<Dokument> list = Database.getServiceContracts();
        if(loggedUser.getDokumenty() == null || !loggedUser.getDokumenty().containsAll(list)) {
            loggedUser.setDokumenty(Collections.singletonList(list));
        }
        return loggedUser.getDokumenty();
    }

    public static List<Pakiet_internetu> getInternetPackets() throws SQLException {
        if(internetList == null) {
            internetList = Database.getInternetPackets();
        }
        return internetList;
    }
    public static List<GSM> getGSMpackets() throws SQLException {
        if(gsmList == null) {
            gsmList = Database.getGSMpackets();
        }
        return gsmList;
    }
    public static List<Telewizja> getTVpackets() throws SQLException {
        if(telewizjaList == null) {
            telewizjaList = Database.getTVpackets();
        }
        return telewizjaList;
    }

    //issuesHandler
    public static int sendZamowienieGetId(Zamowienie zamowienie) throws SQLException {
        return Database.sendZamowienieGetId(zamowienie);
    }

    public static int sendNaprawaGetId(Naprawa_serwisowa naprawa) throws SQLException {
        return Database.sendNaprawaGetId(naprawa);
    }

    public static int sendUrzadzenieGetId(Urzadzenie urzadzenie) throws SQLException {
        return  Database.sendUrzadzenieGetId(urzadzenie);
    }
    public static int sendWpisGetId(Wpis wpis) throws SQLException {
        return Database.sendWpisGetId(wpis);
    }

    public static int sendPowiadomienieGetId(Wpis wpis) throws SQLException {
        return Database.sendPowiadomienieGetId(wpis);
    }

    public static int sendFakturaGetId(Faktura faktura) throws SQLException {
        return Database.sendFakturaGetId(faktura);
    }

    public static List<Utrzymanie_sieci>  getNetworkTicketList() throws SQLException {
        if (naprawySieci == null) {
            naprawySieci = FXCollections.observableList(Database.getNetworkTicketList());
        }
        return naprawySieci;
    }

    public static List<Naprawa_serwisowa> getHardwareTicketList() throws SQLException {
        if(naprawySprzetu == null) {
            naprawySprzetu = FXCollections.observableList(Database.getHardwareTicketList());
        }
        return naprawySprzetu;
    }

    public static List<Wpis> getNotificationList() throws SQLException {
        if(notificationList == null) {
            notificationList = FXCollections.observableList(Database.getNotificationList());
        }
        return notificationList;
    }

    public static int sendNaprawaSieciGetId(Utrzymanie_sieci utrzymanie_sieci) throws SQLException {
        return Database.sendNaprawaSieciGetId(utrzymanie_sieci);
    }

    public static List<Urzadzenie> getDevices() throws SQLException {
        if(loggedUser.getPosiadane_urzadzenia() != null) {
            return loggedUser.getPosiadane_urzadzenia();
        }
        return Database.getDevices();
    }

    //connectionHandler
    public static boolean checkConnection() {
        return Database.checkConnection();
    }
    public static boolean isConnected() {
        return Database.isConnected();
    }
}
