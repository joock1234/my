package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.share.interfaces.ChannelBalCheckJob;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

public class ChannelBalCheckJobImpl extends BaseTaskJobDetail{

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ChannelBalCheckJob orderCheckJob;

	@Override
	public void process(TaskInfoDTO taskInfo) {
		orderCheckJob = Application.applicationContext.getBean("channelBalCheckJob", ChannelBalCheckJob.class);
		orderCheckJob.queryBal();
	}

}
