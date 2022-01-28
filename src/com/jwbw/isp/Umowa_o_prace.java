package com.jwbw.isp;

public class Umowa_o_prace extends Dokument {
    private float wynagrodzenie;
    private Role rola;
    private User pracownik;

    public float getWynagrodzenie() {
        return wynagrodzenie;
    }

    public void setWynagrodzenie(float wynagrodzenie) {
        this.wynagrodzenie = wynagrodzenie;
    }

    public Role getRola() {
        return rola;
    }

    public void setRola(Role rola) {
        this.rola = rola;
    }

    public User getPracownik() {
        return pracownik;
    }

    public void setPracownik(User pracownik) {
        this.pracownik = pracownik;
    }
}
