package com.techelevator.tenmo.services;

import java.util.Properties;

public class EmailService {
    private final String FROM_EMAIL = "TEnmo@Henteleff.com";
    private final String HOST = "";

    public String sendEmail(String subject, String body, String emailAddress){
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
       // Session session = Session.getDefaultInstance(properties);
        return null;
    }
}
