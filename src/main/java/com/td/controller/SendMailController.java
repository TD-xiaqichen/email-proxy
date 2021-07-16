package com.td.controller;

import com.td.dao.mapper.AgentMapper;
import com.td.dao.mapper.EmailMapper;
import com.td.imap.SimpleStoreMails;
import com.td.imap.StoreMailExtend;
import com.td.model.Agent;
import com.td.model.Emailexd;
import com.td.service.SendEmailService;
import com.td.service.receive.FetchRecentEmail;
import com.td.util.HTMLUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.IOException;
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

    @Resource
    private AgentMapper agentMapper;

    @Resource
    private FetchRecentEmail fetchRecentEmail;

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

      @RequestMapping("/refreshUnseenMessage")
      @ResponseBody
      public Object refreshUnseenMessage(@RequestBody Agent agent){
          String email = agent.getEmail();
          Agent obj = agentMapper.getAgentByEmail(email);
          String password = obj.getPassword();
          try {
              List<Emailexd> emailexds = fetchRecentEmail.saveRecentMessage(email, password);
              return emailexds;
          } catch (MessagingException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          }
          return null;
      }

}
