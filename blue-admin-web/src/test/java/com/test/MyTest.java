package com.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.alibaba.fastjson.JSONObject;
import com.blue.admin.web.ApplicationStart;
import com.blue.admin.web.dto.UserInfoDto;
import com.blue.admin.web.mapper.UserInfoDtoMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationStart.class)
@WebAppConfiguration
public class MyTest {
    @Autowired
    private UserInfoDtoMapper userInfoDtoMapper;
    @Test
    public void test(){
        for (int i = 1; i < 20; i++) {
            UserInfoDto u =new UserInfoDto();
            u.setUsername("测试_"+i);
            u.setSection("开发部");
            userInfoDtoMapper.insertSelective(u);
        }
    }

    @Test
    public void testPage(){
        //https://www.cnblogs.com/kangoroo/p/7998433.html
        //https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md

        Map<String, Object> data = new HashMap<>();
        Page<UserInfoDto> page = PageHelper.startPage(1, 2, true);// true表示需要统计总数
        List<UserInfoDto> list = userInfoDtoMapper.queryList();
        data.put("total", page.getTotal());
        data.put("nowPage", 1);
        data.put("data", page.getResult());

        System.out.println(JSONObject.toJSONString(data));


    }

}
