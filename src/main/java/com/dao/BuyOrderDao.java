package com.dao;

import com.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 滩涂上的芦苇 on 2017/5/23.
 */
@Repository(value = "buyOrderDao")
public interface BuyOrderDao {
    void add(Order buyOrder);
    List<Order> queryNoPendingOrder();
    int getMaxID();
    List<Order> queryMatchOrder(int buyOrderID);
}
