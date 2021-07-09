package com.td.controller;

import com.td.dao.AgentRepository;
import com.td.dao.mapper.AgentMapper;
import com.td.model.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

@Controller
public class AgentController {

    @Autowired
    private AgentMapper agentRepository;

    @RequestMapping("/getAgentById")
    @ResponseBody
    public Object getAgentById(@RequestBody Agent agent){
        Agent agentById= agentRepository.getAgentById(agent.getId());
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
