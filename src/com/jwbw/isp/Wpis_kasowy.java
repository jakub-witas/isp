package com.jwbw.isp;

public class Wpis_kasowy extends Dokument {
    private float kwota;
    private User wykonawca;

    public float getKwota() {
        return kwota;
    }

    public void setKwota(float kwota) {
        this.kwota = kwota;
    }

    public User getWykonawca() {
        return wykonawca;
    }

    public void setWykonawca(User wykonawca) {
        this.wykonawca = wykonawca;
    }
}
