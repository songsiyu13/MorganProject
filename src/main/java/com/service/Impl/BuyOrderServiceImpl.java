package com.service.Impl;

import com.dao.BuyOrderDao;
import com.entity.Order;
import com.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 滩涂上的芦苇 on 2017/5/23.
 */
@Service(value = "buyOrderService")
public class BuyOrderServiceImpl implements OrderService{
    @Autowired
    private BuyOrderDao buyOrderDao;

    public void add(Order buyOrder){
        buyOrderDao.add(buyOrder);
    }

}
