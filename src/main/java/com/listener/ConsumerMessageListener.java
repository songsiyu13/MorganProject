package com.listener;

import com.entity.Order;
import com.entity.OrderMessage;
import com.service.OrderPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * Created by 滩涂上的芦苇 on 2017/5/24.
 */
@Component
public class ConsumerMessageListener implements MessageListener {
    private OrderPool orderPool;

    @Autowired
    public void setOrderPool(OrderPool orderPool){this.orderPool = orderPool;}

    public void onMessage(Message message)
    {
        ObjectMessage o = (ObjectMessage)message;
        try {
            OrderMessage orderMessage = (OrderMessage) o.getObject();
            Order order = new Order(orderMessage);
            if(orderMessage.getS_type() == 0)
            {
                //buy
                orderPool.newBuyOrder(order,orderMessage.getS_type());
            }
            else
            {
                //sell
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
