package com.blue.admin.web.controller.product.config;


import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.blue.admin.web.utils.DictionaryUtil;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.utils.StringUtil;
import com.szrhtf.web.share.dto.RateSchemaDTO;
import com.szrhtf.web.share.interfaces.RateSchemaService;
import com.szrhtf.web.share.query.RateSchemaQuery;

@Controller
public class RateSchemaController extends BaseController {


	/**
	 *  //查找费率方案列表
	 * @param request
	 * @param query
	 * @return
	 */
	 @RequestMapping(value = URL.rateSchema_query_list)
	 @ResponseBody
    public Object queryRateSchemaList(HttpServletRequest request,RateSchemaQuery query
    		,@RequestParam int page, @RequestParam int limit) {
		 query.setPageNum(page);
			query.setPageSize(limit);
        RateSchemaService rateSchemaService = (RateSchemaService) getDubboBean(request, "rateSchemaService");
         String applyTo=query.getApplyTo();
        if(StringUtils.isBlank(applyTo)){
        	applyTo="SP，CHANNEL，PRODUCT";
        }
        String applyTos[] = applyTo.split("，");
        if (applyTos.length > 0) {
            query.setApplyTo("");
            query.setApplyTos(applyTos);
        }
        QueryResult<RateSchemaDTO> result = rateSchemaService.queryRateSchemaList(query);
        return respByPage(result.getResults(), result.getPageResult().getTotal());
    }

	/**
     * 费率模板查找带回
     * */
    @RequestMapping(value = URL.rateschema_query_list_back)
    @ResponseBody
    public Object queryRateSchemaListBack(HttpServletRequest request,RateSchemaQuery query,
    		  @RequestParam int page, @RequestParam int limit) {
        RateSchemaService rateSchemaService = (RateSchemaService) getDubboBean(request, "rateSchemaService");
        QueryResult<RateSchemaDTO> result = rateSchemaService.queryRateSchemaList(query);
        return respByPage(result.getResults(), result.getPageResult().getTotal());
    }

}
