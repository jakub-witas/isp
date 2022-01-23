package com.jwbw.isp;

import com.jwbw.Main;

import java.sql.SQLException;
import java.sql.Timestamp;

public class WpisBuilder implements Builder{
    private String type;
    private User autor;
    private User odbiorca;
    private boolean wasRead;
    private String opis;

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public void setAutor(User autor) {
        this.autor = autor;
    }

    @Override
    public void setOdbiorca(User odbiorca) {
        this.odbiorca = odbiorca;
    }

    @Override
    public void setWasRead() {
        this.wasRead = false;
    }

    public Wpis getResult() throws SQLException {
        if (type.equals("wpis"))  return new Wpis(autor, opis);
        else return new Wpis(autor, odbiorca, opis, wasRead);
    }
}
