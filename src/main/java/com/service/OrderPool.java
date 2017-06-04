package com.service;

import com.dao.BuyOrderDao;
import com.dao.OrderItemDao;
import com.dao.SellOrderDao;
import com.entity.*;
import com.producer.BrokerSender;
import com.sun.org.apache.xpath.internal.operations.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 滩涂上的芦苇 on 2017/5/24.
 */
@Service
@Transactional
public class OrderPool {
    @Autowired
    private BrokerSender brokerSender;
    @Autowired
    @Qualifier("queueDestination")
    private Destination destination;

    private BuyOrderDao buyOrderDao;
    private SellOrderDao sellOrderDao;
    private OrderItemDao orderItemDao;

    private Map<String,Map<BigDecimal,List<Order>>> buyLimitOrderPool;
    private Map<String,Map<BigDecimal,List<Order>>> sellLimitOrderPool;

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
                    public int compare(BigDecimal a,BigDecimal b){
                        return b.compareTo(a);
                    }
                });
            }
            else
            {
                goodOrderPool = new TreeMap<BigDecimal, List<Order>>();
            }
            goodOrderPool.put(order.getPrice(),new LinkedList<Order>());
            goodOrderPool.get(order.getPrice()).add(order);
            limitPool.put(goods,goodOrderPool);
        }
    }
    //some methods to process limitOrder
    private Order getLimitOrder(Map<BigDecimal,List<Order>> limitOrderPool)
    {
        Iterator iter = limitOrderPool.keySet().iterator();
        while (iter.hasNext())
        {
            List<Order> temp = limitOrderPool.get(iter.next());
            if (temp == null) continue;
            return temp.get(0);
        }
        return null;
    }
    private void popLimitOrder(Map<BigDecimal,List<Order>> limitOrderPool)
    {
        Iterator iter = limitOrderPool.keySet().iterator();
        while (iter.hasNext())
        {
            List<Order> temp = limitOrderPool.get(iter.next());
            if (temp == null) continue;
            limitOrderPool.remove(0);
            return;
        }
    }
    private BigDecimal getPrice(Map<BigDecimal,List<Order>> limitOrderPool)
    {
        Iterator iter = limitOrderPool.keySet().iterator();
        while (iter.hasNext())
        {
            BigDecimal bg = (BigDecimal) iter.next();
            List<Order> temp = limitOrderPool.get(bg);
            if (temp == null) continue;
            return bg;
        }
        return null;
    }

    private Map<String,List<Order>> buyMarketOrderPool;
    private Map<String,List<Order>> sellMarketOrderPool;
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

    private Map<String,PriorityQueue<Order>> buyStopOrderPool;
    private Map<String,PriorityQueue<Order>> sellStopOrderPool;

    //compare method for priority queue
    private Comparator<Order> buyCompare = new Comparator<Order>() {
        public int compare(Order o1, Order o2) {
            return o2.getPrice().compareTo(o1.getPrice());
        }
    };
    private Comparator<Order> sellCompare = new Comparator<Order>() {
        public int compare(Order o1, Order o2) {
            return o1.getPrice().compareTo(o2.getPrice());
        }
    };

    private void insertStopPool(String goods,Map<String,PriorityQueue<Order>> stopOrderPool,Order order,boolean buy)
    {
        PriorityQueue<Order> goodsQueue = stopOrderPool.get(goods);
        if (goodsQueue != null)
        {
            goodsQueue.add(order);
        }
        else
        {
            if(buy)
            {
                goodsQueue =  new PriorityQueue<Order>(0,buyCompare);
            }
            else
            {
                goodsQueue = new PriorityQueue<Order>(0, sellCompare);
            }
            goodsQueue.add(order);
            stopOrderPool.put(goods,goodsQueue);
        }
    }


    //method for init
    private void insertPool(Map<String,Map<BigDecimal ,List<Order>>> limitPool,Map<String,List<Order>>marketPool,Map<String,PriorityQueue<Order>> stopPool,Order order,boolean buy)
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
            case 2:
            {
                //stop order
                insertStopPool(goods,stopPool,order,buy);
            }
        }
    }
    @Autowired
    public OrderPool(BuyOrderDao buyOrderDao, SellOrderDao sellOrderDao, OrderItemDao orderItemDao){
        this.buyOrderDao = buyOrderDao;
        this.sellOrderDao = sellOrderDao;
        this.orderItemDao = orderItemDao;
        buyMarketOrderPool = new HashMap<String, List<Order>>();
        sellMarketOrderPool = new HashMap<String, List<Order>>();
        buyLimitOrderPool = new HashMap<String, Map<BigDecimal, List<Order>>>();
        sellLimitOrderPool = new HashMap<String, Map<BigDecimal, List<Order>>>();
        buyStopOrderPool = new HashMap<String, PriorityQueue<Order>>();
        sellStopOrderPool = new HashMap<String, PriorityQueue<Order>>();

        List<Order>orderList = this.buyOrderDao.queryNoPendingOrder();
        for (Order order:orderList
             ) {
            insertPool(buyLimitOrderPool,buyMarketOrderPool,buyStopOrderPool,order,true);
        }
        orderList = this.sellOrderDao.queryNoPendingOrder();
        for (Order order:orderList
             ) {
            insertPool(sellLimitOrderPool,sellMarketOrderPool,sellStopOrderPool,order,false);
        }

        //first to try process buy market orders
        for (String key: buyMarketOrderPool.keySet()
                ) {
            processMarketOrderInPool(key,buyMarketOrderPool,true);
        }

        //then try process sell market orders
        for (String key: sellMarketOrderPool.keySet()
                ) {
           processMarketOrderInPool(key,sellMarketOrderPool,false);
        }
    }

    private void processMarketOrderInPool(String goods,Map<String,List<Order>> marketPool,boolean isBuyOrder)
    {
        List<Order>orders = marketPool.get(goods);
        if(orders == null)return;
        while (!orders.isEmpty())
        {
            Order temp = orders.get(0);
            orders.remove(0);
            if (!processMarketOrder(temp,buyLimitOrderPool,buyMarketOrderPool,isBuyOrder))
            {
                break;
            }
        }
    }

    public void newBuyOrder(Order buyOrder,int type, int traderID)
    {
        if(type == 3) {
            processCancelOrder(buyOrder, buyLimitOrderPool, buyMarketOrderPool, buyStopOrderPool);
            return;
        }

            buyOrderDao.add(buyOrder);
        buyOrder.setorderID(buyOrderDao.getMaxID());

        //---------------------send Message--------------------------
        OrderAcknowledge orderAcknowledge = new OrderAcknowledge();
        orderAcknowledge.setBrokerId(buyOrder.getOrderID());
        orderAcknowledge.setBrokerId(traderID);
        brokerSender.sendMessage(destination,orderAcknowledge);
        //---------------------send Message--------------------------

        String goods = buyOrder.getGoodsName().concat(buyOrder.getGoodsDate());
        Map<BigDecimal,List<Order>>goodOrderPool = buyLimitOrderPool.get(goods);
        switch (type)
        {
            case 0:
            {
                //market order
                processMarketOrder(buyOrder,sellLimitOrderPool,sellMarketOrderPool,true);
                break;
            }
            case 1:
            {
                //limit order
                if(!processLimitOrder(buyOrder,sellLimitOrderPool,sellMarketOrderPool,true))
                {
                    stopOrderTrigger(goods,buyMarketOrderPool,buyLimitOrderPool,buyStopOrderPool,buyCompare);
                    processMarketOrderInPool(goods,buyMarketOrderPool,true);
                }
                break;
            }
            case 2:
            {
                //stop order
                if(goodOrderPool != null && getLimitOrder(goodOrderPool) != null && getLimitOrder(goodOrderPool).getPrice().compareTo(buyOrder.getPrice()) > 0)
                {
                    insertMarketPool(goods,buyMarketOrderPool,buyOrder);
                    processMarketOrderInPool(goods,buyMarketOrderPool,true);
                }
                else 
                {
                    insertStopPool(goods,buyStopOrderPool,buyOrder,true);
                }
                break;
            }
        }
        broadcastMarketDepth(buyOrder);
    }

    public void newSellOrder(Order sellOrder,int type, int traderID)
    {
        if(type == 3)
        {
            processCancelOrder(sellOrder,sellLimitOrderPool,sellMarketOrderPool,sellStopOrderPool);
            return;
        }
        
        sellOrderDao.add(sellOrder);
        sellOrder.setorderID(sellOrderDao.getMaxID());

        //---------------------send Message--------------------------
        OrderAcknowledge orderAcknowledge = new OrderAcknowledge();
        orderAcknowledge.setBrokerId(sellOrder.getOrderID());
        orderAcknowledge.setBrokerId(traderID);
        brokerSender.sendMessage(destination,orderAcknowledge);
        //---------------------send Message--------------------------


        String goods = sellOrder.getGoodsName().concat(sellOrder.getGoodsDate());
        Map<BigDecimal,List<Order>>goodOrderPool = sellLimitOrderPool.get(goods);
        switch (type)
        {
            case 0:
            {
                //market order
                processMarketOrder(sellOrder,buyLimitOrderPool,buyMarketOrderPool,false);
                break;
            }
            case 1:
            {
                //limit order
                if(!processLimitOrder(sellOrder,buyLimitOrderPool,buyMarketOrderPool,false))
                {
                    stopOrderTrigger(goods,sellMarketOrderPool,sellLimitOrderPool,sellStopOrderPool,sellCompare);
                    processMarketOrderInPool(goods,sellMarketOrderPool,false);
                }
                break;
            }
            case 2:
            {
                //stop order
                if(goodOrderPool != null && getLimitOrder(goodOrderPool) != null && getLimitOrder(goodOrderPool).getPrice().compareTo(sellOrder.getPrice()) > 0)
                {
                    insertMarketPool(goods,sellMarketOrderPool,sellOrder);
                    processMarketOrderInPool(goods,sellMarketOrderPool,false);
                }
                else
                {
                    insertStopPool(goods,sellStopOrderPool,sellOrder,false);
                }
                break;
            }
            case 3:
            {
                //stop order
            }
        }
        broadcastMarketDepth(sellOrder);
    }

    /*
     * parameters buyOrder sellLimitPool sellMarketPool 
     * or
     *            sellOrder buyLimitPool buyMarketPool
     */
    private boolean processMarketOrder(Order order1, Map<String,Map<BigDecimal,List<Order>>>limitPool,Map<String,List<Order>> marketPool,boolean isBuyOrder)
    {
        String goods = order1.getGoodsName().concat(order1.getGoodsDate());
        Map<BigDecimal,List<Order>> limitOrders = limitPool.get(goods);
        List<Order> marketOrders = marketPool.get(goods);
        try
        {
            while (order1.getQuantity() > 0)
            {
                boolean limit = false;
                BigDecimal marketPrice = getPrice(limitOrders);
                if(marketPrice == null) throw new Exception();
                Order order2 = null;
                //to process market orders
                if(marketOrders != null && (!marketOrders.isEmpty()))
                {
                    //first match market order
                    order2 = marketOrders.get(0);
                    order2.setPrice(marketPrice);
                }
                else
                {
                    //match limited order
                    order2 = getLimitOrder(limitOrders);
                    limit = true;
                }
                if(order2 == null) throw new Exception();
                OrderItem orderItem = new OrderItem();
                if (isBuyOrder)
                {
                    orderItem.setBuyOrderID(order1.getOrderID());
                    orderItem.setSellOrderID(order2.getOrderID());
                }
                else
                {
                    orderItem.setBuyOrderID(order2.getOrderID());
                    orderItem.setSellOrderID(order1.getOrderID());
                }
                orderItem.setFinalPrice(order2.getPrice());
                int quantity = Math.min(order1.getQuantity(),order2.getQuantity());
                orderItem.setQuantity(quantity);
                orderItemDao.add(orderItem);
                if(order2.getQuantity() != quantity)
                {
                    order2.setQuantity(order2.getQuantity() - quantity);
                }
                else
                {
                    /*
                     * code for let trader know
                     *
                     *
                     */
                    broadcastOrders(order2,!isBuyOrder);
                    if(limit) popLimitOrder(limitOrders);
                    else marketOrders.remove(0);
                }
                order1.setQuantity(order1.getQuantity() - quantity);
            }
        }
        catch (Exception e)
        {
            insertMarketPool(goods,marketPool,order1);
            return false;
        }
        broadcastOrders(order1,isBuyOrder);
        return true;
    }

    private boolean processLimitOrder(Order order1, Map<String,Map<BigDecimal,List<Order>>>limitPool,Map<String,List<Order>> marketPool,boolean isBuyOrder)
    {
        String goods = order1.getGoodsName().concat(order1.getGoodsDate());
        Map<BigDecimal,List<Order>> limitOrders = limitPool.get(goods);
        List<Order> marketOrders = marketPool.get(goods);
        try
        {
            while (order1.getQuantity() > 0)
            {
                boolean limit = false;
                Order order2 = null;
                //to process market orders
                if(marketOrders != null && (!marketOrders.isEmpty()))
                {
                    //first match market order
                    order2 = marketOrders.get(0);
                    order2.setPrice(order1.getPrice());
                }
                else
                {
                    //match limited order
                    order2 = getLimitOrder(limitOrders);
                    if(isBuyOrder)
                    {
                        if(order1.getPrice().compareTo(order2.getPrice()) < 0)throw new Exception();
                    }
                    else
                    {
                        if(order1.getPrice().compareTo(order2.getPrice()) > 0)throw new Exception();
                    }
                    limit = true;
                }
                if(order2 == null) throw new Exception();
                OrderItem orderItem = new OrderItem();
                if (isBuyOrder)
                {
                    orderItem.setBuyOrderID(order1.getOrderID());
                    orderItem.setSellOrderID(order2.getOrderID());
                }
                else
                {
                    orderItem.setBuyOrderID(order2.getOrderID());
                    orderItem.setSellOrderID(order1.getOrderID());
                }
                orderItem.setFinalPrice(order2.getPrice().add(order1.getPrice()).divide(new BigDecimal(2)));
                int quantity = Math.min(order1.getQuantity(),order2.getQuantity());
                orderItem.setQuantity(quantity);
                orderItemDao.add(orderItem);
                if(order2.getQuantity() != quantity)
                {
                    order2.setQuantity(order2.getQuantity() - quantity);
                }
                else
                {
                    /*
                     * code for let trader know
                     *
                     *
                     */
                    broadcastOrders(order2,!isBuyOrder);
                    if(limit) popLimitOrder(limitOrders);
                    else marketOrders.remove(0);
                }
                order1.setQuantity(order1.getQuantity() - quantity);
            }
        }
        catch (Exception e)
        {
            insertLimitPool(goods,limitPool,order1,isBuyOrder);
            return false;
        }
        broadcastOrders(order1,isBuyOrder);
        return true;
    }


    /*
    *Method for process the stop order needed to join market order
    *
     */
    private void stopOrderTrigger(String goods,Map<String,List<Order>> marketPool ,Map<String,Map<BigDecimal,List<Order>>> limitOrderPool,Map<String,PriorityQueue<Order>> stopOrderPool, Comparator<Order> comp)
    {
        PriorityQueue<Order> stopOrders = stopOrderPool.get(goods);
        Map<BigDecimal,List<Order>> listMap = limitOrderPool.get(goods);
        if (listMap == null) return;
        Order referenceOrder = getLimitOrder(listMap);
        if (stopOrders == null) return;
        while (!stopOrders.isEmpty())
        {
            Order order = stopOrders.peek();
            if(comp.compare(order,referenceOrder) > 0)
            {
                insertMarketPool(goods,marketPool,order);
                stopOrders.remove();
            }
            else
            {
                break;
            }
        }
    }

    private void processCancelOrder(Order order1, Map<String,Map<BigDecimal,List<Order>>>limitPool,Map<String,List<Order>> marketPool,Map<String,PriorityQueue<Order>> stopOrderPool)
    {
        String goods = order1.getGoodsName().concat(order1.getGoodsDate());
        Map<BigDecimal,List<Order>> limitOrders = limitPool.get(goods);
        List<Order>marketOrders = marketPool.get(goods);
        PriorityQueue<Order> stopOrders = stopOrderPool.get(goods);

        for (Order o:marketOrders
             ) {
            if(o.getOrderID() == order1.getOrderID())
            {
                marketOrders.remove(o);
                return;
            }
        }

        for (BigDecimal key:limitOrders.keySet()
             ) {
            for (Order o:limitOrders.get(key)
                 ) {
                if(o.getOrderID() == order1.getOrderID())
                {
                    marketOrders.remove(o);
                    return;
                }
            }
        }

        for (Order o:stopOrders
             ) {
            if(o.getOrderID() == order1.getOrderID())
            {
                marketOrders.remove(o);
                return;
            }
        }
    }

    private void broadcastOrders(Order order, boolean isBuyOrder)
    {
        List<Order>orders = null;
        if(isBuyOrder)
        {
            orders = buyOrderDao.queryMatchOrder(order.getOrderID());
        }
        else
        {
            orders = sellOrderDao.queryMatchOrder(order.getOrderID());
        }
        TransactionList transactionList = new TransactionList();
        transactionList.setList(new ArrayList<Transactions>());
        for (Order o:orders
             ) {
            Transactions transactions = new Transactions();
            if(isBuyOrder)
            {
                transactions.setBuyName(order.getCompanyName());
                transactions.setSaleName(o.getCompanyName());
            }
            else
            {
                transactions.setBuyName(o.getCompanyName());
                transactions.setSaleName(order.getCompanyName());
            }
            Commodity commodity = new Commodity();
            commodity.setName(order.getGoodsName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date date = null;
            try {
                date = sdf.parse(order.getGoodsDate());
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            commodity.setTime(date);
            commodity.setNumber(o.getQuantity());//set buy table order item
            commodity.setPrice(o.getPrice().doubleValue());//set buy table order item
            transactions.setCommodity(commodity);
            transactionList.getList().add(transactions);
        }

        //----------------------------need to change ----------------------------
        brokerSender.sendMessage(destination, transactionList);
        //----------------------------need to change ----------------------------

    }

    private void broadcastMarketDepth(Order order)
    {

        brokerSender.sendMessage(destination,getMarketDepth(order));
    }

    public MarketDepth getMarketDepth(Order order)
    {
        String goods = order.getGoodsName().concat(order.getGoodsDate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = null;
        try {
            date = sdf.parse(order.getGoodsDate());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        MarketDepth marketDepth = new MarketDepth();
        marketDepth.setTime(new Date());
        marketDepth.setGoodsName(order.getGoodsName());
        marketDepth.setGoodsTime(date);
        marketDepth.setBuyMarketDepth(new LinkedList<MarketCommodity>());
        if(buyLimitOrderPool.get(goods) != null)
        {
            for (BigDecimal key : buyLimitOrderPool.get(goods).keySet()
                    ) {
                int quantity = 0;
                for (Order o:buyLimitOrderPool.get(goods).get(key)
                        ) {
                    quantity += o.getQuantity();
                }
                if(quantity != 0)
                {
                    MarketCommodity marketCommodity = new MarketCommodity();
                    marketCommodity.setPrice(key);
                    marketCommodity.setQuantity(quantity);
                    marketDepth.getBuyMarketDepth().add(marketCommodity);
                }
            }
        }
        marketDepth.setSellMarketDepth(new LinkedList<MarketCommodity>());
        if(sellLimitOrderPool.get(goods) != null)
        {
            for (BigDecimal key : sellLimitOrderPool.get(goods).keySet()
                    ) {
                int quantity = 0;
                for (Order o:sellLimitOrderPool.get(goods).get(key)
                        ) {
                    quantity += o.getQuantity();
                }
                if(quantity != 0)
                {
                    MarketCommodity marketCommodity = new MarketCommodity();
                    marketCommodity.setPrice(key);
                    marketCommodity.setQuantity(quantity);
                    marketDepth.getSellMarketDepth().add(marketCommodity);
                }
            }
        }
        return marketDepth;
    }
}
