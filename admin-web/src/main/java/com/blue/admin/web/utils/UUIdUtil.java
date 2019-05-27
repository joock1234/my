package com.blue.admin.web.utils;

import java.util.UUID;

public class UUIdUtil {
	/**
	 * 获取一个UUID
	 * 如：86a589889dfd4044952f881de6b0d81c
	 * */
	public static String getUUID(){ 
        String s = UUID.randomUUID().toString(); 
        //去掉“-”符号 
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    }
	
	public static void main(String[] args) {
		System.out.println(">>>>>>>>>>" + getUUID());
	}
}
