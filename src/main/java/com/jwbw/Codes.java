package com.jwbw;

public interface Codes {
    //VERY BAD CODE
    String INTERNAL_ERROR = "-500";

    //error codes
    String WRONG_CREDENTIALS = "-1";
    String CLIENT_NOT_LOGGED = "-2";
    String CLIENT_IS_LOGGED = "-3";
    String WRONG_COMMAND = "-4";
    String DATABASE_DISCONNECTED = "-5";
    String LOGIN_EXIST = "-6";
    String NO_PERMISSION = "-7";
    String WRONG_ID = "-8";
    String NO_TASKS_FOUND = "-9";

    //good codes
    String CLIENT_CONNECTED = "0";
    String SUCCESSFUL_LOGIN = "1";
    String SUCCESSFUL_LOGOFF = "2";
    String DATABASE_CONNECTED = "3";
    String SUCCESSFUL_REGISTER = "4";

}
