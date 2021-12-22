package com.jwbw.isp;

import com.jwbw.DatabaseHandler;
import com.jwbw.Main;

import java.sql.SQLException;

public class Urzadzenie_sieciowe extends Urzadzenie {
    private boolean wlan;
    private String przepustowosc;
    private boolean czy_dostepne;
    private Klient wlasciciel;

    public Urzadzenie_sieciowe(String nazwa, String producent, String sn, String przepustowosc, boolean wlan) throws SQLException {
        super();
        this.setNazwa(nazwa);
        this.setProducent(producent);
        this.setSn(sn);
        this.setCzy_dostepne(true);
        this.setPrzepustowosc(przepustowosc);
        this.setWlan(wlan);
        this.setWlasciciel(null);
        //this.setId(Main.connection.databaseHandler.sendUrzadzenieSiecioweGetId(this));
    }

    public boolean isWlan() {
        return wlan;
    }

    public void setWlan(boolean wlan) {
        this.wlan = wlan;
    }

    public String getPrzepustowosc() {
        return przepustowosc;
    }

    public void setPrzepustowosc(String przepustowosc) {
        this.przepustowosc = przepustowosc;
    }

    public boolean isCzy_dostepne() {
        return czy_dostepne;
    }

    public void setCzy_dostepne(boolean czy_dostepne) {
        this.czy_dostepne = czy_dostepne;
    }

    public Klient getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(Klient wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
}