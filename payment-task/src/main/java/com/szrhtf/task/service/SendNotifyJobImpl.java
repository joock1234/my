package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.share.interfaces.NotifyService;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

/**
 * 类SendNotifyJobImpl.java的实现描述：异步通知商户交易结果
 * 
 * @author tulu 2016年4月28日 下午2:09:08
 */
public class SendNotifyJobImpl extends BaseTaskJobDetail {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void process(TaskInfoDTO taskInfo) {
        try {
            NotifyService notifyService = Application.applicationContext.getBean("notifyService",
                                                                                 NotifyService.class);

            notifyService.sendNoticeToMerchant();
            logger.info("SendNotifyJobImpl run is success............");
        } catch (Exception e) {
            logger.error("SendNotifyJobImpl process is error:", e);
        }
    }

}
