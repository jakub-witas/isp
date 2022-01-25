package com.jwbw.isp;

import com.jwbw.Main;
import com.jwbw.Proxy;
import com.jwbw.gui.InterfaceMain;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Naprawa_serwisowa extends Zlecenie {
    private Urzadzenie urzadzenie_naprawiane;
    private Zamowienie zamowienie;
    private List<Cennik_uslug> wykonane_uslugi;
    private float koszt;
    private User wlasciciel;

    public static void utworzZlecenieFormularz(Urzadzenie urzadzenie, String nazwa, String producent, String sn, User klient, String description) throws SQLException {
        Naprawa_serwisowa naprawa = new Naprawa_serwisowa();
        naprawa.setKoszt(0);
        if (urzadzenie != null) naprawa.setUrzadzenie_naprawiane(urzadzenie);
        else {
            Urzadzenie urzadzenie_nowe = new UrzadzenieBuilder().setNazwa(nazwa).setProducent(producent).setSn(sn).build();
            naprawa.setUrzadzenie_naprawiane(urzadzenie_nowe);
        }
        naprawa.setZamowienie(null);
        naprawa.setWlasciciel(klient);
        naprawa.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));

        Director director = new Director();
        WpisBuilder builder = new WpisBuilder();
        director.constructWpis(builder, klient, description);
        Wpis pierwszy_wpis = builder.getResult();
        List<Wpis> wpisy = new ArrayList<>();
        //Wpis pierwszy_wpis = new Wpis(klient, "Utworzenie zlecenia");
        wpisy.add(pierwszy_wpis);
        naprawa.setWpisy(wpisy);
        naprawa.setId(Proxy.sendNaprawaGetId(naprawa));
        Proxy.naprawySprzetu.add(naprawa);
    }

    public void dodajWpis(int number, String customDescription, List<Cennik_uslug> dodatkowe, User odbiorca) throws SQLException {
        Director director = new Director();
        WpisBuilder builder = new WpisBuilder();
        director.constructWpis(builder, Proxy.loggedUser, customDescription);
        Wpis wpis1 = builder.getResult();

        //Wpis wpis1 = new Wpis(InterfaceMain.loggedUser, customDescription);
        this.wpisy.add(wpis1);

        String description;

        if(number == 1) {
            this.wykonane_uslugi.add(Cennik_uslug.Diagnoza);
            if (!dodatkowe.isEmpty()) {
                this.wykonane_uslugi.addAll(dodatkowe);
            }

            description = "Diagnostyka wpisu nr ";
            description += this.getId();
            //Wpis wpis2 = Wpis.wyslijPowiadomienie(InterfaceMain.loggedUser, odbiorca, description);
            director.constructPowiadomienie(builder, Proxy.loggedUser, description, odbiorca);
            Wpis wpis2 = builder.getResult();
            this.wpisy.add(wpis2);
        } else if (number == 2) {
            description = "Dodano odpowiedź do wpisu nr ";
            description += this.getId();
            //Wpis wpis2 = Wpis.wyslijPowiadomienie(InterfaceMain.loggedUser, odbiorca, description);
            director.constructPowiadomienie(builder, Proxy.loggedUser, description, odbiorca);
            Wpis wpis2 = builder.getResult();
            this.wpisy.add(wpis2);
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

    public User getWlasciciel() {
        return wlasciciel;
    }

    public void setWlasciciel(User wlasciciel) {
        this.wlasciciel = wlasciciel;
    }
}
