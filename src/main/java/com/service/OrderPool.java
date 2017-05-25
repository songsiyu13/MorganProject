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

    private Map<String,Map<BigDecimal,List<Order>>> buyLimitOrderPool;
    private Map<String,Map<BigDecimal,List<Order>>> sellLimitOrderPool;

    private Map<String,List<Order>> buyMarketOrderPool;
    private Map<String,List<Order>> sellMarketOrderPool;

    private PriorityQueue<Order> buyStopPool;
    private PriorityQueue<Order> sellStopPool;

    private void insertMarketPool(String goods,Map<String,List<Order>> marketPool, Order order)
    {
        List<Order> orderList = marketPool.get(goods);
        if(orderList != null){
            orderList.add(order);
        }
        else
        {
            orderList = new LinkedList<Order>();
            orderList.add(order);
            marketPool.put(goods,orderList);
        }
    }
    private void insertLimitPool(String goods,Map<String,Map<BigDecimal,List<Order>>> limitPool,Order order,boolean buy)
    {
        Map<BigDecimal,List<Order>>goodOrderPool = limitPool.get(goods);
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
                goodOrderPool.put(order.getPrice(),temp);
            }
        }
        else
        {
            if(buy)
            {
                goodOrderPool = new TreeMap<BigDecimal, List<Order>>(new Comparator<BigDecimal>(){
                    /*
                    * int compare(Object o1, Object o2) 返回一个基本类型的整型，
                    * 返回负数表示：o1 小于o2，
                    * 返回0 表示：o1和o2相等，
                    * 返回正数表示：o1大于o2。
                    */
                    public int compare(BigDecimal a,BigDecimal b){
                        if(b.subtract(a).doubleValue()>0)
                        {
                            return 1;
                        }
                        else if(b.equals(a))
                        {
                            return 0;
                        }
                        return -1;
                    }
                });
            }
            else
            {
                goodOrderPool = new TreeMap<BigDecimal, List<Order>>();
            }
            goodOrderPool.put(order.getPrice(),new LinkedList<Order>());
            goodOrderPool.get(order.getPrice()).add(order);
        }
    }

    private void insertPool(Map<String,Map<BigDecimal,List<Order>>> limitPool,Map<String,List<Order>>marketPool,Order order,boolean buy)
    {
        String goods = order.getGoodsName().concat(order.getGoodsDate());
        switch (order.getOrderType())
        {
            case 0:
            {
             //market order
                insertMarketPool(goods,marketPool,order);
                break;
            }
            case 1:
            {
                //limit order
                insertLimitPool(goods,limitPool,order,buy);
                break;
            }
        }
    }

    @Autowired
    public OrderPool(BuyOrderDao buyOrderDao, SellOrderDao sellOrderDao){
        this.buyOrderDao = buyOrderDao;
        this.sellOrderDao = sellOrderDao;
        buyMarketOrderPool = new HashMap<String, List<Order>>();
        sellMarketOrderPool = new HashMap<String, List<Order>>();
        buyLimitOrderPool = new HashMap<String, Map<BigDecimal, List<Order>>>();
        sellLimitOrderPool = new HashMap<String, Map<BigDecimal, List<Order>>>();

        List<Order>orderList = this.buyOrderDao.queryNoPendingOrder();
        for (Order order:orderList
             ) {
            insertPool(buyLimitOrderPool,buyMarketOrderPool,order,true);
        }
        orderList = this.sellOrderDao.queryNoPendingOrder();
        for (Order order:orderList
             ) {
            insertPool(sellLimitOrderPool,sellMarketOrderPool,order,false);
        }
    }


    public void newBuyOrder(Order buyOrder,int type)
    {
        buyOrderDao.add(buyOrder);
        buyOrder.setorderID(buyOrderDao.getMaxID());
        String str = buyOrder.getGoodsName().concat(buyOrder.getGoodsDate());
        Map<BigDecimal,List<Order>>goodOrderPool = buyLimitOrderPool.get(str);
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
    }
}
