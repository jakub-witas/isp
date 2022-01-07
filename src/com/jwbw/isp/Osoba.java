package com.jwbw.isp;

abstract class Osoba {
    protected int id;
    protected String name;
    protected String surname;
    protected String phone;
    protected String mail;
    protected String pesel;
    protected String city;
    protected String street;
    protected String home_number;
    protected String code;
    protected String id_card;

    public Integer getId() { return id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPesel() { return pesel; }

    public void setPesel(String Pesel) { this.pesel = Pesel; }

    public String getCity() { return city; }

    public void setCity(String City) { this.city = City; }

    public String getStreet() { return street; }

    public void setStreet(String Street) { this.street = Street; }

    public String getHome_number() { return home_number; }

    public void setHome_number(String Home_number) { this.home_number = Home_number; }

    public String getCode() { return code; }

    public void setCode(String Code) { this.code = Code; }

    public String getId_card() { return id_card; }

    public void setId_card(String Id_card) { this.id_card = Id_card; }
}
