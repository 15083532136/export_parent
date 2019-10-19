package cin.itcast.test;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.Test;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/11 21:09
 * @description：测试类
 * @modified By：
 * @version: 1.0$
 */
public class TestApp {

    @Test
    public void testMd5(){
        String password = "1";
        String md5 = new Md5Hash(password).toString();
        System.out.println(md5);
    }

    @Test
    public void md5Salt(){
        String password = "1";
        String email1 = "lw@export.com";
        String email2 = "saas@export.com";
        System.out.println(new Md5Hash(password,email1,1024).toString());
        System.out.println(new Md5Hash(password,email2,1024).toString());
    }
}
