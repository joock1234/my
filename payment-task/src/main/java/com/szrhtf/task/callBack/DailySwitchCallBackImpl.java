/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.callBack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.szrhtf.module.commons.Result;
import com.szrhtf.share.interfaces.DailySwitchService;
import com.szrhtf.utils.DailySwitchUtils;

/**
 * 类DailySwitchCallBackImpl.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年4月5日 下午1:22:35
 */
public class DailySwitchCallBackImpl implements DailySwitchCallBack {

    protected Logger   logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    DailySwitchService dailySwitchService;

    /*
     * (non-Javadoc)
     * @see com.szrhtf.task.callBack.DailySwitchCallBack#onreturn(java.lang.Long)
     */
    @Override
    public void onreturn(Result result, Long spId, String date, String time) {
        DailySwitchUtils.currentCountMap.remove(spId);
        if (result.isSuccess()) {
            logger.info("++++++++++++++++onreturn is successed :result:" + result.isSuccess() + " spId:"
                        + spId);
          
        } else {
            logger.error("====onreturn is failed :result:" + result.isSuccess() + " error:" + result.getErrorHint()
                        + " spId:" + spId);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.szrhtf.task.callBack.DailySwitchCallBack#onthrow(java.lang.Throwable, java.lang.Integer)
     */
    @Override
    public void onthrow(Throwable ex, Long spId, String date, String time) {
        // TODO Auto-generated method stub
        logger.error("++++++++++++onthrow spId:" + spId, ex);
        DailySwitchUtils.currentCountMap.remove(spId);
    }

}
