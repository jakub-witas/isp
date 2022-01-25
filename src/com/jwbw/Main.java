package com.jwbw;

import com.jwbw.gui.InterfaceMain;

public class Main {

    //public static ConnectionHandler connection;
    public static DatabaseInterface Proxy;


    public static void main(String[] args) {
        ApplicationLogger logger = new ApplicationLogger();
        String activeProfile = "local";

        if(args.length >= 1){
            activeProfile = args[0];
        }

        logger.activeProfile(activeProfile);

        Configuration configuration = new Configuration(activeProfile);

         new ConnectionHandler(configuration.getDatabaseUrl(),
                configuration.getDatabaseUser(),
                configuration.getDatabasePassword(),
                logger);


        InterfaceMain.Interface(args);
    }
}