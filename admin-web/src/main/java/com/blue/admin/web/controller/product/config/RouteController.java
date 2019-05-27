package com.blue.admin.web.controller.product.config;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.RouteInfoDTO;
import com.szrhtf.web.share.dto.RuleInfoDTO;
import com.szrhtf.web.share.interfaces.RouteInfoService;
import com.szrhtf.web.share.query.RouteInfoQuery;

@Controller
public class RouteController extends BaseController {


	/***
	 *路由查询 列表
	 */
	@RequestMapping(value=URL.route_query_list)
	@ResponseBody
	public Object queryRouteList(HttpServletRequest request,RouteInfoQuery query
			,@RequestParam int page, @RequestParam int limit){
		query.setPageNum(page);
		query.setPageSize(limit);
		RouteInfoService routeService = (RouteInfoService) getDubboBean(request, "routeInfoService");
		QueryResult<RouteInfoDTO> result=routeService.queryRouteInfoList(query);
		return respByPage(result.getResults(), result.getPageResult().getTotal());
	}

	 /**
     * 路由规则列表
     * */
    @RequestMapping(value = URL.rule_query_list)
    @ResponseBody
    public Object queryRuleList(HttpServletRequest request,RouteInfoQuery query){
    	RouteInfoService routeService = (RouteInfoService) getDubboBean(request, "routeInfoService");
    	QueryResult<RuleInfoDTO> result = routeService.queryRuleInfoList(query);
	    return respByPage(result.getResults(), result.getPageResult().getTotal());
    }
}
