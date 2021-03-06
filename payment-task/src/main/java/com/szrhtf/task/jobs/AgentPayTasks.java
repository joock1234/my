/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the
 * confidential and proprietary information of Colotnet.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Colotnet.com.
 */
package com.szrhtf.task.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 类AgentPayTasks.java的实现描述：代付任务
 * @author Sunney 2016年4月6日 上午10:31:28
 */
@Component
public class AgentPayTasks {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Scheduled(fixedRate = 50000)
    public void taskStart() {
        
    }
}
