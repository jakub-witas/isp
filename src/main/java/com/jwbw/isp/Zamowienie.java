package com.jwbw.isp;

import java.util.List;

public class Zamowienie extends Dokument {
    List<Czesc_komputerowa> czesci;

    public List<Czesc_komputerowa> getCzesci() {
        return czesci;
    }

    public void setCzesci(List<Czesc_komputerowa> czesci) {
        this.czesci = czesci;
    }
}
