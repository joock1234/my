package com.blue.admin.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blue.admin.web.dto.UserInfoDto;
import com.blue.admin.web.service.UserInfoService;
import com.github.pagehelper.Page;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

      @Autowired
      private UserInfoService userInfoService;

      @RequestMapping("/doLogin")
      @ResponseBody
      public Object doLogin(){
        // Session session = SecurityUtils.getSubject().getSession();
        try {
            UsernamePasswordToken token = new UsernamePasswordToken("admin", "123456", false);
            SecurityUtils.getSubject().login(token);
            return respSucceed();
        } catch (Exception e) {
            e.printStackTrace();
            return respFail(e.getMessage());
        }
      }

    //@RequiresPermissions("user:list")
    //@RequestMapping("/list")
    @ResponseBody
    public Object list(@RequestParam int page, @RequestParam int limit, @RequestParam(required = false) String userName) {
        if (StringUtils.isBlank(userName)) {
            // " " 影响查询
            userName = null;
        }
        Page<UserInfoDto> pageInfo = userInfoService.queryByUserName(userName, page, limit);

        return respByPage(pageInfo.getResult(), pageInfo.getTotal());
    }


}
