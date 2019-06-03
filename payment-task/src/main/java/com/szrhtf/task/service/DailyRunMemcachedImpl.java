package com.szrhtf.task.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.constant.Constant;
import com.szrhtf.module.utils.DateUtil;
import com.szrhtf.share.memcache.MemcachedService;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.LiquidationDTO;
import com.szrhtf.web.share.dto.TaskInfoDTO;
import com.szrhtf.web.share.interfaces.TaskInfoService;
import com.szrhtf.web.share.interfaces.TransInfoService;
import com.szrhtf.web.share.query.LiquidationQuery;

/**
 * 每天跑缓存问题
 * @author zhanguohuang
 *
 */
public class DailyRunMemcachedImpl extends BaseTaskJobDetail {

	protected Logger    logger = LoggerFactory.getLogger(this.getClass());
	TaskInfoService     taskInfoService;
	TransInfoService	transInfoService;
	MemcachedService 	memcachedService;
	
	
	@Override
	public void process(TaskInfoDTO taskInfo) {
		logger.info("-=-=-=-=-=-=-=-=-=-=-=执行每日跑缓存任务=-=-=-=-=-=-=-=-=-");
		taskInfoService = Application.applicationContext.getBean("taskInfoService", TaskInfoService.class);
		transInfoService = Application.applicationContext.getBean("transInfoService", TransInfoService.class);
		memcachedService = Application.applicationContext.getBean("memcachedService", MemcachedService.class);
		// 新增执行日志	
		taskInfoService.insetTaskExcuLog(taskInfo);
		runExportMemcached(taskInfo);
        // 更新记录
        taskInfoService.updateTaskInfo(taskInfo);
        logger.info("-=-=-=-=-=-=-=-=-=-=-=每日跑缓存任务完成=-=-=-=-=-=-=-=-=-");
	}
	
	private void runExportMemcached(TaskInfoDTO taskInfo) {
		logger.info("----------------执行跑导出缓存任务----------------");
		/** 跑清分明细的数据 */
		getLiquidationCached();
		logger.info("----------------跑导出缓存任务完成----------------");
	}
	
	private void getLiquidationCached() {
		QueryResult<LiquidationDTO> result = new QueryResult<LiquidationDTO>();
		LiquidationQuery query = new LiquidationQuery();
		query.setDateType("1");
		query.setBeginDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		query.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		result = transInfoService.queryLiquidationInfo(query);
		if(result.isSuccess()) {
			memcachedService.add(Constant.MEMCACHE_LIQUIDATION_LIST + query.hashCode(), result.getResults(), DateUtil.getTodayRemainingSeconds());
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		/** 今天是周一的情况 */
		if(2 == cal.get(Calendar.DAY_OF_WEEK)) { 
			query.setDateType("1");
			cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -2);
			query.setBeginDate(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
			query.setEndDate(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			result = transInfoService.queryLiquidationInfo(query);
			if(result.isSuccess()) {
				memcachedService.add(Constant.MEMCACHE_LIQUIDATION_LIST + query.hashCode(), result.getResults(), DateUtil.getTodayRemainingSeconds());
			}
		}
	}
}
