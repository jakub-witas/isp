package com.jwbw.isp;

import java.util.List;

public class Umowa_usluga {
    private List<Object> oferta;
    private Klient nabywca;
    private Pracownik autor;

    public List<Object> getOferta() {
        return oferta;
    }

    public void setOferta(List<Object> oferta) {
        this.oferta = oferta;
    }

    public Klient getNabywca() {
        return nabywca;
    }

    public void setNabywca(Klient nabywca) {
        this.nabywca = nabywca;
    }

    public Pracownik getAutor() {
        return autor;
    }

    public void setAutor(Pracownik autor) {
        this.autor = autor;
    }
}
