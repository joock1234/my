package com.blue.admin.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.blue.admin.web.dto.MenuDto;

@RestController
@RequestMapping("/index")
public class IndexController extends BaseController {


    @RequestMapping("/getMenu")
    @ResponseBody
    public Object doLogin(){
        List<MenuDto> list =new ArrayList<MenuDto>();
        MenuDto parent= new MenuDto();
        parent.setId(1);
        parent.setName("系统管理");
        parent.setParentId(0);
        parent.setUrl("#");

        MenuDto m1= new MenuDto();
        m1.setId(2);
        m1.setName("用户管理");
        m1.setParentId(1);
        m1.setUrl("/jsp/system/user_list.jsp");

        MenuDto m2= new MenuDto();
        m2.setId(3);
        m2.setName("菜单管理");
        m2.setParentId(1);
        m2.setUrl("/jsp/system/menu_list.jsp");

        list.add(parent);
        list.add(m1);
        list.add(m2);
        return respSucceed(list);
    }

}
