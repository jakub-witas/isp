package com.jwbw.isp;

import com.jwbw.Main;
import com.jwbw.Proxy;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Zamowienie extends Dokument {
    private List<Czesc_komputerowa> czesci;
    private float kwota;

    public Zamowienie(float kwota, List<Czesc_komputerowa> czesci) throws SQLException {
        this.setCzesci(czesci);
        this.setKwota(kwota);
        this.setData_utworzenia(Timestamp.valueOf(LocalDateTime.now()));
        this.setData_wygasniecia(Timestamp.valueOf(this.data_utworzenia.toLocalDateTime().plusDays(14)));
        this.setId(Proxy.sendZamowienieGetId(this));

    }

    public Zamowienie() {}

    public void calculatePrice() {
        float kwota = 0;
        if(getCzesci() != null && !getCzesci().isEmpty()) {
            for (Czesc_komputerowa czesc : getCzesci()) {
               kwota += czesc.getKoszt();
            }
        }
        setKwota(kwota);
    }

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
