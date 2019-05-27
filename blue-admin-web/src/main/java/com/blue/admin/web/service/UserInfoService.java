package com.blue.admin.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blue.admin.web.dto.UserInfoDto;
import com.blue.admin.web.mapper.UserInfoDtoMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class UserInfoService extends BaseService {

    @Autowired
    private UserInfoDtoMapper userInfoDtoMapper;

    public Page<UserInfoDto> queryByUserName(String userName, int page, int limit) {
        Page<UserInfoDto> pageInfo = PageHelper.startPage(page, limit, true);// true表示需要统计总数
        userInfoDtoMapper.queryByUserName(userName);
        return pageInfo;
    }

}
