package cn.itcast.web.utils;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/11 21:21
 * @description：对用户输入的密码进行加密加盐
 * @modified By：
 * @version: 1.0$
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {

    /**
     * 对用户输入的密码进行加密加盐
     * @Method: doCredentialsMatch
     * @Author lenovo
     * @Version  1.0
     * @Description
     * @param token 用户输入的账号和密码
     * @param info shiro从数据库中获取的密码
     * @Return boolean
     * @Exception
     * @Date 2019/10/11 21:23
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //1.获取用户输入的用户名
        String email = upToken.getUsername();
        //2.对输入的密码加密加盐 迭代次数1024
        char[] chars = upToken.getPassword();
        //将密码由字符数组转成字符串
        String password = new String(chars);
        String passwordSalt = new Md5Hash(password, email, 1024).toString();
        //3.比较密码是否相等
        String pwd = (String) info.getCredentials();
        return passwordSalt.equals(pwd);
    }
}
