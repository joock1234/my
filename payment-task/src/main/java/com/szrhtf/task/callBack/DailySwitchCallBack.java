/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.callBack;

import com.szrhtf.module.commons.Result;

/**
 * 类DailySwitchCallBack.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年4月5日 下午1:22:23
 */
public interface DailySwitchCallBack {

    public void onreturn(Result result, Long spId, String date, String time);

    public void onthrow(Throwable ex, Long spId, String date, String time);

}
