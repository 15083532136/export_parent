package cn.itcast.service;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/13 19:59
 * @description：基于main方法启动提供者
 * @modified By：
 * @version: 1.0$
 */
public class StatProvider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("classpath*:spring/applicationContext-*.xml");
        context.start();
        System.in.read(); // 按任意键退出
    }
}
