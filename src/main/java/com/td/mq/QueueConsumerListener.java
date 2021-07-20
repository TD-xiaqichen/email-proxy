package com.td.mq;

import com.td.model.Agent;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class QueueConsumerListener {

    @JmsListener(destination = "ActiveMQQueue")
     public void readActiveQueue(String agent){
         System.out.println(agent);
     }
}
