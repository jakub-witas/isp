package com.jwbw.isp;

import com.jwbw.DatabaseHandler;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Faktura extends Dokument {
    private float kwota;
    private Object nabywca;
    private Object sprzedawca;
    private List<Object> obiekty;

    public Faktura(Object nabywca, Object sprzedawca, List<Object> obiekty) {
        this.nabywca = nabywca;
        this.sprzedawca = sprzedawca;
        this.obiekty = obiekty;
        this.data_utworzenia = new Date(System.currentTimeMillis());
        LocalDateTime tmp = LocalDateTime.now();
        LocalDateTime tmp2 = tmp.plusDays(14);
        this.data_wygasniecia = Date.from(tmp2.atZone(ZoneId.systemDefault()).toInstant());
        this.kwota = obliczKwote(obiekty);
        //this.id = setId(DatabaseHandler.insertFaktura());
    }

    private float obliczKwote(List<Object> obiekty) {
        float kwota = 0;
        for (Object obiekt: obiekty) {
            if(obiekt.getClass() == Zamowienie.class) {
                kwota += ((Zamowienie) obiekt).getKwota();
            } else {
                kwota += ((Cennik_uslug) obiekt).getPrice();
            }
        }
        return kwota;
    }

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
