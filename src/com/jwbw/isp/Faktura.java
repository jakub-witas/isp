package com.jwbw.isp;

import com.jwbw.DatabaseHandler;
import com.jwbw.Main;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Faktura extends Dokument {
    private float kwota;
    private Klient nabywca;
    private List<Object> obiekty;

    public Faktura(Klient nabywca, List<Object> obiekty) throws SQLException {
        this.setNabywca(nabywca);
        this.setObiekty(obiekty);
        this.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));
        this.setData_wygasniecia(Timestamp.valueOf(this.data_utworzenia.toLocalDateTime().plusDays(14)));
        this.setKwota(obliczKwote(this.getObiekty()));
        this.setId(Main.Database.sendFakturaGetId(this));
    }

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

    public Klient getNabywca() {
        return nabywca;
    }

    public void setNabywca(Klient nabywca) {
        this.nabywca = nabywca;
    }


    public List<Object> getObiekty() {
        return obiekty;
    }

    public void setObiekty(List<Object> obiekty) {
        this.obiekty = obiekty;
    }
}
