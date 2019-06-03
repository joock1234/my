package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.share.interfaces.TransService;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

public class UnknownPayQueryJobImpl extends BaseTaskJobDetail {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	TransService transService;

	@Override
	public void process(TaskInfoDTO taskInfo) {
		logger.info("UnknownPayQueryJobImpl job start to process !");
		transService = (TransService) Application.applicationContext.getBean("transService", TransService.class);
		Runnable unkownQueryRunable = new UnkownPayQueryThread();
		unkownQueryRunable.run();
		logger.info("UnknownPayQueryJobImpl job  process end !");
	}

	/**
	 * 向上游查询待支付订单状态
	 * 
	 * @author CK
	 *
	 */
	class UnkownPayQueryThread implements Runnable {

		@Override
		public void run() {
			logger.info("unkown pay query thread started !");
			try {
				transService.unknownPayInfoQuery();
			} catch (Exception e) {
				logger.error("查询待支付订单结果失败", e);
			}
			logger.info("unkown pay query thread end !");
		}
	}
}
