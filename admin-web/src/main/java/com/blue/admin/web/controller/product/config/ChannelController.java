package com.blue.admin.web.controller.product.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.ChannelDTO;
import com.szrhtf.web.share.interfaces.ChannelService;
import com.szrhtf.web.share.query.ChannelQuery;

@Controller
public class ChannelController extends BaseController {
    /**
     * 渠道列表页面
     * @return
     */
    @RequestMapping(value = URL.channel_query_list)
    @ResponseBody
    public Object queryChannelList(HttpServletRequest request,ChannelQuery query
    		,@RequestParam int page, @RequestParam int limit) {
    	query.setPageNum(page);
		query.setPageSize(limit);
    	ChannelService channelService = (ChannelService) getDubboBean(request, "channelService");
    	QueryResult<ChannelDTO> result = channelService.queryChannelList(query);
    	request.setAttribute("page", result.getPageResult());
        request.setAttribute("listInfo", result.getResults());
        return respByPage(result.getResults(), result.getPageResult().getTotal());
    }
}
