/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.module.constant.ParameterConstant;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.task.helper.DailySwitchHelper;
import com.szrhtf.web.share.dto.MerchantInfoDTO;
import com.szrhtf.web.share.dto.TaskInfoDTO;
import com.szrhtf.web.share.interfaces.MerchantInfoService;
import com.szrhtf.web.share.interfaces.PublicInfoService;
import com.szrhtf.web.share.interfaces.TaskInfoService;

/**
 * 类DailySwitch.java的实现描述： 日切任务，主要是为上天数据的统计清算，结算
 * 
 * @author Sunney 2016年2月25日 上午9:24:43
 */
public class DailySwitchJobImpl extends BaseTaskJobDetail {

	protected Logger    logger = LoggerFactory.getLogger(this.getClass());

	TaskInfoService     taskInfoService;
	MerchantInfoService merchantInfoService;
	PublicInfoService   publicInfoService;

	/*
	 * (non-Javadoc)
	 * @see com.szrhtf.task.base.BaseTaskJobDetail#process(com.szrhtf.web.share.dto.TaskInfoDTO)
	 */
	@Override
	public void process(TaskInfoDTO taskInfo) {
		taskInfoService = Application.applicationContext.getBean("taskInfoService", TaskInfoService.class);
		merchantInfoService = Application.applicationContext.getBean("merchantInfoService", MerchantInfoService.class);
		publicInfoService = Application.applicationContext.getBean("publicInfoService", PublicInfoService.class);
		try {
			DailySwitch(taskInfo);
		} catch (InterruptedException e) {
			logger.error("=================DailySwitch is error:", e);
			e.printStackTrace();
		}
	}

	/**
	 * 日切处理
	 * 
	 * @param taskInfo
	 * @throws InterruptedException
	 */
	private void DailySwitch(TaskInfoDTO taskInfo) throws InterruptedException {

		SingleResult<Map<String, String>> paraResult = publicInfoService.queryParameterDailySwitchDateAndTime();
		if (!paraResult.isSuccess()) {
			logger.error("publicInfoService.queryParameterDailySwitchDateAndTime error:" + paraResult.getErrorHint());
		}
		Map<String, String> paraMap = paraResult.getResult();
		// 日切的结算日期
		String dailySwthchDate = paraMap.get(ParameterConstant.DAILYSWITCH_DATE);

		// 日切 的时间点
		String dailySwthchTime = paraMap.get(ParameterConstant.DAILYSWITCH_FRONT_TIME);

		QueryResult<MerchantInfoDTO> merchantResult = merchantInfoService.queryMerchantInfoListAll();
		List<Long> spIdList = null;
		List<Long> parentSpIds = new ArrayList<Long>();

		if (merchantResult.isSuccess()) {
			List<MerchantInfoDTO> merchantList = merchantResult.getResults();
			initParentSps(merchantList,parentSpIds);
			spIdList = new ArrayList<Long>();
			for (MerchantInfoDTO merchantInfoDTO : merchantList) {
				if(merchantInfoDTO.getMinLimitAmount() == null){
					merchantInfoDTO.setMinLimitAmount(0L);
				}
				if(merchantInfoDTO.getMinLimitAmount() != 0){
					continue;
				}
				spIdList.add(Long.valueOf(merchantInfoDTO.getSpId()));
			}
			// 日切处理
			DailySwitchHelper.DailySwitch(dailySwthchDate, dailySwthchTime, spIdList,parentSpIds);
		} else {
			logger.error("=======query queryMerchantInfoListAll is error,msg:" + merchantResult.getErrorHint());
		}

		// 新增执行日志
		taskInfoService.insetTaskExcuLog(taskInfo);
		// 更新记录
		taskInfoService.updateTaskInfo(taskInfo);

		logger.warn("=================================Daily Switch finish:========================");
	}

	private void initParentSps(List<MerchantInfoDTO> merchantList, List<Long> parentSpIds) {
		List<MerchantInfoDTO>removeList = new ArrayList<MerchantInfoDTO>();
		for (MerchantInfoDTO item : merchantList) {
			if(item != null && item.getSpId() != null && new Long(item.getSpId().longValue()).equals(item.getParentSpId())){
				removeList.add(item);
				parentSpIds.add(item.getSpId().longValue());
			}
		}
		if(removeList.size() >0 ){
			merchantList.removeAll(removeList);
		}
	}

}
