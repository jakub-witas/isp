package com.jwbw.isp;

import java.util.List;

public class Zamowienie extends Dokument {
    private List<Czesc_komputerowa> czesci;
    private float kwota;

    public List<Czesc_komputerowa> getCzesci() {
        return czesci;
    }

    public void setCzesci(List<Czesc_komputerowa> czesci) {
        this.czesci = czesci;
    }

    public float getKwota() {
        return kwota;
    }

    public void setKwota(float kwota) {
        this.kwota = kwota;
    }
}
