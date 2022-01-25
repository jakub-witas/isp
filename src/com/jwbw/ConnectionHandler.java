package com.jwbw;

import com.jwbw.isp.User;

public class ConnectionHandler extends Thread{

    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;
    private final ApplicationLogger logger;

    private DatabaseHandler databaseHandler;
    private boolean isDatabaseConnected = false;

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
        this.logger.userLoggedOff(((User) user).getId());
    }

    private void initializeDatabase(){
        databaseHandler = new DatabaseHandler(this, databaseUrl, databaseUser, databasePassword, logger);
        isDatabaseConnected = databaseHandler.isConnected();
        Proxy.Database = databaseHandler;
    }

    public void setDatabaseConnectionState(boolean isDatabaseConnected){
        this.isDatabaseConnected = isDatabaseConnected;
    }
}
