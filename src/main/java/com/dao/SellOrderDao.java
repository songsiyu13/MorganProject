package com.dao;

import com.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 滩涂上的芦苇 on 2017/5/24.
 */
@Repository(value = "sellOrderDao")
public interface SellOrderDao {
    public void add(Order sellOrder);
    List<Order> queryNoPendingOrder();
    int getMaxID();
    List<Order> queryMatchOrder(int sellOrderID);
}
