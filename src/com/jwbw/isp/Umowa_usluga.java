package com.jwbw.isp;

import java.util.List;

public class Umowa_usluga extends Dokument{
    private List<Object> oferta;
    private Klient nabywca;
    private String autor;

    public void setNabywca(Klient nabywca) {
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

    public Klient getNabywca() {
        return nabywca;
    }
}
