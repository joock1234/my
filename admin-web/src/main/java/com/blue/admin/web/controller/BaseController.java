package com.blue.admin.web.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.alibaba.dubbo.rpc.RpcContext;
import com.blue.admin.web.utils.CookieUtil;
import com.szrhtf.module.constant.Constant;
import com.szrhtf.module.utils.StringUtil;
import com.szrhtf.share.memcache.MemcachedService;
import com.szrhtf.web.share.dto.UserDTO;

public class BaseController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	public Object getDubboBean(HttpServletRequest request, String name) {
		WebApplicationContext webApplicationContext = WebApplicationContextUtils
				.getWebApplicationContext(request.getSession().getServletContext());
		RpcContext.getContext().setAttachment(Constant.LOGGER_ID, (null != request.getAttribute(Constant.LOGGER_ID)) ? ((String)request.getAttribute(Constant.LOGGER_ID)) : StringUtil.getLogId());
		return webApplicationContext.getBean(name);
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
	

	

	/** 获取当前登录人信息 */
	protected UserDTO getUserInfo(HttpServletRequest request) {
		String cookieValue = CookieUtil.getCookieByName(request, Constant.ADMIN_COOKIE_NAME).getValue();
		MemcachedService memcachedService = (MemcachedService) getDubboBean(request, "memcachedService");
		return (UserDTO) memcachedService.get(cookieValue.toString());
	}

	/**
	 * 取参数的Integer
	 * 
	 * @param request
	 * @param paraName
	 * @return
	 */
	public Integer getInteger(HttpServletRequest request, String paraName) {
		String tempStr = request.getParameter(paraName);
		if (StringUtils.isBlank(tempStr) || !StringUtils.isNumeric(tempStr)) {
			return 0;
		}
		return Integer.parseInt(tempStr);
	}

	protected void export(HttpServletRequest request, HttpServletResponse response, String fileName, byte[] data) {
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.reset();
			response.setHeader("Content-Disposition",
					"attachment; " + getDownLoadFileName(request, fileName));
			response.addHeader("Content-Length", String.valueOf(data.length));
			response.setContentType("application/octet-stream;charset=UTF-8");
			outputStream.write(data);
		} catch (Exception e) {
			logger.error("export error", e.getMessage());
		} finally {
			try {
				if (null != outputStream) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (Exception e2) {
				logger.error("export close stream error", e2.getMessage());
			}

		}
	}
	
	
	protected void export(HttpServletRequest request, HttpServletResponse response, String fileName, InputStream inputStream) {
		OutputStream outputStream = null;
		try {
			outputStream = response.getOutputStream();
			response.reset();
			response.setHeader("Content-Disposition",
					"attachment; " + getDownLoadFileName(request, fileName));
			response.addHeader("Content-Length", String.valueOf(inputStream.available()));
			response.setContentType("application/octet-stream;charset=UTF-8");
			byte[]buffer = new byte[4096];
			int position = 0;
			while((position = inputStream.read(buffer))>0){
				outputStream.write(buffer,0,position);
			}
			
		} catch (Exception e) {
			logger.error("export error", e.getMessage());
		} finally {
			try {
				if (null != outputStream) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (Exception e2) {
				logger.error("export close stream error", e2.getMessage());
			}

		}
	}
	
	public String getDownLoadFileName(HttpServletRequest request, String filename) {
		String new_filename = null;
		try {
			new_filename = URLEncoder.encode(filename, "UTF8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String userAgent = request.getHeader("User-Agent");
		StringBuffer str=new StringBuffer();
		//String rtn = "filename=\"" + new_filename + "\"";
		// 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
		if (userAgent != null) {
			userAgent = userAgent.toLowerCase();
			// IE浏览器，只能采用URLEncoder编码
			if (userAgent.indexOf("msie") != -1) {
				str.append("filename=\"").append(new_filename).append("\"");
			}
			// Opera浏览器只能采用filename*
			else if (userAgent.indexOf("opera") != -1) {
				str.append("filename*=UTF-8''").append(new_filename);
			}
			// Safari浏览器，只能采用ISO编码的中文输出，暂时不考虑safari
			
			// Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
			else if (userAgent.indexOf("applewebkit") != -1) {
				try {
					new_filename = MimeUtility.encodeText(filename, "UTF8", "B");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				str.append("filename=\"").append( new_filename).append( "\"");
			}
			// FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
			else if (userAgent.indexOf("mozilla") != -1) {
				str.append("filename*=UTF-8''").append(new_filename);
			} 
			else {
	            str.append("filename=\"").append(new_filename).append("\"");
	        }
		} else {
	        str.append("filename=\"").append(new_filename).append("\"");
		}
		return str.toString();
	}

	/**
	 * 注册自定义的属性编辑器
	 * 
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		// 1、日期
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		CustomDateEditor dateEditor = new CustomDateEditor(df, true);
		// 表示如果命令对象有Date类型的属性，将使用该属性编辑器进行类型转换
		binder.registerCustomEditor(Date.class, dateEditor);
	}
}
