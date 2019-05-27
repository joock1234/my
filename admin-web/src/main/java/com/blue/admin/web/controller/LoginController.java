package com.blue.admin.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.blue.admin.web.utils.CookieUtil;
import com.blue.admin.web.utils.UUIdUtil;
import com.szrhtf.module.commons.Result;
import com.szrhtf.module.commons.SingleResult;
import com.szrhtf.module.constant.Constant;
import com.szrhtf.module.exception.ServiceException;
import com.szrhtf.share.functions.PublicFunctions;
import com.szrhtf.share.memcache.MemcachedService;
import com.szrhtf.web.share.dto.MenuDTO;
import com.szrhtf.web.share.dto.UserDTO;
import com.szrhtf.web.share.interfaces.RoleService;
import com.szrhtf.web.share.interfaces.UserService;
import com.szrhtf.web.share.query.UserQuery;

@Controller
public class LoginController extends BaseController {
	public static final String CURRENCY_FEN_REGEX = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,50})";


	@RequestMapping(value = "/doLogin")
	@ResponseBody
	public Object doSmartLogin(HttpServletRequest request,
			UserQuery info,HttpServletResponse response) {
		if (null == info || StringUtils.isEmpty(info.getLoginName())
				|| StringUtils.isEmpty(info.getPassword())) {
			return respFail("数据异常");
		}
		HttpSession session = request.getSession();

		UserService userService = (UserService) getDubboBean(request,
				"userService");
		info.setSiteType(1);// 登录管理系统
		info.setPassword(PublicFunctions.getTextSha256(info.getPassword()));
		SingleResult<UserDTO> result = userService.queryLogin(info);
		if (StringUtils.isEmpty(result.getResult())) {
			return respFail("用户名或密码错误");
		}
		if("INVALID".equals(result.getResult().getState())) {
			return respFail("用户已失效");
		}
		String token = UUIdUtil.getUUID();
		MemcachedService memcachedService = (MemcachedService) getDubboBean(
				request, "memcachedService");
		UserDTO u = result.getResult();
		memcachedService.add(token, u, Constant.COOKIE_AGE);
		session.setAttribute(token, u);
		// cookie改为session
		CookieUtil.addCookie(response, Constant.ADMIN_COOKIE_NAME, token,
		Constant.COOKIE_AGE);
		// 设置session
		session.setAttribute(Constant.ADMIN_COOKIE_NAME, u);

		return respSucceed(null);
	}



	/**
	 * 加载首页菜单
	 * */
	@RequestMapping(value = "/index")
	@ResponseBody
	public Object index(HttpServletRequest request) {
		UserDTO info=null;
        try {
            info = getUserInfo(request);
        } catch (Exception e) {
            //e.printStackTrace();
            return respFail("用户登录失效");
        }
		RoleService roleService = (RoleService) getDubboBean(request,
				"roleService");
		List<MenuDTO> menuList = roleService.queryMenuInfoByRoleID(
				info.getRoleId()).getResults();
		request.setAttribute("user", info);
		request.setAttribute("menuList", menuList);

		Map<String, Object> map =new HashMap<>();
		map.put("user", info);
		map.put("menuList", menuList);
		return respSucceed(map);
	}

	/**
	 * 用户注销
	 * */
	@RequestMapping(value = "/userCancellation")
	public String userCancellation(HttpServletRequest request,
			HttpServletResponse resp) {
		// cookie改为session
		HttpSession session = request.getSession();
		session.removeAttribute(Constant.ADMIN_COOKIE_NAME);
		session.invalidate();
		CookieUtil.deleteCookie(resp, Constant.ADMIN_COOKIE_NAME);
		return "login";
	}

	/**
	 * 去修改密码页面
	 * */
	@RequestMapping(value = "/goUpdateUserPassword")
	public String goUpdateUserPassword() {
		return "updateUserPassword";
	}

	/**
	 * 修改用户密码
	 *
	 * @param password
	 *            密码
	 * @param repassword
	 *            确认密码
	 * */
	@ResponseBody
	@RequestMapping(value = "/updateUserPassword")
	public Object updateUserPassword(HttpServletRequest request,
			HttpServletResponse resp, String password, String repassword)
			throws ServiceException {
		if (!password.matches(CURRENCY_FEN_REGEX)) {
			return respFail("密码至少存在一个大写字母，一个小写字母，一个数字，且长度为8-50位");
		}
		if (!password.equals(repassword)) {
			throw new ServiceException("两次输入密码不一致");
		}
		UserDTO info = getUserInfo(request);
		info.setPassword(PublicFunctions.getTextSha256(password));
		UserService userService = (UserService) getDubboBean(request,
				"userService");
		Result result = userService.updateUserPassword(info);
		if (result.isSuccess()) {
			// cookie修改为session
			// CookieUtil.deleteCookie(resp, Constant.ADMIN_COOKIE_NAME);
			HttpSession session = request.getSession();
			session.removeAttribute(Constant.ADMIN_COOKIE_NAME);

			return respSucceed(null);
		}
		return respFail("修改失败");
	}

}
