package com.jwbw.test;

import com.jwbw.isp.GSM;
import com.jwbw.isp.Pakiet_internetu;
import com.jwbw.isp.Telewizja;
import com.jwbw.isp.Umowa_usluga;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Umowa_uslugaTest {

    @Test
    void calculateMonthlyPaymentTestForTwoServices() {
        var umowa = new Umowa_usluga();
        var internet = new Pakiet_internetu();
        internet.setCena(50);
        var tv = new Telewizja();
        tv.setCena(30);
        List<Object> lista = new ArrayList<>();
        lista.add(internet);
        lista.add(tv);
        umowa.setOferta(lista);
        assertEquals((80*0.95), umowa.calculateMonthlyPayment());
    }

    @Test
    void calculateMonthlyPaymentTestForThreeServices() {
        var umowa = new Umowa_usluga();
        var internet = new Pakiet_internetu();
        internet.setCena(50);
        var tv = new Telewizja();
        var gsm = new GSM();
        gsm.setCena(15);
        tv.setCena(30);
        List<Object> lista = new ArrayList<>();
        lista.add(internet);
        lista.add(tv);
        lista.add(gsm);
        umowa.setOferta(lista);
        assertEquals((95*0.90), umowa.calculateMonthlyPayment());
    }

    @Test
    void calculateMonthlyPaymentTestForOneService() {
        var umowa = new Umowa_usluga();
        var internet = new Pakiet_internetu();
        internet.setCena(50);
        List<Object> lista = new ArrayList<>();
        lista.add(internet);
        umowa.setOferta(lista);
        assertEquals(50, umowa.calculateMonthlyPayment());
    }
}