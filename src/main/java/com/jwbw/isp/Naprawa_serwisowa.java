package com.jwbw.isp;


import com.jwbw.DatabaseHandler;
import com.jwbw.Main;
import com.jwbw.gui.Controllers.InterfaceMain;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Naprawa_serwisowa extends Zlecenie {
    private Urzadzenie urzadzenie_naprawiane;
    private Zamowienie zamowienie;
    private List<Cennik_uslug> wykonane_uslugi;
    private float koszt;
    private Klient wlasciciel;

    public void utworzZlecenieFormularz(Urzadzenie urzadzenie, String nazwa, String producent, String sn, Klient klient) throws SQLException {
        Naprawa_serwisowa naprawa = new Naprawa_serwisowa();
        naprawa.setKoszt(0);
        if (urzadzenie != null) naprawa.setUrzadzenie_naprawiane(urzadzenie);
        else {
            Urzadzenie urzadzenie_nowe = new UrzadzenieBuilder(nazwa, producent, sn).build();
            naprawa.setUrzadzenie_naprawiane(urzadzenie_nowe);
        }
        naprawa.setZamowienie(null);
        naprawa.setWlasciciel(klient);
        naprawa.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));
        Wpis pierwszy_wpis = new Wpis(klient, "Utworzenie zlecenia");
        naprawa.wpisy.add(pierwszy_wpis);
        naprawa.setId(Main.connection.databaseHandler.sendNaprawaGetId(naprawa));
        InterfaceMain.naprawy.addAll(naprawa);
    }

    public Urzadzenie getUrzadzenie_naprawiane() {
        return urzadzenie_naprawiane;
    }

    public void setUrzadzenie_naprawiane(Urzadzenie urzadzenie_naprawiane) {
        this.urzadzenie_naprawiane = urzadzenie_naprawiane;
    }

    public Zamowienie getZamowienie() {
        return zamowienie;
    }

    public void setZamowienie(Zamowienie zamowienie) {
        this.zamowienie = zamowienie;
    }

    public List<Cennik_uslug> getWykonane_uslugi() {
        return wykonane_uslugi;
    }

    public void setWykonane_uslugi(List<Cennik_uslug> wykonane_uslugi) {
        this.wykonane_uslugi = wykonane_uslugi;
    }

    public float getKoszt() {
        return koszt;
    }

    public void setKoszt(float koszt) {
        this.koszt = koszt;
    }

    public Klient getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(Klient wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
}
