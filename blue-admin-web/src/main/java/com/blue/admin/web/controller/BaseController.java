package com.blue.admin.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public Object respSucceed() {
        return  respSucceed(null);
    }

	public Object respSucceed(Object data) {
	    return  resp(0, "成功", data,0);
	}

	public Object respFail(String msg) {
        return  resp(1, msg, null,0);
    }

	public Object resp(int code,String msg,Object data,long count) {
	    RespData r=new RespData();
	    r.setCode(code);
	    r.setMsg(msg);
	    r.setData(data);
	    r.setCount(count);
	    return r;
	}
    /**
     * @param data
     * @param count 总数量
     */
	public Object respByPage(Object data,long count){
        return resp(0, "成功", data, count);

	}

	class RespData{
	    private int code;
	    private String msg;
	    private Object data;
	    private long count;

        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }
        public long getCount() {
            return count;
        }
        public void setCount(long count) {
            this.count = count;
        }
	}





}
