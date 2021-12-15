package com.jwbw.isp;

import java.util.List;

public class Faktura extends Dokument {
    private float kwota;
    private Object nabywca;
    private Object sprzedawca;
    private List<Object> obiekty;

    public float getKwota() {
        return kwota;
    }

    public void setKwota(float kwota) {
        this.kwota = kwota;
    }

    public Object getNabywca() {
        return nabywca;
    }

    public void setNabywca(Object nabywca) {
        this.nabywca = nabywca;
    }

    public Object getSprzedawca() {
        return sprzedawca;
    }

    public void setSprzedawca(Object sprzedawca) {
        this.sprzedawca = sprzedawca;
    }

    public List<Object> getObiekty() {
        return obiekty;
    }

    public void setObiekty(List<Object> obiekty) {
        this.obiekty = obiekty;
    }
}
