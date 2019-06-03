/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.module.enums.DelayTaskStateEnum;
import com.szrhtf.module.enums.DelayTaskTimeEnum;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.task.helper.DelayTaskHelper;
import com.szrhtf.web.share.dto.TaskInfoDTO;
import com.szrhtf.web.share.interfaces.DelayTaskService;
import com.szrhtf.web.share.query.DelayTaskQuery;

/**
 * 类DelayTaskServiceImpl.java的实现描述： 定时扫描DelayTask表，对任务进行处理
 * 
 * @author Sunney 2016年3月7日 上午11:41:05
 */
public class DelayTaskJobImpl extends BaseTaskJobDetail {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    DelayTaskService delayTaskService;

    /*
     * (non-Javadoc)
     * @see om.szrhtf.task.base.BaseTaskJobDetail#process(com.szrhtf.web.share.dto.TaskInfoDTO)
     */
    @Override
    public void process(TaskInfoDTO taskInfo) {
        delayTaskService = Application.applicationContext.getBean("delayTaskService", DelayTaskService.class);
        DelayTaskQuery delayTaskQuery = new DelayTaskQuery();
        //实时要待执行的任务
        delayTaskQuery.setDelayState(DelayTaskStateEnum.WAIT_RUN.getKey());
        delayTaskQuery.setDelayTime(DelayTaskTimeEnum.REALTIME.getKey());
        
        DelayTaskHelper.delayTask(delayTaskQuery);
       
    }
   

}
