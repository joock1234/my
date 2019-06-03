/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.share.interfaces.ProxyPayService;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

/**
 * 类ProxyPayJobImpl.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年4月6日 下午5:54:32
 */
public class ProxyPayJobImpl extends BaseTaskJobDetail {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    ProxyPayService  proxyPayService;

    /*
     * (non-Javadoc)
     * @see com.szrhtf.task.base.BaseTaskJobDetail#process(com.szrhtf.web.share.dto.TaskInfoDTO)
     */
    @Override
    public void process(TaskInfoDTO taskInfo) {

        proxyPayService = Application.applicationContext.getBean("proxyPayService", ProxyPayService.class);

        Runnable proxyPay = new Runnable() {

            @Override
            public void run() {
                proxyPayService.doSingleProxyPay();
                logger.info("========doSingleProxyPay runned");
            }
        };
        proxyPay.run();

    }

}
