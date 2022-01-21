package com.jwbw.isp;

import java.util.List;

public class Pakiet_internetu {
    private int id;
    private float download;
    private float upload;
    private List<AdditionalFeatures> additionalFeaturesList;
    private float cena;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getDownload() {
        return download;
    }

    public void setDownload(float download) {
        this.download = download;
    }

    public float getUpload() {
        return upload;
    }

    public void setUpload(float upload) {
        this.upload = upload;
    }

    public List<AdditionalFeatures> getAdditionalFeaturesList() {
        return additionalFeaturesList;
    }

    public void setAdditionalFeaturesList(List<AdditionalFeatures> additionalFeaturesList) {
        this.additionalFeaturesList = additionalFeaturesList;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }
}
