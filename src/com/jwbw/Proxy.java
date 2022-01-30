package com.jwbw;

import com.jwbw.isp.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Proxy {
    static DatabaseInterface Database;
    public static ObservableList<Naprawa_serwisowa> naprawySprzetu;
    public static ObservableList<Utrzymanie_sieci> naprawySieci;
    public static ObservableList<Wpis> notificationList;
    public static List<Pakiet_internetu> internetList;
    public static List<Telewizja> telewizjaList;
    public static List<GSM> gsmList;
    public static User loggedUser;
    public static List<User> accountList;

    public static List<Dokument> getEmploymentContracts() throws SQLException {
        if(loggedUser.getDokumenty() == null)
           loggedUser.setDokumenty(Database.getEmploymentContracts());
        if(loggedUser.getDokumenty() == null || !loggedUser.getDokumenty().containsAll(Database.getEmploymentContracts())) {
            loggedUser.setDokumenty(Database.getEmploymentContracts());
        }
        return loggedUser.getDokumenty();
    }

    public static int sendServiceContractFromFormGetId(Umowa_usluga umowa, String uslugi) throws SQLException {
        return Database.sendServiceContractFromFormGetId(umowa, uslugi);
    }

    public static void setNotificationStatus(int id, boolean status) throws SQLException {
        Database.setNotificationStatus(id, status);
    }

    public static void setDeleteAccountStatus(int id) throws SQLException {
        Database.setDeleteAccountStatus(id);
    }

    public static int getInternetPacketId(float dl, String features) throws SQLException {
       if (internetList == null) {
           return Database.getInternetPacketId(dl, features);
       }
        for(Pakiet_internetu pakiet: internetList) {
            if (pakiet.getDownload() == dl && features.equals("")) return pakiet.getId();
            else {
                List<InternetFeatures> list = new ArrayList<>();
                for(int i=0; i< features.length()-1; i+=2) {
                    list.add(InternetFeatures.getFeature(Integer.parseInt(features.substring(i, i+1))));
                }
                if (pakiet.getDownload() == dl && pakiet.getAdditionalFeaturesList() == list) return pakiet.getId();
            }
        }
        return Database.getInternetPacketId(dl, features);
    }

    public static Pakiet_internetu getInternetPacket(float dl, String features) throws SQLException {
        if (internetList == null) {
            internetList = Database.getInternetPackets();
        }
        Pakiet_internetu pack = null;
        for(Pakiet_internetu pakiet: internetList) {
            if (pakiet.getDownload() == dl && features.equals(""))  {
                pack = pakiet;
                break;
            }
            else {
                List<InternetFeatures> list = new ArrayList<>();
                for(int i=0; i< features.length()-1; i+=2) {
                    list.add(InternetFeatures.getFeature(Integer.parseInt(features.substring(i, i+1))));
                }
                if (pakiet.getDownload() == dl && pakiet.getAdditionalFeaturesList() == list) {
                    pack =  pakiet;
                    break;
                }
            }
        }
        return pack;
    }

    public static int getTvPacketId(int kanaly, String features) throws SQLException {
        if (telewizjaList == null) {
            return Database.getTvPacketId(kanaly, features);
        }
        for(Telewizja pakiet: telewizjaList) {
            if (pakiet.getIlosc_kanalow() == kanaly && features.equals("")) return pakiet.getId();
            else {
                List<TelevisionFeatures> list = new ArrayList<>();
                for(int i=0; i< features.length()-1; i+=2) {
                    list.add(TelevisionFeatures.getFeature(Integer.parseInt(features.substring(i, i+1))));
                }
                if (pakiet.getIlosc_kanalow() == kanaly && pakiet.getAdditionalFeaturesList() == list) return pakiet.getId();
            }
        }
        return Database.getTvPacketId(kanaly, features);
    }

    public static Telewizja getTvPacket(int kanaly, String features) throws SQLException {
        if (telewizjaList == null) {
            telewizjaList = Database.getTVpackets();
        }
        Telewizja tv = null;
        for(Telewizja pakiet: telewizjaList) {
            if (pakiet.getIlosc_kanalow() == kanaly && features.equals("")) {
                tv = pakiet;
                break;
            }
            else {
                List<TelevisionFeatures> list = new ArrayList<>();
                for(int i=0; i< features.length()-1; i+=2) {
                    list.add(TelevisionFeatures.getFeature(Integer.parseInt(features.substring(i, i+1))));
                }
                if (pakiet.getIlosc_kanalow() == kanaly && pakiet.getAdditionalFeaturesList() == list) {
                    tv = pakiet;
                    break;
                }
            }
        }
        return tv;
    }

    public static int getGsmPacketId(String standard, String features) throws SQLException {
        if (gsmList == null) {
            return Database.getGsmPacketId(standard, features);
        }
        for(GSM pakiet: gsmList) {
            if (Objects.equals(pakiet.getStandard(), standard) && features.equals("")) return pakiet.getId();
            else {
                List<TelephoneFeatures> list = new ArrayList<>();
                for(int i=0; i< features.length()-1; i+=2) {
                    list.add(TelephoneFeatures.getFeature(Integer.parseInt(features.substring(i, i+1))));
                }
                if (Objects.equals(pakiet.getStandard(), standard) && pakiet.getAdditionalFeaturesList() ==list) return pakiet.getId();
            }
        }
        return Database.getGsmPacketId(standard, features);
    }

    public static GSM getGsmPacket(String standard, String features) throws SQLException {
        if (gsmList == null) {
            gsmList = Database.getGSMpackets();
        }
        GSM gsm = null;
        for(GSM pakiet: gsmList) {
            if (Objects.equals(pakiet.getStandard(), standard) && features.equals("")) {
                gsm = pakiet;
            }
            else {
                List<TelephoneFeatures> list = new ArrayList<>();
                for(int i=0; i< features.length()-1; i+=2) {
                    list.add(TelephoneFeatures.getFeature(Integer.parseInt(features.substring(i, i+1))));
                }
                if (Objects.equals(pakiet.getStandard(), standard) && pakiet.getAdditionalFeaturesList() ==list) {
                    gsm =  pakiet;
                }
            }
        }
        return gsm;
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

    public static List<Dokument> getServiceContracts() throws SQLException {
        List<Dokument> list = Database.getServiceContracts();
        if(loggedUser.getDokumenty() == null || !loggedUser.getDokumenty().containsAll(list)) {
            loggedUser.setDokumenty(list);
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

    public static int sendNaprawaSieciGetId(Utrzymanie_sieci utrzymanie_sieci) throws SQLException {
        return Database.sendNaprawaSieciGetId(utrzymanie_sieci);
    }

    public static List<Urzadzenie> getDevices() throws SQLException {
        if(loggedUser.getPosiadane_urzadzenia() != null) {
            return loggedUser.getPosiadane_urzadzenia();
        }
        return Database.getDevices();
    }

    public static List<User> getAccountsClients() throws SQLException {
        if(accountList == null) {
            accountList = Database.getAccountsClients();
        }
        return accountList;
    }

    public static List<Dokument> getContractClient() throws SQLException {
        return Database.getContractClient();
    }

    //connectionHandler
    public static boolean checkConnection() {
        return Database.checkConnection();
    }
    public static boolean isConnected() {
        return Database.isConnected();
    }
}
