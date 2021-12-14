package com.jwbw;


public class ConnectionHandler extends Thread{

    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;
    private final ServerLogger logger;

    protected DatabaseHandler databaseHandler;
    private boolean isDatabaseConnected = false;
    boolean errPromptShown = false;

    public ConnectionHandler(String databaseUrl, String databaseUser, String databasePassword, ServerLogger logger) {
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


    private void initializeDatabase(){
        databaseHandler = new DatabaseHandler(this, databaseUrl, databaseUser, databasePassword, logger);
        isDatabaseConnected = databaseHandler.isConnected();
    }

    public void setDatabaseConnectionState(boolean isDatabaseConnected){
        this.isDatabaseConnected = isDatabaseConnected;
    }

}
