package com.blue.admin.web.config.shiro;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import com.blue.admin.web.dto.UserInfoDto;

public class MyShiroRealm extends AuthorizingRealm {


    /**
      * 身份验证
      */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        System.out.println("开始身份验证");
        String username = (String) token.getPrincipal(); //获取用户名，默认和login.html中的username对应。

        UserInfoDto u=null;
        if("admin".equals(username)){
            u = new UserInfoDto();
            u.setUsername("admin");
            u.setPassword("123456");
            u.setSection("123");
        }
       // UserInfo userInfo = userInfoService.findByUsername(username);
        if (u == null) {
            //没有返回登录用户名对应的SimpleAuthenticationInfo对象时,就会在LoginController中抛出UnknownAccountException异常
            return null;
        }

        //验证通过返回一个封装了用户信息的AuthenticationInfo实例即可。
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                u, //用户信息
                u.getPassword(), //密码
                getName() //realm name
        );
        // authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(u.getSection())); //设置盐
        return authenticationInfo;

    }


    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        // UserInfoDto userInfo = (UserInfoDto) principals.getPrimaryPrincipal();

        /*for (SysRole role : userInfo.getRoleList()) {
            authorizationInfo.addRole(role.getRole());
            for (SysPermission p : role.getPermissions()) {
                authorizationInfo.addStringPermission(p.getPermission());
            }
        }*/

        Set<String> roleSet = new HashSet<String>();
        roleSet.add("admin");
        info.setRoles(roleSet);

        Set<String> permissionSet = new HashSet<String>();
        permissionSet.add("user:list");
        info.setStringPermissions(permissionSet);

        return info;

    }


}
