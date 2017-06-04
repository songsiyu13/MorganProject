/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entity;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author lycronaldo
 */
public class TransactionList implements Serializable{
    
    // 交易的List
    private List<Transactions> list;
    
    // 还可以自行添加所需要的变量
    
    public void setList(List<Transactions> list)
    {
        this.list = list;
    }
    
    public List<Transactions> getList()
    {
        return this.list;
    }
    
}
