package com.jwbw.isp;

public class Pakiet_internetu {
    private int id;
    private float download;
    private float upload;
    private boolean publiczne_ip;
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

    public boolean isPubliczne_ip() {
        return publiczne_ip;
    }

    public void setPubliczne_ip(boolean publiczne_ip) {
        this.publiczne_ip = publiczne_ip;
    }

    public float getCena() {
        return cena;
    }

    public void setCena(float cena) {
        this.cena = cena;
    }
}
