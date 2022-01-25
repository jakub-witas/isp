package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum TelevisionFeatures {
    multiRoom(0, 10);

    private final int value;
    private final int price;

    TelevisionFeatures(int value, int price) {
        this.value = value;
        this.price = price;
    }

    public int getValue() {return value;}

    public int getPrice() {
        return price;
    }

    public static TelevisionFeatures getFeature(int value){
        switch (value) {
            case 0 -> {
                return TelevisionFeatures.multiRoom;
            }
            default -> {
                return null;
            }
        }}

    public static TelevisionFeatures getFeature(String value){
        switch (value) {
            case "Multiroom" -> {
                return TelevisionFeatures.multiRoom;
            }
            default -> {
                return null;
            }
        }}

    public static Optional<TelevisionFeatures> valueOf(int value){
        return Arrays.stream(values())
                .filter(additionalFeatures -> additionalFeatures.value == value)
                .findFirst();
    }

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Multiroom";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
