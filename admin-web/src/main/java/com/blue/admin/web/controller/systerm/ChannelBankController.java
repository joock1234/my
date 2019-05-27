package com.blue.admin.web.controller.systerm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.ChannelBankDTO;
import com.szrhtf.web.share.interfaces.ChannelBankService;
import com.szrhtf.web.share.query.ChannelBankQuery;

@Controller
public class ChannelBankController extends BaseController {

    /**
     * 银行列表页面
     * @return
     */
    @RequestMapping(value = URL.channelBank_query_list)
    @ResponseBody
    public Object queryChannelBankList(HttpServletRequest request,ChannelBankQuery query
    		,@RequestParam int page, @RequestParam int limit) {
    	query.setPageNum(page);
		query.setPageSize(limit);
    	ChannelBankService channelBankService = (ChannelBankService) getDubboBean(request, "channelBankService");
    	QueryResult<ChannelBankDTO> result = channelBankService.queryChannelBankList(query);
    	return respByPage(result.getResults(), result.getPageResult().getTotal());
    }
}
