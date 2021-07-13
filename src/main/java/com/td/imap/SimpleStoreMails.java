package com.td.imap;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;
import com.td.common.ConvertImapMessage;
import com.td.dao.mapper.EmailMapper;
import com.td.model.Emailexd;
import org.apache.commons.mail.util.MimeMessageParser;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Component
public class SimpleStoreMails {

     @Resource
     private ConvertImapMessage convertImapMessage;

     @Resource
     private EmailMapper emailMapper;

     public void saveImapMessages(){
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
             List<Emailexd> emailexdList = new ArrayList<>();
             for (int i = 0; i < folder.getMessageCount(); i++){
               Message message = messages[i];
               Date date = message.getSentDate();
               IMAPMessage imapMessage = (IMAPMessage) message;
               Emailexd emailexd = convertImapMessage.changeImap2Model(imapMessage);
               emailexdList.add(emailexd);
             }
             folder.close(true);
             store.close();
             emailMapper.saveEmail(emailexdList);
         } catch (NoSuchProviderException e) {
             e.printStackTrace();
         } catch (MessagingException e) {
             e.printStackTrace();
         } catch (Exception e) {
             e.printStackTrace();
         }
     }

}
