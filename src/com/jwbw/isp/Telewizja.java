package com.jwbw.isp;

import java.util.List;

public class Telewizja {
    private int id;
    private int ilosc_kanalow;
    private List<TelevisionFeatures> additionalFeaturesList;
    private float cena;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIlosc_kanalow() {
        return ilosc_kanalow;
    }

    public void setIlosc_kanalow(int ilosc_kanalow) {
        this.ilosc_kanalow = ilosc_kanalow;
    }

    public List<TelevisionFeatures> getAdditionalFeaturesList() {
        return additionalFeaturesList;
    }

    public void setAdditionalFeaturesList(List<TelevisionFeatures> additionalFeaturesList) {
        this.additionalFeaturesList = additionalFeaturesList;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }
}
