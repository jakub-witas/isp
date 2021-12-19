package com.jwbw.isp;

import com.jwbw.Main;

import java.sql.SQLException;

public class UrzadzenieBuilder {
    private String nazwa;
    private String producent;
    private String sn;

    public Urzadzenie build() throws SQLException {
        return new Urzadzenie(nazwa, producent, sn);
    }

    public UrzadzenieBuilder(String nazwa, String producent, String sn) {
        this.nazwa = nazwa;
        this.producent = producent;
        this.sn = sn;
    }

    public UrzadzenieBuilder setNazwa(String nazwa) {
        this.nazwa = nazwa;
        return this;
    }

    public UrzadzenieBuilder setProducent(String producent) {
        this.producent = producent;
        return this;
    }

    public UrzadzenieBuilder setSn(String sn) {
        this.sn = sn;
        return this;
    }
}
