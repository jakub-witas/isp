package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum TelephoneFeatures {
    roaming(0, 10);

    private final int value;
    private final int price;

    TelephoneFeatures(int value, int price) {
        this.value = value;
        this.price = price;
    }

    public int getValue() {return value;}

    public int getPrice() {
        return price;
    }

    public static TelephoneFeatures getFeature(int value){
        switch (value) {
            case 0 -> {
                return TelephoneFeatures.roaming;
            }
            default -> {
                return null;
            }
        }}

    public static TelephoneFeatures getFeature(String value){
        switch (value) {
            case "Roaming" -> {
                return TelephoneFeatures.roaming;
            }
            default -> {
                return null;
            }
        }}

    public static Optional<TelephoneFeatures> valueOf(int value){
        return Arrays.stream(values())
                .filter(additionalFeatures -> additionalFeatures.value == value)
                .findFirst();
    }

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Roaming";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
