package com.jwbw;

import com.jwbw.gui.InterfaceMain;
import com.jwbw.isp.*;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends Thread implements DatabaseInterface{
    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;
    private final ApplicationLogger logger;
    private final ConnectionHandler connectionHandler;

    private Connection connection;
    private boolean isConnected;

    public DatabaseHandler(ConnectionHandler connectionHandler, String databaseUrl, String databaseUser, String databasePassword, ApplicationLogger logger){
        this.connectionHandler = connectionHandler;
        this.logger = logger;
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        isConnected = connect();
    }

    private boolean connect(){
        int count = 1;
        final int maxTries = 3;
        logger.databaseConnect();
        while(true){
            try {
                Class.forName("org.postgresql.Driver");
                this.connection = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);
                this.connection.setAutoCommit(true);
                logger.databaseConnected();
                connectionHandler.setDatabaseConnectionState(true);
                return true;
            } catch (ClassNotFoundException | SQLException ignored) {
                logger.databaseConnectionRetry(count);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(count++ == maxTries){
                    connectionHandler.setDatabaseConnectionState(false);
                    logger.databaseDisconnected();
                    return false;
                }
            }
        }
    }

    public boolean updateUserContractData(Object user) throws SQLException {
        try {
            Statement statement = this.connection.createStatement();
            String str;
                str = "UPDATE isp.USERS SET name = '" + ((User) user).getName() + "', surname = '" + ((User) user).getSurname() +
                        "', phone = '" + ((User) user).getPhone() + "', id_card = '" + ((User) user).getId_card()
                        + "', mail = '" + ((User) user).getMail() + "' WHERE id = '" + ((User) user).getId() + "';";

            statement.executeUpdate(str);
            statement.close();
        } catch (PSQLException e) {
            return false;
        }
        return true;
    }

    private List<Object> getDocumentData(int id) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str =  "SELECT * FROM DOKUMENTY WHERE ID = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        List<Object> lista = new ArrayList<>();
        lista.add(resultSet.getTimestamp("data_utworzenie"));
        lista.add(resultSet.getTimestamp("data_wygasniecia"));
        lista.add(resultSet.getString("nr_dokumentu"));
        resultSet.close();
        statement.close();
        return lista;
    }

    public boolean updateUserAddressData(Object user) throws SQLException {
        try {
            Statement statement = this.connection.createStatement();
            String str;
            str = "SELECT address FROM isp.USERS WHERE  id = " + ((User)user).getId() + ";";
            ResultSet resultSet = statement.executeQuery(str);
            resultSet.next();
            int id = resultSet.getInt("address");
            resultSet.close();

            str = "UPDATE isp.ADDRESS SET city = '" + ((User) user).getCity() + "', street = '" + ((User) user).getStreet() +
                    "', house_number = '" + ((User) user).getHome_number() + "', code = '" + ((User) user).getCode()
                     + "' WHERE id = '" + id + "';";

            statement.executeUpdate(str);
            statement.close();
        } catch (PSQLException e) {
            return false;
        }
        return true;
    }

    public List<Umowa_usluga> getServiceContracts() throws SQLException {
        List<Umowa_usluga> lista = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT *  FROM UMOWA_USLUGA WHERE nabywca = "
                + ((User)InterfaceMain.loggedUser).getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Umowa_usluga usluga = new Umowa_usluga();
            String oferta = (resultSet.getString("oferta"));
            usluga.setOferta(getServicesFromContract(oferta));
            usluga.setId(resultSet.getInt("id"));
            int autor = resultSet.getInt("autor");
            List<String> name = getAuthorName(autor);
            usluga.setAutor(name.get(0) + " " + name.get(1));
            usluga.setNabywca(InterfaceMain.loggedUser);
            List<Object> listaDoc = getDocumentData(resultSet.getInt("dokument_fk"));
            usluga.setData_utworzenia((Timestamp) listaDoc.get(0));
            usluga.setData_wygasniecia((Timestamp) listaDoc.get(1));
            usluga.setNr_dokumentu((String) listaDoc.get(2));
            lista.add(usluga);
        }
        return  lista;
    }

    private List<Object> getServicesFromContract(String oferta) throws SQLException {
        List<Object> servicesList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str;
        ResultSet resultSet;
        List<Integer> lista = new ArrayList<>();
        lista.add(Integer.parseInt(oferta.substring(0,1)));
        lista.add(Integer.parseInt(oferta.substring(2,3)));
        lista.add(Integer.parseInt(oferta.substring(4,5)));
        if(!lista.get(0).equals(0)) {
            str = "SELECT * FROM PAKIET_INTERNETU WHERE id = " + lista.get(0) + ";";
            resultSet = statement.executeQuery(str);
            resultSet.next();
            Pakiet_internetu pakiet = new Pakiet_internetu();
            pakiet.setId(resultSet.getInt("id"));
            pakiet.setCena(resultSet.getFloat("cena"));
            pakiet.setDownload(resultSet.getFloat("download"));
            pakiet.setUpload(resultSet.getFloat("upload"));
            String adds = resultSet.getString("additional_features");
            if(!adds.equals("")){
                pakiet.setAdditionalFeaturesList(getFeature(adds));
            } else {
                pakiet.setAdditionalFeaturesList(null);
            }
            servicesList.add(pakiet);
        } else if(!lista.get(1).equals(0)) {
            str = "SELECT * FROM telewizja WHERE id = " + lista.get(1) + ";";
            resultSet = statement.executeQuery(str);
            resultSet.next();
            Telewizja tv = new Telewizja();
            tv.setId(resultSet.getInt("id"));
            tv.setCena(resultSet.getFloat("cena"));
            tv.setIlosc_kanalow(resultSet.getInt("lista_kanalow"));
            String adds = resultSet.getString("additional_features");
            if(!adds.equals("")){
                tv.setAdditionalFeaturesList(getFeature(adds));
            } else {
                tv.setAdditionalFeaturesList(null);
            }
            servicesList.add(tv);
        } else if(!lista.get(2).equals(0)) {
            str = "SELECT * FROM GSM WHERE id = " + lista.get(2) + ";";
            resultSet = statement.executeQuery(str);
            resultSet.next();
            GSM gsm = new GSM();
            gsm.setId(resultSet.getInt("id"));
            gsm.setStandard(resultSet.getString("standard"));
            gsm.setCena(resultSet.getFloat("cena"));
            String adds = resultSet.getString("additional_features");
            if(!adds.equals("")){
                gsm.setAdditionalFeaturesList(getFeature(adds));
            } else {
                gsm.setAdditionalFeaturesList(null);
            }
            servicesList.add(gsm);
        }
        return servicesList;
    }

    private List<AdditionalFeatures> getFeature(String feature) {
        List<AdditionalFeatures> featuresList = new ArrayList<>();
        for(int i=0;i<feature.length(); i+=2) {
            featuresList.add(AdditionalFeatures.getFeature(Integer.parseInt(feature.substring(i,i+1))));
        }
        return featuresList;
    }

    private List<Cennik_uslug> getServiceEnum(String feature) {
        List<Cennik_uslug> servicesList = new ArrayList<>();
        for(int i=0;i<feature.length(); i+=2) {
            servicesList.add(Cennik_uslug.getService(Integer.parseInt(feature.substring(i, i+1))));
        }
        return servicesList;
    }

    private String getIdListForSelect(String string) {
        //return string.substring(0, string.lastIndexOf(",")).replaceAll("," , " OR ");
        return string.substring(0, string.length()-1);
    }

    private List<String> getAuthorName(int id) throws SQLException {
        Statement statement =  this.connection.createStatement();
        String str = "SELECT name, surname FROM USERS WHERE id = " + id +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        List<String> name = new ArrayList<>();
        name.add(resultSet.getString("name"));
        name.add(resultSet.getString("surname"));


        resultSet.close();
        statement.close();
        return name;
    }

    public int sendZamowienieGetId(Zamowienie zamowienie) throws SQLException {
        StringBuilder czesci = null;
        List<Czesc_komputerowa> lista = zamowienie.getCzesci();
        assert false;

        for(Czesc_komputerowa czesc : lista) {
            czesci.append(czesc.getId() + ',');
        }

        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO ZAMOWIENIE(id, creation_date, dueto_date, kwota, czesci) VALUES (nextval('zamowienie_seq'), '"
                + zamowienie.getData_utworzenia() + "', '" + zamowienie.getData_wygasniecia() + "', '"
                + zamowienie.getKwota() + "', '"+ czesci.toString() + "')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM ZAMOWIENIE WHERE creation_date = '" + zamowienie.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String id = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(id);
    }

    public int sendNaprawaGetId(Naprawa_serwisowa naprawa) throws SQLException {
        StringBuilder uslugi = null;
        List<Cennik_uslug> lista = naprawa.getWykonane_uslugi();
        assert false;

        for(Cennik_uslug usluga : lista) {
            uslugi.append(usluga.getValue() + ',');
        }

        StringBuilder wpisy = null;
        List<Wpis> lista_wpisow = naprawa.getWpisy();
        assert false;

        for(Wpis wpis : lista_wpisow) {
            wpisy.append(wpis.getId() + ',');
        }

        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO ZLECENIE_NAPRAWA(id, creation_date, close_date, wpisy, uslugi, kwota, wlasciciel, urzadzenie, zamowienie) " +
                "VALUES (nextval('zlecenie_naprawa_seq'), '" + naprawa.getData_utworzenia() + "', null, '"
                + wpisy + "', '" + uslugi + "', '" + 0 + "', '" + naprawa.getWlasciciel().getId() + "', '" + naprawa.getUrzadzenie_naprawiane().getId()
                + "', null )";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM ZLECENIE_NAPRAWA WHERE creation_date = '" + naprawa.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String id = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(id);
    }

    public int sendUrzadzenieGetId(Urzadzenie urzadzenie) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO URZADZENIE(id, nazwa, producent, sn) VALUES (nextval('urzadzenie_seq'), '"
                + urzadzenie.getNazwa() + "', '" + urzadzenie.getProducent() + "', '" + urzadzenie.getSn() + "')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM URZADZENIE WHERE sn = '" + urzadzenie.getSn() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String id = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(id);
    }

    public int sendWpisGetId(Wpis wpis) throws SQLException {
        int id = getObjectId(wpis.getAutor());

        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO WPIS(id, data_utworzenia, opis, autor) VALUES (nextval('wpis_seq'), '"
                + wpis.getData_utworzenia() + "', '" + wpis.getOpis() + "', null, '" + id + " null')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM WPIS WHERE data_utworzenia = '" + wpis.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String identyfikator = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(identyfikator);
    }

    public int sendPowiadomienieGetId(Wpis wpis) throws SQLException {
        int idAutor, idOdbiorca;
        idAutor = getObjectId(wpis.getAutor());
        idOdbiorca = getObjectId(wpis.getOdbiorca());

        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO WPIS(id, data_utworzenia, opis, przeczytane, autor, odbiorca) VALUES (nextval('wpis_seq'), '"
                + wpis.getData_utworzenia() + "', '" + wpis.getOpis() + "', '" + wpis.isWasRead() + "', '" + idAutor + "', '" + idOdbiorca + "')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM WPIS WHERE data_utworzenia = '" + wpis.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String identyfikator = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(identyfikator);
    }

    private int getObjectId(Object obj) {
        int id;
        if (obj.getClass() == User.class) {
            id = ((User) obj).getId();
        } else {
            id = ((Pracownik)obj).getId();
        }
        return id;
    }

    public int sendFakturaGetId(Faktura faktura) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO FAKTURY(id, creation_date, dueto_date, kwota, nabywca) VALUES (nextval('faktury_seq'), '"
                + faktura.getData_utworzenia() + "', '" + faktura.getData_wygasniecia() + "', '" + faktura.getKwota() + "','"
                + faktura.getNabywca().getId() + "')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM FAKTURY WHERE creation_date = '" + faktura.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String id = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(id);
    }

    private boolean checkForAddress(User user) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM ADDRESS WHERE CITY = '" + user.getCity() + "' AND STREET = '" + user.getStreet() +
                "' AND HOUSE_NUMBER = '" + user.getHome_number() + "' AND CODE = '" + user.getCode()  + "';";
        ResultSet resultSet = statement.executeQuery(str);

        boolean authenticate = resultSet.isBeforeFirst();

        statement.close();
        resultSet.close();

        return authenticate;
    }

    private int registerNewAdress(User user) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO ADDRESS VALUES (nextval('address_seq'), '"
                + user.getCity() + "', '" + user.getStreet() + "', '" + user.getHome_number() + "','"
                + user.getCode() + "')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM ADDRESS WHERE street = '" + user.getStreet() + "' AND house_number = '" + user.getHome_number() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String id = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(id);
    }

    private int getAddressId(User user) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT id FROM ADDRESS WHERE CITY = '" + user.getCity() + "' AND STREET = '" + user.getStreet() +
                "' AND HOUSE_NUMBER = '" + user.getHome_number() + "' AND CODE = '" + user.getCode()  + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();

        int id = resultSet.getInt("id");

        statement.close();
        resultSet.close();

        return id;
    }

    public List<Utrzymanie_sieci> getNetworkTicketList() throws SQLException {
        List<Utrzymanie_sieci> lista = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT *  FROM ZLECENIE_SIEC WHERE klient = "
                + ((User)InterfaceMain.loggedUser).getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Utrzymanie_sieci utrzymanieSieci = new Utrzymanie_sieci();
            utrzymanieSieci.setId(resultSet.getInt("id"));
            utrzymanieSieci.setKlient((User) InterfaceMain.loggedUser);

            List<Object> listaZlecenie = getZlecenieData(resultSet.getInt("zlecenie_fk"));
            utrzymanieSieci.setData_utworzenia((Timestamp) listaZlecenie.get(0));
            utrzymanieSieci.setData_wykonania((Timestamp) listaZlecenie.get(1));
            List<Wpis> listaWpisy = getEntries((String) listaZlecenie.get(2));
            utrzymanieSieci.setWpisy(listaWpisy);
            utrzymanieSieci.setLastEntry(listaWpisy.get(listaWpisy.size()-1).getOpis());
            lista.add(utrzymanieSieci);
        }
        return  lista;
    }

    public List<Naprawa_serwisowa> getHardwareTicketList() throws SQLException {
        List<Naprawa_serwisowa> lista = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT *  FROM ZLECENIE_NAPRAWA WHERE wlasciciel = "
                + (InterfaceMain.loggedUser).getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Naprawa_serwisowa naprawaSerwisowa = new Naprawa_serwisowa();
            naprawaSerwisowa.setId(resultSet.getInt("id"));
            naprawaSerwisowa.setWlasciciel( InterfaceMain.loggedUser);

            List<Urzadzenie> klientDevice = InterfaceMain.loggedUser.getPosiadane_urzadzenia();
            if(klientDevice != null) {
                for (Urzadzenie device : klientDevice) {
                    if (device.getId() == resultSet.getInt("urzadzenie") && !(device instanceof Urzadzenie_sieciowe)) {
                        naprawaSerwisowa.setUrzadzenie_naprawiane(device);
                    }
                }
            }
            naprawaSerwisowa.setKoszt(resultSet.getFloat("kwota"));
            if(resultSet.getInt("zamowienie") != 0) {
                naprawaSerwisowa.setZamowienie(getZamowienie(resultSet.getInt("zamowienie")));
            }

            String services = resultSet.getString("uslugi");
            if(!services.isEmpty()) {
                naprawaSerwisowa.setWykonane_uslugi(getServiceEnum(services));
            }

            List<Object> listaZlecenie = getZlecenieData(resultSet.getInt("zlecenie_fk"));
            naprawaSerwisowa.setData_utworzenia((Timestamp) listaZlecenie.get(0));
            naprawaSerwisowa.setData_wykonania((Timestamp) listaZlecenie.get(1));
            List<Wpis> listaWpisy = getEntries((String) listaZlecenie.get(2));
            naprawaSerwisowa.setWpisy(listaWpisy);
            naprawaSerwisowa.setLastEntry(listaWpisy.get(listaWpisy.size()-1).getOpis());
            lista.add(naprawaSerwisowa);
        }
        return  lista;
    }

    private Zamowienie getZamowienie(int id) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM ZAMOWIENIE WHERE id = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        Zamowienie zamowienie = new Zamowienie();
        zamowienie.setId(resultSet.getInt("id"));
        zamowienie.setKwota(resultSet.getFloat("kwota"));
        String czesci = resultSet.getString("czesci");
        if(!czesci.equals("")) {
            zamowienie.setCzesci(getPartsList(czesci));
        }
        List<Object> doc = getDocumentData(resultSet.getInt("dokument_fk"));
        zamowienie.setData_utworzenia((Timestamp) doc.get(0));
        zamowienie.setData_wygasniecia((Timestamp) doc.get(1));
        zamowienie.setNr_dokumentu((String) doc.get(2));

        resultSet.close();
        statement.close();
        return zamowienie;
    }

    private List<Czesc_komputerowa> getPartsList(String czesci) throws SQLException {
        List<Czesc_komputerowa> parts = new ArrayList<>();
        czesci = getIdListForSelect(czesci);
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM CZESC_KOMPUTEROWA WHERE id IN ( " + czesci + ");";
        ResultSet resultSet = statement.executeQuery(str);
        while (resultSet.next()) {
            Czesc_komputerowa part = new Czesc_komputerowa();
            part.setWlasciciel(null);
            part.setKoszt(resultSet.getFloat("koszt"));
            part.setId(resultSet.getInt("id"));
            part.setPort(resultSet.getString("port"));
            part.setPrzeznaczenie(resultSet.getString("przeznaczenie"));
            Urzadzenie urzadzenie = getDevice(resultSet.getInt("urzadzenie_fk"));
            part.setWlasciciel(urzadzenie.getWlasciciel());
            part.setNazwa(urzadzenie.getNazwa());
            part.setSn(urzadzenie.getSn());

            parts.add(part);
        }
        resultSet.close();
        statement.close();
        return parts;
    }

    private Urzadzenie getDevice(int id) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM URZADZENIE WHERE id = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        Urzadzenie urzadzenie = new Urzadzenie();
        urzadzenie.setId(resultSet.getInt("id"));
        urzadzenie.setNazwa(resultSet.getString("nazwa"));
        urzadzenie.setProducent(resultSet.getString("producent"));
        urzadzenie.setSn(resultSet.getString("sn"));
        urzadzenie.setWlasciciel((User)InterfaceMain.loggedUser);

        resultSet.close();
        statement.close();
        return urzadzenie;
    }

    private List<Wpis> getEntries(String wpisy) throws SQLException {
        //wpisy = wpisy.replace(",", " OR ");
        wpisy = getIdListForSelect(wpisy);
        List<Wpis> wpisList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM WPIS WHERE id IN (  " + wpisy + ");";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
        Wpis wpis = new Wpis();
        wpis.setId(resultSet.getInt("id"));
        wpis.setOpis(resultSet.getString("opis"));
        wpis.setData_utworzenia(resultSet.getTimestamp("data_utworzenia"));
        User user = new User();
        user.setId(resultSet.getInt("autor"));
        List<String> name = getAuthorName(user.getId());
        user.setName(name.get(0));
        user.setSurname(name.get(1));
        wpis.setAutor(user);
        if(resultSet.getString("odbiorca") != null) {
            user.setId(resultSet.getInt("odbiorca"));
            name = getAuthorName(user.getId());
            user.setName(name.get(0));
            user.setSurname(name.get(1));
            wpis.setOdbiorca(user);
            wpis.setWasRead(resultSet.getBoolean("przeczytane"));
        }

        wpisList.add(wpis);
        }
        return wpisList;
    }

    private List<Object> getZlecenieData(int id) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str =  "SELECT * FROM ZLECENIE WHERE ID = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        List<Object> lista = new ArrayList<>();
        lista.add(resultSet.getTimestamp("creation_date"));
        lista.add(resultSet.getTimestamp("close_date"));
        lista.add(resultSet.getString("wpisy"));
        resultSet.close();
        statement.close();
        return lista;
    }

    public int registerNewUser(User user, String username, String password) throws SQLException {
        int addressId;
        if(!checkForAddress(user)) {
            addressId = this.registerNewAdress(user);
        } else {
            addressId = this.getAddressId(user);
        }
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO USERS VALUES (nextval('user_seq'), '" + username + "', '" + password +  "', '" +
                user.getName() + "', '" + user.getSurname() + "', '" + user.getPhone() + "', '" + user.getMail() + "', '" +
                user.getPesel() + "', '" + user.getId_card() + "', 2, " + addressId + ");";
       statement.executeUpdate(str);

        str = "SELECT MAX(ID) FROM USERS WHERE username = '" + username + "' AND password = '" + password + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int id = resultSet.getInt("max");

        statement.close();
        resultSet.close();
        return id;
    }

    public User fetchUserData(String username, String password) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM USERS WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            user.setId_card(resultSet.getString("ID_card"));
            user.setMail(resultSet.getString("mail"));
            user.setPesel(resultSet.getString("pesel"));
            user.setPhone(resultSet.getString("phone"));
            user.setRole(Role.getRole(resultSet.getInt("role")));
            List<String> lista = getUserAddresInfo(resultSet.getInt("address"));

            user.setCity(lista.get(0));
            user.setStreet(lista.get(1));
            user.setCode(lista.get(2));
            user.setHome_number(lista.get(3));

            user.setPosiadane_urzadzenia(getDevices(user.getId()));
            user.setDokumenty(getDocuments(user.getId()));

            statement.close();
            resultSet.close();
            logger.userLoggedIn(user.getId());
            return user;
    }

    private List<Dokument> getDocuments(int id) throws SQLException {
        List<Dokument> dokumentList = new ArrayList<>();
        dokumentList.addAll(getInvoices(id));
        dokumentList.addAll(getCashEntries(id));

        return dokumentList;
    }

    private List<Wpis_kasowy> getCashEntries(int id) throws SQLException {
        List<Wpis_kasowy> cashList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM WPISY_KASOWE WHERE wykonawca = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Wpis_kasowy wpis = new Wpis_kasowy();
            wpis.setId(resultSet.getInt("id"));
            wpis.setWykonawca(InterfaceMain.loggedUser);
            wpis.setKwota(resultSet.getFloat("kwota"));
            List<Object> listaDoc = getDocumentData(resultSet.getInt("dokument_fk"));
            wpis.setData_utworzenia((Timestamp) listaDoc.get(0));
            wpis.setData_wygasniecia((Timestamp) listaDoc.get(1));
            wpis.setNr_dokumentu((String) listaDoc.get(2));

            cashList.add(wpis);
        }

        resultSet.close();
        statement.close();
        return cashList;
    }

    private List<Faktura> getInvoices(int id) throws SQLException {
        List<Faktura> fakturaList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM FAKTURY WHERE nabywca = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Faktura faktura = new Faktura();
            faktura.setId(resultSet.getInt("id"));
            faktura.setNabywca(InterfaceMain.loggedUser);
            faktura.setKwota(resultSet.getFloat("kwota"));
            List<Object> listaDoc = getDocumentData(resultSet.getInt("dokument_fk"));
            faktura.setData_utworzenia((Timestamp) listaDoc.get(0));
            faktura.setData_wygasniecia((Timestamp) listaDoc.get(1));
            faktura.setNr_dokumentu((String) listaDoc.get(2));

            //TODO: brakuje wczytywanie obiektow (bo ich w bazie nie ma atm)
            fakturaList.add(faktura);
        }

        resultSet.close();
        statement.close();
        return fakturaList;
    }

    private List<Urzadzenie> getDevices(int id) throws SQLException {
        List<Urzadzenie> urzadzenieList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM URZADZENIE WHERE wlasciciel = " + id + ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Statement statement1 = this.connection.createStatement();
            String str1 = "SELECT * FROM URZADZENIE_SIECIOWE WHERE id = " + resultSet.getInt("id") + ";";
            ResultSet resultSet1 = statement1.executeQuery(str1);
            if(resultSet1.next()) {
                Urzadzenie_sieciowe urzadzenie = new Urzadzenie_sieciowe();
                urzadzenie.setPrzepustowosc(resultSet1.getString("przepustowosc"));
                urzadzenie.setCzy_dostepne(false);
                urzadzenie.setWlan(resultSet1.getBoolean("wlan"));
                urzadzenie.setIp_address(resultSet1.getString("ip_address"));
                urzadzenie.setId(resultSet1.getInt("id"));
                urzadzenie.setWlasciciel(InterfaceMain.loggedUser);
                urzadzenie.setNazwa(resultSet.getString("nazwa"));
                urzadzenie.setSn(resultSet.getString("sn"));
                urzadzenie.setProducent(resultSet.getString("producent"));
                urzadzenieList.add(urzadzenie);
            } else {
                Urzadzenie urzadzenie = new Urzadzenie();
                urzadzenie.setId(resultSet.getInt("id"));
                urzadzenie.setWlasciciel(InterfaceMain.loggedUser);
                urzadzenie.setNazwa(resultSet.getString("nazwa"));
                urzadzenie.setSn(resultSet.getString("sn"));
                urzadzenie.setProducent(resultSet.getString("producent"));
                urzadzenieList.add(urzadzenie);
            }
        }
        resultSet.close();
        statement.close();
        return urzadzenieList;
    }

    private List<String> getUserAddresInfo(int id) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM ADDRESS WHERE ID = '" + id + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();

        List<String> lista = new ArrayList<>();
                lista.add(resultSet.getString("city"));
        lista.add(resultSet.getString("street"));
        lista.add(resultSet.getString("code"));
        lista.add(resultSet.getString("house_number"));


        statement.close();
        resultSet.close();

        return lista;
    }

    public boolean checkForExistingUser(String username) {
        try {
            Statement statement = this.connection.createStatement();
            String str = "SELECT * FROM USERS WHERE USERNAME = '" + username + "';";
            ResultSet resultSet = statement.executeQuery(str);

            boolean authenticate = resultSet.isBeforeFirst();

            statement.close();
            resultSet.close();

            return authenticate;
        } catch (SQLException e) {
            return false;
        }
    }

    private int getUserRole(String username, String password) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT role FROM USERS WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int role = resultSet.getInt("role");

        statement.close();
        resultSet.close();

        return role;
    }

    public boolean authenticateUser(String username, String password) {
        try {
            Statement statement = this.connection.createStatement();
            String str = "SELECT * FROM USERS WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';";
            ResultSet resultSet = statement.executeQuery(str);

            boolean authenticate = resultSet.isBeforeFirst();

            statement.close();
            resultSet.close();

            return authenticate;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean checkConnection(){
        try {
            this.connection.createStatement().executeQuery("SELECT 1");
        } catch (SQLException | NullPointerException ignored) {
            if(!(isConnected = connect())){
                connectionHandler.setDatabaseConnectionState(false);
                return false;
            }
        }
        connectionHandler.setDatabaseConnectionState(true);
        return true;
    }

    public boolean isConnected() {
        return isConnected;
    }

}
