/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lycronaldo
 */
public class MarketDepth implements Serializable{
    
    // Market Depth的时间
    private Date time;
    
    // Market Depth 列表
    private List<marketCommodity> market_depth;
    
    public void setTime(Date time)
    {
        this.time = time;
    }
    
    public void setMarket_depth(List<marketCommodity> market_depth)
    {
        this.market_depth = market_depth;
    }
    
    public Date getTime()
    {
        return this.time;
    }
    
    public List<marketCommodity> getMarket_depth()
    {
        return this.market_depth;
    }
}
