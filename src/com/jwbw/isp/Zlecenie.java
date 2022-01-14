package com.jwbw.isp;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public abstract class Zlecenie {
    protected int id;
    protected Timestamp data_utworzenia;
    protected Timestamp data_wykonania;
    protected List<Wpis> wpisy;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getData_utworzenia() {
        return data_utworzenia;
    }

    public void setData_utworzenia(Timestamp data_utworzenia) {
        this.data_utworzenia = data_utworzenia;
    }

    public void setData_wykonania(Timestamp data_wykonania) {
        this.data_wykonania = data_wykonania;
    }

    public Date getData_wykonania() {
        return data_wykonania;
    }



    public List<Wpis> getWpisy() {
        return wpisy;
    }

    public void setWpisy(List<Wpis> wpisy) {
        this.wpisy = wpisy;
    }
}
