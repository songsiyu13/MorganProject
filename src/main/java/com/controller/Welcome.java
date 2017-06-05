package com.controller;

import com.entity.MarketDepth;
import com.entity.Order;
import com.service.OrderPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Song on 2017/5/23.
 */
@Controller
public class Welcome {
    @Autowired
    private OrderPool orderPool;

    @RequestMapping("/")

    public String welcome()
    {
        return "index";
    }

    @RequestMapping("/marketDepth")
    public String viewMarketDepth(HttpServletRequest request, @RequestParam(value = "goodsName") String goodsName,@RequestParam(value = "goodsDate") String goodsDate)
    {
        Order order= new Order();
        goodsName = goodsName.toLowerCase();
        order.setGoodsName(goodsName);
        order.setGoodsDate(goodsDate);
        MarketDepth marketDepth = orderPool.getMarketDepth(order);
        request.setAttribute("marketDepth",marketDepth);
        request.setAttribute("name",goodsDate.concat(" ").concat(goodsName));
        return "marketDepth";
    }

    @RequestMapping("/transactions")
    public String viewOrderItem(@RequestParam(value = "goodsName") String goodsName,@RequestParam(value = "goodsDate") String goodsDate)
    {

        return "index";
    }
}
