package com.jwbw.isp;

public class Telewizja {
    private int id;
    private int ilosc_kanalow;
    private boolean multiroom;
    private float cena;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIlosc_kanalow() {
        return ilosc_kanalow;
    }

    public void setIlosc_kanalow(int ilosc_kanalow) {
        this.ilosc_kanalow = ilosc_kanalow;
    }

    public boolean isMultiroom() {
        return multiroom;
    }

    public void setMultiroom(boolean multiroom) {
        this.multiroom = multiroom;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }
}
