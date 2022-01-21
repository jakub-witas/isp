package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum AdditionalFeatures {
    publicIp(0),
    multiRoom(1),
    roaming(2);

    private final int value;

    AdditionalFeatures(int value) {this.value = value;}

    public int getValue() {return value;}

    public static AdditionalFeatures getFeature(int value){
        switch (value) {
            case 0 -> {
                return AdditionalFeatures.publicIp;
            }
            case 1 -> {
                return AdditionalFeatures.multiRoom;
            }
            case 2 -> {
                return AdditionalFeatures.roaming;
            }
            default -> {
                return null;
            }
        }}

    public static Optional<AdditionalFeatures> valueOf(int value){
        return Arrays.stream(values())
                .filter(additionalFeatures -> additionalFeatures.value == value)
                .findFirst();
    }

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Public IP";
            }
            case 1 -> {
                return "Multiroom";
            }
            case 2 -> {
                return "Roaming";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
