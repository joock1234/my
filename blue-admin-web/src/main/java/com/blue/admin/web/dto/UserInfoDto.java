package com.blue.admin.web.dto;

public class UserInfoDto {
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String section;//部门
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getSection() {
        return section;
    }
    public void setSection(String section) {
        this.section = section;
    }






}
