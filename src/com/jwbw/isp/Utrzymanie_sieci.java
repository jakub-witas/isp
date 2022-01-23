package com.jwbw.isp;

import com.jwbw.Main;
import com.jwbw.gui.InterfaceMain;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Utrzymanie_sieci extends Zlecenie {
    //private List<Urzadzenie_sieciowe> sprzet;
    private User klient;
    private String nr_umowy;

    public Utrzymanie_sieci(String umowa, String description) throws SQLException {
        this.setKlient(InterfaceMain.loggedUser);
        this.setNr_umowy(umowa);
        this.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));
        Director director = new Director();
        WpisBuilder builder = new WpisBuilder();
        director.constructWpis(builder, InterfaceMain.loggedUser, description);
        Wpis wpis = builder.getResult();
        List<Wpis> wpisList = new ArrayList<>();
        wpisList.add(wpis);
        this.setWpisy(wpisList);
        Main.Database.sendNaprawaSieciGetId(this);
    }

    public Utrzymanie_sieci() {}
    public String getNr_umowy() {
        return nr_umowy;
    }

    public void setNr_umowy(String nr_umowy) {
        this.nr_umowy = nr_umowy;
    }
//    public List<Urzadzenie_sieciowe> getSprzet() {
//        return sprzet;
//    }
//
//    public void setSprzet(List<Urzadzenie_sieciowe> sprzet) {
//        this.sprzet = sprzet;
//    }

    public User getKlient() {
        return klient;
    }

    public void setKlient(User user) {
        this.klient = user;
    }
}
