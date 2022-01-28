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
                if (((Pakiet_internetu) object).getAdditionalFeaturesList() != null)
                for(InternetFeatures feature: ((Pakiet_internetu) object).getAdditionalFeaturesList()) {
                    cena += feature.getPrice();
                }
            } else if(object instanceof Telewizja) {
                cena += ((Telewizja) object).getCena();
                if (((Telewizja) object).getAdditionalFeaturesList() != null)
                for(TelevisionFeatures feature: ((Telewizja) object).getAdditionalFeaturesList()) {
                    cena += feature.getPrice();
                }
            } else if (object instanceof  GSM){
                cena += ((GSM) object).getCena();
                if (((GSM) object).getAdditionalFeaturesList() != null)
                for(TelephoneFeatures feature: ((GSM) object).getAdditionalFeaturesList()) {
                    cena += feature.getPrice();
                }
            }
        }
        if(oferta.size() == 2) cena *= 0.95;
        if(oferta.size() == 3) cena *= 0.90;
        return cena;
    }

    public void setNabywca(User nabywca) {
        this.nabywca = nabywca;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(User autor) {
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
