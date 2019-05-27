package com.blue.admin.web.controller.systerm;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.DictionaryDTO;
import com.szrhtf.web.share.interfaces.DictionaryService;
import com.szrhtf.web.share.query.DictionaryQuery;

@Controller
public class DictionaryController extends BaseController {

	 /**
     * 查询字典数据
     *
     * @return
     */
    @RequestMapping(value =URL.dictionary_query_list)
    @ResponseBody
    public Object queryDictionaryInfo(HttpServletRequest request,DictionaryQuery query
    		,@RequestParam int page, @RequestParam int limit) {
    	query.setPageNum(page);
		query.setPageSize(limit);
        DictionaryService dictionaryService = (DictionaryService) getDubboBean(request, "dictionaryService");
        //DictionaryQuery query = new DictionaryQuery();
        // PageNum=0时返回所有记录
        QueryResult<DictionaryDTO> result = dictionaryService.queryList(query);
        List<DictionaryDTO> listInfo = result.getResults();
        return respByPage(listInfo, result.getPageResult().getTotal());
    }


	 /**
     * 通过表名，列名获取查询的数据字典
     *
     * @param table 表名，column 列名
     * @return 数据字典
     */
    @ResponseBody
    @RequestMapping(value = URL.dictionary_get_item)
    public Object getDictionaryItem(HttpServletRequest request, String table, String column) {
    	DictionaryQuery query = new DictionaryQuery();
    	query.setTableInfo(table);
    	query.setColumnInfo(column);
    	DictionaryService dictionaryService = (DictionaryService) getDubboBean(request, "dictionaryService");
    	QueryResult<DictionaryDTO> result = dictionaryService.queryDictionarySelectList(query);
    	System.out.println(JSONObject.toJSONString(result.getResults()));
        return respSucceed(result.getResults());
    }

}
