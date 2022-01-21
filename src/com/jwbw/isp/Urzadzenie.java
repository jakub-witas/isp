package com.jwbw.isp;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;

import java.sql.SQLException;

public class Urzadzenie {
    protected int id;
    protected String nazwa;
    protected String producent;
    protected String sn;
    protected User wlasciciel;

    public Urzadzenie (String nazwa, String producent, String sn) throws SQLException {
        this.setNazwa(nazwa);
        this.setProducent(producent);
        this.setSn(sn);
        this.setWlasciciel((User)InterfaceMain.loggedUser);
        this.setId(Main.Database.sendUrzadzenieGetId(this));
    }

    public Urzadzenie() {}

    public User getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(User wlasciciel) {
        this.wlasciciel = wlasciciel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getProducent() {
        return producent;
    }

    public void setProducent(String producent) {
        this.producent = producent;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }


}
