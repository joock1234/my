/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the
 * confidential and proprietary information of Colotnet.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Colotnet.com.
 */
package com.szrhtf.task.helper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.szrhtf.Application;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.commons.Result;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.module.enums.DelayTaskStateEnum;
import com.szrhtf.module.enums.DelayTaskTypeEnum;
import com.szrhtf.share.interfaces.BankService;
import com.szrhtf.share.transaction.vo.Msg;
import com.szrhtf.web.share.dto.DelayTaskDTO;
import com.szrhtf.web.share.interfaces.DelayTaskService;
import com.szrhtf.web.share.interfaces.MerchantInfoService;
import com.szrhtf.web.share.query.DelayTaskQuery;

/**
 * 类DelayTaskHelper.java的实现描述：TODO 类实现描述 
 * @author Sunney 2016年3月17日 上午11:40:09
 */
public class DelayTaskHelper {
    public  static Logger logger = LoggerFactory.getLogger(DelayTaskHelper.class);
   

    /**
     * 渠道签名
     */
    public static void delayTask(DelayTaskQuery delayTaskQuery) {
        DelayTaskService    delayTaskService = Application.applicationContext.getBean("delayTaskService", DelayTaskService.class);
        QueryResult<DelayTaskDTO> result = delayTaskService.queryDelayTaskList(delayTaskQuery);
        if (result.isSuccess()) {
            List<DelayTaskDTO> delgyList = result.getResults();
            for (DelayTaskDTO delayTaskDTO : delgyList) {
                
                DelayTaskTypeEnum delayTaskType = DelayTaskTypeEnum.valueOf(delayTaskDTO.getDelayType());
                logger.info("delayTaskDTO.getDelayType():"+delayTaskDTO.getDelayType());
                switch (delayTaskType) {
                    
                    case CHANNEL_SIGNIN:
                        channelSingin(delayTaskService,delayTaskDTO);
                        break;
                    // default is DailySwitchFront
                    default:
                        dailySwitchRate(delayTaskService, delayTaskDTO);
                        break;
                }
            }
        }
    }
    /**
     * 渠道签名
     */
    public static void channelSingin(DelayTaskService delayTaskService,DelayTaskDTO delayTask) {
        try {
            logger.info("======================channelSingin in");
            BankService bankService=Application.applicationContext.getBean("bankService", BankService.class);
            String content = delayTask.getJsonContent();
            JSONObject json = JSON.parseObject(content);
            Integer channelId = json.getInteger("id");
            //调用签名接口
            SingleResult<Msg> result= bankService.deductMoney(channelId,json);
            if(!result.isSuccess()){
                logger.error("channelSingin is fail,error:"+result.getErrorCode());
            }
            logger.info("channelSingin is success channelId:"+channelId);
            delayTaskService.updateDelayTaskState(delayTask.getSn(), DelayTaskStateEnum.DONE.getKey());
        } catch (Exception e) {
            logger.error("channelSingin is error:",e);
        }
    }
    /**
     * 更新已过期的商户状态
     * @param merchantInfoService
     * @param expiresDate
     */
    public  static void updateMerchantStateByExpiresDate(MerchantInfoService merchantInfoService ,String expiresDate) {
        try {
            Result result = merchantInfoService.updateMerchantStateByExpiresDate(expiresDate);
            if (!result.isSuccess()) {
                logger.error("====updateMerchantStateByExpiresDate is error ,expiresDate:"+expiresDate);
            }else{
                logger.info("====updateMerchantStateByExpiresDate is success ,expiresDate:"+expiresDate);
            }
        } catch (Exception e) {
            logger.error("channelSingin is error:", e);
        }
    }
    /**
     * 费率变更
     * 
     * @param delayTaskService
     * @param delayTask
     */
    public static void dailySwitchRate(DelayTaskService delayTaskService, DelayTaskDTO delayTask) {
        logger.info("======================================================executeRateChange===================");
        try {
            Result result = delayTaskService.executeRateChange(delayTask);
            if (result.isSuccess()) {
                logger.info("===============dailySwitchRate 任务：" + delayTask.getSn() + " 执行完成");
            }
        } catch (Exception e) {
            logger.error("dailySwitchRate is error:", e);
        }
    }

}
