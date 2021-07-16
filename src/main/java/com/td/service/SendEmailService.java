package com.td.service;

import com.td.dao.mapper.AgentMapper;
import com.td.model.Agent;
import com.td.model.Emailexd;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class SendEmailService {

    @Resource
    private AgentMapper agentMapper;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public void send(Emailexd email) throws Exception{
        setMailServerProperties();
        createEmailMessage(email);
        sendEmail(email);
    }

    public void setMailServerProperties() {

        String emailPort = "587";// gmail's smtp port
        String emailPort2 = "465";
        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
//        emailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    }

    public void createEmailMessage(Emailexd emailexd) throws AddressException,
            MessagingException
    {
        String to = emailexd.getTo();
      //  String[] toEmails = { "jiali.xiang@china.talkdesk.com" };
        String[] toEmails = to.split(",");
      //  String emailSubject = "Java Email from qichen.xia@china.talkdesk.com";
        String emailSubject =emailexd.getSubject();

       // String emailBody = "This is an email sent by <b>JavaMail</b> api.";
        String emailBody = emailexd.getContent();

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++)
        {
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");
        // for a html email
    }

    public void sendEmail(Emailexd emailexd) throws AddressException, MessagingException
    {
        String emailHost = "smtp.gmail.com";
      //  String fromUser = "qichen.xia@china.talkdesk.com";
        String fromUser = emailexd.getFrom();
        // just the id alone without
        Agent agentByEmail = agentMapper.getAgentByEmail(fromUser);
        // @gmail.com
       // String fromUserEmailPassword = "hubthugrweccyoqy";
        String fromUserEmailPassword = agentByEmail.getPassword();

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        System.out.println("Email sent successfully.");
    }

}
