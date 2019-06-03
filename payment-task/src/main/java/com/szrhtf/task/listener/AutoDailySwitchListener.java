/*
 * Copyright 1999-2024 Colotnet.com All right reserved. This software is the confidential and proprietary information of
 * Colotnet.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with Colotnet.com.
 */
package com.szrhtf.task.listener;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.share.interfaces.DailySwitchService;
import com.szrhtf.task.helper.DailySwitchHelper;
import com.szrhtf.task.vo.TaskVO;
import com.szrhtf.web.share.dto.MerchantInfoDTO;
import com.szrhtf.web.share.interfaces.MerchantInfoService;

import io.github.xdiamond.client.annotation.OneKeyListener;
import io.github.xdiamond.client.event.ConfigEvent;
import io.github.xdiamond.client.event.EventType;

/**
 * 类MyServletContextListener.java的实现描述：TODO 类实现描述
 * 
 * @author Sunney 2016年4月5日 上午10:35:28
 */
@Service
public class AutoDailySwitchListener {

    protected Logger                 logger                  = LoggerFactory.getLogger(this.getClass());
    public final static String       ALL                     = "ALL";
    public final static String       DEFAULT_DAILYSWTHCHTIME = "23:30:00";
    @Autowired
    MerchantInfoService              merchantInfoService;
    @Autowired
    public static DailySwitchService dailySwitchService;

    @OneKeyListener(key = "AutoDailySwitchConfigKeyListener")
    public void autoDailySwitchConfigKeyListener(ConfigEvent event) {
        logger.info("ListenerExampleService, testOneKeyListener, event :" + event);

        try {
            // 如果删除操作就不处理直接返回
            if (event.getEventType().equals(EventType.DELETE)) {
                logger.info("=====autoDailySwitchKeyListener,EventType:" + EventType.DELETE + " return");
                return;
            } else {
                String value = event.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    TaskVO taskVO = JSON.parseObject(value, TaskVO.class);
                    AutoDailySwitch(taskVO);
                }
            }

        } catch (Exception ex) {
            logger.error("=====autoDailySwitchConfigKeyListener error:" + ex);
        }
    }

    /**
     * @param event
     */
    @OneKeyListener(key = "AutoSettlementKeyListener")
    public void autoSettlementKeyListener(ConfigEvent event) {
        logger.info("autoSettlementKeyListener, event :" + event);
        try {
            // 如果删除操作就不处理直接返回
            if (event.getEventType().equals(EventType.DELETE)) {
                logger.info("=====autoDailySwitchKeyListener,EventType:" + EventType.DELETE + " return");
                return;
            } else {
                String value = event.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    TaskVO taskVO = JSON.parseObject(value, TaskVO.class);
                    AutoSettlement(taskVO);
                }
            }

        } catch (Exception ex) {
            logger.error("=====autoDailySwitchConfigKeyListener error:" + ex);
        }

    }

    /**
     * 日切
     * 
     * @param taskVO
     */
    private void AutoDailySwitch(TaskVO taskVO) {
        List<Long> spIdList = null;
        List<Long> parentSpIds = null;
        if (taskVO.getSpId().toUpperCase().equals(ALL)) {
            // 所有商户日切
            QueryResult<MerchantInfoDTO> merchantResult = merchantInfoService.queryMerchantInfoListAll();
            if (merchantResult.isSuccess()) {
                List<MerchantInfoDTO> merchantList = merchantResult.getResults();
                initParentSps(merchantList, parentSpIds);
                spIdList = new ArrayList<Long>();
                for (MerchantInfoDTO merchantInfoDTO : merchantList) {
                    spIdList.add(Long.valueOf(merchantInfoDTO.getSpId()));
                }
            }

        } else {
            // 单个商户日切
            spIdList = new ArrayList<Long>();
            spIdList.add(Long.valueOf(taskVO.getSpId()));
            parentSpIds = new ArrayList<Long>();
        }
        String dailySwthchTime = taskVO.getDailySwthchTime();
        if (StringUtils.isEmpty(dailySwthchTime)) {
            dailySwthchTime = DEFAULT_DAILYSWTHCHTIME;
        }
        DailySwitchHelper.DailySwitch(taskVO.getDailySwthchDate(), dailySwthchTime, spIdList,parentSpIds);
    }
    
    private void initParentSps(List<MerchantInfoDTO> merchantList, List<Long> parentSpIds) {
		List<MerchantInfoDTO>removeList = new ArrayList<MerchantInfoDTO>();
		for (MerchantInfoDTO item : merchantList) {
			if(item != null && item.getSpId() != null && new Long(item.getSpId().longValue()).equals(item.getParentSpId())){
				removeList.add(item);
				parentSpIds.add(item.getSpId().longValue());
			}
		}
		if(removeList.size() >0 ){
			merchantList.removeAll(removeList);
		}
	}

    /**
     * 只结算，不清算，
     * 
     * @param taskVO
     */
    private void AutoSettlement(TaskVO taskVO) {
        List<Long> spIdList = null;
        if (taskVO.getSpId().toUpperCase().equals(ALL)) {
            // 所有商户日切
            QueryResult<MerchantInfoDTO> merchantResult = merchantInfoService.queryMerchantInfoListAll();
            if (merchantResult.isSuccess()) {
                List<MerchantInfoDTO> merchantList = merchantResult.getResults();
                spIdList = new ArrayList<Long>();
                for (MerchantInfoDTO merchantInfoDTO : merchantList) {
                    spIdList.add(Long.valueOf(merchantInfoDTO.getSpId()));
                }
            }

        } else {
            // 单个商户日切
            spIdList = new ArrayList<Long>();
            spIdList.add(Long.valueOf(taskVO.getSpId()));

        }
        String dailySwthchTime = taskVO.getDailySwthchTime();
        if (StringUtils.isEmpty(dailySwthchTime)) {
            dailySwthchTime = DEFAULT_DAILYSWTHCHTIME;
        }
        DailySwitchHelper.settlement(spIdList, taskVO.getDailySwthchDate());

    }

}
