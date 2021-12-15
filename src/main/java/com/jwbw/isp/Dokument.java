package com.jwbw.isp;

import java.util.Date;

public class Dokument {
    protected int id;
    protected String nr_dokumentu;
    protected Date data_utworzenia;
    protected Date data_wygasniecia;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNr_dokumentu() { return nr_dokumentu; }

    public void setNr_dokumentu(String nr_dokumentu) { this.nr_dokumentu = nr_dokumentu; }

    public void setData_utworzenia(Date data_utworzenia) { this.data_utworzenia = data_utworzenia; }

    public Date getData_wygasniecia() { return data_wygasniecia; }

    public void setData_wygasniecia(Date data_wygasniecia) { this.data_wygasniecia = data_wygasniecia; }

    public Date getData_utworzenia() { return data_utworzenia; }


}
