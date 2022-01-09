package com.jwbw;

import com.jwbw.isp.*;

import java.sql.*;
import java.util.List;

public class DatabaseHandler extends Thread{
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

        return Integer.parseInt(id);
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
            user = (Klient) this.getUserAddresInfo(resultSet.getInt("address"), user);

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
            user = (Pracownik) this.getUserAddresInfo(resultSet.getInt("address"), user);

            statement.close();
            resultSet.close();
            logger.userLoggedIn(user.getId());
            return user;
        }
    }

    private Object getUserAddresInfo(int id, Object user) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM ADDRESS WHERE ID = '" + id + "';";
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();
        switch(user.getClass().toString()) {
            case "Pracownik" ->  {
                ((Pracownik)user).setCity(resultSet.getString("city"));
                ((Pracownik)user).setStreet(resultSet.getString("street"));
                ((Pracownik)user).setCode(resultSet.getString("code"));
                ((Pracownik)user).setHome_number(resultSet.getString("house_number"));
            }
            case "Klient" -> {
                ((Klient)user).setCity(resultSet.getString("city"));
                ((Klient)user).setStreet(resultSet.getString("street"));
                ((Klient)user).setCode(resultSet.getString("code"));
                ((Klient)user).setHome_number(resultSet.getString("house_number"));
            }
        }

        statement.close();
        resultSet.close();

        return user;
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

    public boolean authenticateUser(String username, String password) throws SQLException {
        Statement statement = this.connection.createStatement();
        String str = "SELECT * FROM USERS WHERE USERNAME = '" + username + "' AND PASSWORD = '" + password + "';";
        ResultSet resultSet = statement.executeQuery(str);

        boolean authenticate = resultSet.isBeforeFirst();

        statement.close();
        resultSet.close();

        return authenticate;
    }

    public boolean checkConnection(){
        try {
            this.connection.createStatement().executeQuery("SELECT 1");
        } catch (SQLException ignored) {
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
