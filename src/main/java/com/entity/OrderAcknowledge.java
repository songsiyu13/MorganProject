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
public class OrderAcknowledge implements Serializable{
    
    // TraderId
    private int traderId;
    
    // BrokerId
    private int brokerId;
    
    // BrokerName
    private String brokerName;
    
    // Status
    private String status;
    
    public void setTraderId(int traderId)
    {
        this.traderId = traderId;
    }
    
    public void setBrokerId(int brokerId)
    {
        this.brokerId = brokerId;
    }
    
    public void setBrokerName(String brokerName)
    {
        this.brokerName = brokerName;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public int getTraderId()
    {
        return this.traderId;
    }
    
    public int getBrokerId()
    {
        return this.brokerId;
    }
    
    public String getBrokerName()
    {
        return this.brokerName;
    }
    
    public String getStatus()
    {
        return this.status;
    }
}
