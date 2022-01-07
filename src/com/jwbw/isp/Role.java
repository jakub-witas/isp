package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    OWNER(0),
    SPECIALIST(1),
    CLIENT(2),
    ACCOUNTANT(3),
    OFFICE_WORKER(4);

    private final int value;

    Role(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Optional<Role> valueOf(int value){
        return Arrays.stream(values())
                .filter(role -> role.value == value)
                .findFirst();
    }

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Owner";
            }
            case 1 -> {
                return "Specialist";
            }
            case 2 -> {
                return "Client";
            }
            case 3 -> {
                return "Accountant";
            }
            case 4 -> {
                return "Office worker";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
