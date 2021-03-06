package com.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * Created by 滩涂上的芦苇 on 2017/5/24.
 */
public class Order {
    private int orderID;
    private int orderType;
    private String goodsName;
    private String goodsDate;
    private BigDecimal price;
    private int quantity;
    private String companyName;

    public Order(){

    }

    public Order(OrderMessage orderMessage)
    {
        orderType = orderMessage.getType();
        goodsName = orderMessage.getCommodity().getName();
        goodsDate = (new SimpleDateFormat("yyyy-MM")).format(orderMessage.getCommodity().getTime());
        price =  new BigDecimal(orderMessage.getCommodity().getPrice());
        quantity = orderMessage.getCommodity().getNumber();
        companyName = orderMessage.getCompany_name();
    }

    public int getOrderID(){return orderID;}
    public int getOrderType(){return orderType;}
    public String getGoodsName(){return goodsName;}
    public String getGoodsDate(){return goodsDate;}
    public BigDecimal getPrice(){return price;}
    public int getQuantity(){return quantity;}
    public String getCompanyName(){return companyName;}

    public void setorderID(int orderID){this.orderID = orderID;}
    public void setOrderType(int orderType){this.orderType = orderType;}
    public void setGoodsName(String goodsName){this.goodsName = goodsName;}
    public void setGoodsDate(String goodsDate){this.goodsDate = goodsDate;}
    public void setPrice(BigDecimal price){this.price = price;}
    public void setQuantity(int quantity){this.quantity = quantity;}
    public void setCompanyName(String companyName){this.companyName = companyName;}
}
