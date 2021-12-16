package com.jwbw.isp;

public class Czesc_komputerowa extends Urzadzenie {
    private String port;
    private String przeznaczenie;
    private float koszt;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPrzeznaczenie() {
        return przeznaczenie;
    }

    public void setPrzeznaczenie(String przeznaczenie) {
        this.przeznaczenie = przeznaczenie;
    }

    public float getKoszt() {
        return koszt;
    }

    public void setKoszt(float koszt) {
        this.koszt = koszt;
    }
}
