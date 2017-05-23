package com.entity;

import java.math.BigDecimal;

/**
 * Created by 滩涂上的芦苇 on 2017/5/23.
 */
public class SellOrder {
    private int sellOrderID;
    private int orderType;
    private String goodsName;
    private String goodsDate;
    private BigDecimal price;
    private int quantity;
    private String companyName;

    public int getsellOrderID(){return sellOrderID;}
    public int getOrderType(){return orderType;}
    public String getGoodsName(){return goodsName;}
    public String getGoodsDate(){return goodsDate;}
    public BigDecimal getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public String getCompanyName(){return companyName;}

    public void setsellOrderID(int sellOrderID){this.sellOrderID = sellOrderID;}
    public void setOrderType(int orderType){this.orderType = orderType;}
    public void setGoodsName(String goodsName){this.goodsName = goodsName;}
    public void setGoodsDate(String goodsDate){this.goodsDate = goodsDate;}
    public void setPrice(BigDecimal price){this.price = price;}
    public void setQuantity(int quantity){this.quantity = quantity;}
    public void setCompanyName(String companyName){this.companyName = companyName;}
}
