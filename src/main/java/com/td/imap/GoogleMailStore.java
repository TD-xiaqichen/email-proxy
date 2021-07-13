package com.td.imap;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import org.apache.commons.mail.util.MimeMessageParser;

import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class GoogleMailStore {

       public static void main(String[] args){
           IMAPFolder folder = null;

           Store store = null;

           String subject = null;

           Flags.Flag flag = null;

           Properties props = System.getProperties();

           props.setProperty("mail.store.protocol", "imaps");

           Session session = Session.getDefaultInstance(props, null);
           try {
               store = session.getStore("imaps");
               store.connect("imap.googlemail.com",993,"xqichen233@gmail.com", "aafiouhnkealnwyb");
               folder = (IMAPFolder) store.getFolder("inbox");
               folder.open(Folder.READ_ONLY);
               Message[] messages = folder.getMessages();
               /*
               for (int i = 0; i < folder.getMessageCount(); i++){
                   Message message = messages[i];
                   Date date = message.getSentDate();
                   IMAPMessage imapMessage = (IMAPMessage) message;
                   MimeMessageParser mimeMessageParser = new MimeMessageParser(imapMessage);
                   String htmlContent = mimeMessageParser.getHtmlContent();
                   System.out.println(htmlContent);
                   System.out.println("Mail Subject:- " + message.getSubject());
                   System.out.println("Mail Content Type:- " + message.getContentType());
                   System.out.println("Mail Sent Date:- " + date);
               }
             */
               IMAPMessage imapMessage = (IMAPMessage) messages[messages.length-1];
//               InputStream mimeStream = imapMessage.getMimeStream();
//               MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(new Properties()),mimeStream);
//               MimeMessageParser parser = new MimeMessageParser(mimeMessage);
//               String htmlContent = parser.getHtmlContent();

               Object content = imapMessage.getContent();
               MimeMultipart multipart = (MimeMultipart) content;
               int count = multipart.getCount();
               for(int i=0;i<count;i++){
                   BodyPart bodyPart = multipart.getBodyPart(i);
                   ContentType ct = new ContentType(bodyPart.getDataHandler().getContentType());
                   boolean match1 = ct.match("text/plain");
                   boolean match2 = ct.match("text/html");
                   boolean isTextBody = match1 || match2;
                   if(isTextBody){
                       String partContent = (String) bodyPart.getContent();
                       System.out.println(partContent);
                   }
               }
               System.out.println(content);
               folder.close(true);
               store.close();
           } catch (NoSuchProviderException e) {
               e.printStackTrace();
           } catch (MessagingException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }

       public static Map<String,String> getHtml(IMAPMessage imapMessage) throws MessagingException, IOException {
           Map<String,String> map = new HashMap<>();
           Object content = imapMessage.getContent();
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
           return map;
       }

}
