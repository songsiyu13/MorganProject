package com.service;

import com.dao.BuyOrderDao;
import com.dao.SellOrderDao;
import com.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 滩涂上的芦苇 on 2017/5/24.
 */
@Service
@Transactional
public class OrderPool {
    private BuyOrderDao buyOrderDao;
    private SellOrderDao sellOrderDao;

    private Map<String,Map<BigDecimal,List<Order>>> buyOrderPool;
    private Map<String,Map<BigDecimal,List<Order>>> sellOrderPool;

    private void insertPool(Map<String,Map<BigDecimal,List<Order>>> pool,Order order)
    {
        String str = order.getGoodsName().concat(order.getGoodsDate());
        Map<BigDecimal,List<Order>>goodOrderPool = pool.get(str);
        if(goodOrderPool != null)
        {
            List<Order> temp = goodOrderPool.get(order.getPrice());
            if(temp != null)
            {
                temp.add(order);
            }
            else
            {
                temp = new LinkedList<Order>();
                temp.add(order);
            }
        }
        else
        {
            goodOrderPool = new TreeMap<BigDecimal, List<Order>>();
            goodOrderPool.put(order.getPrice(),new LinkedList<Order>());
            goodOrderPool.get(order.getPrice()).add(order);
        }
    }

    @Autowired
    public OrderPool(BuyOrderDao buyOrderDao, SellOrderDao sellOrderDao){
        this.buyOrderDao = buyOrderDao;
        this.sellOrderDao = sellOrderDao;
        List<Order>orderList = this.buyOrderDao.queryNoPendingOrder();
        buyOrderPool = new HashMap<String, Map<BigDecimal, List<Order>>>();
        for (Order order:orderList
             ) {
            insertPool(buyOrderPool,order);
        }
        orderList = this.sellOrderDao.queryNoPendingOrder();
        sellOrderPool = new HashMap<String, Map<BigDecimal, List<Order>>>();
        for (Order order:orderList
             ) {
            insertPool(sellOrderPool,order);
        }
    }


    public void newBuyOrder(Order buyOrder,int type)
    {
        buyOrderDao.add(buyOrder);
        buyOrder.setorderID(buyOrderDao.getMaxID());
        String str = buyOrder.getGoodsName().concat(buyOrder.getGoodsDate());
        Map<BigDecimal,List<Order>>goodOrderPool = buyOrderPool.get(str);
        if(goodOrderPool != null)
        {
            switch (type)
            {
                case 0:
                {
                    //market order
                    break;
                }
                case 1:
                {
                    //limit order
                    break;
                }
                case 2:
                {
                    //stop order
                    break;
                }
                case 3:
                {
                    //stop order
                }
            }
        }
        else{
            insertPool(buyOrderPool,buyOrder);
        }
    }
}
