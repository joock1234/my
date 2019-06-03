/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.module.commons.Result;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.module.constant.ParameterConstant;
import com.szrhtf.module.enums.DelayTaskStateEnum;
import com.szrhtf.module.enums.DelayTaskTimeEnum;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.task.helper.DelayTaskHelper;
import com.szrhtf.utils.DailySwitchUtils;
import com.szrhtf.web.share.dto.TaskInfoDTO;
import com.szrhtf.web.share.interfaces.DelayTaskService;
import com.szrhtf.web.share.interfaces.MerchantInfoService;
import com.szrhtf.web.share.interfaces.PublicInfoService;
import com.szrhtf.web.share.query.DelayTaskQuery;

/**
 * 类DailySwitch.java的实现描述： 日切任务，主要是为上天数据的统计清算，结算
 * 
 * @author Sunney 2016年2月25日 上午9:24:43
 */
public class DailySwitchFrontJobImpl extends BaseTaskJobDetail {

    protected Logger    logger = LoggerFactory.getLogger(this.getClass());
    DelayTaskService    delayTaskService;
    MerchantInfoService merchantInfoService;
    PublicInfoService publicInfoService;

    /*
     * (non-Javadoc)
     * @see om.szrhtf.task.base.BaseTaskJobDetail#process(com.szrhtf.web.share.dto.TaskInfoDTO)
     */
    @Override
    public void process(TaskInfoDTO taskInfo) {
       
        merchantInfoService = Application.applicationContext.getBean("merchantInfoService", MerchantInfoService.class);
        publicInfoService=Application.applicationContext.getBean("publicInfoService", PublicInfoService.class);
        
        SingleResult<Map<String,String>> paraResult=publicInfoService.queryParameterDailySwitchDateAndTime();
        
        if(!paraResult.isSuccess()){
            logger.error("publicInfoService.queryParameterDailySwitchDateAndTime error:"+paraResult.getErrorHint());
        }
        Map<String,String> paraMap=paraResult.getResult();
        //获取日切日期
        String dailySwthchDate= paraMap.get(ParameterConstant.DAILYSWITCH_DATE);
        // 更新日切日期
        Result result=publicInfoService.updateParameterById(ParameterConstant.DAILYSWITCH_DATE, DailySwitchUtils.nextDailyDate(dailySwthchDate));
        if(!result.isSuccess()){
            logger.error("publicInfoService.updateParameterById is error,更新日切日期报错，请处理...............");
        }else{
            logger.info("======================================日切日期更新完成!");
        }
        
        //更新过期商户的状态
        DelayTaskHelper.updateMerchantStateByExpiresDate(merchantInfoService,dailySwthchDate);
        //查询要执行的任务
        DelayTaskQuery delayTaskQuery = new DelayTaskQuery();
        delayTaskQuery.setDelayState(DelayTaskStateEnum.WAIT_RUN.getKey());
        delayTaskQuery.setDelayTime(DelayTaskTimeEnum.DAILYSWITCH.getKey());
        DelayTaskHelper.delayTask(delayTaskQuery);
        
    }


}
