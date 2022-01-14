package com.jwbw.isp;

import com.jwbw.Main;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Wpis {
    private int id;
    private Timestamp data_utworzenia;
    private Object autor;
    private Object odbiorca;
    private boolean wasRead;
    private String opis;

    public Object getOdbiorca() {
        return odbiorca;
    }

    public void setOdbiorca(Object odbiorca) {
        this.odbiorca = odbiorca;
    }

    public boolean isWasRead() {
        return wasRead;
    }

    public void setWasRead(boolean wasRead) {
        this.wasRead = wasRead;
    }

    public Wpis (Object autor, String opis) throws SQLException {
        this.data_utworzenia = Timestamp.valueOf(LocalDateTime.now());
        this.setAutor(autor);
        this.setOpis(opis);
        this.setId(Main.Database.sendWpisGetId(this));
    }

    public Wpis (Object autor, Object odbiorca, String opis, boolean wasRead) throws SQLException {
        this.data_utworzenia = Timestamp.valueOf(LocalDateTime.now());
        this.setAutor(autor);
        this.setOpis(opis);
        this.setWasRead(wasRead);
        this.setOdbiorca(odbiorca);
        this.setId(Main.Database.sendPowiadomienieGetId(this));
    }

    public Wpis() {}

    public static Wpis wyslijPowiadomienie(Object autor, Object odbiorca, String description) throws SQLException {
        Wpis nowy = new Wpis();
        nowy.setAutor(autor);
        nowy.setOpis(description);
        nowy.setOdbiorca(odbiorca);
        nowy.setWasRead(false);
        nowy.setId(Main.Database.sendPowiadomienieGetId(nowy));
        return nowy;
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
