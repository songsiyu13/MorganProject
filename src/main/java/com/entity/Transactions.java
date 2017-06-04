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
public class Transactions implements Serializable{
    
    // 买方公司名称
    private String buyName;
    
    // 卖方公司名称
    private String saleName;
    
    // 商品类
    private Commodity commodity;
    
    // 交易完成时间
    private Date time;
    
    public void setBuyName(String buyName)
    {
        this.buyName = buyName;
    }
    
    public void setSaleName(String saleName)
    {
        this.saleName = saleName;
    }
    
    public void setCommodity(Commodity commodity)
    {
        this.commodity = commodity;
    }
    
    public void setTime(Date time)
    {
        this.time = time;
    }
    
    public String getBuyName()
    {
        return this.buyName;
    }
    
    public String getSaleName()
    {
        return this.saleName;
    }
    
    public Commodity getCommodity()
    {
        return this.commodity;
    }
    
    public Date getTime()
    {
        return this.time;
    }
}
