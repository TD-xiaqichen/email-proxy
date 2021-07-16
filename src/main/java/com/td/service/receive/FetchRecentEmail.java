package com.td.service.receive;

import com.sun.mail.imap.IMAPMessage;
import com.td.common.ConvertImapMessage;
import com.td.dao.mapper.EmailMapper;
import com.td.model.Emailexd;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class FetchRecentEmail {

    @Resource
    private ConvertImapMessage convertImapMessage;

    @Resource
    private EmailMapper emailMapper;

    public List<Emailexd> saveRecentMessage(String email,String password) throws MessagingException, IOException {
        Properties props = new Properties();
        props.setProperty("mail.store.protocol","imaps");
//        Session session = Session.getDefaultInstance(new Properties());
        Session session = Session.getDefaultInstance(props,null);
        Store store = session.getStore("imaps");
        store.connect("imap.googlemail.com", 993, email, password);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] messages = inbox.search(
                new FlagTerm(new Flags(Flags.Flag.SEEN), false));

        Arrays.sort(messages, (m1, m2) -> {
                    int i = 0;
                    try {
                        i = m2.getSentDate().compareTo(m1.getSentDate());
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                    return i;
                }
        );

        List<Emailexd> emailexdList = new ArrayList<>();
        if(messages.length>0){
            for (int i = 0; i < messages.length; i++){
                Message message = messages[i];
                IMAPMessage imapMessage = (IMAPMessage) message;
                Emailexd emailexd = convertImapMessage.changeImap2Model(imapMessage);
                emailexdList.add(emailexd);
            }
        }
        inbox.close(true);
        store.close();
        if(emailexdList.size()>0){
          //  emailMapper.saveEmail(emailexdList);
            emailMapper.saveBatch(emailexdList);
        }
        List<Emailexd> list = emailMapper.findList();
        return list;
    }

}
