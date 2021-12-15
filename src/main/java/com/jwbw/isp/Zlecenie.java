package com.jwbw.isp;

import java.util.Date;
import java.util.List;

public abstract class Zlecenie {
    protected int id;
    protected String nr_zlecenia;
    protected Date data_utworzenia;
    protected Date data_wykonania;
    protected List<Wpis> wpisy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNr_zlecenia() {
        return nr_zlecenia;
    }

    public void setNr_zlecenia(String nr_zlecenia) {
        this.nr_zlecenia = nr_zlecenia;
    }

    public Date getData_utworzenia() {
        return data_utworzenia;
    }

    public void setData_utworzenia(Date data_utworzenia) {
        this.data_utworzenia = data_utworzenia;
    }

    public Date getData_wykonania() {
        return data_wykonania;
    }

    public void setData_wykonania(Date data_wykonania) {
        this.data_wykonania = data_wykonania;
    }

    public List<Wpis> getWpisy() {
        return wpisy;
    }

    public void setWpisy(List<Wpis> wpisy) {
        this.wpisy = wpisy;
    }
}
