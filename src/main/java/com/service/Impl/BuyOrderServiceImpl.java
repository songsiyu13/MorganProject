package com.service.Impl;

import com.dao.BuyOrderDao;
import com.entity.BuyOrder;
import com.service.BuyOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 滩涂上的芦苇 on 2017/5/23.
 */
@Service(value = "buyOrderService")
public class BuyOrderServiceImpl implements BuyOrderService{
    @Autowired
    private BuyOrderDao buyOrderDao;

    public void add(BuyOrder buyOrder){
        buyOrderDao.add(buyOrder);
    }
}
