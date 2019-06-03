/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.module.utils.ExceptionUtil;
import com.szrhtf.share.interfaces.DailySwitchService;

/**
 * 类DailySwitchThead.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年3月15日 下午1:59:03
 */
public class DailySwitchThead extends Thread {

    protected Logger           logger = LoggerFactory.getLogger(this.getClass());
    private Long            spId;
    private String             dailySwthchDate;
    private String             dailySwthchTime;
    private DailySwitchService dailySwitchService;

    public DailySwitchThead(DailySwitchService dailySwitchService, final Long spId, final String dailySwthchDate,
                            final String dailySwthchTime) {
        this.spId = spId;
        this.dailySwthchDate = dailySwthchDate;
        this.dailySwthchTime = dailySwthchTime;
        this.dailySwitchService = dailySwitchService;
    }

    @Override
    public void run() {
        try {
            dailySwitchService.liquidation(spId, dailySwthchDate, dailySwthchTime);
            /*
             * Result result = if (!result.isSuccess()) { logger.warn("spId:" + spId + " ,dailySwthchDate:" +
             * dailySwthchDate + " is failed!"); } else { logger.info("spId:" + spId + " ,dailySwthchDate:" +
             * dailySwthchDate + " liquidation is  complete!"); // 清算成功后，删除FundInOut的数据 Result deleteResult =
             * dailySwitchService.deleteFundInOut(Long.valueOf(spId), dailySwthchDate, dailySwthchTime); if
             * (deleteResult.isSuccess()) { logger.info("spId:" + spId + " ,dailySwthchDate:" + dailySwthchDate +
             * "deleteFundInOut is  complete!"); } }
             */
        } catch (Exception ex) {
            ExceptionUtil.printExceptionLog(ex);
            logger.error("liquidation spId:" + spId + " ,dailySwthchDate:" + dailySwthchDate + " is error!msg:" + ex);
        } finally {
            // threadsSignal.countDown();// 线程结束时计数器减1
        }

    }
}
