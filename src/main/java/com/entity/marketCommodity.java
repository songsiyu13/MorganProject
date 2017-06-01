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
public class marketCommodity implements Serializable{
    
    // 商品类
    private Commodity commodity;
    
    // 来源方:false为买方, true为卖方
    private boolean type;
    
    public void setCommodity(Commodity commodity)
    {
        this.commodity = commodity;
    }

    public void setType(boolean type)
    {
        this.type = type;
    }
    
    public Commodity getCommodity()
    {
        return this.commodity;
    }

    public boolean getType()
    {
        return this.type;
    }
}
