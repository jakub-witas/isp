package com.jwbw.isp;

import java.util.List;

public class User extends Osoba{
    List<Dokument> dokumenty;
    List<Urzadzenie> posiadane_urzadzenia;
    Role role;


    public List<Dokument> getDokumenty() {
        return dokumenty;
    }

    public void setDokumenty(List<Dokument> dokumenty) {
        this.dokumenty = dokumenty;
    }

    public List<Urzadzenie> getPosiadane_urzadzenia() {
        return posiadane_urzadzenia;
    }

    public void setPosiadane_urzadzenia(List<Urzadzenie> posiadane_urzadzenia) {
        this.posiadane_urzadzenia = posiadane_urzadzenia;
    }

    public void setRole(Role role) {this.role = role;}

    public Role getRole(){return role;}
}
