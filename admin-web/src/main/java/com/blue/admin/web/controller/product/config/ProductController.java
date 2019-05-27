package com.blue.admin.web.controller.product.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.util.JSONUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.blue.admin.web.utils.DictionaryUtil;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.commons.Result;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.web.share.dto.DictionaryDTO;
import com.szrhtf.web.share.dto.ProductInfoDTO;
import com.szrhtf.web.share.interfaces.DictionaryService;
import com.szrhtf.web.share.interfaces.ProductInfoService;
import com.szrhtf.web.share.query.DictionaryQuery;
import com.szrhtf.web.share.query.ProductInfoQuery;

@Controller
public class ProductController extends BaseController {


    /**
     * 查询产品列表
     */
    @RequestMapping(value = URL.product_query_list_url)
    @ResponseBody
    public Object querytransList(HttpServletRequest request,
            ProductInfoQuery productInfoQuery,
            @RequestParam int page, @RequestParam int limit
            ) {
        productInfoQuery.setPageNum(page);
        productInfoQuery.setPageSize(limit);
        ProductInfoService productInfoService = (ProductInfoService) getDubboBean(request, "productInfoService");
        QueryResult<ProductInfoDTO> result = productInfoService.queryProductInfoList(productInfoQuery);
        List<ProductInfoDTO> list= result.getResults();
        List<ProductInfoDTO> respList= new ArrayList<>();
        for (ProductInfoDTO p:  list) {
             String temp= DictionaryUtil.formatStrArray(request, "t_order", "Ftrans_type", p.getAuthedTransType());
             p.setAuthedTransType(temp);
             respList.add(p);
        }
        return respByPage(respList, result.getPageResult().getTotal());
    }

    /**
     *
     * @return 修改产品信息页面
     */
    @RequestMapping(value = URL.product_to_update_url)
    @ResponseBody
    public Object goUpdateProductInfo(HttpServletRequest request,
    		@RequestParam String spId) {
        ProductInfoService productInfoService = (ProductInfoService) getDubboBean(request, "productInfoService");
        SingleResult<ProductInfoDTO> result = productInfoService.queryProductInfoById(spId);
        String transType=result.getResult().getAuthedTransType();
        if(StringUtils.isNotEmpty(transType)){
        	     String [] transTypes = result.getResult().getAuthedTransType().split(",");
                 result.getResult().setAuthedTransTypes(transTypes);
                 String temp= DictionaryUtil.formatStrArray(request, "t_order", "Ftrans_type", transType);
                 result.getResult().setAuthedTransType(temp);
        }
        DictionaryQuery query = new DictionaryQuery();
    	query.setTableInfo("t_order");
    	query.setColumnInfo("Ftrans_type");
    	DictionaryService dictionaryService = (DictionaryService) getDubboBean(request, "dictionaryService");
    	QueryResult<DictionaryDTO> transTypeResult = dictionaryService.queryDictionarySelectList(query);

    	Map<String, Object> map=new HashMap<String, Object>();
    	map.put("info", result.getResult());
    	map.put("transTypeList", transTypeResult.getResults());
        return respSucceed(map);
    }

    /**
     * 修改产品信息
     * @param
     * @return 提示信息
     */
    @RequestMapping(value = URL.product_update_url)
    @ResponseBody
    public Object updateProductInfo(HttpServletRequest request, ProductInfoDTO productinfo,@RequestParam("authedTransTypes_temp[]")String[] authedTransTypes_temp) {
        ProductInfoService productInfoService = (ProductInfoService) getDubboBean(request, "productInfoService");
        productinfo.setAuthedTransTypes(authedTransTypes_temp);
        Result result = productInfoService.updateProductInfo(productinfo);
        if (result.isSuccess()) {
            return respSucceed(null);
        }
        return respFail("修改失败");
    }
}
