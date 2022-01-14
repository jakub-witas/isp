package com.jwbw;

import com.jwbw.isp.*;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseInterface {
    boolean updateUserContractData(Object user) throws SQLException;
    boolean updateUserAddressData(Object user) throws SQLException;
    List<Umowa_usluga> getServiceContracts() throws SQLException;
    int sendZamowienieGetId(Zamowienie zamowienie) throws SQLException;
    int sendNaprawaGetId(Naprawa_serwisowa naprawa) throws SQLException;
    int sendUrzadzenieGetId(Urzadzenie urzadzenie) throws SQLException;
    int sendWpisGetId(Wpis wpis) throws SQLException;
    int sendPowiadomienieGetId(Wpis wpis) throws SQLException;
    int sendFakturaGetId(Faktura faktura) throws SQLException;
    int registerNewUser(Klient user, String username, String password) throws SQLException;
    Object fetchUserData(String username, String password) throws SQLException;
    boolean checkForExistingUser(String username);
    boolean authenticateUser(String username, String password) throws SQLException;
    boolean checkConnection();
    boolean isConnected();
    List<Utrzymanie_sieci>  getNetworkTicketList() throws SQLException;
}