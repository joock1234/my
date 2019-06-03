/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.szrhtf.module.commons.Result;
import com.szrhtf.module.utils.StringUtil;
import com.szrhtf.task.helper.DailySwitchHelper;
import com.szrhtf.web.share.dto.DailySwitchTaskDTO;
import com.szrhtf.web.share.interfaces.DailySwitchTaskService;

/**
 * 类DailySwitchTaskServiceImpl.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年6月17日 下午3:14:05
 */
@Service("dailySwitchTaskService")
public class DailySwitchTaskServiceImpl implements DailySwitchTaskService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /*
     * (non-Javadoc)
     * @see com.szrhtf.web.share.interfaces.DailySwitchTaskService#dailySwitchTask(com.szrhtf.web.share.dto.
     * DailySwitchTaskDTO)
     */
    @Override
    public Result dailySwitchTask(DailySwitchTaskDTO taskVO) {
        Result result = new Result();
        try {
        	
        	if(StringUtil.isEmpty(taskVO.getDailySwthchStartDate()) || StringUtil.isEmpty(taskVO.getDailySwthchEndDate())){
        		
        		logger.info("============================>" + JSON.toJSONString(taskVO));
        		
        		taskVO.setDailySwthchStartDate(taskVO.getDailySwthchDate());
        		taskVO.setDailySwthchEndDate(taskVO.getDailySwthchDate());
        	}
            DailySwitchHelper.AutoDailySwitch(taskVO);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            logger.error("dailySwitchTask is error", e);
        }

        return result;
    }

}
