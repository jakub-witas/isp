package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum InternetFeatures {
    publicIp(0, 15);

    private final int value;
    private final int price;

    InternetFeatures(int value, int price) {
        this.value = value;
        this.price = price;
    }

    public int getValue() {return value;}

    public int getPrice() {
        return price;
    }

    public static InternetFeatures getFeature(int value){
        switch (value) {
            case 0 -> {
                return InternetFeatures.publicIp;
            }
            default -> {
                return null;
            }
        }}

    public static InternetFeatures getFeature(String value){
        switch (value) {
            case "Publiczne IP" -> {
                return InternetFeatures.publicIp;
            }
            default -> {
                return null;
            }
        }}

    public static Optional<InternetFeatures> valueOf(int value){
        return Arrays.stream(values())
                .filter(additionalFeatures -> additionalFeatures.value == value)
                .findFirst();
    }

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Publiczne IP";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
