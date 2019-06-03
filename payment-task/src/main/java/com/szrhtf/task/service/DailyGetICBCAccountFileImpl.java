/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.module.enums.CorgeEnum;
import com.szrhtf.module.utils.DateUtil;
import com.szrhtf.share.interfaces.FileDownAccountService;
import com.szrhtf.share.transaction.vo.ChannelMsg;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

/**
 * 类DailyGetAccountFileImpl.java的实现描述： 获取对账文件
 * 
 * @author Sunney 2016年2月25日 上午9:24:43
 */
public class DailyGetICBCAccountFileImpl extends BaseTaskJobDetail {

    protected Logger    logger = LoggerFactory.getLogger(this.getClass());

    FileDownAccountService fileDownAccountService;
    
    /*
     * (non-Javadoc)
     * @see com.szrhtf.task.base.BaseTaskJobDetail#process(com.szrhtf.web.share.dto.TaskInfoDTO)
     */
    @Override
    public void process(TaskInfoDTO taskInfo) {
    	fileDownAccountService = Application.applicationContext.getBean("fileDownAccountService", FileDownAccountService.class);
    	try {
            DailyGetAccountFile(taskInfo);
        } catch (InterruptedException e) {
            logger.error("=================DailyGetWeiXinAccountFile is error:", e);
            e.printStackTrace();
        }
    }

    /**
     *获取对账文件
     * 
     * @param taskInfo
     * @throws InterruptedException
     */
    private void DailyGetAccountFile(TaskInfoDTO taskInfo) throws InterruptedException {
    	logger.warn("=================================Daily Switch ICBC start:========================");
    	String channlBankId = CorgeEnum.ICBC_PROXY.getValue();
    	ChannelMsg msg = new ChannelMsg();
    	msg.setChannelBank("ICBC");
    	msg.setBill_type("ALL");
    	msg.setBill_date(DateUtil.getRoundDateString().replaceAll("-", ""));//上一日日期yyyyMMdd
    	SingleResult<String> result = fileDownAccountService.getAccountFile(msg,channlBankId);
    	if(result.isSuccess()){
    		logger.info("=================DailyGetICBCAccountFile FileDown success!");
    	}else{
    		logger.info("=================DailyGetICBCAccountFile FileDown fail!");
    	}
        logger.warn("=================================Daily Switch ICBC end:========================");
    }

}
