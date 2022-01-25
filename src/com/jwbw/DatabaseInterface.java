package com.jwbw;

import com.jwbw.isp.*;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseInterface {
    //userHandler
    boolean updateUserContractData(Object user) throws SQLException;
    boolean updateUserAddressData(Object user) throws SQLException;
    int registerNewUser(User user, String username, String password) throws SQLException;
    User fetchUserData(String username, String password) throws SQLException;
    boolean checkForExistingUser(String username);
    boolean authenticateUser(String username, String password) throws SQLException;

    //servicesHandler
    List<Umowa_usluga> getServiceContracts() throws SQLException;
    List<Pakiet_internetu> getInternetPackets() throws SQLException;
    List<GSM> getGSMpackets() throws SQLException;
    List<Telewizja> getTVpackets() throws SQLException;

    //issuesHandler
    int sendZamowienieGetId(Zamowienie zamowienie) throws SQLException;
    int sendNaprawaGetId(Naprawa_serwisowa naprawa) throws SQLException;
    int sendUrzadzenieGetId(Urzadzenie urzadzenie) throws SQLException;
    int sendWpisGetId(Wpis wpis) throws SQLException;
    int sendPowiadomienieGetId(Wpis wpis) throws SQLException;
    int sendFakturaGetId(Faktura faktura) throws SQLException;
    List<Utrzymanie_sieci>  getNetworkTicketList() throws SQLException;
    List<Naprawa_serwisowa> getHardwareTicketList() throws SQLException;
    List<Wpis> getNotificationList() throws SQLException;
    int sendNaprawaSieciGetId(Utrzymanie_sieci utrzymanie_sieci) throws SQLException;
    List<Urzadzenie> getDevices() throws SQLException;

    //connectionHandler
    boolean checkConnection();
    boolean isConnected();


}