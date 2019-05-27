package com.blue.admin.web.controller.systerm;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.constants.URL;
import com.blue.admin.web.controller.BaseController;
import com.szrhtf.module.commons.QueryResult;
import com.szrhtf.web.share.dto.RoleDTO;
import com.szrhtf.web.share.interfaces.RoleService;

@Controller
public class RoleController extends BaseController {


	/**
     * 查询角色列表
     *
     * @return 角色列表页面
     */
    @RequestMapping(value = URL.role_query_list)
    @ResponseBody
    public Object queryRoleList(HttpServletRequest request) {
        RoleService roleService = (RoleService) getDubboBean(request, "roleService");
        QueryResult<RoleDTO> result = roleService.queryRoleList();
        return respSucceed(result.getResults());
    }

}
