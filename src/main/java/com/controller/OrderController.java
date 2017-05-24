package com.controller;

import com.entity.Order;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by 滩涂上的芦苇 on 2017/5/24.
 */
@Controller
public class OrderController {
    @Autowired
    private OrderService buyOrderService;

    public void executeBuyOrder(Order buyOrder)
    {

    }
}
