package com.jwbw.isp;

import com.jwbw.Proxy;

import java.sql.SQLException;

public class Urzadzenie_sieciowe extends Urzadzenie {
    private boolean wlan;
    private String przepustowosc;
    private String ip_address;
    private String nrUmowy;
    private boolean czy_dostepne;
    private User wlasciciel;

    public Urzadzenie_sieciowe(String nazwa, String producent, String sn, String przepustowosc, boolean wlan, String ip_address) throws SQLException {
        super();
        this.ip_address = ip_address;
        this.setNazwa(nazwa);
        this.setProducent(producent);
        this.setSn(sn);
        this.setCzy_dostepne(true);
        this.setPrzepustowosc(przepustowosc);
        this.setWlan(wlan);
        this.setWlasciciel(null);
        //this.setId(Proxy.sendUrzadzenieSiecioweGetId(this));
    }

    public String getNrUmowy() {
        return nrUmowy;
    }

    public void setNrUmowy(String nrUmowy) {
        this.nrUmowy = nrUmowy;
    }

    public Urzadzenie_sieciowe() {}

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
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

    public User getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(User wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
}
