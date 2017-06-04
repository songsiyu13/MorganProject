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
    private List<MarketCommodity> marketDepth;
    
    public void setTime(Date time)
    {
        this.time = time;
    }
    
    public void setMarketDepth(List<MarketCommodity> market_depth)
    {
        this.marketDepth = market_depth;
    }
    
    public Date getTime()
    {
        return this.time;
    }
    
    public List<MarketCommodity> getMarketDepth()
    {
        return this.marketDepth;
    }
}
