package com.jwbw.isp;

import java.util.List;

public class Klient extends Osoba{
    List<Object> dokumenty;
    List<Urzadzenie_sieciowe> posiadane_urzadzenia;


    public List<Object> getDokumenty() {
        return dokumenty;
    }

    public void setDokumenty(List<Object> dokumenty) {
        this.dokumenty = dokumenty;
    }

    public List<Urzadzenie_sieciowe> getPosiadane_urzadzenia() {
        return posiadane_urzadzenia;
    }

    public void setPosiadane_urzadzenia(List<Urzadzenie_sieciowe> posiadane_urzadzenia) {
        this.posiadane_urzadzenia = posiadane_urzadzenia;
    }
}
