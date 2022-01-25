package com.jwbw.isp;

import java.util.List;

public class GSM {
    private int id;
    private String standard;
    private List<TelephoneFeatures> additionalFeaturesList;
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

    public List<TelephoneFeatures> getAdditionalFeaturesList() {
        return additionalFeaturesList;
    }

    public void setAdditionalFeaturesList(List<TelephoneFeatures> additionalFeaturesList) {
        this.additionalFeaturesList = additionalFeaturesList;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }
}
