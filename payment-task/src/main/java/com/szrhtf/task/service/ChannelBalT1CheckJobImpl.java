package com.szrhtf.task.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.szrhtf.Application;
import com.szrhtf.share.interfaces.ChannelBalT1CheckJob;
import com.szrhtf.task.base.BaseTaskJobDetail;
import com.szrhtf.web.share.dto.TaskInfoDTO;

public class ChannelBalT1CheckJobImpl extends BaseTaskJobDetail{

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	private ChannelBalT1CheckJob channelBalT1CheckJob;

	@Override
	public void process(TaskInfoDTO taskInfo) {
		channelBalT1CheckJob = Application.applicationContext.getBean("channelBalT1CheckJob", ChannelBalT1CheckJob.class);
		channelBalT1CheckJob.queryBal();
	}

}
