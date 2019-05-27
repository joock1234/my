package com.blue.admin.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blue.admin.web.dto.UserInfoDto;

public interface UserInfoDtoMapper {
    int insert(UserInfoDto record);

    int insertSelective(UserInfoDto record);

    List<UserInfoDto> queryList();

    List<UserInfoDto> queryByUserName(@Param("username")String username);


}