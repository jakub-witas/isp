package com.jwbw;

public class Configuration {
    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;

    public Configuration(String activeProfile){
        if(activeProfile.equalsIgnoreCase("develop")){
            databaseUrl = "jdbc:postgresql://postgres:5432/isp";
        }else if(activeProfile.equalsIgnoreCase("local")){
            databaseUrl = "jdbc:postgresql://localhost:5432/isp";
        }else{
            databaseUrl = "jdbc:postgresql://localhost:5432/isp";
        }
        databaseUser = "admin";
        databasePassword = "admin";
    }

    public String getDatabaseUrl(){
        return databaseUrl;
    }

    public String getDatabaseUser(){
        return databaseUser;
    }

    public String getDatabasePassword(){
        return databasePassword;
    }

}
