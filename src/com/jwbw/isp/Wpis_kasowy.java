package com.jwbw.isp;

public class Wpis_kasowy extends Dokument {
    private float kwota;
    private Klient wykonawca;

    public float getKwota() {
        return kwota;
    }

    public void setKwota(float kwota) {
        this.kwota = kwota;
    }

    public Klient getWykonawca() {
        return wykonawca;
    }

    public void setWykonawca(Klient wykonawca) {
        this.wykonawca = wykonawca;
    }
}
