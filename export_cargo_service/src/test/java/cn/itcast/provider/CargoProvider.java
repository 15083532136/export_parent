package cn.itcast.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/14 19:48
 * @description：1
 * @modified By：
 * @version: 1$
 */
public class CargoProvider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        context.start();
        System.in.read();
    }
}
