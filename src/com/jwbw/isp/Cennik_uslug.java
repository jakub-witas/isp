package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum Cennik_uslug {
    Diagnoza(0, 50),
    Czyszczenie(1, 30),
    WymianaCzesci(2, 60),
    Przelutowanie(3, 100);

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
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
