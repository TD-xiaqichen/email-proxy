package com.td.common;

import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.util.Arrays;
import java.util.Properties;

public class UnseenMessage {

    public static void main(String[] args) throws Exception {
        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("imaps");
        store.connect("imap.googlemail.com", 993, "xqichen233@gmail.com", "aafiouhnkealnwyb");
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

        System.out.println(messages);
    }

}
