package com.jwbw.isp;

public class    Pracownik extends Osoba{
    private String nr_umowy;
    private Role role;


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNr_umowy() { return nr_umowy; }

    public void setNr_umowy(String nr_umowy) { this.nr_umowy = nr_umowy; }
}
