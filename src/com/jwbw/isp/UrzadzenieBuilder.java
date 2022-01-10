package com.jwbw.isp;


import java.sql.SQLException;

public class UrzadzenieBuilder {
    private Urzadzenie urzadzenie;

    public Urzadzenie build() throws SQLException {
        return urzadzenie;
    }

    public UrzadzenieBuilder() {
        this.reset();
    }

    private void reset() {
        this.urzadzenie = new Urzadzenie();
    }

    public UrzadzenieBuilder setNazwa(String nazwa) {
        this.urzadzenie.setNazwa(nazwa);
        return this;
    }

    public UrzadzenieBuilder setProducent(String producent) {
        this.urzadzenie.setProducent(producent);
        return this;
    }

    public UrzadzenieBuilder setSn(String sn) {
        this.urzadzenie.setSn(sn);
        return this;
    }
}
