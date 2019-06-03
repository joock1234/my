/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.helper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.szrhtf.Application;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.commons.Result;
import com.szrhtf.share.interfaces.DailySwitchService;
import com.szrhtf.task.base.DailySwitchThead;
import com.szrhtf.task.base.DailySwitchTheadByDate;
import com.szrhtf.utils.DailySwitchUtils;
import com.szrhtf.web.share.dto.DailySwitchTaskDTO;
import com.szrhtf.web.share.dto.MerchantInfoDTO;
import com.szrhtf.web.share.interfaces.AgentService;
import com.szrhtf.web.share.interfaces.MerchantInfoService;

/**
 * 类DailySwitchHelper.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年4月20日 上午10:34:10
 */
public class DailySwitchHelper {

	public static Logger              logger                  = LoggerFactory.getLogger(DelayTaskHelper.class);
	public final static String        ALL                     = "ALL";
	public final static String        DEFAULT_DAILYSWTHCHTIME = "23:30:00";

	public static AgentService        agentService;
	public static DailySwitchService  dailySwitchService;
	@Autowired
	public static MerchantInfoService merchantInfoService;

	private static final int THREAD_POOL_SIZE = 2;

	private static final ThreadPoolExecutor executorFixedService = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	//    private static 

	/*
	public static void DailySwitch(String dailySwthchDate, String dailySwthchTime, List<Long> spIdList) {

		dailySwitchService = Application.applicationContext.getBean("dailySwitchService", DailySwitchService.class);
		agentService = Application.applicationContext.getBean("agentService", AgentService.class);

		logger.warn("==========Daily Switch start,dailySwthchDate:" + dailySwthchDate + ",dailySwthchTime:"
				+ dailySwthchTime + "==========");

		// Init clear count
		DailySwitchUtils.currentCountMap.clear();

		// 清算,结算要传当天的日期
		liquidation(spIdList, dailySwthchDate, dailySwthchTime);
		// 结算,结算要传当天的日期
		settlement(spIdList, dailySwthchDate);

		String frontDailySwthchDate = DailySwitchUtils.frontDailyDate(dailySwthchDate);

		// 统计商户账号
		statisticsMerchant(spIdList, frontDailySwthchDate, dailySwthchTime);
		// 代理数据统计
		statisticsAngle(frontDailySwthchDate, dailySwthchTime);
		// 统计公司内部账号
		statisticsCompany(frontDailySwthchDate, dailySwthchTime);
		// 账号记录转移
		insertFundAccountRecord(frontDailySwthchDate);

		logger.warn("==========Daily Switch END==========");
	}*/



	public static void DailySwitch(String dailySwthchDate, String dailySwthchTime, List<Long> spIdList, List<Long> parentSpIds) {
		
		logger.warn("==========Daily Switch start==========");
		
		dailySwitchService = Application.applicationContext.getBean("dailySwitchService", DailySwitchService.class);

		Iterator<Long>itms = spIdList.iterator();
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			executorFixedService.execute(new DailySwitchTheadByDate(dailySwitchService, itms, dailySwthchDate,dailySwthchTime));
		}
		logger.warn("==========Daily Switch END==========");
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(true){
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(executorFixedService.getQueue().size() == 0){
				if(executorFixedService.getActiveCount() == 0){
					dailySwitchParentSpId(dailySwthchDate,dailySwthchTime,parentSpIds);
					return;
				}
			}
		}
	}


	private static void dailySwitchParentSpId(String dailySwthchDate, String dailySwthchTime, List<Long> parentSpIds) {
		
		logger.warn("==========Daily Switch ParentSPID [ " + parentSpIds + " ]==========");
		
		
		dailySwitchService = Application.applicationContext.getBean("dailySwitchService", DailySwitchService.class);
		Iterator<Long>itms = parentSpIds.iterator();
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			executorFixedService.execute(new DailySwitchTheadByDate(dailySwitchService, itms, dailySwthchDate,dailySwthchTime));
		}
		logger.warn("==========Daily Switch END==========");
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(true){
			try {
				Thread.sleep(10L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(executorFixedService.getQueue().size() + "<>" + executorFixedService.getActiveCount());
			if(executorFixedService.getQueue().size() == 0){
				if(executorFixedService.getActiveCount() == 0){
					String frontDailySwthchDate = DailySwitchUtils.frontDailyDate(dailySwthchDate);
					logger.info("================ start statisticsAngle");
					dailySwitchService.statisticsAngle(frontDailySwthchDate, dailySwthchTime);
					logger.info("================ end statisticsAngle");
					dailySwitchService.statisticsCompanyAccountNew(frontDailySwthchDate, dailySwthchTime);
					return;
				}
			}
		}
	}


	private static void DailySwitch(String dailySwthchStartDate, String dailySwthchEndDate, String dailySwthchTime,
			List<Long> spIdList) {
		dailySwitchService = Application.applicationContext.getBean("dailySwitchService", DailySwitchService.class);
		agentService = Application.applicationContext.getBean("agentService", AgentService.class);

		logger.warn("==========Daily Switch start,dailySwthchDate:" + dailySwthchStartDate + " -> "+ dailySwthchEndDate+",dailySwthchTime:"
				+ dailySwthchTime + "==========");

		// 清算,结算要传当天的日期
		liquidation(spIdList.iterator(), dailySwthchStartDate,dailySwthchEndDate, dailySwthchTime);
		// 结算,结算要传当天的日期
		logger.warn("==========Daily Switch END==========");
	}



	private static void liquidation(Iterator<Long> spIdList, String dailySwthchStartDate, String dailySwthchEndDate,
			String dailySwthchTime) {
		// 清算处理
		// 初始化countDown
		logger.info("==========liquidation Bebin dailySwthchDate:" + dailySwthchStartDate + "-"+dailySwthchEndDate +" spIdList ");
		for (int i = 0; i < THREAD_POOL_SIZE; i++) {
			executorFixedService.execute(new DailySwitchTheadByDate(dailySwitchService, spIdList, dailySwthchStartDate,dailySwthchEndDate, dailySwthchTime));    	
		}
		logger.info("==========liquidation end==========");
		try {
			Thread.sleep(3000L);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(true){
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(executorFixedService.getQueue().size() == 0){
				if(executorFixedService.getActiveCount() == 0){
					String frontDailySwthchDate = DailySwitchUtils.frontDailyDate(dailySwthchEndDate);
					logger.info("================ start statisticsAngle");
					dailySwitchService.statisticsAngle(frontDailySwthchDate, dailySwthchTime);
					logger.info("================ end statisticsAngle");
					dailySwitchService.statisticsCompanyAccountNew(frontDailySwthchDate, dailySwthchTime);


					return;
				}
			}
		}
	}

	public synchronized static void reginLiquidation(Iterator<Long> spIdList, String dailySwthchStartDate, String dailySwthchEndDate,
			String dailySwthchTime) {
		executorFixedService.execute(new DailySwitchTheadByDate(dailySwitchService, spIdList, dailySwthchStartDate,dailySwthchEndDate, dailySwthchTime));    	

	}

	public static void reginLiquidation(Iterator<Long> spIds, String dailySwitchDate, String dailySwthchTime) {
		executorFixedService.execute(new DailySwitchTheadByDate(dailySwitchService, spIds, dailySwitchDate,dailySwthchTime));    	
	}


	public static synchronized Long getNextSpId(Iterator<Long>nextIterator){
		if(nextIterator.hasNext()){
			return nextIterator.next();
		}
		return null;
	}



	/**
	 * 清算
	 * 
	 * @param merchantList 商户列表
	 * @param dailySwthchDate 日切日期
	 * @param dailySwthchTime 日切时间
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unused")
	private static void liquidation(List<Long> spIdList, String dailySwthchDate, String dailySwthchTime) {

		try {
			Thread thread = null;
			// 清算处理
			// 初始化countDown
			logger.info("==========liquidation Bebin dailySwthchDate:" + dailySwthchDate + "spIdList size:"
					+ spIdList.size());
			Integer count = 0;
			for (Long spId : spIdList) {
				count = getCurrentCount();
				while (count >= 2) { // 循环条件中直接为TRUE
					Thread.sleep(3000);
					count = getCurrentCount();
				}
				logger.info("==========liquidation pId:" + spId);
				thread = new DailySwitchThead(dailySwitchService, spId, dailySwthchDate, dailySwthchTime);
				DailySwitchUtils.currentCountMap.put(spId, spId);
				thread.start();

			}
			count = getCurrentCount();
			// 等待子线程结束后再向下执行
			while (count > 0) { // 循环条件中直接为TRUE
				Thread.sleep(3000);
				count = getCurrentCount();
			}
		} catch (InterruptedException e) {
			logger.error("InterruptedException ex", e);
		}
		logger.info("==========liquidation end==========");
	}




	/**
	 * 结算
	 * 
	 * @param merchantList 商户列表
	 * @param dailySwthchDate 日切日期
	 */
	public static void settlement(List<Long> spIdList, String dailySwthchDate) {
		logger.info("==========settlement Bebin dailySwthchDate:" + dailySwthchDate);
		// 结算处理
		for (Long spId : spIdList) {
			logger.info("==========settlement spId:" + spId + "==start==");
			Result result = dailySwitchService.settlement(spId, dailySwthchDate);
			if (!result.isSuccess()) {
				logger.error("settlement pId:" + spId + " dailySwthchDate:" + dailySwthchDate + " is error:",
						result.getErrorHint());
			}
			logger.info("==========settlement pId:" + spId + "==end==");
		}
		logger.info("==========settlement end==========");

	}



	/**
	 * 结算 单个商户
	 * 
	 * @param merchantList 商户列表
	 * @param dailySwthchDate 日切日期
	 */
	public static void settlementCollect(Long spId, String dailySwthchDate) {
		logger.info("==========settlement Bebin dailySwthchDate:" + dailySwthchDate);
		// 结算处理
		logger.info("==========settlement spId:" + spId + "==start==");
		Result result = dailySwitchService.settlementCollect(spId, dailySwthchDate);
		if (!result.isSuccess()) {
			logger.error("settlement pId:" + spId + " dailySwthchDate:" + dailySwthchDate + " is error:",
					result.getErrorHint());
		}
		logger.info("==========settlement pId:" + spId + "==end==");

		logger.info("==========settlement end==========");

	}

	/**
	 * 代理数据统计
	 * 
	 * @param dailySwthchDate 日切日期
	 * @param dailySwthchTime 日切时间
	 */
	@SuppressWarnings("unused")
	private static void statisticsAngle(String dailySwthchDate, String dailySwthchTime) {
		logger.info("==========statisticsAngleAccount begin==========");
		QueryResult<Long> agentResult = agentService.queryAgentInfoAll();
		if (agentResult.isSuccess()) {
			List<Long> agentList = agentResult.getResults();
			for (Long angleId : agentList) {
				logger.info("==========statisticsAngle angleId:" + angleId + "==start==");
				Result result = dailySwitchService.statisticsAngleAccount(angleId, dailySwthchDate, dailySwthchTime);
				if (!result.isSuccess()) {
					logger.error("==========angleId:" + angleId + "======statisticsAngleAccount is error"
							+ result.getErrorHint());
				}
				logger.info("==========statisticsAngle angleId:" + angleId + "==end==");
			}
		}
		logger.info("==========statisticsAngleAccount end========================");
	}




	/**
	 * 统计公司内部账号
	 * 
	 * @param dailySwthchDate 日切日期
	 * @param dailySwthchTime 日切时间
	 */
	@SuppressWarnings("unused")
	private static void statisticsCompany(String dailySwthchDate, String dailySwthchTime) {
		logger.info("==========内部账号统计 statisticsCompanyAccount Bebin==========");
		Result companyResult = dailySwitchService.statisticsCompanyAccount(dailySwthchDate, dailySwthchTime);
		if (!companyResult.isSuccess()) {
			logger.error("statisticsCompanyAccount dailySwthchDate:" + dailySwthchDate + " is error:",
					companyResult.getErrorHint());
		}
		logger.info("==========内部账号统计 statisticsCompanyAccount End ==========");
	}

	/**
	 * 统计商户账号
	 * 
	 * @param merchantList 商户列表
	 * @param dailySwthchDate 日切日期
	 * @param dailySwthchTime 日切时间
	 */
	@SuppressWarnings("unused")
	private static void statisticsMerchant(List<Long> spIdList, String dailySwthchDate, String dailySwthchTime) {
		logger.info("==========statisticsMerchant Bebin==========");
		// 账户统计移除
		for (Long spId : spIdList) {
			logger.info("==========statistics spId:" + spId + "==start==");
			Result result = dailySwitchService.statistics(spId, dailySwthchDate, dailySwthchTime);
			logger.info("==========statistics pId:" + spId + "==end==");
			if (!result.isSuccess()) {
				logger.error("====statistics pId:" + spId + " dailySwthchDate:" + dailySwthchDate + " is error:",
						result.getErrorHint());
			}
			logger.info("==========statistics pId:" + spId + "==end==");
		}
		logger.info("==========statisticsMerchant End==========");
	}


	public static void statisticsMerchant(Long spId, String dailySwthchDate, String dailySwthchTime) {
		logger.info("==========statistics spId:" + spId + "==start==");
		Result result = dailySwitchService.statistics(spId, dailySwthchDate, dailySwthchTime);
		logger.info("==========statistics pId:" + spId + "==end==");
		if (!result.isSuccess()) {
			logger.error("====statistics pId:" + spId + " dailySwthchDate:" + dailySwthchDate + " is error:",
					result.getErrorHint());
		}
		logger.info("==========statistics pId:" + spId + "==end==");
	}

	/**
	 * 账号记录转移
	 * 
	 * @param dailySwthchDate 日切日期
	 */
	@SuppressWarnings("unused")
	private static void insertFundAccountRecord(String dailySwthchDate) {
		logger.info("========== insertFundAccountRecord Bebin==========");
		Result recordResult = dailySwitchService.insertFundAccountRecord(dailySwthchDate);
		if (!recordResult.isSuccess()) {
			logger.error("insertFundAccountRecord dailySwthchDate:" + dailySwthchDate + " is error:",
					recordResult.getErrorHint());
		}
		logger.info("==========insertFundAccountRecord end==========");
	}

	/**
	 * 获取当前执行的任务数
	 * 
	 * @return
	 */
	private static int getCurrentCount() {
		Integer count = DailySwitchUtils.currentCountMap.size();
		return count;
	}

	/**
	 * 日切
	 * 
	 * @param taskVO
	 */
	public static void AutoDailySwitch(DailySwitchTaskDTO taskVO) {
		List<Long> spIdList = null;
		if (taskVO.getSpId().toUpperCase().equals(ALL)) {
			// 所有商户日切
			QueryResult<MerchantInfoDTO> merchantResult = merchantInfoService.queryMerchantInfoListAll();
			if (merchantResult.isSuccess()) {
				List<MerchantInfoDTO> merchantList = merchantResult.getResults();
				spIdList = new ArrayList<Long>();
				for (MerchantInfoDTO merchantInfoDTO : merchantList) {
					spIdList.add(Long.valueOf(merchantInfoDTO.getSpId()));
				}
			}

		} else {
			// 单个商户日切
			spIdList = new ArrayList<Long>();
			spIdList.add(Long.valueOf(taskVO.getSpId()));

		}
		String dailySwthchTime = taskVO.getDailySwthchTime();
		if (StringUtils.isEmpty(dailySwthchTime)) {
			dailySwthchTime = DEFAULT_DAILYSWTHCHTIME;
		}
		DailySwitchHelper.DailySwitch(taskVO.getDailySwthchStartDate(),taskVO.getDailySwthchEndDate(), dailySwthchTime, spIdList);
	}



	private static int i = 0;

	public synchronized static int getIndex(){
		return i++;
	}





	//	public static void main(String[] args) throws InterruptedException, ExecutionException {
	//		//    	
	//		    	for (int i = 0; i < 100; i++) {
	//		    		executorFixedService.execute(new Runnable() {
	//		    			
	//		    			@Override
	//		    			public void run() {
	//		//    				try {
	//		//    					Thread.sleep(1000L);
	//		//    				} catch (InterruptedException e) {
	//		//    					// TODO Auto-generated catch block
	//		//    					e.printStackTrace();
	//		//    				}
	//		    				try {
	//								Thread.sleep(100);
	//								System.out.println(getIndex());
	//							} catch (InterruptedException e) {
	//								// TODO Auto-generated catch block
	//								e.printStackTrace();
	//							}
	//		    				System.out.println("aaaaaaaaaaaaaaaaaaaaaaa");
	//		    				
	//		    			}
	//		    		});
	//				}
	//		    	
	//		    	while(true){
	//		    		
	//		    		System.out.println("========================>" +executorFixedService.getQueue().size());
	//		    		Thread.sleep(1000L);
	//		    	}
	//		    	
	//		    	
	//		//    	
	//		//    	
	//		//    	List<Runnable> run = executorFixedService.shutdownNow();
	//		//    	
	//		//    	executorFixedService.
	//		//    	for (Runnable runnable : run) {
	//		//    		executorFixedService.execute(runnable);
	//		//		}
	//		//    	System.out.println(executorFixedService.shutdownNow().size());
	//	}



}
