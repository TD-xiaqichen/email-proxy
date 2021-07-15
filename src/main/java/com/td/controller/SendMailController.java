package com.td.controller;

import com.td.dao.mapper.EmailMapper;
import com.td.imap.SimpleStoreMails;
import com.td.imap.StoreMailExtend;
import com.td.model.Emailexd;
import com.td.service.SendEmailService;
import com.td.util.HTMLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SendMailController {

    @Resource
    private SendEmailService sendEmailService;

    @Resource
    private SimpleStoreMails simpleStoreMails;

    @Resource
    private StoreMailExtend storeMailExtend;

    @Resource
    private EmailMapper emailMapper;

       @RequestMapping("/sendEmail")
       @ResponseBody
       public Object sendEmail(@RequestBody Emailexd email){
           Map map = new HashMap<>();

           try {
               sendEmailService.send(email);
               map.put("status","200");
               map.put("msg","OK");
           } catch (Exception e) {
               map.put("status","500");
               map.put("msg","send email error");
               e.printStackTrace();
           }
           return map;
       }

    @RequestMapping("/imapMessages")
    @ResponseBody
      public Object listReceiveMessages(){
//        simpleStoreMails.saveImapMessages();
        try {
            storeMailExtend.saveImapMessages();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
      }

    @RequestMapping("/findListMails")
    @ResponseBody
      public Object findListMails(){
          List<Emailexd> list = emailMapper.findList();

          return list;
      }

    @RequestMapping("/detailMessage")
    @ResponseBody
      public Object detailMessage(@RequestBody Emailexd email){
        String id = email.getId();
        String messageId = email.getMessageId();
        messageId= messageId.replaceAll("\"","");
        Emailexd emailexd = emailMapper.getMessageById(messageId);
        String s = HTMLUtil.removeHtmlTag(emailexd.getContent());
        emailexd.setContent(s);
        return emailexd;
      }

}
