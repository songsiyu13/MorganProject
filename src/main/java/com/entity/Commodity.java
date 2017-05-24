/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author lycronaldo
 */
public class Commodity implements Serializable{
    // 商品类,独立出来作为一个类进行封装,可根据需要进行功能或属性的添加
    
    // 商品名
    private String name;
    
    // 商品时间
    private Date time;
    
    // 商品数量,注意,在cancel order中此项应为-1
    private int number;
    
    // 商品价格,注意,在cancel order和market order中此项应为-1
    private double price;
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public void setTime(Date time)
    {
        this.time = time;
    }
    
    public void setNumber(int number)
    {
        this.number = number;
    }
    
    public void setPrice(double price)
    {
        this.price = price;
    }
    
    public String getName()
    {
        return this.name;
    }
    
    public Date getTime()
    {
        return this.time;
    }
    
    public int getNumber()
    {
        return this.number;
    }
    
    public double getPrice()
    {
        return this.price;
    }
}
