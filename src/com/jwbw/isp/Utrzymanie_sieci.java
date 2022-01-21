package com.jwbw.isp;

public class Utrzymanie_sieci extends Zlecenie {
    //private List<Urzadzenie_sieciowe> sprzet;
    private User klient;
    private String lastEntry;

    public String getLastEntry() {
        return lastEntry;
    }

    public void setLastEntry(String lastEntry) {
        this.lastEntry = lastEntry;
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
