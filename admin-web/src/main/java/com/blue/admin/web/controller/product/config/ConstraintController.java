package com.blue.admin.web.controller.product.config;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.ConstraintDTO;
import com.szrhtf.web.share.interfaces.ConstraintService;
import com.szrhtf.web.share.query.ConstraintQuery;

@Controller
public class ConstraintController extends BaseController {


	 /**
     * 约束列表页面
     * @return
     */
    @RequestMapping(value = URL.constraint_query_list)
    @ResponseBody
    public Object queryConstraintList(HttpServletRequest request,ConstraintQuery query
    		,@RequestParam int page, @RequestParam int limit) {
    	query.setPageNum(page);
		query.setPageSize(limit);
    	ConstraintService constraintService = (ConstraintService) getDubboBean(request, "constraintService");
    	QueryResult<ConstraintDTO> result = constraintService.queryConstraintList(query);
    	request.setAttribute("page", result.getPageResult());
        request.setAttribute("listInfo", result.getResults());
        return respByPage(result.getResults(), result.getPageResult().getTotal());
    }



	 /**
     * 去约束列表页面查找带回
     * @return
     */
    @RequestMapping(value = URL.constraint_query_list_back)
    @ResponseBody
    public Object queryConstraintListBack(HttpServletRequest request,ConstraintQuery query, @RequestParam int page, @RequestParam int limit) {
    	ConstraintService constraintService = (ConstraintService) getDubboBean(request, "constraintService");
    	QueryResult<ConstraintDTO> result = constraintService.queryConstraintList(query);
    	request.setAttribute("page", result.getPageResult());
        request.setAttribute("listInfo", result.getResults());
        System.out.println(JSONObject.toJSONString(result.getResults()));
        return respByPage(result.getResults(), result.getPageResult().getTotal());
    }
}
