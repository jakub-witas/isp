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
    User fetchUserData(int id) throws SQLException;

    //servicesHandler
    List<Dokument> getServiceContracts(int id) throws SQLException;
    List<Pakiet_internetu> getInternetPackets() throws SQLException;
    List<GSM> getGSMpackets() throws SQLException;
    List<Telewizja> getTVpackets() throws SQLException;
    int getInternetPacketId(float dl, String features) throws SQLException;
    int getTvPacketId(int kanaly, String features) throws SQLException;
    int getGsmPacketId(String standard, String features) throws SQLException;
    int sendServiceContractFromFormGetId(Umowa_usluga umowa, String uslugi) throws SQLException;
    List<Dokument> getEmploymentContracts() throws SQLException;
    List<Dokument> getContractClient() throws SQLException;

    //issuesHandler
    int sendZamowienieGetId(Zamowienie zamowienie) throws SQLException;
    int sendNaprawaGetId(Naprawa_serwisowa naprawa) throws SQLException;
    int sendUrzadzenieGetId(Urzadzenie urzadzenie) throws SQLException;
    int sendWpisGetId(Wpis wpis) throws SQLException;
    int sendPowiadomienieGetId(Wpis wpis) throws SQLException;
    //int sendFakturaGetId(Faktura faktura) throws SQLException;
    List<Utrzymanie_sieci>  getNetworkTicketList() throws SQLException;
    List<Naprawa_serwisowa> getHardwareTicketList(Role role) throws SQLException;
    List<Wpis> getNotificationList() throws SQLException;
    int sendNaprawaSieciGetId(Utrzymanie_sieci utrzymanie_sieci) throws SQLException;
    List<Urzadzenie> getDevices() throws SQLException;
    void setNotificationStatus(int id, boolean status) throws SQLException;
    List<User> getAccountsClients() throws SQLException;
    void setDeleteAccountStatus(int id) throws SQLException;
    boolean updateHardwareTicketData(Naprawa_serwisowa naprawaSerwisowa);
    boolean addNewEntry(int idWpisu, int idZlecenie, int mode);
    boolean updateEntry(Wpis wpis);
    boolean removeEntry(Zlecenie naprawa);
    boolean upgradeIssueLevel(Zlecenie zlecenie);
    boolean updatePartsOwner(Czesc_komputerowa czesc);
    boolean removeOrderFromIssue(Naprawa_serwisowa naprawa, int zamowienieId);
    boolean closeIssueTicket(Zlecenie naprawa);
    List<Czesc_komputerowa> getUnownedPartsList() throws SQLException;
    boolean addPartsOwner(Czesc_komputerowa czesc);
    Urzadzenie_sieciowe getNetworkDevice(String nrUmowy);


    //connectionHandler
    boolean checkConnection();
    boolean isConnected();


}