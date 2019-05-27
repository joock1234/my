package com.blue.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.utils.DictionaryUtil;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.module.utils.DateUtil;
import com.szrhtf.module.utils.StringUtil;
import com.szrhtf.share.dto.OrderInfoDto;
import com.szrhtf.share.interfaces.OrderInfoService;
import com.szrhtf.share.query.OrderBorrowingQuery;
import com.szrhtf.share.query.OrderInfoQquery;
import com.szrhtf.web.share.dto.ConfigDTO;
import com.szrhtf.web.share.interfaces.ConfigService;

@Controller
public class TransController extends BaseController {
    /**
     * 查询交易列表
     * @return 交易列表页面
     */
    @RequestMapping(value = URL.trans_query_list_url)
    public String queryTransList(HttpServletRequest request, OrderInfoQquery query) {
        OrderInfoService orderInfoService = (OrderInfoService) getDubboBean(request, "orderInfoService");
        if(StringUtils.isEmpty(query.getCreateDateStart())){
            query.setCreateDateStart(DateUtil.getDateString() + " 00:00:00");
        }
        if(StringUtils.isEmpty(query.getCreateDateEnd())){
            query.setCreateDateEnd(DateUtil.getDateString() + " 23:59:59");
        }
        if(!StringUtils.isEmpty(query.getOrderStates())){
            if (StringUtil.isEmpty(DictionaryUtil.getValue(request, "t_order", "order_state", query.getOrderStates(), ""))) {
                query.setOrderStates("");
            } else {
                String str[] = query.getOrderStates().split("，");
                query.setOrderStateArr(str);
            }
        }
        getOrderBorrowingQueryList(request, query);
        QueryResult<OrderInfoDto> result=orderInfoService.queryOrderInfoList(query);
        request.setAttribute("page", result.getPageResult());
        request.setAttribute("listInfo", result.getResults());
        request.setAttribute("query", query);
        request.getSession().setAttribute("statisticsQuery", query);
        return "trans/transList";
    }
    
    private void getOrderBorrowingQueryList(HttpServletRequest request, OrderInfoQquery query) {
        ConfigService configService = (ConfigService)getDubboBean(request, "configService");
        QueryResult<ConfigDTO> amountDto = configService.listModuleItems("amount");
        List<OrderBorrowingQuery> amountList = new ArrayList<OrderBorrowingQuery>();
        List<OrderBorrowingQuery> amountList0 = new ArrayList<OrderBorrowingQuery>();
        if (amountDto.isSuccess() && amountDto.getResults().size() > 0) {
            for (ConfigDTO dto : amountDto.getResults()) {
                String keys[] = dto.getTheKey().split("\\|");
                if (keys.length == 2) {
                    OrderBorrowingQuery orderBorrowingQuery = new OrderBorrowingQuery();
                    orderBorrowingQuery.setProductId(keys[0]);
                    orderBorrowingQuery.setTransType(keys[1]);
                    orderBorrowingQuery.setBorrowing(dto.getTheValue());
                    if ("-1".equals(dto.getTheValue())) {
                        amountList.add(orderBorrowingQuery);
                    } else if ("0".equals(dto.getTheValue())) {
                        amountList0.add(orderBorrowingQuery);
                    }
                    
                }
            }
        }
        query.setAmountList(amountList);
        query.setAmountList0(amountList0);
        
        QueryResult<ConfigDTO> feeDto = configService.listModuleItems("fee");
        List<OrderBorrowingQuery> feeList = new ArrayList<OrderBorrowingQuery>();
        List<OrderBorrowingQuery> feeList0 = new ArrayList<OrderBorrowingQuery>();
        if (feeDto.isSuccess() && feeDto.getResults().size() > 0) {
            for (ConfigDTO dto : feeDto.getResults()) {
                OrderBorrowingQuery orderBorrowingQuery = new OrderBorrowingQuery();
                String keys[] = dto.getTheKey().split("\\|");
                if (keys.length == 2) {
                    orderBorrowingQuery.setProductId(keys[0]);
                    orderBorrowingQuery.setTransType(keys[1]);
                    orderBorrowingQuery.setBorrowing(dto.getTheValue());
                    if ("-1".equals(dto.getTheValue())) {
                        feeList.add(orderBorrowingQuery);
                    } else if ("0".equals(dto.getTheValue())) {
                        feeList0.add(orderBorrowingQuery);
                    }
                }
            }
        }
        query.setFeeList(feeList);
        query.setFeeList0(feeList0);
    }

}
