package com.company;

import java.util.Properties;

public class DBcredentials {
    //Sets the Database Credentials
    public static void setIdentity(Properties prop) {
        prop.setProperty("database", "class_3901");
        prop.setProperty("user", "root");
        prop.setProperty("password", "");
    }
}
