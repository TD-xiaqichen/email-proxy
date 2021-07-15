package com.td.common;

import com.sun.mail.imap.IMAPMessage;
import com.td.model.Emailexd;
import org.springframework.stereotype.Component;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConvertImapMessage {

        public Emailexd changeImap2Model(IMAPMessage imapMessage) throws MessagingException, IOException {
            Emailexd emailexd = new Emailexd();
            emailexd.setBoxType("INBOUND");
            String subject = imapMessage.getSubject();
            emailexd.setSubject(subject);
            Address[] from = imapMessage.getFrom();
            Address address = from[0];
            InternetAddress internetAddress = (InternetAddress) address;
            String fromStr = internetAddress.getAddress();
            emailexd.setFrom(fromStr);
            Address[] recipients = imapMessage.getRecipients(Message.RecipientType.TO);
            String to = getRecipientStr(recipients);
            emailexd.setTo(to);
            Address[] recipients1 = imapMessage.getRecipients(Message.RecipientType.CC);
            String cc = getRecipientStr(recipients1);
            emailexd.setContent(cc);
            Address[] recipients2 = imapMessage.getRecipients(Message.RecipientType.BCC);
            String bcc = getRecipientStr(recipients2);
            emailexd.setBcc(bcc);
            Map<String, String> map = getHtml(imapMessage);
            String htmlContent = map.get("htmlContent");
            emailexd.setContent(htmlContent);
           // String date = imapMessage.getHeader("Date", ",");
            Date sentDate = imapMessage.getSentDate();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(sentDate);
            emailexd.setSentDate(date);
            String messageId = imapMessage.getHeader("Message-Id", ",");
            emailexd.setMessageId(messageId);
            return emailexd;
        }

      private String getRecipientStr(Address[] addresses){
            if(addresses==null || addresses.length==0){
                return "";
            }
            StringBuffer sb = new StringBuffer();
            for(Address m:addresses){
                InternetAddress internetAddress = (InternetAddress) m;
                sb.append(","+internetAddress.getAddress());
            }
            return sb.toString().substring(1);
      }

    public  Map<String,String> getHtml(IMAPMessage imapMessage) throws MessagingException, IOException {
        Map<String,String> map = new HashMap<>();
        Object content = imapMessage.getContent();
        if(content instanceof String){
            map.put("htmlContent",(String) content);
        }
       else  if(content instanceof MimeMultipart){
            MimeMultipart multipart = (MimeMultipart) content;
            int count = multipart.getCount();
            for(int i=0;i<count;i++){
                BodyPart bodyPart = multipart.getBodyPart(i);
                ContentType ct = new ContentType(bodyPart.getDataHandler().getContentType());
                boolean match1 = ct.match("text/plain");
                boolean match2 = ct.match("text/html");
                boolean isTextBody = match1 || match2;
                if(match1){
                    map.put("content",(String)bodyPart.getContent());
                }
                if(match2){
                    map.put("htmlContent",(String)bodyPart.getContent());
                }
            }
        }
        return map;
    }

}
