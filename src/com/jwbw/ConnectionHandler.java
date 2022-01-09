package com.jwbw;


import com.jwbw.isp.Klient;
import com.jwbw.isp.Pracownik;

public class ConnectionHandler extends Thread{

    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;
    private final ApplicationLogger logger;

    public DatabaseHandler databaseHandler;
    private boolean isDatabaseConnected = false;
    boolean errPromptShown = false;

    public ConnectionHandler(String databaseUrl, String databaseUser, String databasePassword, ApplicationLogger logger) {
        this.logger = logger;
        this.databaseUrl = databaseUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
        this.start();
    }

    @Override
    public void run() {
        initializeDatabase();

    }

    public void userLoggedOff(Object user) {
        if(user.getClass() == Pracownik.class) {
            this.logger.userLoggedOff(((Pracownik) user).getId());
        } else {
            this.logger.userLoggedOff(((Klient) user).getId());
        }
    }

    private void initializeDatabase(){
        databaseHandler = new DatabaseHandler(this, databaseUrl, databaseUser, databasePassword, logger);
        isDatabaseConnected = databaseHandler.isConnected();
    }

    public void setDatabaseConnectionState(boolean isDatabaseConnected){
        this.isDatabaseConnected = isDatabaseConnected;
    }

}
