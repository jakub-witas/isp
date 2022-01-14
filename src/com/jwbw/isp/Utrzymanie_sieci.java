package com.jwbw.isp;

import java.util.List;

public class Utrzymanie_sieci extends Zlecenie {
    //private List<Urzadzenie_sieciowe> sprzet;
    private Klient klient;

//    public List<Urzadzenie_sieciowe> getSprzet() {
//        return sprzet;
//    }
//
//    public void setSprzet(List<Urzadzenie_sieciowe> sprzet) {
//        this.sprzet = sprzet;
//    }

    public Klient getKlient() {
        return klient;
    }

    public void setKlient(Klient klient) {
        this.klient = klient;
    }
}
