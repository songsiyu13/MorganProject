package com.controller;

import com.producer.BrokerSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.jms.Destination;

/**
 * Created by Song on 2017/5/23.
 */
@Controller
public class Welcome {

    @RequestMapping("/")

    public String welcome()
    {
        return "index";
    }
}
