package com.controlor;

import com.entity.BuyOrder;
import com.service.BuyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;

/**
 * Created by Song on 2017/5/23.
 */
@Controller
public class Welcome {
    @Autowired
    private BuyOrderService buyOrderService;

    @RequestMapping("/")
    public String welcome()
    {
        return "index";
    }

    @RequestMapping("/test")
    public String test()
    {
        BuyOrder buyOrder = new BuyOrder();
        buyOrder.setOrderType(0);
        buyOrder.setCompanyName("111");
        buyOrder.setGoodsDate("201409");
        buyOrder.setGoodsName("ddd");
        buyOrder.setPrice(new BigDecimal(3.3));
        buyOrderService.add(buyOrder);
        return "index";
    }
}
