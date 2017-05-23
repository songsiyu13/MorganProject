package com.dao;

import com.entity.BuyOrder;
import org.springframework.stereotype.Repository;

/**
 * Created by 滩涂上的芦苇 on 2017/5/23.
 */
@Repository(value = "buyOrderDao")
public interface BuyOrderDao {
    void add(BuyOrder buyOrder);
}
