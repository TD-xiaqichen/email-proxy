package com.td.controller;

import com.alibaba.fastjson.JSON;
import com.td.dao.AgentRepository;
import com.td.dao.mapper.AgentMapper;
import com.td.model.Agent;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jms.*;
import javax.validation.Payload;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

@Controller
public class AgentController {

    @Autowired
    private AgentMapper agentRepository;

    //注入存放消息的队列，用于下列方法一
    @Autowired
    private Queue queue;

    //注入springboot封装的工具类
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;


    @RequestMapping("/getAgentById")
    @ResponseBody
    public Object getAgentById(@RequestBody Agent agent){
        Agent agentById= agentRepository.getAgentById(agent.getId());

        String requestJson= JSON.toJSONString(agent);
        try {

            jmsMessagingTemplate.convertAndSend(queue,requestJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if(agentById!=null){
            return agentById;
        }
        return new Agent();
    }

    @RequestMapping("/getAgentByAccount")
    @ResponseBody
    public Object getAgentByAccount(@RequestBody Agent agent){
        Agent agentByAccount = agentRepository.getAgentByAccount(agent.getAccount());
        if(agentByAccount!=null){
            return agentByAccount;
        }
        return null;
    }

    @RequestMapping("/getAgentByEmail")
    @ResponseBody
    public Object getAgentByEmail(@RequestBody Agent agent){
        Agent agentByEmail = agentRepository.getAgentByEmail(agent.getEmail());
        if(agentByEmail!=null){
            return agentByEmail;
        }
        return new Agent();
    }

    @RequestMapping("/listAgent")
    @ResponseBody
    public Object getAgentList(){
        List<Agent> agents = agentRepository.agentList();
        if(agents!=null&&agents.size()>0){
            return agents;
        }
        return new ArrayList<Agent>();
    }

    @RequestMapping("/saveAgent")
    @ResponseBody
    public Object saveAgent(@RequestBody Agent agent){
        Map map = new HashMap();
        try {
            agentRepository.saveAgent(agent);
            map.put("status","200");
            map.put("msg","OK");
        }catch (Exception e){
            map.put("status",500);
            map.put("msg","saveAgent error");
        }
        return map;
    }

}
