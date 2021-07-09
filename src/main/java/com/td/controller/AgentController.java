package com.td.controller;

import com.td.dao.AgentRepository;
import com.td.dao.mapper.AgentMapper;
import com.td.model.Agent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

@Controller
public class AgentController {

    @Autowired
    private AgentMapper agentRepository;

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
