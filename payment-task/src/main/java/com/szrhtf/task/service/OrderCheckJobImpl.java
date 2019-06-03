package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.share.interfaces.OrderCheckJob;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

public class OrderCheckJobImpl extends BaseTaskJobDetail{

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private OrderCheckJob orderCheckJob;

	@Override
	public void process(TaskInfoDTO taskInfo) {
		orderCheckJob = Application.applicationContext.getBean("orderCheckJob", OrderCheckJob.class);
		orderCheckJob.checkOrder();
	}

}
