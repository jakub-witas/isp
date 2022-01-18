package com.jwbw.isp;

import java.util.ArrayList;
import java.util.List;

public class Director {
    public void constructWpis(Builder builder, Object autor, String opis) {
        builder.setType("wpis");
        builder.setOdbiorca(null);
        builder.setAutor(autor);
        builder.setOpis(opis);
    }

    public void constructPowiadomienie(Builder builder, Object autor, String opis, Object odbiorca) {
        builder.setType("powiadomienie");
        builder.setAutor(autor);
        builder.setOpis(opis);
        builder.setOdbiorca(odbiorca);
        builder.setWasRead();
    }
}
