package com.techelevator.tenmo.services;

import java.util.Properties;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailService {
    private final String FROM_EMAIL = "Tenmo@Henteleff.com";
    private final String HOST = "smtp.hostinger.com";

    public String sendEmail(String subject, String body, String emailAddress){
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", HOST);
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");

        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, "123TEnmo");
            }
        };

        try{

            Session session = Session.getInstance(properties, authenticator);


            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress(emailAddress));
            message.setSubject(subject);
            message.setText(body);


            Transport.send(message, FROM_EMAIL, "123TEnmo");
            return "message sent successfully.";

        }catch (MessagingException mex) {
            return mex.getMessage();
        }

    }
}
