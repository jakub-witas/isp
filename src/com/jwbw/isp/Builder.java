package com.jwbw.isp;


public interface Builder {
    void setType(String type);
    void setOpis(String opis);
    void setOdbiorca(User odbiorca);
    void setAutor(User autor);
    void setWasRead();
}
