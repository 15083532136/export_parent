package cn.itcast.web.controller;


import cn.itcast.domain.system.User;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class BaseController {

    @Autowired
    public HttpServletRequest request;

    @Autowired
    public HttpServletResponse response;

    @Autowired
    public HttpSession session;

    /**
     * 获取登陆用户所属企业id
     */
    public String getLoginCompanyId(){

        return getUser().getCompanyId();
    }

    /**
     * 获取登陆用户所属企业名称
     */
    public String getLoginCompanyName(){
        return getUser().getCompanyName();
    }

    /**
     * 从session中获取登录用户信息
     * @Method: getUser
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param
     * @Return cn.itcast.domain.system.User
     * @Exception
     * @Date 2019/10/10 20:58
     */
    public User getUser(){
        return (User) session.getAttribute("loginUser");
    }
}
