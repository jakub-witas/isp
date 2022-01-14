package com.jwbw.isp;

import java.sql.Timestamp;
import java.util.Date;

public abstract class Dokument {
    int id;
    String nr_dokumentu;
    Timestamp data_utworzenia;
    Timestamp data_wygasniecia;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNr_dokumentu() { return nr_dokumentu; }

    public void setNr_dokumentu(String nr_dokumentu) { this.nr_dokumentu = nr_dokumentu; }

    public Date getData_wygasniecia() { return data_wygasniecia; }

    public void setData_utworzenia(Timestamp data_utworzenia) {
        this.data_utworzenia = data_utworzenia;
    }

    public void setData_wygasniecia(Timestamp data_wygasniecia) {
        this.data_wygasniecia = data_wygasniecia;
    }

    public Date getData_utworzenia() { return data_utworzenia; }


}
