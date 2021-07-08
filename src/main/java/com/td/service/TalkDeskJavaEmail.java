package com.td.service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class TalkDeskJavaEmail {

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public static void main(String args[])
    {

        TalkDeskJavaEmail javaEmail = new TalkDeskJavaEmail();

        javaEmail.setMailServerProperties();
        try
        {
            javaEmail.createEmailMessage();
            javaEmail.sendEmail();
        }
        catch (AddressException e)
        {
            System.out.println("Address Exception:" + e.getMessage());
            e.printStackTrace();
        }
        catch (MessagingException e)
        {
            System.out.println("Message Exception:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setMailServerProperties()
    {

        String emailPort = "587";// gmail's smtp port

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");

    }

    public void createEmailMessage() throws AddressException,
            MessagingException
    {
      //  String[] toEmails = { "qichen.xia@china.talkdesk.com" };
        String[] toEmails = { "jiali.xiang@china.talkdesk.com" };
        String emailSubject = "Java Email from xqichen233@gmail.com";
        String emailBody = "This is an email sent by <b>JavaMail</b> api.";

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++)
        {
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email

    }

    public void sendEmail() throws AddressException, MessagingException
    {
        String emailHost = "smtp.gmail.com";
        String fromUser = "qichen.xia@china.talkdesk.com";// just the id alone without
        // @gmail.com
        String fromUserEmailPassword = "hubthugrweccyoqy";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }


}
