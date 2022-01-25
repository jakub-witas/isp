package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum AdditionalFeatures {
    publicIp(0, 1, 15),
    multiRoom(1, 2, 10),
    roaming(2, 3, 10);

    private final int value;
    private final int packet;
    private final int price;

    AdditionalFeatures(int value, int packet, int price) {
        this.value = value;
        this.packet = packet;
        this.price = price;
    }

    public int getValue() {return value;}

    public int getPacket() {
        return packet;
    }

    public int getPrice() {
        return price;
    }

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

    public static AdditionalFeatures getFeature(String value){
        switch (value) {
            case "Publiczne IP" -> {
                return AdditionalFeatures.publicIp;
            }
            case "Multiroom" -> {
                return AdditionalFeatures.multiRoom;
            }
            case "Roaming" -> {
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
                return "Publiczne IP";
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
