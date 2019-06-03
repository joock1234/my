/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.szrhtf.module.enums.DelayTaskStateEnum;
import com.szrhtf.module.enums.DelayTaskTimeEnum;
import com.szrhtf.task.helper.DelayTaskHelper;
import com.szrhtf.web.share.interfaces.DelayTaskService;
import com.szrhtf.web.share.interfaces.MerchantInfoService;
import com.szrhtf.web.share.query.DelayTaskQuery;

import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import io.github.xdiamond.client.event.EventType;

/**
 * 类MyServletContextListener.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年4月5日 上午10:35:28
 */
@Service
public class AutoDailySwitchFrontListener {

    protected Logger                 logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DelayTaskService                 delayTaskService;
    @Autowired
    MerchantInfoService              merchantInfoService;

    @OneKeyListener(key = "AutoDailySwitchKeyFrontListener")
    public void autoDailySwitchFrontListener(ConfigEvent event) {
        if (!event.getEventType().equals(EventType.DELETE)) {
            String dailySwthchDate=event.getValue();
            //更新过期商户的状态
            DelayTaskHelper.updateMerchantStateByExpiresDate(merchantInfoService,dailySwthchDate);
            //查询要执行的任务
            DelayTaskQuery delayTaskQuery = new DelayTaskQuery();
            delayTaskQuery.setDelayState(DelayTaskStateEnum.WAIT_RUN.getKey());
            delayTaskQuery.setDelayTime(DelayTaskTimeEnum.DAILYSWITCH.getKey());
            DelayTaskHelper.delayTask(delayTaskQuery);
        }

    }

}
