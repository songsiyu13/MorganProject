/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.producer;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

/**
 *
 * @author lycronaldo
 */
@Component
public class BrokerSender{
    private JmsTemplate jmsTemplate;
    @Resource
    public void setJmsTemplate(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }
    
    public JmsTemplate getJmsTemplate()
    {
        return this.jmsTemplate;
    }

    public void sendMessage(Destination destination, final Object o) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage((Serializable) o);
            }
        });
    }
}
