/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author lycronaldo
 */
public class MarketCommodity implements Serializable{
    //价格
    private BigDecimal price;
    //数量
    private int quantity;
    
    public void setPrice(BigDecimal price){this.price = price;}

    public void setQuantity(int quantity){this.quantity = quantity;}

    public BigDecimal getPrice(){return price;}

    public int getQuantity(){return quantity;}
}
