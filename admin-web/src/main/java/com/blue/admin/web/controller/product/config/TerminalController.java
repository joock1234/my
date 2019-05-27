package com.blue.admin.web.controller.product.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.TerminalDTO;
import com.szrhtf.web.share.interfaces.TerminalService;
import com.szrhtf.web.share.query.TerminalQuery;

@Controller
public class TerminalController extends BaseController {

	/**
	 * 终端列表页面
	 */
	@RequestMapping(value = URL.terminal_query_list)
	@ResponseBody
	public Object queryTerminalList(HttpServletRequest request, TerminalQuery query
			,@RequestParam int page, @RequestParam int limit) {
		query.setPageNum(page);
		query.setPageSize(limit);
		TerminalService terminalService = (TerminalService) getDubboBean(request, "terminalService");
		QueryResult<TerminalDTO> result = terminalService.queryTerminalList(query);
		return respByPage(result.getResults(), result.getPageResult().getTotal());
	}
}
