package cn.itcast.web.utils;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/11 19:52
 * @description：自定义Shiro的Realm
 * @modified By：
 * @version: 1.0$
 */
public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 授权验证
     * @Method: doGetAuthorizationInfo
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param principal
     * @Return org.apache.shiro.authz.AuthorizationInfo
     * @Exception
     * @Date 2019/10/12 10:16
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        User user = (User) principal.getPrimaryPrincipal();
        /**
         *  AuthorizationInfo和它的子类是用于聚合授权信息
         *  AuthorizationInfo和它的子类是用于聚合授权信息
         *  public interface AuthorizationInfo extends Serializable {
         *    Collection<String> getRoles(); //获取角色字符串信息
         *    Collection<String> getStringPermissions(); //获取权限字符串信息
         *    Collection<Permission> getObjectPermissions(); //获取 Permission 对象信息
         *   }
         */
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //1.获取权限的名称
        Set<Module> modules = userService.showMenus(user.getUserId());
        Set<String> moduleNames = new HashSet<>(16);
        for (Module module : modules) {
            moduleNames.add(module.getName());
        }
        info.addStringPermissions(moduleNames);
        return info;
    }

    /**
     * 自定义认证
     *
     * @param token
     * @Method: doGetAuthenticationInfo
     * @Author lenovo
     * @Version 1.0
     * @Description
     * @Return org.apache.shiro.authc.AuthenticationInfo
     * @Exception
     * @Date 2019/10/11 19:53
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.获取用户输入的邮箱
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String email = upToken.getUsername();
        //2.通过邮箱获取用户
        User user = userService.findByEmail(email);
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),getName());
        return info;

    }
}
