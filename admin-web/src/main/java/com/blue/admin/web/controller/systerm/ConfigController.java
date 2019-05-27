package com.blue.admin.web.controller.systerm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.ConfigDTO;
import com.szrhtf.web.share.interfaces.ConfigService;
import com.szrhtf.web.share.query.ConfigQuery;

@Controller
public class ConfigController extends BaseController {

	/**
	 * 查询配置中心数据列表
	 * @return
	 */
	@RequestMapping(value=URL.config_query_list)
	@ResponseBody
	public Object queryConfigInfo(HttpServletRequest request ,ConfigQuery query
			,@RequestParam int page, @RequestParam int limit) {
		query.setPageNum(page);
		query.setPageSize(limit);
		ConfigService configService = (ConfigService)getDubboBean(request, "configService");
	    QueryResult<ConfigDTO> result = configService.queryList(query);
		return respByPage(result.getResults(), result.getPageResult().getTotal());
	}
}
