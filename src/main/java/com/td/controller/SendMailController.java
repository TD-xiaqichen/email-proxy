package com.td.controller;

import com.td.imap.SimpleStoreMails;
import com.td.model.Emailexd;
import com.td.service.SendEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SendMailController {

    @Resource
    private SendEmailService sendEmailService;

    @Resource
    private SimpleStoreMails simpleStoreMails;

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
        simpleStoreMails.saveImapMessages();
           return null;
      }

}
