package com.jwbw.isp;

import java.util.*;
import java.util.stream.Stream;

public enum Cennik_uslug {
    Diagnoza(0, 50),
    Czyszczenie(1, 30),
    WymianaCzesci(2, 60),
    Przelutowanie(3, 100),
    System(4, 70);

    private final int value;
    private final int price;

    Cennik_uslug(int value, int price){
        this.value = value;
        this.price = price;
    }

    public int getValue() {
        return value;
    }
    public int getPrice() {return price;}

    public static Optional<Cennik_uslug> valueOf(int value){
        return Arrays.stream(values())
                .filter(cennikUslug -> cennikUslug.value == value)
                .findFirst();
    }

    public static List<Cennik_uslug> getValuesList() {
        return new ArrayList<>(EnumSet.allOf(Cennik_uslug.class));
    }

    public static Cennik_uslug getService(int value){
        switch (value) {
            case 0 -> {
                return Cennik_uslug.Diagnoza;
            }
            case 1 -> {
                return Cennik_uslug.Czyszczenie;
            }
            case 2 -> {
                return Cennik_uslug.WymianaCzesci;
            }
            case 3 -> {
                return Cennik_uslug.Przelutowanie;
            }
            case 4 -> {
                return Cennik_uslug.System;
            }
            default -> {
                return null;
            }
        }}

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Diagnoza";
            }
            case 1 -> {
                return "Czyszczenie";
            }
            case 2 -> {
                return "Wymiana Czesci";
            }
            case 3 -> {
                return "Przelutowanie elementu";
            }
            case 4 -> {
                return "Instalacja systemu";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
