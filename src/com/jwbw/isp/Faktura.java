package com.jwbw.isp;

import com.jwbw.Main;
import com.jwbw.Proxy;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Faktura extends Dokument {
    private float kwota;
    private User nabywca;
    private List<Object> obiekty;

    public Faktura(User nabywca, List<Object> obiekty) throws SQLException {
        this.setNabywca(nabywca);
        this.setObiekty(obiekty);
        this.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));
        this.setData_wygasniecia(Timestamp.valueOf(this.data_utworzenia.toLocalDateTime().plusDays(14)));
        this.setKwota(obliczKwote(this.getObiekty()));
        this.setId(Proxy.sendFakturaGetId(this));
    }

    public Faktura() {}

    private float obliczKwote(List<Object> obiekty) {
        float kwota = 0;
        for (Object obiekt: obiekty) {
            if(obiekt.getClass() == Zamowienie.class) {
                kwota += ((Zamowienie) obiekt).getKwota()* 1.2;
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

    public User getNabywca() {
        return nabywca;
    }

    public void setNabywca(User nabywca) {
        this.nabywca = nabywca;
    }


    public List<Object> getObiekty() {
        return obiekty;
    }

    public void setObiekty(List<Object> obiekty) {
        this.obiekty = obiekty;
    }
}
