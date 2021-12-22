package com.jwbw.isp;

import com.jwbw.Main;
import com.jwbw.gui.Controllers.InterfaceMain;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

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

    public void dodajWpis(int number, String customDescription, List<Cennik_uslug> dodatkowe, Object odbiorca) throws SQLException {
        Wpis wpis1 = new Wpis(InterfaceMain.loggedUser, customDescription);
        this.wpisy.add(wpis1);

        String description;

        if(number == 1) {
            this.wykonane_uslugi.add(Cennik_uslug.Diagnoza);
            if (!dodatkowe.isEmpty()) {
                this.wykonane_uslugi.addAll(dodatkowe);
            }

            description = "Diagnostyka wpisu nr ";
            description += this.getId();
            Wpis wpis2 = Wpis.wyslijPowiadomienie(InterfaceMain.loggedUser, odbiorca, description);
        } else if (number == 2) {
            description = "Dodano odpowiedź do wpisu nr ";
            description += this.getId();
            Wpis wpis2 = Wpis.wyslijPowiadomienie(InterfaceMain.loggedUser, odbiorca, description);
        }

    }

    public void dodajUsluge(List<Cennik_uslug> uslugi) {
        this.wykonane_uslugi.addAll(uslugi);
    }

    public void rozlicz() throws SQLException {
        List<Object> obiekty = null;
        assert false;
        obiekty.addAll(this.getWykonane_uslugi());
        obiekty.add(this.getZamowienie());
        Faktura faktura = new Faktura(this.getWlasciciel(), obiekty);
        this.getWlasciciel().dokumenty.add(faktura);
    }

    public void dodajZamowienie(float kwota, List<Czesc_komputerowa>czesci) throws SQLException {
        Zamowienie zamowienie = new Zamowienie(kwota, czesci);
        this.setZamowienie(zamowienie);
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
