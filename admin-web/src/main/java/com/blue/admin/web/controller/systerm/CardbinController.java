package com.blue.admin.web.controller.systerm;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.CardbinInfoDTO;
import com.szrhtf.web.share.interfaces.CardbinInfoService;
import com.szrhtf.web.share.query.CardbinInfoQuery;

@Controller
public class CardbinController extends BaseController {

	Logger logger = LoggerFactory.getLogger(CardbinController.class);

	@RequestMapping(value = URL.cardbin_search_list)
	@ResponseBody
	public Object search(HttpServletRequest request, CardbinInfoQuery query
			,@RequestParam int page, @RequestParam int limit) {
		query.setPageNum(page);
		query.setPageSize(limit);
		CardbinInfoService service = (CardbinInfoService) getDubboBean(request,"cardbinInfoService");
		QueryResult<CardbinInfoDTO> result = service.search(query);
		return respByPage(result.getResults(), result.getPageResult().getTotal());
	}
}
