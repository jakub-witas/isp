package com.jwbw.isp;

import com.jwbw.DatabaseHandler;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Wpis {
    private int id;
    private Timestamp data_utworzenia;
    private Object autor;
    private String opis;

    public Wpis (Object autor, String opis) throws SQLException {
        this.data_utworzenia = Timestamp.valueOf(LocalDateTime.now());
        this.setAutor(autor);
        this.setOpis(opis);
        this.setId(DatabaseHandler.sendWpisGetId(this));

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getData_utworzenia() {
        return data_utworzenia;
    }

    public void setData_utworzenia(Timestamp data_utworzenia) {
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
