package com.jwbw.isp;

import java.util.Arrays;
import java.util.Optional;

public enum Role {
    OWNER(0),
    JUNIOR_SPECIALIST(1),
    MID_SPECIALIST(2),
    SENIOR_SPECIALIST(3),
    CLIENT(4),
    ACCOUNTANT(5),
    OFFICE_WORKER(6),
    ;

    private final int value;

    Role(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Role getRole(int value){
        switch (value) {
            case 0 -> {
                return Role.OWNER;
            }
            case 1 -> {
                return Role.JUNIOR_SPECIALIST;
            }
            case 4 -> {
                return Role.CLIENT;
            }
            case 5 -> {
                return Role.ACCOUNTANT;
            }
            case 6 -> {
                return Role.OFFICE_WORKER;
            }
            case 2 -> {
                return Role.MID_SPECIALIST;
            }
            case 3 -> {
                return Role.SENIOR_SPECIALIST;
            }
            default -> {
                return null;
            }
    }}

    public static Optional<Role> valueOf(int value){
        return Arrays.stream(values())
                .filter(role -> role.value == value)
                .findFirst();
    }

    public String getName(){

        switch (value) {
            case 0 -> {
                return "Właściciel";
            }
            case 1 -> {
                return "Młodszy specjalista";
            }
            case 4 -> {
                return "Klient";
            }
            case 5 -> {
                return "Księgowy";
            }
            case 6 -> {
                return "Pracownik biurowy";
            }
            case 2 -> {
                return "Specjalista";
            }
            case 3 -> {
                return "Starszy specjalista";
            }
            default -> {
                return "UNKNOWN";
            }
        }
    }
}
