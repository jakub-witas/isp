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
                str = "UPDATE isp.USERS SET name = '" + ((Klient) user).getName() + "', surname = '" + ((Klient) user).getSurname() +
                        "', phone = '" + ((Klient) user).getPhone() + "', id_card = '" + ((Klient) user).getId_card()
                        + "', mail = '" + ((Klient) user).getMail() + "' WHERE id = '" + ((Klient) user).getId() + "';";

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
            str = "SELECT address FROM isp.USERS WHERE  id = " + ((Klient)user).getId() + ";";
            ResultSet resultSet = statement.executeQuery(str);
            resultSet.next();
            int id = resultSet.getInt("address");
            resultSet.close();

            str = "UPDATE isp.ADDRESS SET city = '" + ((Klient) user).getCity() + "', street = '" + ((Klient) user).getStreet() +
                    "', house_number = '" + ((Klient) user).getHome_number() + "', code = '" + ((Klient) user).getCode()
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
                + ((Klient)InterfaceMain.loggedUser).getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Umowa_usluga usluga = new Umowa_usluga();
            String oferta = (resultSet.getString("oferta"));
            usluga.setId(resultSet.getInt("id"));
            int autor = resultSet.getInt("autor");
            usluga.setAutor(getAuthorName(autor));
            usluga.setNabywca((Klient) InterfaceMain.loggedUser);
            List<Object> listaDoc = getDocumentData(resultSet.getInt("dokument_fk"));
            usluga.setData_utworzenia((Timestamp) listaDoc.get(0));
            usluga.setData_wygasniecia((Timestamp) listaDoc.get(1));
            usluga.setNr_dokumentu((String) listaDoc.get(2));
            lista.add(usluga);
        }
        return  lista;
    }

    private String getAuthorName(int id) throws SQLException {
        Statement statement =  this.connection.createStatement();
        String str = "SELECT name, surname FROM USERS WHERE id = " + id +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String name = "";
        name += resultSet.getString("name");
        name +=  " ";
        name += resultSet.getString("surname");


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
                + wpis.getData_utworzenia() + "', '" + wpis.getOpis() + "', '" + id + "')";

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
        String str = "INSERT INTO POWIADOMIENIE(id, data_utworzenia, opis, przeczytane, autor, odbiorca) VALUES (nextval('wpis_seq'), '"
                + wpis.getData_utworzenia() + "', '" + wpis.getOpis() + "', '" + wpis.isWasRead() + "', '" + idAutor + "', '" + idOdbiorca + "')";

        statement.executeUpdate(str);
        str = "SELECT MAX(ID) FROM POWIADOMIENIE WHERE data_utworzenia = '" + wpis.getData_utworzenia() + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        String identyfikator = resultSet.getString("max");

        statement.close();
        resultSet.close();
        return Integer.parseInt(identyfikator);
    }

    private int getObjectId(Object obj) {
        int id;
        if (obj.getClass() == Klient.class) {
            id = ((Klient) obj).getId();
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

    private boolean checkForAddress(Klient user) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM ADDRESS WHERE CITY = '" + user.getCity() + "' AND STREET = '" + user.getStreet() +
                "' AND HOUSE_NUMBER = '" + user.getHome_number() + "' AND CODE = '" + user.getCode()  + "';";
        ResultSet resultSet = statement.executeQuery(str);

        boolean authenticate = resultSet.isBeforeFirst();

        statement.close();
        resultSet.close();

        return authenticate;
    }

    private int registerNewAdress(Klient user) throws SQLException {
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

    private int getAddressId(Klient user) throws SQLException {
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
                + ((Klient)InterfaceMain.loggedUser).getId() +  ";";
        ResultSet resultSet = statement.executeQuery(str);
        while(resultSet.next()) {
            Utrzymanie_sieci utrzymanieSieci = new Utrzymanie_sieci();
            utrzymanieSieci.setId(resultSet.getInt("id"));
            utrzymanieSieci.setKlient((Klient) InterfaceMain.loggedUser);

            List<Object> listaZlecenie = getZlecenieData(resultSet.getInt("zlecenie_fk"));
            utrzymanieSieci.setData_utworzenia((Timestamp) listaZlecenie.get(0));
            utrzymanieSieci.setData_wykonania((Timestamp) listaZlecenie.get(1));
            List<Wpis> listaWpisy = getEntries((String) listaZlecenie.get(2));

            lista.add(utrzymanieSieci);
        }
        return  lista;
    }

    private List<Wpis> getEntries(String wpisy) throws SQLException {
        Statement statement = this.connection.createStatement();
        //TODO: dokonczyc
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

    public int registerNewUser(Klient user, String username, String password) throws SQLException {
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

    public Object fetchUserData(String username, String password) throws SQLException {
        int role = this.getUserRole(username, password);

        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM USERS WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        if(role == 2)  {
            Klient user = new Klient();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setSurname(resultSet.getString("surname"));
            user.setId_card(resultSet.getString("ID_card"));
            user.setMail(resultSet.getString("mail"));
            user.setPesel(resultSet.getString("pesel"));
            user.setPhone(resultSet.getString("phone"));
            List<String> lista = getUserAddresInfo(resultSet.getInt("address"));

            user.setCity(lista.get(0));
            user.setStreet(lista.get(1));
            user.setCode(lista.get(2));
            user.setHome_number(lista.get(3));

            statement.close();
            resultSet.close();
            logger.userLoggedIn(user.getId());
            return user;
        } else {
            Pracownik user = new Pracownik();
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

            statement.close();
            resultSet.close();
            logger.userLoggedIn(user.getId());
            return user;
        }
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
