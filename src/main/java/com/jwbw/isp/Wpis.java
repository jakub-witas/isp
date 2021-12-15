package com.jwbw.isp;

import java.util.Date;

public class Wpis {
    private int id;
    private Date data_utworzenia;
    private Object autor;
    private String opis;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData_utworzenia() {
        return data_utworzenia;
    }

    public void setData_utworzenia(Date data_utworzenia) {
        this.data_utworzenia = data_utworzenia;
    }

    public Object getAutor() {
        return autor;
    }

    public void setAutor(Object autor) {
        this.autor = autor;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }
}
