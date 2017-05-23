package com.entity;

import java.math.BigDecimal;

/**
 * Created by 滩涂上的芦苇 on 2017/5/23.
 */
public class OrderItem {
    private int orderItemID;
    private int buyOrderID;
    private int sellOrderID;
    private BigDecimal finalPrice;
    private int quantity;

    public int getOrderItemID(){return orderItemID;}
    public int getBuyOrderID(){return buyOrderID;}
    public int getSellOrderID(){return sellOrderID;}
    public BigDecimal getFinalPrice(){return finalPrice;}
    public int getQuantity(){return quantity;}

    public void setOrderItemID(int orderItemID){this.orderItemID = orderItemID;}
    public void setBuyOrderID(int buyOrderID){this.buyOrderID = buyOrderID;}
    public void setSellOrderID(int sellOrderID){this.sellOrderID = sellOrderID;}
    public void setFinalPrice(BigDecimal finalPrice){this.finalPrice = finalPrice;}
    public void setQuantity(int quantity){this.quantity = quantity;}
}
