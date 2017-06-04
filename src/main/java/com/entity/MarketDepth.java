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

    private String goodsName;

    private Date goodsTime;
    
    // Market Depth 列表
    private List<MarketCommodity> buyMarketDepth;

    private List<MarketCommodity> sellMarketDepth;
    
    public void setTime(Date time)
    {
        this.time = time;
    }

    public void setGoodsName(String name){goodsName = name;}

    public void setGoodsTime(Date date){goodsTime = date;}
    
    public void setBuyMarketDepth(List<MarketCommodity> market_depth)
    {
        this.buyMarketDepth = market_depth;
    }

    public void setSellMarketDepth(List<MarketCommodity> sellMarketDepth){this.sellMarketDepth = sellMarketDepth;}
    
    public Date getTime()
    {
        return this.time;
    }

    public String getGoodsName(){return this.goodsName;}

    public Date getGoodsTime(){return this.goodsTime;}
    
    public List<MarketCommodity> getBuyMarketDepth()
    {
        return this.buyMarketDepth;
    }

    public List<MarketCommodity> getSellMarketDepth() { return this.sellMarketDepth;}
}
