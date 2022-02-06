package com.jwbw.test;

import com.jwbw.isp.Cennik_uslug;
import com.jwbw.isp.Naprawa_serwisowa;
import com.jwbw.isp.Zamowienie;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Naprawa_serwisowaTest {
    
    @Test
    void calculatePriceTest() {
        var naprawa = new Naprawa_serwisowa();
        var zamowienie = new Zamowienie();
        zamowienie.setKwota(50);
        naprawa.setZamowienie(new ArrayList<>());
        naprawa.getZamowienie().add(zamowienie);
        naprawa.calculatePrice();
        assertEquals(50, naprawa.getKoszt());
    }

    @Test
    void dodajUslugeTest() {
        var naprawa = new Naprawa_serwisowa();
        naprawa.setWykonane_uslugi(new ArrayList<>());
        naprawa.dodajUsluge(Cennik_uslug.Diagnoza);
        assertEquals(1, naprawa.getWykonane_uslugi().size());
    }
}