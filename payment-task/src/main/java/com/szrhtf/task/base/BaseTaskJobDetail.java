/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.base;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.szrhtf.Application;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.task.QuartzManager;
import com.szrhtf.web.share.dto.TaskInfoDTO;
import com.szrhtf.web.share.interfaces.TaskInfoService;

/**
 * 类BaseTaskJobDetail.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年2月18日 下午5:10:00
 */
public abstract class BaseTaskJobDetail implements Job {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    TaskInfoService  taskInfoService;

    /*
     * (non-Javadoc)
     * @see org.quartz.Job#execute(org.quartz.JobExecutionContext)
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String jobName = context.getJobDetail().getKey().getName();
        String groupName = context.getJobDetail().getKey().getGroup();
        String triggerName = context.getTrigger().getKey().getName();
        String triggerGroupName = context.getTrigger().getKey().getGroup();
        taskInfoService = Application.applicationContext.getBean("taskInfoService", TaskInfoService.class);
        SingleResult<TaskInfoDTO> result = taskInfoService.queryTaskInfoByTaskNameAndGroupNameWithCached(jobName,
                                                                                                         groupName);
        if (result.isSuccess()) {
            TaskInfoDTO taskInfo = result.getResult();
            try {
                logger.info("------" + jobName + "----" + groupName + "-------start-----");
                if (null == taskInfo || taskInfo.getStatus() != 1) {
                    QuartzManager.removeJob(jobName, groupName, triggerName, triggerGroupName);
                    logger.info("------" + jobName + "----" + groupName + "--------remove succeed-----");
                } else {
                    process(taskInfo);
                }
                logger.info("------" + jobName + "----" + groupName + "--------end-----");
                // taskInfo.setErrorMsg("excu succeed");
            } catch (Exception e) {
            	logger.error("定时任务执行失败", e);
                // taskInfo.setErrorMsg(e.getMessage());
            } finally {
                // taskManageService.updateTaskExcuTimeByNameAndGroupName(taskInfo);
            }
        }
    }

    public abstract void process(TaskInfoDTO taskInfo);

}
