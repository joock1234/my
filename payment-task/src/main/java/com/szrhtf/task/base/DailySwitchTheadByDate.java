/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.base;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.module.utils.ExceptionUtil;
import com.szrhtf.share.interfaces.DailySwitchService;
import com.szrhtf.task.helper.DailySwitchHelper;
import com.szrhtf.utils.DailySwitchUtils;

/**
 * 类DailySwitchThead.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年3月15日 下午1:59:03
 */
public class DailySwitchTheadByDate extends Thread {

	protected Logger           logger = LoggerFactory.getLogger(this.getClass());
	private Iterator<Long>            spIds;
	private String             dailySwthchStartDate;
	private String             dailySwthchEndDate;
	private String             dailySwthchTime;
	private DailySwitchService dailySwitchService;
	private String 			   dailySwitchDate = null;

	public DailySwitchTheadByDate(DailySwitchService dailySwitchService,  Iterator<Long> spIdList,  String dailySwthchStartDate,String dailySwthchEndDate,
			String dailySwthchTime) {
		this.spIds = spIdList;
		this.dailySwthchStartDate = dailySwthchStartDate;
		this.dailySwthchEndDate = dailySwthchEndDate;
		this.dailySwthchTime = dailySwthchTime;
		this.dailySwitchService = dailySwitchService;
	}


	public DailySwitchTheadByDate(DailySwitchService dailySwitchService,  Iterator<Long> spIdList,  String dailySwthchDate,
			String dailySwthchTime) {
		this.spIds = spIdList;
		this.dailySwthchTime = dailySwthchTime;
		this.dailySwitchService = dailySwitchService;
		this.dailySwitchDate = dailySwthchDate;
	}







	@Override
	public void run() {
		try {
			Long spId = DailySwitchHelper.getNextSpId(this.spIds);
			if(spId != null){
//				logger.info("==========liquidation Bebin dailySwthchDate:" + dailySwthchStartDate + "-"+dailySwthchEndDate +" spIdList ");
				
				if(this.dailySwitchDate != null){
					
					
					logger.info("==========["+spId+"]liquidation begin==========");
					long start = System.currentTimeMillis();
					dailySwitchService.liquidation(spId, this.dailySwitchDate, this.dailySwthchTime);
					long end = System.currentTimeMillis();
					logger.info("==========["+spId+"]liquidation end========== USING ["+(end-start)+"] ms");
					
					
					logger.info("==========["+spId+"]settlement begin========== USING ["+(end-start)+"] ms");
					
					start = System.currentTimeMillis();
					dailySwitchService.settlement(spId, this.dailySwitchDate);
					end = System.currentTimeMillis();
					
					logger.info("==========["+spId+"]settlement end========== USING ["+(end-start)+"] ms");
					
					dailySwitchService.afterLiquidationAndSettlement(spId,this.dailySwitchDate,this.dailySwthchTime);
					
					
//					DailySwitchHelper.reginLiquidation(this.spIds, this.dailySwitchDate,this.dailySwthchTime);
//					String frontDailySwthchDate = DailySwitchUtils.frontDailyDate(this.dailySwthchEndDate);
//					// 统计商户账号
//					DailySwitchHelper.statisticsMerchant(spId, frontDailySwthchDate, this.dailySwthchTime);
//					logger.info("==========liquidation end==========");
					DailySwitchHelper.reginLiquidation(this.spIds, this.dailySwitchDate, this.dailySwthchTime);
					
				}else{
					dailySwitchService.liquidationCollect(spId, this.dailySwthchStartDate,this.dailySwthchEndDate, this.dailySwthchTime);
					DailySwitchHelper.settlementCollect(spId, this.dailySwthchEndDate);
					String frontDailySwthchDate = DailySwitchUtils.frontDailyDate(this.dailySwthchEndDate);
					// 统计商户账号
					DailySwitchHelper.statisticsMerchant(spId, frontDailySwthchDate, this.dailySwthchTime);
					logger.info("==========liquidation end==========");
					DailySwitchHelper.reginLiquidation(this.spIds, this.dailySwthchStartDate, this.dailySwthchEndDate, this.dailySwthchTime);
				}
				
				
			}
		} catch (Exception ex) {
			ExceptionUtil.printExceptionLog(ex);
			logger.error("liquidation spId:" + spIds + " ,dailySwthchDate:" + dailySwthchStartDate + " is error!msg:" + ex);
		} finally {
			// threadsSignal.countDown();// 线程结束时计数器减1
		}

	}
}
