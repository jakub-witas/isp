package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum Cennik_uslug {
    Diagnoza(0),
    Czyszczenie(1),
    WymianaCzesci(2),
    Przelutowanie(3);

    private final int value;

    Cennik_uslug(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

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
