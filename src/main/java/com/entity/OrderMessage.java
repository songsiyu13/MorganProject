/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;

/**
 *
 * @author lycronaldo
 */
public class OrderMessage implements Serializable{
    
    // Order ID,自增,Trader用于标识该Order.注意:broker也有自己的订单标识系统,该ID仅用于cancel order的订单对照
    private int id;
    
    // 公司名
    private String company_name;
    
    // 0: market order, 1: limit order, 2: stop order, 3: cancel order
    private int type;
    
    // 商品
    private Commodity commodity;

    // 来自买方报价或是卖方报价, 0代表买方, 1代表卖方
    private int s_type;
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public void setCompany_name(String company_name)
    {
        this.company_name = company_name;
    }
    
    public void setType(int type)
    {
        this.type = type;
    }
    
    public void setCommodity(Commodity commodity)
    {
        this.commodity = commodity;
    }
    
    public void setS_type(int s_type)
    {
        this.s_type = s_type;
    }
    
    public int getId()
    {
        return this.id;
    }
    
    public String getCompany_name()
    {
        return this.company_name;
    }
    
    public int getType()
    {
        return this.type;
    }
    
    public Commodity getCommodity()
    {
        return this.commodity;
    }
    
    public int getS_type()
    {
        return this.s_type;
    }
}
