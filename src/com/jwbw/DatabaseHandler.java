package com.jwbw;

import com.jwbw.isp.*;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
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

    public void setNotificationStatus(int id, boolean status) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "UPDATE WPIS SET przeczytane = '" + status + "' WHERE id = '" + id + "';";
        statement.executeUpdate(str);
        statement.close();
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

    public int sendServiceContractFromFormGetId(Umowa_usluga umowa, String uslugi) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO DOKUMENTY (id, data_utworzenie, data_wygasniecia) VALUES (nextval('isp.dokument_seq'), '" +
                umowa.getData_utworzenia() + "','" + umowa.getData_wygasniecia() + "');";
        statement.executeUpdate(str);
        str = "SELECT max(id) FROM DOKUMENTY WHERE data_utworzenie = '" + umowa.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int dokumentId = resultSet.getInt("max");
        str = "INSERT INTO umowa_usluga VALUES(nextval('isp.umowa_usluga_seq'), '" + uslugi + "', '" + umowa.getNabywca().getId() +
                "', null, '" + dokumentId + "');";
        statement.executeUpdate(str);

        str = "SELECT MAX(id) FROM UMOWA_USLUGA WHERE dokument_fk = '" + dokumentId + "';";
        resultSet = statement.executeQuery(str);
        resultSet.next();
        dokumentId = resultSet.getInt("max");

        resultSet.close();
        statement.close();
        return dokumentId;
    }

    public List<Dokument> getServiceContracts() throws SQLException {
        List<Dokument> lista = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT *  FROM UMOWA_USLUGA WHERE nabywca = "
                + (Proxy.loggedUser).getId() + " AND autor IS NOT NULL" + ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Umowa_usluga usluga = new Umowa_usluga();
            String oferta = (resultSet.getString("oferta"));
            usluga.setOferta(getServicesFromContract(oferta));
            usluga.setId(resultSet.getInt("id"));
            int autor = resultSet.getInt("autor");
            List<String> name = getAuthorName(autor);
            usluga.setAutor(name.get(0) + " " + name.get(1));
            usluga.setNabywca(Proxy.loggedUser);
            List<Object> listaDoc = getDocumentData(resultSet.getInt("dokument_fk"));
            usluga.setData_utworzenia((Timestamp) listaDoc.get(0));
            usluga.setData_wygasniecia((Timestamp) listaDoc.get(1));
            usluga.setNr_dokumentu((String) listaDoc.get(2));
            lista.add(usluga);
        }
        return lista;
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
                pakiet.setAdditionalFeaturesList(getInternetFeature(adds));
            } else {
                pakiet.setAdditionalFeaturesList(null);
            }
            servicesList.add(pakiet);
        }
        if(!lista.get(1).equals(0)) {
            str = "SELECT * FROM telewizja WHERE id = " + lista.get(1) + ";";
            resultSet = statement.executeQuery(str);
            resultSet.next();
            Telewizja tv = new Telewizja();
            tv.setId(resultSet.getInt("id"));
            tv.setCena(resultSet.getFloat("cena"));
            tv.setIlosc_kanalow(resultSet.getInt("lista_kanalow"));
            String adds = resultSet.getString("additional_features");
            if(!adds.equals("")){
                tv.setAdditionalFeaturesList(getTelevisionFeature(adds));
            } else {
                tv.setAdditionalFeaturesList(null);
            }
            servicesList.add(tv);
        }
        if(!lista.get(2).equals(0)) {
            str = "SELECT * FROM GSM WHERE id = " + lista.get(2) + ";";
            resultSet = statement.executeQuery(str);
            resultSet.next();
            GSM gsm = new GSM();
            gsm.setId(resultSet.getInt("id"));
            gsm.setStandard(resultSet.getString("standard"));
            gsm.setCena(resultSet.getFloat("cena"));
            String adds = resultSet.getString("additional_features");
            if(!adds.equals("")){
                gsm.setAdditionalFeaturesList(getTelephoneFeature(adds));
            } else {
                gsm.setAdditionalFeaturesList(null);
            }
            servicesList.add(gsm);
        }
        return servicesList;
    }

    private List<InternetFeatures> getInternetFeature(String feature) {
        List<InternetFeatures> featuresList = new ArrayList<>();
        for(int i=0;i<feature.length(); i+=2) {
            featuresList.add(InternetFeatures.getFeature(Integer.parseInt(feature.substring(i,i+1))));
        }
        return featuresList;
    }

    private List<TelephoneFeatures> getTelephoneFeature(String feature) {
        List<TelephoneFeatures> featuresList = new ArrayList<>();
        for(int i=0;i<feature.length(); i+=2) {
            featuresList.add(TelephoneFeatures.getFeature(Integer.parseInt(feature.substring(i,i+1))));
        }
        return featuresList;
    }

    private List<TelevisionFeatures> getTelevisionFeature(String feature) {
        List<TelevisionFeatures> featuresList = new ArrayList<>();
        for(int i=0;i<feature.length(); i+=2) {
            featuresList.add(TelevisionFeatures.getFeature(Integer.parseInt(feature.substring(i,i+1))));
        }
        return featuresList;
    }

    private List<Cennik_uslug> getServiceEnum(String feature) {
        List<Cennik_uslug> servicesList = new ArrayList<>();
        for(int i=0;i<feature.length()-1; i+=2) {
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

    public int sendNaprawaSieciGetId(Utrzymanie_sieci utrzymanieSieci) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO ZLECENIE VALUES (nextval('isp.zlecenie_seq'), '" + utrzymanieSieci.getData_utworzenia() + "', null, '" +
                utrzymanieSieci.getWpisy().get(0).getId() + ",');";
        statement.executeUpdate(str);

        str = "SELECT MAX(id) FROM ZLECENIE WHERE creation_date = '" + utrzymanieSieci.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int id = resultSet.getInt("max");

        str = "INSERT INTO ZLECENIE_SIEC VALUES (nextval('isp.zlecenie_siec_seq'), '" + utrzymanieSieci.getNr_umowy() + "', '" +
                utrzymanieSieci.getKlient().getId() + "', '" + id + "');";
        statement.executeUpdate(str);

        str = "SELECT MAX(id) FROM ZLECENIE_SIEC WHERE zlecenie_fk = '" + id + "';";
        resultSet = statement.executeQuery(str);
        resultSet.next();
        id = resultSet.getInt("max");

        statement.close();
        resultSet.close();
        return id;
    }

    public int sendNaprawaGetId(Naprawa_serwisowa naprawa) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO ZLECENIE VALUES (nextval('isp.zlecenie_seq'), '" + naprawa.getData_utworzenia() + "', null, '" +
                naprawa.getWpisy().get(0).getId() + ",');";
        statement.executeUpdate(str);

        str = "SELECT MAX(id) FROM ZLECENIE WHERE creation_date = '" + naprawa.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int id = resultSet.getInt("max");

        str = "INSERT INTO ZLECENIE_NAPRAWA VALUES (nextval('isp.zlecenie_naprawa_seq'), '', 0, '" + Proxy.loggedUser.getId() + "', '" +
                naprawa.getUrzadzenie_naprawiane().getId() + "', null, '" + id + "');";
        statement.executeUpdate(str);

        str = "SELECT MAX(id) FROM ZLECENIE_NAPRAWA WHERE zlecenie_fk = '" + id + "';";
        resultSet = statement.executeQuery(str);
        resultSet.next();
        id = resultSet.getInt("max");

        statement.close();
        resultSet.close();
        return id;
    }

    public int sendUrzadzenieGetId(Urzadzenie urzadzenie) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "INSERT INTO URZADZENIE VALUES (nextval('urzadzenie_seq'), '"
                + urzadzenie.getNazwa() + "', '" + urzadzenie.getProducent() + "', '" + urzadzenie.getSn() + "','" + urzadzenie.getWlasciciel().getId() + "')";

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
        String str = "INSERT INTO WPIS VALUES (nextval('isp.wpis_seq'), '" + wpis.getData_utworzenia() + "', '" +
                wpis.getOpis() + "', null, " + id + ",null);";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM WPIS WHERE data_utworzenia = '" + wpis.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String identyfikator = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(identyfikator);
    }

    public List<Wpis> getNotificationList() throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM WPIS WHERE odbiorca = " + Proxy.loggedUser.getId() + ";";
        ResultSet resultSet = statement.executeQuery(str);
        List<Wpis> wpisList = new ArrayList<>();
        makeEntriesList(resultSet, wpisList);
        return wpisList;
    }

    private void makeEntriesList(ResultSet resultSet, List<Wpis> wpisList) throws SQLException {
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
                User user1 = new User();
                user1.setId(resultSet.getInt("odbiorca"));
                name = getAuthorName(user.getId());
                user1.setName(name.get(0));
                user1.setSurname(name.get(1));
                wpis.setOdbiorca(user1);
                wpis.setWasRead(resultSet.getBoolean("przeczytane"));
            }

            wpisList.add(wpis);
        }
    }

    public List<Urzadzenie> getDevices() throws SQLException {
        List<Urzadzenie> urzadzenieList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "select * from urzadzenie where id NOT IN (SELECT urzadzenie_fk " +
                "FROM urzadzenie_sieciowe) AND id NOT IN (SELECT urzadzenie_fk FROM czesc_komputerowa) AND urzadzenie.wlasciciel = " +  3 + ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Urzadzenie urzadzenie = new Urzadzenie();
            urzadzenie.setSn(resultSet.getString("sn"));
            urzadzenie.setId(resultSet.getInt("id"));
            urzadzenie.setWlasciciel(Proxy.loggedUser);
            urzadzenie.setProducent(resultSet.getString("producent"));
            urzadzenie.setNazwa(resultSet.getString("nazwa"));
            urzadzenieList.add(urzadzenie);
        }
        resultSet.close();
        statement.close();
        return urzadzenieList;
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
                + Proxy.loggedUser.getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Utrzymanie_sieci utrzymanieSieci = new Utrzymanie_sieci();
            utrzymanieSieci.setId(resultSet.getInt("id"));
            utrzymanieSieci.setKlient(Proxy.loggedUser);
            utrzymanieSieci.setNr_umowy(resultSet.getString("umowa"));
            List<Object> listaZlecenie = getZlecenieData(resultSet.getInt("zlecenie_fk"));
            utrzymanieSieci.setData_utworzenia((Timestamp) listaZlecenie.get(0));
            utrzymanieSieci.setData_wykonania((Timestamp) listaZlecenie.get(1));
            List<Wpis> listaWpisy = getEntries((String) listaZlecenie.get(2));
            utrzymanieSieci.setWpisy(listaWpisy);
            lista.add(utrzymanieSieci);
        }
        return  lista;
    }

    public List<Naprawa_serwisowa> getHardwareTicketList() throws SQLException {
        List<Naprawa_serwisowa> lista = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT *  FROM ZLECENIE_NAPRAWA WHERE wlasciciel = "
                + Proxy.loggedUser.getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Naprawa_serwisowa naprawaSerwisowa = new Naprawa_serwisowa();
            naprawaSerwisowa.setId(resultSet.getInt("id"));
            naprawaSerwisowa.setWlasciciel(Proxy.loggedUser);

            List<Urzadzenie> klientDevice = Proxy.loggedUser.getPosiadane_urzadzenia();
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
            lista.add(naprawaSerwisowa);
        }
        return  lista;
    }

    public int getInternetPacketId(float dl, String features) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT id FROM PAKIET_INTERNETU WHERE download = '" + dl + "' AND additional_features = '" + features + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int id = resultSet.getInt("id");

        resultSet.close();
        statement.close();
        return id;
    }

    public int getTvPacketId(int kanaly, String features) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT id FROM TELEWIZJA WHERE lista_kanalow = '" + kanaly + "' AND additional_features = '" + features + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int id = resultSet.getInt("id");

        resultSet.close();
        statement.close();
        return id;
    }

    public int getGsmPacketId(String standard, String features) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT id FROM GSM WHERE standard = '" + standard + "' AND additional_features = '" + features + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        int id = resultSet.getInt("id");

        resultSet.close();
        statement.close();
        return id;
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

    public List<Pakiet_internetu> getInternetPackets() throws SQLException {
        List<Pakiet_internetu> internetList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM PAKIET_INTERNETU";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Pakiet_internetu pakiet = new Pakiet_internetu();
            pakiet.setId(resultSet.getInt("id"));
            pakiet.setUpload(resultSet.getFloat("upload"));
            pakiet.setDownload(resultSet.getFloat("download"));
            pakiet.setCena(resultSet.getFloat("cena"));
            String features = resultSet.getString("additional_features");
            if(!features.isEmpty()) {
                pakiet.setAdditionalFeaturesList(getInternetFeature(features));
            }
            internetList.add(pakiet);
        }
        resultSet.close();
        statement.close();
        return internetList;
    }

    public List<Telewizja> getTVpackets() throws SQLException {
        List<Telewizja> tvList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM TELEWIZJA";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Telewizja tv = new Telewizja();
            tv.setId(resultSet.getInt("id"));
            tv.setCena(resultSet.getFloat("cena"));
            tv.setIlosc_kanalow(resultSet.getInt("lista_kanalow"));
            String features = resultSet.getString("additional_features");
            if(!features.isEmpty()) {
                tv.setAdditionalFeaturesList(getTelevisionFeature(features));
            }
            tvList.add(tv);
        }
        resultSet.close();
        statement.close();
        return tvList;
    }

    public List<GSM> getGSMpackets() throws SQLException {
        List<GSM> gsmList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM GSM";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            GSM gsm = new GSM();
            gsm.setId(resultSet.getInt("id"));
            gsm.setCena(resultSet.getFloat("cena"));
            gsm.setStandard(resultSet.getString("standard"));
            String features = resultSet.getString("additional_features");
            if(!features.isEmpty()) {
                gsm.setAdditionalFeaturesList(getTelephoneFeature(features));
            }
            gsmList.add(gsm);
        }
        resultSet.close();
        statement.close();
        return gsmList;
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
        urzadzenie.setWlasciciel(Proxy.loggedUser);

        resultSet.close();
        statement.close();
        return urzadzenie;
    }

    public List<User> getAccountsClients() throws SQLException {
        List<User> accountsList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM USERS WHERE role = '" + 2 + "';";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setUsername(resultSet.getString("username"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            user.setPhone(resultSet.getString("phone"));
            user.setMail(resultSet.getString("mail"));
            user.setPesel(resultSet.getString("pesel"));
            accountsList.add(user);
        }
        resultSet.close();
        statement.close();
        return accountsList;
    }

    private List<Wpis> getEntries(String wpisy) throws SQLException {
        //wpisy = wpisy.replace(",", " OR ");
        wpisy = getIdListForSelect(wpisy);
        List<Wpis> wpisList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM WPIS WHERE id IN (  " + wpisy + ");";
        ResultSet resultSet = statement.executeQuery(str);
        makeEntriesList(resultSet, wpisList);
        resultSet.close();
        statement.close();
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

    public List<Dokument> getEmploymentContracts() throws SQLException {
        List<Dokument> dokumentList = new ArrayList<>();
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM UMOWA_PRACA WHERE pracownik = '" + Proxy.loggedUser.getId() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        while (resultSet.next()) {
            var umowa = new Umowa_o_prace();
            umowa.setPracownik(Proxy.loggedUser);
            umowa.setRola(Role.getRole(resultSet.getInt("role")));
            umowa.setWynagrodzenie(resultSet.getFloat("wynagrodzenie"));
            umowa.setId(resultSet.getInt("id"));
            List<Object> lista = getDocumentData(resultSet.getInt("dokument_fk"));
            umowa.setData_utworzenia((Timestamp) lista.get(0));
            umowa.setData_wygasniecia((Timestamp) lista.get(1));
            umowa.setNr_dokumentu((String) lista.get(2));
            dokumentList.add(umowa);
        }
        resultSet.close();
        statement.close();
        return dokumentList;
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
            wpis.setWykonawca(Proxy.loggedUser);
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
            faktura.setNabywca(Proxy.loggedUser);
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
                urzadzenie.setWlasciciel(Proxy.loggedUser);
                urzadzenie.setNazwa(resultSet.getString("nazwa"));
                urzadzenie.setSn(resultSet.getString("sn"));
                urzadzenie.setProducent(resultSet.getString("producent"));
                urzadzenieList.add(urzadzenie);
            } else {
                Urzadzenie urzadzenie = new Urzadzenie();
                urzadzenie.setId(resultSet.getInt("id"));
                urzadzenie.setWlasciciel(Proxy.loggedUser);
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
