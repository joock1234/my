/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.jobs;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.task.QuartzManager;
import com.szrhtf.web.share.dto.TaskInfoDTO;
import com.szrhtf.web.share.interfaces.TaskInfoService;

/**
 * 类ScheduledTasks.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年2月18日 下午5:53:27
 */
@Component
public class ScheduledTasks {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TaskInfoService  taskInfoService;

    @Scheduled(fixedRate = 50000)
    public void taskStart() {
        QueryResult<TaskInfoDTO> result = taskInfoService.queryTaskInfoAll();
        //判断是否成功
        if (result.isSuccess()) {
            List<TaskInfoDTO> taskList = result.getResults();
            for (TaskInfoDTO taskInfo : taskList) {
                if (taskInfo.getStatus() == 1) {
                    boolean flag = QuartzManager.checkExists(taskInfo.getJobName(), taskInfo.getJobGroup());
                    if (!flag) {
                        QuartzManager.addJob(taskInfo.getJobName(), taskInfo.getJobGroup(), taskInfo.getTriggerName(),
                                             taskInfo.getTriggerGroupName(), taskInfo.getProcessClass(),
                                             taskInfo.getCronExpression());
                        logger.info("------" + taskInfo.getJobName() + "----" + taskInfo.getJobGroup()
                                    + "-------add succeed-----");
                    }
                }
            }
        }else{
            logger.error("call taskInfoService.queryTaskInfoAllWithCached error");
        }
    }
}
