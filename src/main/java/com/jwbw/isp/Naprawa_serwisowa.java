package com.jwbw.isp;

import java.util.List;

public class Naprawa_serwisowa extends Zlecenie {
    private Urzadzenie urzadzenie_naprawiane;
    private List<Czesc_komputerowa> czesci;
    private List<Cennik_uslug> wykonane_uslugi;
    private float koszt;
    private Klient wlasciciel;

    public Urzadzenie getUrzadzenie_naprawiane() {
        return urzadzenie_naprawiane;
    }

    public void setUrzadzenie_naprawiane(Urzadzenie urzadzenie_naprawiane) {
        this.urzadzenie_naprawiane = urzadzenie_naprawiane;
    }

    public List<Czesc_komputerowa> getCzesci() {
        return czesci;
    }

    public void setCzesci(List<Czesc_komputerowa> czesci) {
        this.czesci = czesci;
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
