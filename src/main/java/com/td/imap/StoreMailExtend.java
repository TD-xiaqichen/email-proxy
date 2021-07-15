package com.td.imap;

import com.sun.mail.imap.IMAPMessage;
import com.td.common.ConvertImapMessage;
import com.td.dao.mapper.EmailMapper;
import com.td.model.Emailexd;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.*;

@Component
public class StoreMailExtend {

    @Resource
    private ConvertImapMessage convertImapMessage;

    @Resource
    private EmailMapper emailMapper;

    public void saveImapMessages() throws Exception{
        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("imaps");
        store.connect("imap.googlemail.com", 993, "xqichen233@gmail.com", "aafiouhnkealnwyb");
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);
        Message[] messages = inbox.search(
                new FlagTerm(new Flags(Flags.Flag.SEEN), true));
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
        for (int i = 0; i < inbox.getMessageCount(); i++){
            Message message = messages[i];
            IMAPMessage imapMessage = (IMAPMessage) message;
            Emailexd emailexd = convertImapMessage.changeImap2Model(imapMessage);
            emailexdList.add(emailexd);
        }
        inbox.close(true);
        store.close();
        emailMapper.saveEmail(emailexdList);

    }

}
