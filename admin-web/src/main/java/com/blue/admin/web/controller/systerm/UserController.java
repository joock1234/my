package com.blue.admin.web.controller.systerm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.UserDTO;
import com.szrhtf.web.share.interfaces.UserService;
import com.szrhtf.web.share.query.UserQuery;

@Controller
public class UserController extends BaseController {

	  /**
     *
     * @return 用户列表页面
     */
    @RequestMapping(value = URL.user_query_list)
    @ResponseBody
    public Object queryUserList(HttpServletRequest request ,UserQuery query
    		,@RequestParam int page, @RequestParam int limit) {
    	query.setPageNum(page);
		query.setPageSize(limit);
        UserService userService = (UserService) getDubboBean(request, "userService");
        QueryResult<UserDTO> result=userService.queryUserList(query);
    	return respByPage(result.getResults(), result.getPageResult().getTotal());
    }
}
