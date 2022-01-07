package com.jwbw.isp;

import com.jwbw.Main;

import java.sql.SQLException;

public class Urzadzenie {
    protected int id;
    protected String nazwa;
    protected String producent;
    protected String sn;

    public Urzadzenie (String nazwa, String producent, String sn) throws SQLException {
        this.setNazwa(nazwa);
        this.setProducent(producent);
        this.setSn(sn);
        this.setId(Main.connection.databaseHandler.sendUrzadzenieGetId(this));
    }

    public Urzadzenie() {}

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
