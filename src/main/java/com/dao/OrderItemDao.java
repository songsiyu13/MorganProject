package com.dao;

import com.entity.OrderItem;
import org.springframework.stereotype.Repository;

/**
 * Created by 滩涂上的芦苇 on 2017/5/25.
 */
@Repository(value = "orderItemDao")
public interface OrderItemDao {
    public void add(OrderItem orderItem);
}
