/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.module.utils.StringUtil;

/**
 * 类DailySwitchUtils.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年3月23日 下午7:41:11
 */
public class DailySwitchUtils {

    protected static Logger       logger          = LoggerFactory.getLogger(DailySwitchUtils.class);
    public static Map<Long, Long> currentCountMap = new ConcurrentHashMap<Long, Long>();
    // 是否动态日切处理
    public static boolean         AutoDailySwitch = false;

    /**
     * 获取下一下日结日期
     * 
     * @param dailySwthchDate
     * @return
     */
    public static String nextDailyDate(String dailySwthchDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date switchDate = sdf.parse(dailySwthchDate);
            Calendar cl = Calendar.getInstance();
            cl.setTime(switchDate);
            cl.add(Calendar.DATE, 1);
            String temp = sdf.format(cl.getTime());
            return temp;

        } catch (ParseException e) {
            logger.error("nextDailyDate is error:", e);
            e.printStackTrace();
        }
        return dailySwthchDate;
    }

    /**
     * 日结日期前一天
     * 
     * @param dailySwthchDate
     * @return
     */
    public static String frontDailyDate(String dailySwthchDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        
        if(StringUtil.isEmpty(dailySwthchDate)){
        	
        }
        
        String returnDate = null;
        try {
        	System.out.println("=============>" + dailySwthchDate);
            Date switchDate = sdf.parse(dailySwthchDate);
            calendar.setTime(switchDate);
        } catch (ParseException e) {
            logger.error("converToDate is error,", e);
            // 如果转化失败取当天时间
            calendar.setTime(new Date());
        } finally {
            calendar.add(Calendar.DATE, -1);
            returnDate = sdf.format(calendar.getTime());
        }
        return returnDate;
    }

}
