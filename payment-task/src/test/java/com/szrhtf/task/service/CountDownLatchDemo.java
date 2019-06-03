/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the
 * confidential and proprietary information of Colotnet.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Colotnet.com.
 */
package com.szrhtf.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.szrhtf.task.base.DailySwitchThead;

/**
 * 类CountDownLatchDemo.java的实现描述：TODO 类实现描述 
 * @author Sunney 2016年3月16日 下午5:06:23
 */
public class CountDownLatchDemo {

    /**
     * @param args
     */
    public static void main(String[] args) {
        List<Integer> list=new ArrayList<Integer>();
        for(int i =1;i<=200000;i++){
            list.add(i);
        }
     
        Thread thread= null;
        ExecutorService pool = Executors.newFixedThreadPool(10);
        CountDownLatch threadSignal = new CountDownLatch(list.size());//初始化countDown
        for (Integer val : list) {
            //thread=new DailySwitchThead(threadSignal,val,"2016-12-12","23:30:00");
            pool.execute(thread);
        }
        try {
            threadSignal.await();
            System.out.println("================================================================end");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
