package com.jwbw.isp;

import java.util.List;

public class Umowa_usluga extends Dokument{
    private List<Object> oferta;
    private User nabywca;
    private String autor;

    public float calculateMonthlyPayment() {
        float cena = 0;
        for(Object object: oferta) {
            if(object instanceof Pakiet_internetu) {
                cena += ((Pakiet_internetu) object).getCena();
                for(AdditionalFeatures feature: ((Pakiet_internetu) object).getAdditionalFeaturesList()) {
                    cena += feature.getPrice();
                }
            } else if(object instanceof Telewizja) {
                cena += ((Telewizja) object).getCena();
                for(AdditionalFeatures feature: ((Telewizja) object).getAdditionalFeaturesList()) {
                    cena += feature.getPrice();
                }
            } else {
                cena += ((GSM) object).getCena();
                for(AdditionalFeatures feature: ((GSM) object).getAdditionalFeaturesList()) {
                    cena += feature.getPrice();
                }
            }
        }
        return cena;
    }

    public void setNabywca(User nabywca) {
        this.nabywca = nabywca;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(Pracownik autor) {
        this.autor = autor.getName()+ " " + autor.getSurname();
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public List<Object> getOferta() {
        return oferta;
    }

    public void setOferta(List<Object> oferta) {
        this.oferta = oferta;
    }

    public User getNabywca() {
        return nabywca;
    }
}
