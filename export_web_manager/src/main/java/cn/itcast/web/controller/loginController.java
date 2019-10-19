package cn.itcast.web.controller;

import cn.itcast.domain.system.Module;
import cn.itcast.domain.system.User;
import cn.itcast.service.system.UserService;
import com.alibaba.druid.util.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;

@Controller
public class loginController extends BaseController{

    @Autowired
    private UserService userService;

    /**
     * 1.显示首页
     * @return
     * 请求地址：index.jsp
     * 响应地址：WEB-INF/pages/home/main.jsp
     */
    @RequestMapping("/login")
    public String login(String email,String password){

        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            request.setAttribute("error", "用户名或密码错误");
            //email为空，返回登录页面
            return "forward:/login.jsp";
        }

        try {
            Subject subject = SecurityUtils.getSubject();
            AuthenticationToken token = new UsernamePasswordToken(email,password);

            subject.login(token);
            User user = (User) subject.getPrincipal();
            session.setAttribute("loginUser",user);
            //5.动态菜单显示
            Set<Module> modules = userService.showMenus(user.getUserId());
            session.setAttribute("modules",modules);
            return "home/main";
        } catch (AuthenticationException e) {

            e.printStackTrace();
            request.setAttribute("error", "用户名或密码错误");
            //email为空，返回登录页面
            return "forward:/login.jsp";
        }
    }

    /**
     * 2.显示首页框中的内容
     * @return
     * 请求地址：WEB-INF/pages/home/main.jsp
     * 响应地址：WEB-INF/pages/home/home.jsp
     */
    @RequestMapping("/home")
    public String home(){
        return "home/home";
    }

    /**
     * 注销登录
     * @Method: loginout
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param
     * @Return java.lang.String
     * @Exception
     * @Date 2019/10/10 20:54
     */
    @RequestMapping("/logout")
    public String logout(){
        session.removeAttribute("loginUser");
        SecurityUtils.getSubject().logout();

        return "forward:/login.jsp";
    }
}
