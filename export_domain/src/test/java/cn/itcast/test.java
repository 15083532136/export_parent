package cn.itcast;

import org.junit.Test;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/18 11:40
 * @description：面试题 自增变量
 * @modified By：
 * @version: 1.0$
 */
public class test {

    /**
     * 自增变量
     */
    @Test
    public void test01(){
        int i = 1;
        i = i++;
        int j = i++;
        int k = i + ++i*i++;
        System.out.println("i="+i);
        System.out.println("j="+j);
        System.out.println("k="+k);
    }
}
