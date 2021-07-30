package com.td.anotationbean;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

@Configuration
public class BeanConfig {

    //定义存放消息的队列
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("ActiveMQQueue");
    }

    @Bean
    public BrokerService startMQ(){
        String serviceURL = "tcp://localhost:61616";
        /**BrokerService 表示 ActiveMQ 服务，每一个 BrokerService 表示一个消息服务器实例
         * 如果想启动多个，只需要 start 多个不同端口的 BrokerService 即可*/
        BrokerService brokerService = new BrokerService();
        brokerService.setUseJmx(true);//设置是否应将代理的服务公开到jmx中。默认是 true
        try {
            brokerService.addConnector(serviceURL);//为指定地址添加新的传输连接器
            /**启动 ActiveMQ 服务，此时客户端便可以使用提供的地址进行连接，然后发送消息过来，或者从这里消费消息。
             * 注意：这里内嵌启动后，默认是没有提供 8161 端口的 web 管理界面的，照样能做消息中间件使用*/
            brokerService.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("启动内嵌 ActiveMQ 服务器完成......");
         return brokerService;
    }

}
