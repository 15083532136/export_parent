package cn.itcast.Service.stat;

import java.util.List;
import java.util.Map;

/**
 * @author ：屈雪耀
 * @date ：Created in 2019/10/20 19:29
 * @description：图形
 * @modified By：
 * @version: 1.0$
 */
public interface StatService {

    /**
     * 生产厂家销售统计
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> factorySale();

    /**
     *  商品销售统计排行
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> sellSale(int top);

    /**
     *  系统访问压力图
     * @return List<Map<String,Object>>
     */
    List<Map<String,Object>> online();
}
