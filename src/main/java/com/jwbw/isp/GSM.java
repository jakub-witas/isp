package com.jwbw.isp;

public class GSM {
    private int id;
    private String standard;
    private boolean roaming;
    private float cena;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public boolean isRoaming() {
        return roaming;
    }

    public void setRoaming(boolean roaming) {
        this.roaming = roaming;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }
}
