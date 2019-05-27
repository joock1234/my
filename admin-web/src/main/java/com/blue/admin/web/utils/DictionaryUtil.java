package com.blue.admin.web.utils;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.web.share.dto.AreaInfoDTO;
import com.szrhtf.web.share.dto.DictionaryDTO;
import com.szrhtf.web.share.interfaces.DictionaryService;
import com.szrhtf.web.share.interfaces.PublicInfoService;
import com.szrhtf.web.share.query.DictionaryQuery;

public class DictionaryUtil {

    /**
     * 根据表名和key值取value
     * 
     * @param tableName
     * @param key
     */
    public static String getValue(HttpServletRequest request, String table, String column, String key, String defaut) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        DictionaryService dictionaryService = (DictionaryService) webApplicationContext.getBean("dictionaryService");
        DictionaryQuery query = new DictionaryQuery();
        query.setTableInfo(table);
        query.setColumnInfo(column);
        query.setValue(key);
        SingleResult<DictionaryDTO> result = dictionaryService.queryDictionaryValueInfo(query);
        if (StringUtils.isEmpty(result.getResult()) || StringUtils.isEmpty(result.getResult().getDesc())) {
            return defaut;
        }
        return result.getResult().getDesc();
    }

    /**
     * 通过地区表ID获取对应地区名称
     * 
     * @param areaId 地区ID
     * @return String 地区名称
     */
    public static String getAreaValue(HttpServletRequest request, String areaId) {
        if (StringUtils.isEmpty(areaId)) {
            return "";
        }
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        PublicInfoService publicInfoService = (PublicInfoService) webApplicationContext.getBean("publicInfoService");
        SingleResult<AreaInfoDTO> result = publicInfoService.queryAreaById(areaId);
        if (StringUtils.isEmpty(result.getResult())) {
            return null;
        }
        return result.getResult().getName();
    }

    /***
     * 金额转换，分转换成元
     */
    public static String getMoney(Long money) {
        BigDecimal m = new BigDecimal(money);
        BigDecimal str = m.divide(new BigDecimal(100));
        return str + "";
    }

    /**
     * 传入一串KEY，从字典里获取一串value
     * 
     * @param key: k1,k2,k3
     * @return value: v1,v2,v3
     */
    public static String formatStrArray(HttpServletRequest request, String table, String column, String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String[] arr = key.split(",");
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        DictionaryService dictionaryService = (DictionaryService) webApplicationContext.getBean("dictionaryService");
        DictionaryQuery query = new DictionaryQuery();
        query.setTableInfo(table);
        query.setColumnInfo(column);
        Map<String, DictionaryDTO> map = dictionaryService.getMemcachedDictionaryMap(query);// 获取字典
        if (StringUtils.isEmpty(map) || 0 == map.size()) {
            return "";
        }
        StringBuffer sb = new StringBuffer("");
        for (String a : arr) {
            if (!StringUtils.isEmpty(a)) {
                DictionaryDTO info = map.get(a);
                if (!StringUtils.isEmpty(info)) {
                    sb.append(info.getDesc() + ",");
                }
            }
        }
        if (StringUtils.isEmpty(sb) || sb.length() < 1) {
            return "";
        }
        return sb.substring(0, sb.length() - 1);
    }
    
    /**
     * 当value为中文逗号分割的多个值时，传入任何一个可返回其desc
     * @param request
     * @param table
     * @param column
     * @param key
     * @param defaut
     * @return
     */
    public static String getValues(HttpServletRequest request, String table, String column, String key, String defaut) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
        DictionaryService dictionaryService = (DictionaryService) webApplicationContext.getBean("dictionaryService");
        DictionaryQuery query = new DictionaryQuery();
        query.setTableInfo(table);
        query.setColumnInfo(column);
        Map<String, DictionaryDTO> map = dictionaryService.getMemcachedDictionaryMap(query);
        if(!StringUtils.isEmpty(map)){
            Set<String> keySet = map.keySet();
            for(String str : keySet){
                String tmp[] = str.split("，");
                for (int i = 0; i < tmp.length; i++) {
                    if (key.equals(tmp[i])) {
                        return map.get(str).getDesc();
                    }
                }
            }
        }
        return defaut;
    }

}
